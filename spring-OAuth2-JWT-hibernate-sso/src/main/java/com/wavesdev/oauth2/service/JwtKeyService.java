package com.wavesdev.oauth2.service;

import org.springframework.security.core.Authentication;

import com.wavesdev.oauth2.security.JwtKey;





public interface JwtKeyService {
	public JwtKey getJwtKey();
	public String getAccessToken(Authentication authentication);
}
