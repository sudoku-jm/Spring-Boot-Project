package aloha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/popup")
public class PopupController {

	@GetMapping("/close")
	public void close() throws Exception {
		
	}
	
}
