package aloha.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import aloha.domain.Member;
import aloha.domain.MemberImg;
import aloha.domain.MemberInfo;
import aloha.domain.MemberLink;
import aloha.service.MemberService;

@Controller
@RequestMapping("/user")
public class MemberController {
	
	
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService service;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	// 회원가입 화면
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void registerForm(Member member, Model model) throws Exception {
		log.info("register Member.....");
	}
	

	// 회원가입 처리
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@Validated Member member,MemberInfo memberInfo, BindingResult result, Model model, RedirectAttributes rttr) throws Exception {
		
		/*
		 * @Validated 유효성 검사 후. 예) 최소 4글자부터~ 영문자,숫자만 가능합니다~ 등... 메세지 리턴.
		 * BindingResult 유효성 문제 있을 경우 , 다시 돌아 가게끔
		 * */
		
		if(result.hasErrors()) {
			return "user/register";
		}
		
		String inputPassword = member.getUserPw();
		member.setUserPw(passwordEncoder.encode(inputPassword));
		
		service.register(member,memberInfo);
	
		rttr.addFlashAttribute("userName", member.getUserName());
		
		return "redirect:/user/registerSuccess";
	}
	
	// 회원가입 성공 화면
	@RequestMapping(value = "/registerSuccess", method = RequestMethod.GET)
	public void registerSuccess(Model model) throws Exception{
		
	}
	
	// 회원목록 화면
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void list(Model model) throws Exception{
		model.addAttribute("list", service.list());
	}
	
	
	//마이페이지
	@GetMapping("/mypage")
	public void mypage(Model model, Principal user) throws Exception{
		
		String userId = user.getName();			//로그인된 userId
		
		Member member = service.read(userId);	// 회원정보
		int userNo = member.getUserNo();
		
		//회원 부가정보 가져오기
		MemberInfo memberInfo = service.readMemberInfo(userNo);
		
		//프로필 이미지 조회
		MemberImg memberImg = service.readProfileImg(userNo);
		
		// sns 링크 조회
		List<MemberLink> snsList = service.readSnsUrl(userNo);
		
		// 프로필 전체 조회
		List<MemberImg> profileList = service.profileList(userNo);
		
		log.info("member : " + member);
		log.info("memberInfo : " + memberInfo);
		log.info("memberImg : " + memberImg);
		
		model.addAttribute("member",member);
		model.addAttribute("memberInfo",memberInfo);
		model.addAttribute("memberImg",memberImg);
		model.addAttribute("snsList",snsList);
		model.addAttribute("profileList",profileList);
	
		
				
	}
	
	
	// 회원정보 수정 처리
	@PostMapping("/change")
	public String profileChange(Model model,Member member,MemberInfo memberInfo,String[] snsName,String[] snsUrl) throws Exception{
		
		//넘어오는 정보 확인
		log.info(member.toString());
		log.info(memberInfo.toString());
		
		for(int i = 0; i < snsName.length; i++) {
			MemberLink memberLink = new MemberLink();
			memberLink.setUserNo(member.getUserNo());
			memberLink.setLinkType(snsName[i]);
			memberLink.setLink(snsUrl[i]);
			
			service.updateSnsUrl(memberLink);
		}
		
		service.changeProfile(member,memberInfo);
		/*
		 * Forward 방식 
		 	: 사용자가 최초로 요청한 요청정보는 다음 URL에서도 유효. 
		 	  시스템에 변화가 생기지 않는 단순 조회 요청의 경우.
		 * Redirect 방식 
		  	: 최초 요청을 받은 URL1에서 클라이언트에게 rediret할 URL2를 반환, 
		  	클라이언트에서는 새로운 요청을 생성하여 URL2에 다시 요청을 보낸다. 
		  	최초의 Request와 Respone갹체는 유효하지 않고 새롭게 생성. 
		  	시스템에 변화가 생기는 요청의 경우에 redirection을 사용.
		 * */
		return "redirect:/user/mypage"; //수정 후 url이 change에 머물러 있지 않게 함. mypage로 돌아가게함.
	}
	
	
	
	@GetMapping("/check/passCheck")
	public void passCheckPopup(Model model) throws Exception{
		
	}
	
	@PostMapping("/check/passCheck")
	public String passCheck(Model model, String password, Principal user, RedirectAttributes rttr) throws Exception{
		
		String userId = "";
		if(user != null) {
			userId = user.getName();
			model.addAttribute("userId", userId);
			
		}else{
			return "redirect:/popup/close";
		}
		
		
		log.info("password : " + password);
		String userPw = password;
		Member member = new Member(userId, userPw);
		
		log.info("userPw : " + userPw);
		
		boolean check = service.checkPassword(member);
		
		if( check ) {
			rttr.addFlashAttribute("check", check);
			return "redirect:/user/check/passChange";
		}else {
			rttr.addFlashAttribute("check", check);
			return "redirect:/user/check/passCheck";
		}
		
		
	}
	
	
	@GetMapping("/check/passChange")
	public void passChangePopup(Model model) throws Exception{
		
	}
	
	@PostMapping("/check/passChange")
	public String passChange(Model model, String password, String passwordCheck, Principal user, RedirectAttributes rttr) throws Exception{
		
		boolean check = false;
		
		String userId = "";
		if(user != null) {
			userId = user.getName();
			model.addAttribute("userId", userId);
			
		}else{
			return "redirect:/popup/close";
		}
		
		//비밀번호 , 비밀번호 확인 일치할 경우
		if( password.equals(passwordCheck) ) {
			check = true;
			Member member = new Member(userId, password);
			service.changePassword(member);
			
			rttr.addFlashAttribute("msg", "비밀번호 변경이 완료되었습니다.");
			return "redirect:/popup/close";
		}
		
		//일치하지 않을 경우
		else {
			check = false;
			rttr.addFlashAttribute("check", check);
			return "redirect:/user/check/passChange";
		}
		
	}
	
	@GetMapping("/profile/info")
	public void profileInfo(Model model,Integer userNo) throws Exception{
		log.info("userNo : " + userNo);		

		model.addAttribute("user", service.readMemberInfo(userNo));
		model.addAttribute("profile", service.readProfileImg(userNo));

		// sns 링크 조회
		List<MemberLink> snsList = service.readSnsUrl(userNo);
	
		model.addAttribute("snsList",snsList);

	}

	
}











