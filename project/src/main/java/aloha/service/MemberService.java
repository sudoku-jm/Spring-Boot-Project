package aloha.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import aloha.domain.Member;
import aloha.domain.MemberImg;
import aloha.domain.MemberInfo;

public interface MemberService {
	
	//회원가입
	public void register(Member member,MemberInfo memberInfo) throws Exception;
	
	public List<Member> list() throws Exception;
	// 회원 정보 조회
	public Member read(String userId) throws Exception;
	
	//비밀번호 확인
	public boolean checkPassword(Member member) throws Exception;
	
	//비밀번호 변경
	public void changePassword(Member member) throws Exception;
	
	//프로필 변경
	public void updateProfile(MemberImg img) throws Exception;

	//회원정보 변경
	public void changeProfile(Member member, MemberInfo memberInfo) throws Exception;

	//회원 부가정보 조회
	public MemberInfo readMemberInfo(int userNo) throws Exception;


	// 회원 프로필 이미지 조회
	public MemberImg readProfileImg(@Param("userNo") int userNo) throws Exception;

}
