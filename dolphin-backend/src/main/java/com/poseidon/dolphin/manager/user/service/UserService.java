package com.poseidon.dolphin.manager.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.poseidon.dolphin.manager.user.Authority;
import com.poseidon.dolphin.manager.user.User;
import com.poseidon.dolphin.manager.user.UserCommand;
import com.poseidon.dolphin.manager.user.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User create(String username, String password, List<String> authorities) {
		User user = new User();
		user.setUsername(username);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(password));
		user.setEnabled(true);
		authorities.stream().forEach(authority -> {
			user.getAuthorities().add(new Authority(authority));
		});
		return userRepository.save(user);
	}

	public User save(UserCommand userCommand) {
		User user = new User();
		user.setUsername(userCommand.getUsername());
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(userCommand.getPassword()));
		user.setEnabled(userCommand.isEnabled());
		user.setAuthorities(userCommand.getAuthorities());
		return userRepository.save(user);
	}

}
