package com.wavesdev.oauth2.service;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.Path;

import com.wavesdev.oauth2.model.User;




@WebService
@Path("/users")
public interface UserManager {
	public int saveUser(User u);
	public void updateUser(User u);
	public int deleteUser(Long userid);
	public User getById(Long id);
	List<User> getUsers();
}
