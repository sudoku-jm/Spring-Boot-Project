package aloha.config;


import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import aloha.common.security.CustomAccessDeniedHandler;
import aloha.common.security.CustomLoginSuccessHandler;
import aloha.common.security.CustomUserDetailsService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
	
	@Autowired
	DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("security config......");
		
		/*
		// 로그인 설정
		http.formLogin()
		.loginPage("/auth/login")
		.loginProcessingUrl("/login")
		.successHandler( createAuthenticationSuccessHandler() );
		
		
		// 로그아웃 설정
		http.logout()
		.logoutUrl("/auth/logout")
		.invalidateHttpSession(true);
		
		
		// 예외처리 - 접근거부 처리
		http.exceptionHandling()
		.accessDeniedHandler( createAccessDeniedHandler() );
		
		
		// 자동로그인 설정
		http.rememberMe()
		.key("aloha")
		.tokenRepository( createJDBCRepository() )
		.tokenValiditySeconds( 60*60*24 );
		*/
		// url 에 대한 허용여부 설정
		// permitAll() 				:  	모두에게 접근 허용
		// hasRole(), hasAnyRole()	:   특정권한을 가진 사용자만 접근 허용	   
		http.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/css/**", "/js/**", "/img/**").permitAll()
//						.antMatchers("/guest/**").permitAll()
//						.antMatchers("/member/**").hasAnyRole("USER","ADMIN")
//						.antMatchers("/admin/**").hasRole("	ADMIN")		
				.antMatchers("/**").permitAll()
				.anyRequest().authenticated();
		
		// 로그인 설정
		// 로그인 페이지 		: /auth/loginForm
		// 로그인 에러 페이지		: /auth/loginError
		http.formLogin()
			.loginPage("/auth/login")
			.loginProcessingUrl("/auth/login")
			.failureUrl("/auth/loginError")
			.usernameParameter("username")
			.passwordParameter("password")
			.permitAll();
		
		
		// 로그아웃 설정
		// 로그아웃 처리 요청 		: /auth/logout
		http.logout()
			.logoutUrl("/auth/logout")
			.invalidateHttpSession(true)
			.logoutSuccessUrl("/")
			.permitAll();
		
		
		// 접근거부 처리
		http.exceptionHandling()
			.accessDeniedPage("/auth/accessError");
		
	}
	
	
	@Bean
	public PersistentTokenRepository createJDBCRepository() {
		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		repo.setDataSource(dataSource);
		return repo;
	}

	@Bean
	public AccessDeniedHandler createAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public AuthenticationSuccessHandler createAuthenticationSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(createUserDetailsService())
		.passwordEncoder(createPasswordEncoder());
	}

	@Bean
	protected PasswordEncoder createPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService createUserDetailsService() {
		return new CustomUserDetailsService();
	}

	
}







