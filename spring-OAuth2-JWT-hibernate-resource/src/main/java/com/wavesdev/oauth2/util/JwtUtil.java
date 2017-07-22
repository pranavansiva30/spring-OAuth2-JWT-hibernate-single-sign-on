package com.wavesdev.oauth2.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JwtUtil {
	 public static String getSubject(HttpServletRequest httpServletRequest, String jwtTokenCookieName, String signingKey) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException{
	        String token = WebUtils.getCookie(httpServletRequest, jwtTokenCookieName).getValue();
	        if(token == null) return null;
	        String jwtstring=Jwts.parser().setSigningKey(signingKey.getBytes("UTF-8")).parseClaimsJws(token).getBody().getSubject();
	        return jwtstring;
	    }
}
