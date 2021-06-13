package aloha.domain;

import java.util.Date;

import lombok.Data;

@Data	
public class MemberInfo {
	private int infoNo;
	private int userNo;
	
	private String nickname;
	private String email;
	private String url;
	
	
	private Date regDate;
	private Date updDate;
}
