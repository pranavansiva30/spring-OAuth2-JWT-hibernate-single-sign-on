package com.wavesdev.oauth2.service.impl;

import java.util.Arrays;
import java.util.LinkedHashMap;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wavesdev.oauth2.security.JwtKey;
import com.wavesdev.oauth2.security.OAuth2ServiceProperties;
import com.wavesdev.oauth2.service.JwtKeyService;





@Service("jwtKeyService")
public class JwtKeyServiceImpl implements JwtKeyService {

	@Autowired
	private OAuth2ServiceProperties oAuth2ServiceProperties;
	private HttpHeaders getHeadersWithClientCredentials() {
		String plainClientCredentials = oAuth2ServiceProperties.getClientId()+":"+oAuth2ServiceProperties.getClientSecret();
		String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + base64ClientCredentials);
		return headers;
	}

	
	
	@Override
	public JwtKey getJwtKey() {
		RestTemplate restTemplate = new RestTemplate();
		JwtKey jwtKey = null;
		HttpEntity<String> request = new HttpEntity<String>(getHeadersWithClientCredentials());
		ResponseEntity<Object> response = restTemplate.exchange(oAuth2ServiceProperties.getJwtkeyUri(), HttpMethod.GET,
				request, Object.class);
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) response.getBody();

		if (map != null) {
			jwtKey = new JwtKey();
			jwtKey.setAlg((String) map.get("alg"));
			jwtKey.setValue((String) map.get("value"));

		} else {

		}
		return jwtKey;
	}

	@Override
	public String getAccessToken(Authentication authentication) {
		String access_token=null;
		try{
		String AUTHORIZATION_CODE = authentication.getCredentials().toString();
		String QPM_AUTHCODE_GRANT = "?grant_type=authorization_code&code=" + AUTHORIZATION_CODE
				+ "&redirect_uri="+oAuth2ServiceProperties.getRedirectUri();

		RestTemplate restTemplate = new RestTemplate();

		HttpEntity<String> request = new HttpEntity<String>(getHeadersWithClientCredentials());
		ResponseEntity<Object> response = restTemplate.exchange(oAuth2ServiceProperties.getAccessTokenUri() + QPM_AUTHCODE_GRANT,
				HttpMethod.POST, request, Object.class);
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) response.getBody();
		access_token = (String) map.get("access_token");
		}
		catch(Exception e){
			
		}
		return access_token;
	}

}
