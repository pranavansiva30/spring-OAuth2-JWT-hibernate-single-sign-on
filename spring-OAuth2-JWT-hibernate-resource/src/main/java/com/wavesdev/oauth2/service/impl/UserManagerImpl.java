package com.wavesdev.oauth2.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jws.WebService;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wavesdev.oauth2.dao.UserDao;
import com.wavesdev.oauth2.model.Role;
import com.wavesdev.oauth2.service.UserManager;

@Service("userManager")
@WebService(serviceName = "UserService", endpointInterface = "com.epitom.hr.service.UserManager")
@Transactional
public class UserManagerImpl implements UserDetailsService,UserManager{

	@Autowired
	private UserDao userDao;

	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		com.wavesdev.oauth2.model.User user = userDao.findByUserName(username);
		List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());

		return buildUserForAuthentication(user, authorities);
		

	}

	// Converts com.epitom.hr.model.User user to
	// org.springframework.security.core.userdetails.User
	private User buildUserForAuthentication(com.wavesdev.oauth2.model.User user, List<GrantedAuthority> authorities) {
		return new User(user.getUsername(), user.getPassword(), user.isActive(), true, true, true, authorities);
	}

	private List<GrantedAuthority> buildUserAuthority(Set<Role> userRoles) {

		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		for (Role userRole : userRoles) {
			setAuths.add(new SimpleGrantedAuthority(userRole.getName()));
		}

		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);

		return Result;
	}

	public int saveUser(com.wavesdev.oauth2.model.User u) {
		
		return userDao.saveUser(u);
	}

	public void updateUser(com.wavesdev.oauth2.model.User u) {
		userDao.updateUser(u);
		
	}

	public int deleteUser(Long userid) {
		
		return userDao.deleteUser(userid);
	}

	public com.wavesdev.oauth2.model.User getById(Long id) {
		
		return userDao.getById(id);
	}

	public List<com.wavesdev.oauth2.model.User> getUsers() {
		
		return userDao.getUsers();
	}

}
