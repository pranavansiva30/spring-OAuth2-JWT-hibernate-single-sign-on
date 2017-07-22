package com.wavesdev.oauth2.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.util.WebUtils;

import com.wavesdev.oauth2.model.User;

import io.jsonwebtoken.Claims;
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
	 
	 
	public static OAuth2AuthenticationToken getOAuth2AuthenticationToken(String token,String jwtKey) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException{
			
		
		Claims claims=Jwts.parser().setSigningKey(jwtKey.getBytes("UTF-8")).parseClaimsJws(token).getBody();
	
		  ArrayList<String >authorities=(ArrayList<String >)claims.get("authorities");
          ArrayList<SimpleGrantedAuthority> authorityList=new ArrayList<SimpleGrantedAuthority>();
          for (String role : authorities) {
          	authorityList.add(new SimpleGrantedAuthority(role));
			}
          
         User user=new User();
         user.setUsername(claims.get("user_name").toString());
         

         
          OAuth2AuthenticationToken oAuth2AuthenticationToken =
                  new OAuth2AuthenticationToken(user,token, authorityList);
         // oAuth2AuthenticationToken.setDetails();
	
          return oAuth2AuthenticationToken;
	
	}
}
