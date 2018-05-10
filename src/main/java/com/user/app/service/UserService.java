package com.user.app.service;

import java.util.List;

import com.user.app.entity.User;

/**
 * @author Sukanta
 *
 */
public interface UserService {

	public List<User> getAlluser();
	
	public User getUserById(Long id);
	
	public User saveUser(User user);
	
	public void deleteUser(User user);
}
