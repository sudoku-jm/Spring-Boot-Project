package aloha.mapper;

import org.apache.ibatis.annotations.Mapper;

import aloha.domain.FileAttach;

@Mapper
public interface FileMapper {

	// 파일 삭제
	public void deleteFile(FileAttach fileAttach) throws Exception;
	
}
