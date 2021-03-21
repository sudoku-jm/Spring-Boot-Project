package aloha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/auth")
public class LoginController {
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	// custom 로그인 페이지
	@RequestMapping("/login")
	public String loginForm(String error, String logout, Model model) {
		
		if(error != null) {
			model.addAttribute("error", "Login Error!!!");
		}
		
		if(logout != null) {
			model.addAttribute("error", "Logout!!!");
		}
		
		return "auth/loginForm";
	}
	
	
	// custom 로그아웃 페이지
	@GetMapping("/loginError")
	public String loginError(String error, String logout, Model model) {
		
		model.addAttribute("error", "가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.");
		
		return "auth/loginForm";
	}
	
	// custom 로그아웃 페이지
	@GetMapping("/logout")
	public String logoutForm() {
		return "auth/logoutForm";
	}

}




















