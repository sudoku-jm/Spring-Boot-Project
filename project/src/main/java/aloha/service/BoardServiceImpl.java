package aloha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aloha.domain.Board;
import aloha.domain.BoardAttach;
import aloha.domain.Page;
import aloha.domain.Reply;
import aloha.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardMapper mapper; 

	@Override
	public List<Board> list() throws Exception {
		return mapper.list();
	}

	@Override
	public void register(Board board) throws Exception {
		mapper.create(board);
		
		int boardNo = mapper.maxBoardNo();
		board.setGroupNo(boardNo);
		board.setBoardNo(boardNo);
		
		mapper.updateGroupNo(board);
		
	}

	@Override
	public Board read(Integer boardNo) throws Exception {
		return mapper.read(boardNo);
	}

	@Override
	public void modify(Board board) throws Exception {
		mapper.update(board);
	}

	@Override
	public void remove(Integer boardNo) throws Exception {
		mapper.delete(boardNo);
		
	}

	@Override
	public List<Board> search(String keyword) {
		return mapper.search(keyword);
	}

	@Override
	public void uploadFile(BoardAttach attach) throws Exception {
		mapper.uploadFile(attach);
	}

	@Override
	public List<BoardAttach> readFileList(Integer boardNo) throws Exception {
		return mapper.readFileList(boardNo);
	}

	@Override
	public void deleteFile(Integer fileNo) throws Exception {
		mapper.deleteFile(fileNo);
	}

	@Override
	public void uploadModifyFile(BoardAttach attach) throws Exception {
		mapper.uploadUpdateFile(attach);
	}

	@Override
	public BoardAttach readFile(Integer fileNo) throws Exception {
		return mapper.readFile(fileNo);
	}

	@Override
	public Integer totalCount() throws Exception {
		return mapper.totalCount();
	}

	@Override
	public List<Board> list(Page page) throws Exception {
		return mapper.listWithPage(page);
	}

	@Override
	public List<Board> search(Page page) throws Exception {
		return mapper.searchWithPage(page);
	}

	@Override
	public Integer totalCount(String keyword) throws Exception {
		return mapper.totalCountByKeyword(keyword);
	}

	@Override
	public void replyRegister(Reply reply) throws Exception {
		mapper.replyCreate(reply);
	}

	@Override
	public List<Reply> replyList(Integer boardNo) throws Exception {
		return mapper.replyList(boardNo);
	}

	@Override
	public void replyModify(Reply reply) throws Exception {
		mapper.replyUpdate(reply);
	}

	@Override
	public void replyRemove(Integer reply_no) throws Exception {
		mapper.replyDelete(reply_no);
	}

	@Override
	public List<String> getAttach(Integer boardNo) throws Exception {
		return mapper.getAttach(boardNo);
	}

	@Override
	public void updateGroupNo(Board board) throws Exception {
		mapper.updateGroupNo(board);
	}

	@Override
	public void answerRegister(Board board) throws Exception {
		// 계층번호
		int depthNo = mapper.readDepthNo(board.getBoardNo());
		// 계층번호 = 부모글의 계층번호 + 1
		board.setDepthNo(depthNo+1);
		
		
		// 부모글이 답글인 경우
		if( board.getSeqNo() == 0 ) {
			// 순서번호의 MAX
			int maxSeqNo = mapper.maxSeqNo();
			board.setSeqNo(maxSeqNo+1);
		}
		
		mapper.answerCreate(board);
		
	}

	@Override
	public int countAnswer(Integer groupNo) throws Exception {
		return mapper.countAnswer(groupNo);
	}

	@Override
	public int readDepthNo(Integer boardNo) throws Exception {
		return mapper.readDepthNo(boardNo);
	}

	@Override
	public int maxSeqNo() throws Exception {
		return mapper.maxSeqNo();
	}

	@Override
	public int readGroupNo(Integer boardNo) throws Exception {
		return mapper.readGroupNo(boardNo);
	}

	@Override
	public void view(Integer boardNo) throws Exception {
		mapper.view(boardNo);
	}
	
	

}
