package com.wavesdev.oauth2.dao;

import java.util.List;

import com.wavesdev.oauth2.model.User;







public interface UserDao {
	User findByUserName(String username);
	public int saveUser(User u);
	public void updateUser(User u);
	public int deleteUser(Long userid);
	public User getById(Long id);
	List<User> getUsers();
}
