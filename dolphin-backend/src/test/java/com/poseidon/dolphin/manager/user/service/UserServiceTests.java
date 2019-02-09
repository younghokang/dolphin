package com.poseidon.dolphin.manager.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.manager.user.Authority;
import com.poseidon.dolphin.manager.user.User;
import com.poseidon.dolphin.manager.user.UserCommand;
import com.poseidon.dolphin.manager.user.repository.UserRepository;

@RunWith(SpringRunner.class)
public class UserServiceTests {
	private UserService userService;
	
	@MockBean
	private UserRepository userRepository;
	
	@Before
	public void setUp() {
		userService = new UserService(userRepository);
	}
	
	@Test
	public void givenUsernameAndPasswordAndAuthorityStringsThenCreate() {
		String username = "username";
		String password = "password";
		String authority = "ROLE_USER";
		List<String> authorities = Arrays.asList(authority);
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(new BCryptPasswordEncoder().encode(password));
		user.getAuthorities().add(new Authority(authority));
		System.out.println(user.getPassword());
		
		given(userRepository.save(any(User.class))).willReturn(user);
		
		User created = userService.create(username, password, authorities);
		assertThat(created).isEqualToComparingFieldByField(user);
		
		verify(userRepository, times(1)).save(any(User.class));
	}
	
	@Test
	public void givenUserCommandThenReturnSave() {
		User user = new User();
		user.setUsername("afraid86");
		user.setPassword("password");
		user.setEnabled(true);
		user.setAuthorities(Arrays.asList(new Authority("ROLE_USER")));
		
		UserCommand userCommand = UserCommand.from(user);
		
		given(userRepository.save(any(User.class))).willReturn(user);
		
		assertThat(userService.save(userCommand)).isNotNull();
		
		verify(userRepository, times(1)).save(any(User.class));
	}

}
