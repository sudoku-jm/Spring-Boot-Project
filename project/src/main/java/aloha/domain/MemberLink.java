package aloha.domain;

import java.util.Date;

import lombok.Data;

@Data
public class MemberLink {

	private int linkNo;
	private int userNo;
	
	private String linkType;
	private String link;

	private Date regDate;
	private Date updDate;
	
}
