package com.wavesdev.oauth2.security;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.wavesdev.oauth2.filter.CsrfFilter;
import com.wavesdev.oauth2.util.JwtAuthenticationProvider;
import com.wavesdev.oauth2.util.MyAuthenticationFailureHandler;
import com.wavesdev.oauth2.util.MySuccessHandler;
import com.wavesdev.oauth2.util.OAuth2AuthenticationEntryPoint;
import com.wavesdev.oauth2.util.OAuth2AuthenticationFilter;
import com.wavesdev.oauth2.util.OAuth2ServiceProperties;




@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	 @Bean 
	    public MySuccessHandler mySuccessHandler(){
	    	return new MySuccessHandler();
	    } 
	 @Bean 
	    public MyAuthenticationFailureHandler myAuthenticationFailureHandler(){
	    	return new MyAuthenticationFailureHandler();
	    } 
	 @Autowired
	 private OAuth2ServiceProperties oAuth2ServiceProperties;
	
	 @Bean
	 public OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint(){
		 OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint=new OAuth2AuthenticationEntryPoint();
		 oAuth2AuthenticationEntryPoint.setoAuth2ServiceProperties(oAuth2ServiceProperties);
		 return oAuth2AuthenticationEntryPoint;
	 }
	 @Autowired
	 private JwtKeyService jwtKeyService;
	   @Bean 
	   public OAuth2AuthenticationFilter oAuth2AuthenticationFilter(){
		   OAuth2AuthenticationFilter OAuth2AuthenticationFilter=new OAuth2AuthenticationFilter(new OAuthRequestedMatcher());
		   try {
			OAuth2AuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
			OAuth2AuthenticationFilter.setJwtKeyService(jwtKeyService);
			OAuth2AuthenticationFilter.setAuthenticationSuccessHandler(mySuccessHandler());
			OAuth2AuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return OAuth2AuthenticationFilter;
		   
	   }
	    @Autowired
	    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
	    	auth.authenticationProvider(authenticationProvider());
	    }
	        
	     
	    @Bean
	    public JwtAuthenticationProvider authenticationProvider() {
	    	JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider();
	    	authenticationProvider.setJwtKeyService(jwtKeyService);
	       
	        return authenticationProvider;
	    }
	 
	 @Override
	    @Bean
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	http
    	.authorizeRequests()
    	.antMatchers("/logout")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilterBefore(oAuth2AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        //.addFilterAfter(new CsrfFilter(),UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint(oAuth2AuthenticationEntryPoint())
       // .formLogin()
        //.loginPage("http://localhost:8080/spring-security-sso-auth-server/oauth/authorize?response_type=code&client_id=trusted-app&redirect_uri=http://localhost:8080/spring-OAuth2-JWT-hibernate-sso/login")
        .and()
        .logout()
        .logoutSuccessUrl("http://localhost:8080/spring-security-sso-auth-server/exit?redirect_uri=http://localhost:8080/spring-OAuth2-JWT-hibernate-sso/")
        .permitAll()
        .deleteCookies("JSESSIONID","access_token")
        .and().httpBasic()
        .and().csrf().disable();
         
        
            }
    
    private static class OAuthRequestedMatcher implements RequestMatcher {
        public boolean matches(HttpServletRequest request) {
        	boolean haveloginurl = request.getPathInfo()!="/login";
            boolean haveAccessToken = request.getParameter("code")!=null;
			return  haveAccessToken && haveloginurl ;
        }
    }

   
}
