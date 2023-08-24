package com.xantrix.webapp.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import lombok.extern.java.Log;

@Log
public class AuthEntryPoint extends BasicAuthenticationEntryPoint{

	private static String REALM= "REAME";
	
	@Override
	public void afterPropertiesSet() {
		setRealmName(REALM);
		super.afterPropertiesSet();
	}


	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		// TODO Auto-generated method stub
		super.commence(request, response, authException);
		
		String errMsg = "Userid e/o password non corrette";
		log.warning("Errore di sicurezza");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		//response.setContentType("application/json;charset=UTF=8");
		response.addHeader("WWW-Authenticate", "Basic realm=" +getRealmName()  );
		
		PrintWriter writer = response.getWriter();
		writer.println(errMsg);
	}
	
	
	
	
	

}
