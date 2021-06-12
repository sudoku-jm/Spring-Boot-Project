package aloha.service;

import java.util.List;

import aloha.domain.Member;
import aloha.domain.MemberImg;

public interface MemberService {
	public void register(Member member) throws Exception;
	
	public List<Member> list() throws Exception;
	
	public Member read(String userId) throws Exception;
	
	//프로필 변경
	public void updateProfile(MemberImg img) throws Exception;
}
