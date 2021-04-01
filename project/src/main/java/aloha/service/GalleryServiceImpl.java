package aloha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aloha.domain.Gallery;
import aloha.domain.GalleryAttach;
import aloha.domain.Page;
import aloha.domain.Reply;
import aloha.mapper.GalleryMapper;

@Service
public class GalleryServiceImpl implements GalleryService {

	@Autowired
	private GalleryMapper mapper;
	
	@Override
	public List<Gallery> list() throws Exception {
		return mapper.list();
	}

	@Override
	public void register(Gallery gallery) throws Exception {
		mapper.create(gallery);
		
		int boardNo = mapper.maxBoardNo();
		gallery.setGroupNo(boardNo);
		gallery.setBoardNo(boardNo);
		
		mapper.updateGroupNo(gallery);
	}

	@Override
	public Gallery read(Integer boardNo) throws Exception {
		return mapper.read(boardNo);
	}

	@Override
	public void modify(Gallery gallery) throws Exception {
		mapper.update(gallery);
	}

	@Override
	public void remove(Integer boardNo) throws Exception {
		mapper.delete(boardNo);
		
	}

	@Override
	public List<Gallery> search(String keyword) {
		return mapper.search(keyword);
	}

	@Override
	public void uploadFile(GalleryAttach attach) throws Exception {
		mapper.uploadFile(attach);
	}

	@Override
	public void uploadModifyFile(GalleryAttach attach) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GalleryAttach> readFileList(Integer boardNo) throws Exception {
		return mapper.readFileList(boardNo);
	}

	@Override
	public GalleryAttach readFile(Integer fileNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFile(Integer fileNo) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer totalCount() throws Exception {
		return mapper.totalCount();
	}

	@Override
	public Integer totalCount(String keyword) throws Exception {
		return mapper.totalCountByKeyword(keyword);
	}

	@Override
	public List<Gallery> list(Page page) throws Exception {
		return mapper.listWithPage(page);
	}

	@Override
	public List<Gallery> search(Page page) throws Exception {
		return mapper.searchWithPage(page);
	}

	@Override
	public void replyRegister(Reply reply) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Reply> replyList(Integer boardNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void replyModify(Reply reply) throws Exception {
		mapper.replyCreate(reply);
	}

	@Override
	public void replyRemove(Integer reply_no) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getAttach(Integer boardNo) throws Exception {
		return mapper.getAttach(boardNo);
	}

	@Override
	public void updateGroupNo(Gallery gallery) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void answerRegister(Gallery gallery) throws Exception {
		// 계층번호
		int depthNo = mapper.readDepthNo(gallery.getBoardNo());
		// 계층번호 = 부모글의 계층번호 + 1
		gallery.setDepthNo(depthNo+1);
		
		
		// 부모글이 답글인 경우
		if( gallery.getSeqNo() == 0 ) {
			// 순서번호의 MAX
			int maxSeqNo = mapper.maxSeqNo();
			gallery.setSeqNo(maxSeqNo+1);
		}
		
		mapper.answerCreate(gallery);
	}

	@Override
	public int countAnswer(Integer groupNo) throws Exception {
		return mapper.countAnswer(groupNo);
	}

	@Override
	public int readDepthNo(Integer boardNo) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readGroupNo(Integer boardNo) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int maxSeqNo() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void view(Integer boardNo) throws Exception {
		mapper.view(boardNo);
	}

}
