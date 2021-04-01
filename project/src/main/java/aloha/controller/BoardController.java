package aloha.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import aloha.domain.Board;
import aloha.domain.BoardAttach;
import aloha.domain.Page;
import aloha.domain.Reply;
import aloha.service.BoardService;
import aloha.util.MediaUtils;

@Controller
@RequestMapping("/subpage/board")
public class BoardController {
	
	// 업로드 경로
	@Value("${upload.path}")
	private String uploadPath;
	
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

	
	@Autowired
	private BoardService service;
	
	
	// 게시글 목록 화면
	@RequestMapping( value = "/list", method = RequestMethod.GET)
	public void list(Model model, Page page) throws Exception {
		Integer totalCount = null;
		Integer rowsPerPage = null;
		Integer pageCount = null;
		Integer pageNum = page.getPageNum();
		String keyword = page.getKeyword();
		
		// 조회된 전체 게시글 수
		if( page.getTotalCount() == 0 )
			totalCount = service.totalCount();
		else
			totalCount = page.getTotalCount();
		
		// 페이지 당 노출 게시글 수
		if( page.getRowsPerPage() == 0 ) 
			rowsPerPage = 10;
		else
			rowsPerPage = page.getRowsPerPage();
		
		// 노출 페이지 수
		if( page.getPageCount() == 0 )
			pageCount = 10;
		else
			pageCount = page.getPageCount();
			
		
		if( page.getPageNum() == 0 ) {
			page = new Page(1, rowsPerPage, totalCount, pageCount);
		} else {
			page = new Page(pageNum, rowsPerPage, totalCount, pageCount);
		}
		
		
		if( keyword == null || keyword == "" ) {
			page.setKeyword("");
			model.addAttribute("list", service.list(page));
		} else {
			page.setKeyword(keyword);
			model.addAttribute("list", service.search(page));
		}
		
		
		model.addAttribute("page", page);
		
		log.info(totalCount.toString());
		log.info(page.toString());
	}	
	
	// 게시글 쓰기 화면
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping( value = "/register", method = RequestMethod.GET)
	public void registerForm(Model model, Board board, Principal user) throws Exception {
		String userId = "";
		if( user != null ) {
			userId = user.getName();
			model.addAttribute("userId", userId);
		}
	}
	
	// 게시글 쓰기 처리
	@RequestMapping( value = "/register", method = RequestMethod.POST)
	public String register(Model model, Board board) throws Exception {
		// [파일 업로드]
		MultipartFile[] file = board.getFile();
		String[] filePath = board.getFilePath();
		
		// file 정보 확인
		for (MultipartFile f : file) {
			log.info("originalName : " + f.getOriginalFilename());
			log.info("size : " + f.getSize());
			log.info("contentType : " + f.getContentType());
		}
		
		// 파일 업로드 처리 - uploadFiles()
		ArrayList<BoardAttach> attachList = uploadFiles(file);
		
		// 게시글 등록
		service.register(board);
		
		// 첨부파일 등록
		for (BoardAttach attach : attachList) {
			service.uploadFile(attach);
		}
		
		model.addAttribute("msg", "등록이 완료되었습니다.");
		return "subpage/board/success";
	}
	
	// 게시글 읽기 화면
	@RequestMapping( value = "/read", method = RequestMethod.GET)
	public String read(Model model, Integer boardNo, Principal user) throws Exception {
		
		Board board = service.read(boardNo);
		if( board == null ) {
			model.addAttribute("msg", "조회할 수 없는 게시글입니다.");
			return "subpage/board/empty";
		}
		
		// 로그인 id == 작성자 id 
		String userId = "";
		if( user != null ) {
			userId = user.getName();
			model.addAttribute("userId", userId);
		}
		
		String writerId = board.getWriter();
		if( writerId.equals(userId) ) {
			model.addAttribute("set", true);//작성자일 경우만 수정,삭제 노출
		}
		
		
		// 조회 수 증가
		service.view(boardNo);
		
				
		model.addAttribute("board", board );
		// 파일 목록 조회
		model.addAttribute("files", service.readFileList(boardNo) );
		// 댓글 목록 조회
		model.addAttribute("replyList", service.replyList(boardNo));
		
		return "subpage/board/read";
	}
	
	
	// 게시글 수정 화면
	@RequestMapping( value = "/modify", method = RequestMethod.GET)
	public String modifyForm(Model model, Integer boardNo, Principal user) throws Exception {
		// 로그인 id == 작성자 id 
		String userId = "";
		if( user != null ) {
			userId = user.getName();
			model.addAttribute("userId", userId);
		}
		Board board = service.read(boardNo);
		String writerId = board.getWriter();
		if( !writerId.equals(userId) ) {
			return "redirect:/error/noAuth";
		}
				
		
		model.addAttribute( board );
		model.addAttribute("files", service.readFileList(boardNo) );
		
		return "subpage/board/modify";
	}
	
	
	// 게시글 수정 처리
	@PostMapping("/modify")
	public String modify(Model model, Board board, Integer[] deleteNo) throws Exception {
		// 선택한 파일 DB에서 삭제
		ArrayList<BoardAttach> deleteFileList = new ArrayList<BoardAttach>(); 
		// 삭제번호를 클릭한 경우에만 반복
		if( deleteNo != null ) {
			for (Integer no : deleteNo) {
				log.info("삭제 파일번호 : " + no);
				deleteFileList.add( service.readFile(no) );
			}
		}
		
		// 파일 삭제
		deleteFiles(deleteFileList);
		
		// [파일 업로드]
		MultipartFile[] file = board.getFile();
		String[] filePath = board.getFilePath();
		
		// file 정보 확인
		for (MultipartFile f : file) {
			log.info("originalName : " + f.getOriginalFilename());
			log.info("size : " + f.getSize());
			log.info("contentType : " + f.getContentType());
		}
		
		// 파일 업로드 처리 - uploadFiles()
		ArrayList<BoardAttach> attachList = uploadFiles(file);
		
		service.modify(board);
		
		// 첨부파일 등록
		for (BoardAttach attach : attachList) {
			attach.setBoardNo(board.getBoardNo());
			service.uploadModifyFile(attach);
		}

		model.addAttribute("msg", "수정이 완료되었습니다.");
		return "subpage/board/success";
	}
	
	// 게시글 삭제 처리
	@RequestMapping( value = "/remove", method = RequestMethod.POST)
	public String remove(Model model,Integer boardNo) throws Exception{
		List<BoardAttach> attachList =  service.readFileList(boardNo);
		
		// 게시글에 첨부한 파일 삭제
		deleteFiles(attachList);
		
		
		int groupNo = service.readGroupNo(boardNo);
		log.info("groupNo : " + groupNo);
		
		// 답글이 달려있는지 확인
		int groupCount = service.countAnswer(groupNo);
		
		if( groupCount > 1 ) {
			Board board = new Board();
			board.setBoardNo(boardNo);
			board.setTitle("삭제된 글입니다");
			board.setContent("-");
			board.setWriter("-");
			service.modify(board);
		} else {
			service.remove(boardNo);
		}
 		
		
		model.addAttribute("msg", "삭제가 완료되었습니다.");
		return "subpage/board/success";
	}
	
	
	// 게시글 검색 처리
	@RequestMapping( value = "/search", method = RequestMethod.GET)
	public String search(Model model, Page page) throws Exception { 
		
		String keyword = page.getKeyword();
		Integer totalCount = service.totalCount(keyword);
		Integer rowsPerPage = null;
		Integer pageCount = null;
		
		// 페이지 당 노출 게시글 수
		if( page.getRowsPerPage() == 0 ) 
			rowsPerPage = 10;
		else
			rowsPerPage = page.getRowsPerPage();
		
		// 노출 페이지 수
		if( page.getPageCount() == 0 )
			pageCount = 10;
		else
			pageCount = page.getPageCount();
		
		page = new Page(1, rowsPerPage, totalCount, pageCount);
		page.setKeyword(keyword);
		
		model.addAttribute("list", service.search(page));
		model.addAttribute("page", page);
		
		return "subpage/board/list";
	}
	
	
	
	// 파일 업로드 
	private ArrayList<BoardAttach> uploadFiles(MultipartFile[] files) throws IOException {
		
		ArrayList<BoardAttach> attachList = new ArrayList<BoardAttach>();
		
		// 업로드 경로에 파일 복사
		for (MultipartFile file : files) {
			// 파일 존재여부 확인
			if( file.isEmpty() ) {
				continue;
			}
			
			// 파일명 중복 방지를 위한 고유 ID 생성
			UUID uid = UUID.randomUUID();
			
			// 실제 원본 파일 이름
			String originalFileName =  file.getOriginalFilename();
			
			// UID_강아지.png
			String uploadFileName = uid.toString() + "_" + originalFileName;
			
			// 업로드 폴더에 업로드할 파일 복사 (upload)
			byte[] fileData = file.getBytes();
			File target = new File(uploadPath, uploadFileName);
			FileCopyUtils.copy(fileData, target);
			
			BoardAttach attach = new BoardAttach();
			attach.setFullName(uploadPath + "/" + uploadFileName);	// 업로드 파일 전체경로
			attach.setFileName(originalFileName);					// 파일명
			attachList.add(attach);
			
		}
		
		return attachList;
	}
	
	
	// 실제 파일 삭제
	public void deleteFiles(List<BoardAttach> deleteFileList) throws Exception {
		
		// 해당 게시글의 첨부파일 전체 삭제
		for (BoardAttach deleteFile : deleteFileList) {
			String fullName = deleteFile.getFullName();
			Integer fileNo = deleteFile.getFileNo();
			
			File file = new File(fullName);
			// 실제로 파일이 존재하는 확인
			if(file.exists()) {
				// 파일 삭제
				if(file.delete()) {
					log.info("삭제한 파일 : " + fullName);
					log.info("파일삭제 성공");
					
					// DB에서 해당 파일 데이터 삭제 
					service.deleteFile(fileNo);
				} else {
					log.info("파일삭제 실패");
					
				}
			} else {
				log.info("삭제(실패) : " + fullName);
				log.info("파일이 존재하지 않습니다.");
			}
		}
		
	}
	
	// 댓글 등록
	@RequestMapping(value = "/replyRegister", method = RequestMethod.GET)
	public String replyRegister(Model model, Reply reply, Principal user) throws Exception {
		String userId = "";
		if( user != null ) {
			userId = user.getName();
			model.addAttribute("userId", userId);
		}
		
		// 댓글 등록 
		service.replyRegister(reply);
		
		Integer boardNo = reply.getBoardNo();
		// 댓글 목록 조회
		model.addAttribute("replyList", service.replyList(boardNo));
		
		return "subpage/board/reply/list";
	}
	
	
	// 댓글 수정
	@RequestMapping(value = "replyModify", method = RequestMethod.GET)
	public String replyModify(Model model, Reply reply, Principal user) throws Exception {
		String userId = "";
		if( user != null ) {
			userId = user.getName();
			model.addAttribute("userId", userId);
		}
		
		log.info("#replyModify");
		log.info(reply.toString());
		
		// 댓글 수정
		service.replyModify(reply);
		
		Integer boardNo = reply.getBoardNo();
		// 댓글 목록 조회
		model.addAttribute("replyList", service.replyList(boardNo));
		
		
		
		return "subpage/board/reply/list";
	}
	
	// 댓글 삭제
	@RequestMapping(value = "replyRemove", method = RequestMethod.GET)
	public String replyRemove(Model model, Reply reply, Principal user) throws Exception {
		String userId = "";
		if( user != null ) {
			userId = user.getName();
			model.addAttribute("userId", userId);
		}
		
		// 댓글 삭제
		service.replyRemove(reply.getReplyNo());
		
		Integer boardNo = reply.getBoardNo();
		// 댓글 목록 조회
		model.addAttribute("replyList", service.replyList(boardNo));
		
		return "subpage/board/reply/list";
	}
	
	
	// 첨부파일 경로 가져오기
	@RequestMapping(value = "/getAttach/{boardNo}")
	@ResponseBody
	public List<String> getAttach(@PathVariable("boardNo") Integer boardNo ) throws Exception {
		log.info("getAttach boardNo : " + boardNo);
		
		return service.getAttach(boardNo);
		
	}
	
	// 썸네일 보여주기
	@ResponseBody
	@RequestMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String fileName) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		
		log.info("FILE NAME: " + fileName);
		
		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
			log.info("FILE FORMAT: " + formatName);
			
			MediaType mType = MediaUtils.getMediaType(formatName);
			
			HttpHeaders headers = new HttpHeaders();
			
			in = new FileInputStream(fileName);
//			in = new FileInputStream(uploadPath + fileName);
			
			if( mType != null) {
				headers.setContentType(mType);
			} else {
				fileName = fileName.substring(fileName.lastIndexOf("_") + 1);
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859") + "\"");
			}
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
			
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}
		return entity;
	}

	
	// 답글 화면
	@RequestMapping( value = "/answer", method = RequestMethod.GET)
	public void answerForm(Model model, Board board, Principal user) throws Exception {
		
		String userId = "";
		if( user != null ) {
			userId = user.getName();
			model.addAttribute("userId", userId);
		}
	
		Integer boardNo = board.getGroupNo();
		board = service.read(boardNo);
		
		// 부모글 정보 조회
		model.addAttribute("board",  board);
		// 파일 목록 조회
		model.addAttribute("files", service.readFileList(boardNo) );
		
		
		
	}
	
	
	// 답글 쓰기 처리
	@RequestMapping(value = "answerRegister", method = RequestMethod.POST)
	public String answerRegister(Model model, Board board) throws Exception {
		log.info("답글 쓰기 처리");
		log.info(board.toString());
		
		// [파일 업로드]
		MultipartFile[] file = board.getFile();
		String[] filePath = board.getFilePath();
		
		// file 정보 확인
		for (MultipartFile f : file) {
			log.info("originalName : " + f.getOriginalFilename());
			log.info("size : " + f.getSize());
			log.info("contentType : " + f.getContentType());
		}
		
		// 파일 업로드 처리 - uploadFiles()
		ArrayList<BoardAttach> attachList = uploadFiles(file);
		
		// 게시글 등록
		service.answerRegister(board);
		
		// 첨부파일 등록
		for (BoardAttach attach : attachList) {
			service.uploadFile(attach);
		}
		
		
		
		model.addAttribute("msg", "등록이 완료되었습니다.");
		return "subpage/board/success";
	}
	
	
	@RequestMapping( value = "/success", method = RequestMethod.GET)
	public void success(Model model) throws Exception {
		
		
	}
	
}















