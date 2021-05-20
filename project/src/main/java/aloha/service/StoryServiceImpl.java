package aloha.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aloha.domain.Page;
import aloha.domain.Reply;
import aloha.domain.Story;
import aloha.domain.StoryAttach;
import aloha.mapper.StoryMapper;

@Service
public class StoryServiceImpl implements StoryService {
	
	
	private static final Logger log = LoggerFactory.getLogger(StoryServiceImpl.class);


	@Autowired
	private StoryMapper mapper;
	
	@Override
	public List<Story> list() throws Exception {
		return mapper.list();
	}

	@Override
	public void register(Story story) throws Exception {
		mapper.create(story);
	}

	@Override
	public Story read(Integer boardNo) throws Exception {
		return mapper.read(boardNo);
	}

	@Override
	public void modify(Story story) throws Exception {
		mapper.update(story);
	}

	@Override
	public void remove(Integer boardNo) throws Exception {
		mapper.read(boardNo);
	}

	@Override
	public List<Story> search(String keyword) {
		return mapper.search(keyword);
	}

	@Override
	public void uploadFile(StoryAttach attach) throws Exception {
		mapper.uploadFile(attach);
	}

	@Override
	public void uploadModifyFile(StoryAttach attach) throws Exception {
		mapper.uploadUpdateFile(attach);
	}

	@Override
	public List<StoryAttach> readFileList(Integer boardNo) throws Exception {
		return mapper.readFileList(boardNo);
	}

	@Override
	public StoryAttach readFile(Integer fileNo) throws Exception {
		return mapper.readFile(fileNo);
	}

	@Override
	public void deleteFile(Integer fileNo) throws Exception {
		mapper.deleteFile(fileNo);
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
	public List<Story> list(Page page) throws Exception {
		return mapper.listWithPage(page);
	}

	@Override
	public List<Story> search(Page page) throws Exception {
		return mapper.searchWithPage(page);
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
	public List<String> getFullName(Integer boardNo) throws Exception {
		return mapper.getFullName(boardNo);
	}
	
	@Override
	public List<StoryAttach> getAttach(Integer boardNo) throws Exception {
		return mapper.getAttach(boardNo);
	}

	@Override
	public void updateGroupNo(Story story) throws Exception {
		mapper.updateGroupNo(story);
	}

	@Override
	public void answerRegister(Story story) throws Exception {
		mapper.answerCreate(story);
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
	public int readGroupNo(Integer boardNo) throws Exception {
		return mapper.readGroupNo(boardNo);
	}

	@Override
	public int maxSeqNo() throws Exception {
		return mapper.maxSeqNo();
	}

	@Override
	public void view(Integer boardNo) throws Exception {
		mapper.view(boardNo);
	}

	@Override
	public StoryAttach readThumbnail(Integer boardNo) throws Exception {
		return mapper.readThumbnail(boardNo);
	}

	@Override
	public void updateThumbnailNo(Integer boardNo, Integer thumbnailNo) throws Exception {
		
		log.info("boardNo : " + boardNo);
		log.info("thumbnailNo : " + thumbnailNo);

		
		// 기존 category:thumbnail인 첨부파일을 contentType으로 지정(임시 : "img" )
		mapper.cancelThumbnail(boardNo,"img");

		// 새 섬네일로 지정
		mapper.updateThumbnailNo(boardNo, thumbnailNo);
		
		
		// 새로 지정한 thumbnailNo 이외에 나머지 첨부파일의 seq를 1,2,3,4로 수정
		mapper.initSeq(boardNo,thumbnailNo);
		
	}

	@Override
	public int maxSeqByBoardNo(Integer boardNo) throws Exception {
		return mapper.maxSeqByBoardNo(boardNo);
	}

}
