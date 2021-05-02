package aloha.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Tag {

	private int tagNo;
	private int boardNo;
	private String tableName;
	private String name;
	private Date redDate;
	
	public Tag(String tableName, String name) {
		this.tableName = tableName;
		this.name = name;
	}
	
	public Tag(String tableName, Integer boardNo) {
		this.tableName = tableName;
		this.boardNo = boardNo;
	}

	public Tag(int tagNo, int boardNo, String tableName, String name, Date redDate) {
		super();
		this.tagNo = tagNo;
		this.boardNo = boardNo;
		this.tableName = tableName;
		this.name = name;
		this.redDate = redDate;
	}
	
	
	
}
