package com.poseidon.dolphin.manager.user;

import java.util.List;

import javax.validation.constraints.Size;

public class UserCommand {
	@Size(min=4, max=15)
	private String username;
	private String password;
	private boolean enabled;
	private List<Authority> authorities;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public List<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	
	public static UserCommand from(User user) {
		UserCommand uc = new UserCommand();
		uc.setUsername(user.getUsername());
		uc.setPassword(user.getPassword());
		uc.setEnabled(user.isEnabled());
		uc.setAuthorities(user.getAuthorities());
		return uc;
	}
}
