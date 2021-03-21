package aloha.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import aloha.domain.BoardAttach;
import aloha.service.BoardService;

@Controller
@RequestMapping("/file")
public class FileController {
	
	
	private static final Logger log = LoggerFactory.getLogger(FileController.class);

	
	@Autowired
	private BoardService service;
	
	@RequestMapping(value = "/fileDownload", method = RequestMethod.GET )
	public void fileDownload(HttpServletResponse response, HttpServletRequest request, @RequestParam Map<String,String> paramMap) {
		String fullName = paramMap.get("fullName");		// 파일 전체경로
		String fileName = paramMap.get("fileName");		// 파일명
		
	
		// 다운로드할 파일
		File file = new File(fullName);
		
		FileInputStream fileInputStream = null;
	    ServletOutputStream servletOutputStream = null;
	
	    try{
	        String downName = null;
	        String browser = request.getHeader("User-Agent");
	    //파일 인코딩
	    if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")){//브라우저 확인 파일명 encode  
	    	downName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
	    }else{
	        downName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
	    }
	    
	    response.setHeader("Content-Disposition","attachment;filename=\"" + downName+"\"");
	    response.setContentType("text/html");
	    response.setHeader("Content-Transfer-Encoding", "binary;");
 	 
	    fileInputStream = new FileInputStream(file);
	    servletOutputStream = response.getOutputStream();
 	 
	    byte b [] = new byte[1024];
	    int data = 0;
 	 
	    while((data=(fileInputStream.read(b, 0, b.length))) != -1){
	        servletOutputStream.write(b, 0, data);
	    }
	    
	    servletOutputStream.flush();//출력
	        
	    }catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	        if(servletOutputStream!=null){
	            try{
	                servletOutputStream.close();
	            }catch (IOException e){
	                e.printStackTrace();
	            }
	        }
	        if(fileInputStream!=null){
	            try{
	                fileInputStream.close();
	            }catch (IOException e){
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	@RequestMapping(value = "/zipDownload", method = RequestMethod.GET )
	public void zipDownload(HttpServletResponse response, HttpServletRequest request, @RequestParam Map<String,String> paramMap) throws Exception {
		// 글 번호
		Integer boardNo = Integer.parseInt( paramMap.get("boardNo") );
		// 글 제목
		String title = paramMap.get("title");
		
		// 글 번호에 따른 모든 첨부파일 리스트 조회
		List<BoardAttach> fileList = service.readFileList(boardNo);
		
		// 다운로드 파일 명
		String zipFile = "temp.zip";
		String downloadFileName = title;
		
		//파일 인코딩
		String browser = request.getHeader("User-Agent");
		if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")){//브라우저 확인 파일명 encode  
		   downloadFileName = URLEncoder.encode(downloadFileName,"UTF-8").replaceAll("\\+", "%20");
		    
		}else{
		   downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		  
		  try {
		     
		     FileOutputStream fout = new FileOutputStream(zipFile);
		     ZipOutputStream zout = new ZipOutputStream(fout);
		     
		     for (int i = 0; i < fileList.size(); i++) {
		        
		        // 본래 파일명 유지, 경로제외 파일압축을 위해 new File로
			    ZipEntry zipEntry = new ZipEntry(new File(fileList.get(i).getFileName()).getName());
			    zout.putNextEntry(zipEntry);
			    
			    FileInputStream fin = new FileInputStream(fileList.get(i).getFullName());
			    byte[] buffer = new byte[1024];
			    int length;
		    
		    
			    while( (length = fin.read(buffer)) > 0 ) {
			       zout.write(buffer, 0 ,length);
		    }
		    
		    zout.closeEntry();
		    fin.close();
		 }
		 
		 zout.close();
		 
		 response.setContentType("application/zip");
		 response.addHeader("Content-Disposition", "attachment; filename=" + downloadFileName + ".zip");
		     
		     FileInputStream fis = new FileInputStream(zipFile);
		     BufferedInputStream bis = new BufferedInputStream(fis);
		     ServletOutputStream so = response.getOutputStream();
		     BufferedOutputStream bos = new BufferedOutputStream(so);
		     
		     byte[] data = new byte[2048];
		     int input = 0;
		     
		     while( (input=bis.read(data)) != -1 ) {
		        bos.write(data, 0, input);
		        bos.flush();
		     }
		     
		     if(bos != null) bos.close();
		     if(bis != null) bis.close();
		     if(so != null) so.close();
		     if(fis != null) fis.close();
		           
		     
		  } catch (Exception e) {
		     e.printStackTrace();
		  }
 		
 	}
	
	@RequestMapping(value = "/deleteFile", method = RequestMethod.GET)
	public String deleteFile(Integer fileNo, String fullName) throws Exception {
		
		// 파일 삭제
		File file = new File(fullName);
		
		// 실제로 파일이 존재하는 확인
		if(file.exists()) {
			if(file.delete()) {
				log.info("삭제한 파일 : " + fullName);
				log.info("파일삭제 성공");
			} else {
				log.info("파일삭제 실패");
				
			}
		} else {
			log.info("삭제(실패) : " + fullName);
			log.info("파일이 존재하지 않습니다.");
		}
		
		
		service.deleteFile(fileNo);
		
		return "subpage/board/success";
	}
	
}







