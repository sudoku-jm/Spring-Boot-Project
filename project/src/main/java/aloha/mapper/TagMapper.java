package aloha.mapper;

import org.apache.ibatis.annotations.Mapper;

import aloha.domain.Tag;

@Mapper
public interface TagMapper {

	//태그등록
	public void createTag(Tag tag) throws Exception;
}
