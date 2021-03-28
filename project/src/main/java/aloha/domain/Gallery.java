package aloha.domain;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data
public class Gallery {

	private int rowNo;
	private int boardNo;
	private int groupNo;				// 그룹번호
	private int depthNo;				// 계층번호
	private int seqNo;					// 순서번호
	
	private String title;
	private String content;
	private String writer;
	private Date regDate;
	private MultipartFile[] file;		// 파일정보
	private String[] filePath;			// 파일경로
	
	private MultipartFile thumbnail;	// 썸네일 이미지파일 정보
	private String thumbnailPath;		// 썸네일 경로
	private String thumbnailName;		// 썸네일 파일 이름
	
	private int view;					// 조회수
	
	
}
