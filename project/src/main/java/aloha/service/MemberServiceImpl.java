package aloha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import aloha.domain.Member;
import aloha.domain.MemberAuth;
import aloha.domain.MemberImg;
import aloha.domain.MemberInfo;
import aloha.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberMapper mapper;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void register(Member member,MemberInfo memberInfo) throws Exception {
		mapper.create(member);
		member = mapper.read(member.getUserId());
		int userNo = member.getUserNo();
		//권한 추가
		MemberAuth memberAuth = new MemberAuth();
		memberAuth.setUserNo(userNo); //회원번호를 받아서 권한테이블에 추가
		memberAuth.setAuth("ROLE_USER");	
		mapper.createAuth(memberAuth);
		
		//회원 부가 정보 추가
		memberInfo.setUserNo(userNo);
		mapper.createMemberInfo(memberInfo);
	}

	@Override
	public List<Member> list() throws Exception {
		return mapper.list();
	}

	@Override
	public Member read(String userId) throws Exception {
		return mapper.read(userId);
	}

	@Override
	public void updateProfile(MemberImg img) throws Exception {
		// 대표 프로필 사진이 있는지?
		int profileCount = mapper.checkProfile(img);
		img.setCategory("thumbnail");
		
		boolean checkProfile = profileCount > 0 ? true : false; // 대표 사진이 있으면 true 업데이트 없으면 false
		
		// 대표 프로필 있으면 update
		if( checkProfile ) {
			// 새로 변경할 이미지는 추가.
			// 기존 대표 프로필은 category 데이터를 thumbnail에서 img로 변경
			mapper.updateProfile(img);
		}
		mapper.insertProfile(img);
	}

	@Override
	public boolean checkPassword(Member member) throws Exception {
		Member realMember = mapper.checkPassword(member);
		
		String encodedPassword = realMember.getUserPw(); //
		String rawPassword = member.getUserPw();
		
		if( passwordEncoder.matches(rawPassword, encodedPassword ))
			return true;
		
		return false;
	}

	
	@Override
	public void changePassword(Member member) throws Exception {
		
		// 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode( member.getUserPw() );
		member.setUserPw(encodedPassword);
		
		mapper.changePassword(member);
		
	}

	@Override
	public void changeProfile(Member member, MemberInfo memberInfo) throws Exception {
		mapper.changeProfile(member); //userId를 받아와서 변경
		
		member = mapper.read(member.getUserId());
		int userNo = member.getUserNo();
		memberInfo.setUserNo(userNo);
		
		mapper.changeInfo(memberInfo); //userNo로 변경
		
	}

	@Override
	public MemberInfo readMemberInfo(int userNo) throws Exception {
		return mapper.readMemberInfo(userNo);
	}

	
	@Override
	public MemberImg readProfileImg(int userNo) throws Exception {
		return mapper.readProfileImg(userNo);
	}
	
	
}

















