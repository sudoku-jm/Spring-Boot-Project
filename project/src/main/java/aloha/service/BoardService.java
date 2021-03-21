package aloha.service;

import java.util.List;

import aloha.domain.Board;
import aloha.domain.BoardAttach;
import aloha.domain.Page;
import aloha.domain.Reply;

public interface BoardService {

	// 게시글 목록 조회
	public List<Board> list() throws Exception;
	
	// 게시글 쓰기
	public void register(Board board) throws Exception;
	
	// 게시글 읽기
	public Board read(Integer boardNo) throws Exception;
	
	// 게시글 수정
	public void modify(Board board) throws Exception;
	
	// 게시글 삭제
	public void remove(Integer boardNo) throws Exception;
	
	// 게시글 검색
	public List<Board> search(String keyword);
	
	// 파일 업로드
	public void uploadFile(BoardAttach attach) throws Exception;
	
	// 파일 수정 업로드
	public void uploadModifyFile(BoardAttach attach) throws Exception;
	
	// 파일 읽기 - 글번호
	public List<BoardAttach> readFileList(Integer boardNo) throws Exception;
	
	// 파일 읽기 - 파일번호
	public BoardAttach readFile(Integer fileNo) throws Exception;
	
	// 파일 삭제
	public void deleteFile(Integer fileNo) throws Exception;
	
	// 전체 게시글 수
	public Integer totalCount() throws Exception;
	
	// 전체 게시글 수
	public Integer totalCount(String keyword) throws Exception;
	
	// [페이지] 게시글 목록
	public List<Board> list(Page page) throws Exception;
	
	// [검색어][페이지] 게시글 검색
	public List<Board> search(Page page) throws Exception;
	
	// 댓글 등록
	public void replyRegister(Reply reply) throws Exception;
	
	// 댓글 목록 조회
	public List<Reply> replyList(Integer boardNo) throws Exception;
	
	// 댓글 수정 
	public void replyModify(Reply reply) throws Exception;
	
	//댓글 삭제
	public void replyRemove(Integer reply_no) throws Exception;
	
	// [썸네일] 파일경로 조회
	public List<String> getAttach(Integer boardNo) throws Exception;
	
	// 그룹번호 수정
	public void updateGroupNo(Board board) throws Exception;
	
	// 답글 쓰기
	public void answerRegister(Board board) throws Exception;
	
	// 그룹번호 게시글 개수
	public int countAnswer(Integer groupNo) throws Exception;
	
	// 계층번호 조회
	public int readDepthNo(Integer boardNo) throws Exception;
	
	// 계층번호 조회
	public int readGroupNo(Integer boardNo) throws Exception;
	
	// 순서번호 MAX 조회
	public int maxSeqNo() throws Exception;
	
	// 조회수 증가
	public void view(Integer boardNo) throws Exception;

}







