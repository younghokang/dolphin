package com.poseidon.dolphin.manager.user.service;

import java.util.List;

import com.poseidon.dolphin.manager.user.User;
import com.poseidon.dolphin.manager.user.UserCommand;

public interface UserService {
	User create(String username, String password, List<String> authorities);
	User save(UserCommand userCommand);
}
