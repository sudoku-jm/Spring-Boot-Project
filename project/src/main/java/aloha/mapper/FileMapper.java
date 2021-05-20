package aloha.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import aloha.domain.FileAttach;

@Mapper
public interface FileMapper {

	// 파일 삭제
	public void deleteFile(FileAttach fileAttach) throws Exception;
	
	//파일 목록 조회 : 각각의 매개변수를 넘기므로 @param을 사용해서 파라미터 이름을 넣어준다.
	public List<FileAttach> readFileList(@Param("boardNo") Integer boardNo,@Param("table") String table) throws Exception;
	
	//파일 조회
	public FileAttach readFile(@Param("fileNo") Integer fileNo,@Param("table") String table) throws Exception;

	//수정 시, seq 갱신
	public void initUpdateSeq(FileAttach fileAttach) throws Exception;
}
