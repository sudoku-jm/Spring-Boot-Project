package aloha.domain;

import java.util.Date;

import lombok.Data;

@Data
public class MemberImg {

	private int imgNo;
	private int userNo;
	
	private String fullName;
	private String fileName;
	private String category;

	private Date regDate;
	private Date updDate;
	
	
}
