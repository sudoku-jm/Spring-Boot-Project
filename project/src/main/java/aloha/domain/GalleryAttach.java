package aloha.domain;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;


@Data
public class GalleryAttach {
	
	private int fileNo;
	private String fullName;
	private String fileName;
	private String category;
	private int boardNo;
	private Date regDate;
	private Date updDate;
	

}
