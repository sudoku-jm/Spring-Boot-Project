package aloha.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import aloha.domain.Member;
import aloha.domain.MemberAuth;

@Mapper
public interface MemberMapper {
	public void create(Member member) throws Exception;

	public void createAuth(MemberAuth memberAuth);
	
	public Member readByUserId(String userId);
	
	public List<Member> list() throws Exception;
	
	public Member read(String userId) throws Exception;
	
	
}
