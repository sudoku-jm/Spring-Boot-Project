package aloha.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import aloha.domain.BoardAttach;
import aloha.domain.FileAttach;
import aloha.domain.MemberImg;
import aloha.domain.StoryAttach;
import aloha.service.BoardService;
import aloha.service.FileService;
import aloha.service.MemberService;
import aloha.util.MediaUtils;

@Controller
@RequestMapping("/file")
public class FileController {
	
	
	// 업로드 경로
	@Value("${upload.path}")
	private String uploadPath;
	
	private static final Logger log = LoggerFactory.getLogger(FileController.class);

	
	@Autowired
	private BoardService service;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value = "/fileDownload", method = RequestMethod.GET )
	public void fileDownload(HttpServletResponse response, HttpServletRequest request, @RequestParam Map<String,String> paramMap) {
		String fullName = paramMap.get("fullName");		// 파일 전체경로
		String fileName = paramMap.get("fileName");		// 파일명
		
	
		// 다운로드할 파일
		File file = new File(fullName);
		
		FileInputStream fileInputStream = null;
	    ServletOutputStream servletOutputStream = null;
	
	    try{
	        String downName = null;
	        String browser = request.getHeader("User-Agent");
	    //파일 인코딩
	    if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")){//브라우저 확인 파일명 encode  
	    	downName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
	    }else{
	        downName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
	    }
	    
	    response.setHeader("Content-Disposition","attachment;filename=\"" + downName+"\"");
	    response.setContentType("text/html");
	    response.setHeader("Content-Transfer-Encoding", "binary;");
 	 
	    fileInputStream = new FileInputStream(file);
	    servletOutputStream = response.getOutputStream();
 	 
	    byte b [] = new byte[1024];
	    int data = 0;
 	 
	    while((data=(fileInputStream.read(b, 0, b.length))) != -1){
	        servletOutputStream.write(b, 0, data);
	    }
	    
	    servletOutputStream.flush();//출력
	        
	    }catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	        if(servletOutputStream!=null){
	            try{
	                servletOutputStream.close();
	            }catch (IOException e){
	                e.printStackTrace();
	            }
	        }
	        if(fileInputStream!=null){
	            try{
	                fileInputStream.close();
	            }catch (IOException e){
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	@RequestMapping(value = "/zipDownload", method = RequestMethod.GET )
	public void zipDownload(HttpServletResponse response, HttpServletRequest request, @RequestParam Map<String,String> paramMap) throws Exception {
		// 글 번호
		Integer boardNo = Integer.parseInt( paramMap.get("boardNo") );
		// 글 제목
		String title = paramMap.get("title");
		
		//테이블 명
		String table = paramMap.get("table");
		
		// 글 번호에 따른 모든 첨부파일 리스트 조회
		//board_attach로만 첨부파일 요청을 보냈었음.
//		List<BoardAttach> fileList = service.readFileList(boardNo, table);
		//각 테이블 첨부파일 요청 보낼 수 있도록 테이블 지정함.
		List<FileAttach> fileList = fileService.readFileList(boardNo, table);
		
		// 다운로드 파일 명
		String zipFile = "temp.zip";
		String downloadFileName = title;
		
		//파일 인코딩
		String browser = request.getHeader("User-Agent");
		if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")){//브라우저 확인 파일명 encode  
		   downloadFileName = URLEncoder.encode(downloadFileName,"UTF-8").replaceAll("\\+", "%20");
		    
		}else{
		   downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		  
		  try {
		     
		     FileOutputStream fout = new FileOutputStream(zipFile);
		     ZipOutputStream zout = new ZipOutputStream(fout);
		     
		     for (int i = 0; i < fileList.size(); i++) {
		        
		        // 본래 파일명 유지, 경로제외 파일압축을 위해 new File로
			    ZipEntry zipEntry = new ZipEntry(new File(fileList.get(i).getFileName()).getName());
			    zout.putNextEntry(zipEntry);
			    
			    FileInputStream fin = new FileInputStream(fileList.get(i).getFullName());
			    byte[] buffer = new byte[1024];
			    int length;
		    
		    
			    while( (length = fin.read(buffer)) > 0 ) {
			       zout.write(buffer, 0 ,length);
		    }
		    
		    zout.closeEntry();
		    fin.close();
		 }
		 
		 zout.close();
		 
		 response.setContentType("application/zip");
		 response.addHeader("Content-Disposition", "attachment; filename=" + downloadFileName + ".zip");
		     
		     FileInputStream fis = new FileInputStream(zipFile);
		     BufferedInputStream bis = new BufferedInputStream(fis);
		     ServletOutputStream so = response.getOutputStream();
		     BufferedOutputStream bos = new BufferedOutputStream(so);
		     
		     byte[] data = new byte[2048];
		     int input = 0;
		     
		     while( (input=bis.read(data)) != -1 ) {
		        bos.write(data, 0, input);
		        bos.flush();
		     }
		     
		     if(bos != null) bos.close();
		     if(bis != null) bis.close();
		     if(so != null) so.close();
		     if(fis != null) fis.close();
		           
		     
		  } catch (Exception e) {
		     e.printStackTrace();
		  }
 		
 	}
	
	@RequestMapping(value = "/deleteFile", method = RequestMethod.GET)
	public String deleteFile(FileAttach fileAttach) throws Exception {
		
		
		log.info(fileAttach + "");
		// 파일 삭제
		String fullName = fileAttach.getFullName();
		Integer boardNo = fileAttach.getBoardNo();
		Integer fileNo = fileAttach.getFileNo();
		String table = fileAttach.getTable();
		
		//fileNo 없으면..
		
		//fullName 없으면 조회
		if(fullName == null) {
			fileAttach = fileService.readFile(fileNo,"story_attach");
			fullName = fileAttach.getFullName();
			fileAttach.setTable(table);
		}
		
		
		File file = new File(fullName);
		
		// 실제로 파일이 존재하는 확인
		if(file.exists()) {
			if(file.delete()) {
				log.info("삭제한 파일 : " + fullName);
				log.info("파일삭제 성공");
			} else {
				log.info("파일삭제 실패");
				
			}
		} else {
			log.info("삭제(실패) : " + fullName);
			log.info("파일이 존재하지 않습니다.");
		}
		
//		service.deleteFile(fileNo);
		fileService.deleteFile(fileAttach);
		
		
		return "subpage/board/success";
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
//				in = new FileInputStream(uploadPath + fileName);
			
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
	
	

	// 비동기 처리
   @ResponseBody
   @RequestMapping(value = "/uploadAjax", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
   public void uploadAjax(MultipartFile file) throws Exception {
      log.info("originalName: " + file.getOriginalFilename());
      
      
      //return new ResponseEntity<String>("test", HttpStatus.CREATED);
   }
   
    // 프로필 업로드
	@PostMapping("/profileUpload") 
	public String handleFileUpload( @RequestParam("profile") MultipartFile file, @RequestParam("userNo") Integer userNo) throws Exception {

		// file 정보 확인
		if( !file.isEmpty() ) {
			
			log.info("originalName : " + file.getOriginalFilename());
			log.info("size : " + file.getSize());
			log.info("contentType : " + file.getContentType());
		
		} else {
			log.info("업로드 요청한 파일이 존재하지 않습니다.");
			return "redirect:/user/mypage";
		}

		
		MemberImg img = (MemberImg) uploadFile(file);
		img.setUserNo(userNo);
		memberService.updateProfile(img);
		  
		return "redirect:/user/mypage";
	 }
	
	

	// 단일 파일 업로드 
	private Object uploadFile(MultipartFile file) throws IOException {
		
		// 업로드 경로에 파일 복사
		// 파일 존재여부 확인
		if( file.isEmpty()) {
			return null;
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
		
		MemberImg img = new MemberImg();

		img.setFullName(uploadPath + "/" + uploadFileName);	// 업로드 파일 전체경로
		img.setFileName(originalFileName);					// 파일명
		img.setCategory(file.getContentType());              // 카테고리(파일종류)
		
		return img;
	}
	
	
	
   
   
}







