package aloha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

	
	@GetMapping("/noAuth")
	public void noAuth(Model model) throws Exception {
		model.addAttribute("msg", "접근 권한이 없습니다.");
		
	}
}
