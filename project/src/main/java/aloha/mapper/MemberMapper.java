package aloha.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import aloha.domain.Member;
import aloha.domain.MemberAuth;
import aloha.domain.MemberImg;

@Mapper
public interface MemberMapper {
	public void create(Member member) throws Exception;

	public void createAuth(MemberAuth memberAuth);
	
	public Member readByUserId(String userId);
	
	public List<Member> list() throws Exception;
	
	public Member read(String userId) throws Exception;
	
	// 대표 프로필 사진 체크
	public int checkProfile(MemberImg img) throws Exception;
	
	// 대표 프로필 변경
	public void updateProfile(MemberImg img) throws Exception;
	
	// 대표 프로필 추가
	public void insertProfile(MemberImg img) throws Exception;
	
}
