package com.wavesdev.oauth2.security;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;


import com.wavesdev.oauth2.security.exceptions.JwtExpiredTokenException;
import com.wavesdev.oauth2.service.JwtKeyService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * @author pranavan
 *
 */
public class JwtAuthenticationProvider implements AuthenticationProvider, InitializingBean {
	private boolean throwExceptionWhenTokenRejected = false;
	private JwtKeyService jwtKeyService=null;

	@Override
	public void afterPropertiesSet() throws Exception {
		 Assert.notNull(jwtKeyService, "jwtKeyService must be set");

	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		OAuth2AuthenticationToken result = null;

		if (!supports(authentication.getClass())) {
			return null;
		}
		if (authentication.getCredentials() == null) {

			if (throwExceptionWhenTokenRejected) {
				throw new BadCredentialsException("No pre-authenticated credentials found in request.");
			}
			return null;
		}
		String token = authentication.getCredentials().toString();

		JwtKey jwtKey = jwtKeyService.getJwtKey();
		try {
			result = JwtUtil.getOAuth2AuthenticationToken(token, jwtKey.getValue());
		}  catch (UnsupportedEncodingException | UnsupportedJwtException | MalformedJwtException
				| IllegalArgumentException | SignatureException e) {
			 throw new BadCredentialsException("Invalid JWT token: ", e);
		}
		catch (ExpiredJwtException expiredEx) {
			 throw new JwtExpiredTokenException("JWT Token expired", expiredEx);
		}
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2AuthenticationToken.class.isAssignableFrom(authentication);
	}

	
	public void setJwtKeyService(JwtKeyService jwtKeyService) {
		this.jwtKeyService = jwtKeyService;
	}

	

		

}
