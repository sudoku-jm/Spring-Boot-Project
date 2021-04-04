package aloha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aloha.domain.FileAttach;
import aloha.mapper.FileMapper;

@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private FileMapper mapper;
	
	@Override
	public void deleteFile(FileAttach fileAttach) throws Exception {
		mapper.deleteFile(fileAttach);
	}

}
