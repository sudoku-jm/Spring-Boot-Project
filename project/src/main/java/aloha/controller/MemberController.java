package aloha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
	
	
}
