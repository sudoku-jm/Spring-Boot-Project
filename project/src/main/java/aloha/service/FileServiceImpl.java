package aloha.service;

import java.util.List;

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

	@Override
	public List<FileAttach> readFileList(Integer boardNo, String table) throws Exception {
		return mapper.readFileList(boardNo, table);
	}

	@Override
	public FileAttach readFile(Integer fileNo, String table) throws Exception {
		return mapper.readFile(fileNo,table);
	}

}
