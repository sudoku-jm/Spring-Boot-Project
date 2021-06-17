package aloha.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import aloha.domain.Member;
import aloha.domain.MemberAuth;
import aloha.domain.MemberImg;
import aloha.domain.MemberInfo;

@Mapper
public interface MemberMapper {
	public void create(Member member) throws Exception;

	public void createAuth(MemberAuth memberAuth);
	
	public Member readByUserId(String userId);
	
	public List<Member> list() throws Exception;
	
	public Member read(String userId) throws Exception;
	
	
	//비밀번호 확인
	public Member checkPassword(Member member) throws Exception;
	
	//비밀번호 변경
	public void changePassword(Member member) throws Exception;
	
	// 대표 프로필 사진 체크
	public int checkProfile(MemberImg img) throws Exception;
	
	// 대표 프로필 변경
	public void updateProfile(MemberImg img) throws Exception;
	
	// 대표 프로필 추가
	public void insertProfile(MemberImg img) throws Exception;

	// 회원 기본정보 변경
	public void changeProfile(Member member) throws Exception;

	// 회원 부가정보 변경
	public void changeInfo(MemberInfo memberInfo) throws Exception;

	// 회원 부가정보 등록
	public void createMemberInfo(MemberInfo memberInfo) throws Exception;

	// 회원 부가정보 조회. 변수를 하나만 넘길때는 @Param으로 넘김
	public MemberInfo readMemberInfo(@Param("userNo") int userNo) throws Exception;
	
	// 회원 프로필 이미지 조회
	public MemberImg readProfileImg(@Param("userNo") int userNo) throws Exception;

}
