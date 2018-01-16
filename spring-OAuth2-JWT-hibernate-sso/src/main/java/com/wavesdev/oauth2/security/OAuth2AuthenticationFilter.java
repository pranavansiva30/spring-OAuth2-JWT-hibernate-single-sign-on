package com.wavesdev.oauth2.security;

import java.io.IOException;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import com.wavesdev.oauth2.service.JwtKeyService;





public class OAuth2AuthenticationFilter extends AbstractAuthenticationProcessingFilter implements InitializingBean {
	private JwtKeyService jwtKeyService=null;
	
		 public OAuth2AuthenticationFilter(final String defaultFilterProcessesUrl) {
	        super(defaultFilterProcessesUrl);
	    }
	 public OAuth2AuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
	        super(requiresAuthenticationRequestMatcher);
	    }

	 
	 @Override
	    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

	        String code =null;

	        
	        // request parameters
	        final Map<String, String[]> parameters = request.getParameterMap();
	        
	        // Check to see if there was an error response from the OAuth Provider
	        checkForErrors(parameters);

	        // Check state parameter to avoid cross-site-scripting attacks
	       checkStateParameter(request.getSession(), parameters);

	        final String codeValues[] = parameters.get("code");
	        if (codeValues != null && codeValues.length > 0) {
	            code = codeValues[0];
	           
	        }
	        if(code==null){
	        	throw new BadCredentialsException("No pre-authenticated credentials found in request.");
	        }

	        String token = jwtKeyService.getAccessToken(new OAuth2AuthenticationToken(code));
	        if(token==null){
	        	throw new BadCredentialsException("Wrong pre-authenticated credentials found in request.");
	        }
	        // Allow subclasses to set the "details" property
	        OAuth2AuthenticationToken authRequest = new OAuth2AuthenticationToken(token);
	        setDetails(request, authRequest);
	        
	        AuthenticationManager authenticationManager= this.getAuthenticationManager();
	        Authentication authentication=authenticationManager.authenticate(authRequest);
	        
	        return authentication;
	    }

	    /**
	     * Check the state parameter to ensure it is the same as was originally sent. Subclasses can override this
	     * behaviour if they so choose, but it is not recommended.
	     * @param session The http session, which will contain the original scope as an attribute.
	     * @param parameters The parameters received from the OAuth 2 Provider, which should contain the same state as
	     *                   originally sent to it and stored in the http session.
	     * @throws AuthenticationException If the state differs from the original.
	     */
	    protected void checkStateParameter(HttpSession session, Map<String, String[]> parameters)
	            throws AuthenticationException {

	        String originalState = (String)session.getAttribute("state");
	        String receivedStates[] = parameters.get("state");

	        // There should only be one entry in the array, if there are more they will be ignored.
	        if (receivedStates == null || receivedStates.length == 0 ||
	                !receivedStates[0].equals(originalState)) {
	            String errorMsg = String.format("Received states %s was not equal to original state %s",
	                    receivedStates, originalState);
	            
	            throw new AuthenticationServiceException(errorMsg);
	        }
	    }

	    /**
	     * Checks to see if an error was returned by the OAuth Provider and throws an {@link AuthenticationException} if
	     * it was.
	     * @param parameters Parameters received from the OAuth Provider.
	     * @throws AuthenticationException If an error was returned by the OAuth Provider.
	     */
	    protected void checkForErrors(Map<String, String[]> parameters) throws AuthenticationException {
	        final String errorValues[] = parameters.get("error");
	        final String errorReasonValues[] = parameters.get("error_reason");
	        final String errorDescriptionValues[] = parameters.get("error_description");

	        if (errorValues != null && errorValues.length > 0) {
	            final String error = errorValues[0];
	            final String errorReason = errorReasonValues != null && errorReasonValues.length > 0 ?
	                    errorReasonValues[0] : null;
	            final String errorDescription = errorDescriptionValues != null && errorDescriptionValues.length > 0 ?
	                    errorDescriptionValues[0] : null;
	            final String errorText = String.format("An error was returned by the OAuth Provider: error=%s, " +
	                    "error_reason=%s, error_description=%s", error, errorReason, errorDescription);
	           
	            throw new AuthenticationServiceException(errorText);
	        }
	    }

	    /**
	     * Provided so that subclasses may configure what is put into the authentication request's details
	     * property.
	     *
	     * @param request that an authentication request is being created for
	     * @param authRequest the authentication request object that should have its details set
	     */
	    protected void setDetails(HttpServletRequest request, OAuth2AuthenticationToken authRequest) {
	        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	    }
	   
		
	    
	    /**
	     * Check properties are set
	     */
		
		
	    @Override
	    public void afterPropertiesSet() {
	        super.afterPropertiesSet();
	        Assert.notNull(jwtKeyService, "jwtKeyService must be set");
	      
	}
		
		public void setJwtKeyService(JwtKeyService jwtKeyService) {
			this.jwtKeyService = jwtKeyService;
		}
		
		
	    

}
