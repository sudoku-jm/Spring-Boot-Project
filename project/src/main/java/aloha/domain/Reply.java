package aloha.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Reply {

	private int replyNo;
	private int boardNo;
	private String content;
	private String writer;
	private Date regDate;
	
}
