package aloha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aloha.domain.Member;
import aloha.domain.MemberAuth;
import aloha.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberMapper mapper;

	@Override
	public void register(Member member) throws Exception {
		mapper.create(member);
		member = mapper.read(member.getUserId());
		
		MemberAuth memberAuth = new MemberAuth();
		memberAuth.setUserNo(member.getUserNo());
		memberAuth.setAuth("ROLE_USER");
		
		mapper.createAuth(memberAuth);
	}

	@Override
	public List<Member> list() throws Exception {
		return mapper.list();
	}

	@Override
	public Member read(String userId) throws Exception {
		return mapper.read(userId);
	}
	
	
}
