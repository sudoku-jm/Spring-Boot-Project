package aloha.domain;

import java.util.Date;

import lombok.Data;

@Data
public class BoardAttach {
	private int fileNo;
	private String fullName;
	private String fileName;
	private int boardNo;
	private Date regDate;
}
