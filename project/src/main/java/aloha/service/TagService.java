package aloha.service;

import java.util.List;

import aloha.domain.Tag;

public interface TagService {
	//태그등록
	public void createTag(Tag tag) throws Exception;
	
	//태그 조회
	public List<Tag> readTagList(Tag tag) throws Exception;
}
