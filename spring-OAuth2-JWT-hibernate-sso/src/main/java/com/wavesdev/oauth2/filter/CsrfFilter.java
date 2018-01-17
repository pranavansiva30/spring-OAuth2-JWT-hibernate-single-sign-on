package com.wavesdev.oauth2.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class CsrfFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			String token = auth.getCredentials().toString();
		
				Cookie cookie = new Cookie("access_token", token);
				cookie.setPath("/");
				response.addCookie(cookie);
				filterChain.doFilter(request, response);
			

		}
		else{
			
			filterChain.doFilter(request, response);
		}

		

	}
}
