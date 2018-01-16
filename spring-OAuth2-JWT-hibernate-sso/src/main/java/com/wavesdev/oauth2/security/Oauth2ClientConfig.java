package com.wavesdev.oauth2.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:oauth2.properties")
public class Oauth2ClientConfig {
	    @Value("${clientID}")
	    private String clientID;
	    @Value("${userAuthorizationUri}")
	    private String authorizeUrl;
	    @Value("${accessTokenUri}")
	    private String accessTokenUri;
	    @Value("${clientSecret}")
	    private String clientSecret;
	    @Value("${redirectUri}")
	    private String redirectUri;
	    @Value("${JwtkeyUri}")
	    private String JwtkeyUri;
	    @Autowired
	    private OAuth2ClientContext oauth2Context;
	    @Bean 
		 public OAuth2ServiceProperties oAuth2ServiceProperties(){
			 
			 OAuth2ServiceProperties oAuth2ServiceProperties=new OAuth2ServiceProperties();
			 oAuth2ServiceProperties.setUserAuthorisationUri(authorizeUrl);
			 oAuth2ServiceProperties.setRedirectUri(redirectUri);
			 oAuth2ServiceProperties.setAccessTokenUri(accessTokenUri);
			 oAuth2ServiceProperties.setClientId(clientID);
			 oAuth2ServiceProperties.setClientSecret(clientSecret);
			 oAuth2ServiceProperties.setJwtkeyUri(JwtkeyUri);
		     return oAuth2ServiceProperties;
		 
		 }
	    @Bean
	    protected OAuth2ProtectedResourceDetails resource() {

	    	AuthorizationCodeResourceDetails resource = new AuthorizationCodeResourceDetails();

	        List scopes = new ArrayList<String>(2);
	        scopes.add("write");
	        scopes.add("read");
	        resource.setAccessTokenUri(accessTokenUri);
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


