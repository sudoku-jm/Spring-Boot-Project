package aloha.controller;

import java.security.Principal;

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
	public String register(@Validated Member member, BindingResult result, Model model, RedirectAttributes rttr) throws Exception {
		if(result.hasErrors()) {
			return "user/register";
		}
		
		String inputPassword = member.getUserPw();
		member.setUserPw(passwordEncoder.encode(inputPassword));
		
		service.register(member);
		
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
		
		log.info("member : " + member);
		
		model.addAttribute("member",member);
		
				
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
	
	
}











