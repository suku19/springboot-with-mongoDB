package com.user.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.app.entity.User;
import com.user.app.repository.UserRepository;

/**
 * @author Sukanta
 *
 */
@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired 
	private UserRepository userRepository;
	
	@Override
	public List<User> getAlluser() {
		return (List<User>) userRepository.findAll();
	}
	
	@Override
	public User getUserById(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	
	
}
