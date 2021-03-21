package aloha.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import aloha.common.security.domain.CustomUser;
import aloha.domain.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler { 
	
	private static final Logger logger = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException { 
		CustomUser customUser = (CustomUser)authentication.getPrincipal();
		Member member = customUser.getMember();
	    
		logger.info("Userid = " + member.getUserId());
		
		super.onAuthenticationSuccess(request, response, authentication);
	} 
	
}
