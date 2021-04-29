package aloha.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Tag {

	private int tagNo;
	private int boardNo;
	private String table;
	private String name;
	private Date redDate;
	
	public Tag(String table, String name) {
		this.table = table;
		this.name = name;
	}
	
	
}
