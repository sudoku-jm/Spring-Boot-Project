package aloha.service;

import aloha.domain.FileAttach;

public interface FileService {

	// 파일 삭제
	public void deleteFile(FileAttach fileAttach) throws Exception;
}
