package com.wavesdev.oauth2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    
   
    private String resourceId="spring-OAuth2-JWT-hibernate-resource";
    
    @Autowired
    TokenStore tokenStore;
    @Autowired
    private DefaultTokenServices tokenServices;
  
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
    	  resources
         .resourceId(resourceId)
         .tokenServices(tokenServices)
         .tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        	                   
                    http
                    .csrf().disable()
                    .anonymous().disable()
                    .requestMatchers().antMatchers("/**")
                    .and().authorizeRequests()
                    .antMatchers("/resources/user").access("hasAnyRole('USER')")          
                    .antMatchers("/resources/admin").hasRole("ADMIN")
                    // restricting all access to /** to authenticated users
                    .antMatchers("/**").authenticated()
                    .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
        	
    }
    
 
}
