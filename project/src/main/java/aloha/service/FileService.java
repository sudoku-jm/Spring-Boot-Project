package aloha.service;

import java.util.List;

import aloha.domain.FileAttach;

public interface FileService {

	// 파일 삭제
	public void deleteFile(FileAttach fileAttach) throws Exception;

	//파일 목록 조회
	public List<FileAttach> readFileList(Integer boardNo,String table) throws Exception;
		
	//파일 조회
	public FileAttach readFile(Integer fileNo,String table)throws Exception;
}
