package aloha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aloha.domain.Tag;
import aloha.mapper.TagMapper;

@Service
public class TagServiceImpl implements TagService{

	@Autowired
	private TagMapper mapper;
	
	@Override
	public void createTag(Tag tag) throws Exception {
		mapper.createTag(tag);
		
	}

}
