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
	}

	@Override
	public Gallery read(Integer boardNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modify(Gallery gallery) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Integer boardNo) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Gallery> search(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void uploadFile(GalleryAttach attach) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadModifyFile(GalleryAttach attach) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GalleryAttach> readFileList(Integer boardNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer totalCount(String keyword) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Gallery> list(Page page) throws Exception {
		return mapper.listWithPage(page);
	}

	@Override
	public List<Gallery> search(Page page) throws Exception {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replyRemove(Integer reply_no) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getAttach(Integer boardNo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateGroupNo(Gallery gallery) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void answerRegister(Gallery gallery) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int countAnswer(Integer groupNo) throws Exception {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		
	}

}
