package com.wavesdev.oauth2.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@EnableOAuth2Client
@Configuration
public class Oauth2ClientConfig {
	    @Value("${oauth.resource:http://localhost:8080/spring-security-sso-auth-server/user/me}")
	    private String baseUrl;
	    @Value("${oauth.authorize:http://localhost:8080/spring-security-sso-auth-server/oauth/authorize}")
	    private String authorizeUrl;
	    @Value("${oauth.token:http://localhost:8080/spring-security-sso-auth-server/oauth/token}")
	    private String tokenUrl;
	    @Autowired
	    private OAuth2ClientContext oauth2Context;
	    
	    @Bean
	    protected OAuth2ProtectedResourceDetails resource() {

	    	AuthorizationCodeResourceDetails resource = new AuthorizationCodeResourceDetails();

	        List scopes = new ArrayList<String>(2);
	        scopes.add("write");
	        scopes.add("read");
	        resource.setAccessTokenUri(tokenUrl);
	        resource.setClientId("trusted-app");
	        resource.setClientSecret("secret");
	        resource.setScope(scopes);
	        resource.setClientAuthenticationScheme(AuthenticationScheme.header);
            resource.setUserAuthorizationUri(authorizeUrl);
            resource.setUseCurrentUri(false);
            resource.setGrantType("authorization_code");
            
	        

	        return resource;
	    }

	    @Bean
	    public OAuth2RestOperations restTemplate() {
	        AccessTokenRequest atr = new DefaultAccessTokenRequest();
	        return new OAuth2RestTemplate(resource(), oauth2Context);
	    }
}


