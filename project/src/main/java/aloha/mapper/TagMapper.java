package aloha.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import aloha.domain.Tag;

@Mapper
public interface TagMapper {

	//태그등록
	public void createTag(Tag tag) throws Exception;
	
	//태그 조회
	public List<Tag> readTagList(Tag tag) throws Exception;
}
