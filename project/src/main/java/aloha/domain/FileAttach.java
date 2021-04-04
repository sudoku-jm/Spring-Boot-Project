package aloha.domain;

import java.util.Date;

import lombok.Data;

@Data
public class FileAttach {
	
	private int fileNo;
	private String fullName;
	private String fileName;
	private String category;
	private int boardNo;
	private Date regDate;
	private Date updDate;
	
	private String table;
	
}
