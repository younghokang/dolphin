package com.poseidon.dolphin.simulator.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberCommand {
	private Long id;
	private String username;
	private SocialType socialType;
	private String email;
	private LocalDate current;
	private LocalDateTime createdDate;
	private LocalDateTime lastModifiedDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public SocialType getSocialType() {
		return socialType;
	}
	public void setSocialType(SocialType socialType) {
		this.socialType = socialType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getCurrent() {
		return current;
	}
	public void setCurrent(LocalDate current) {
		this.current = current;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public static MemberCommand from(Member member) {
		MemberCommand mb = new MemberCommand();
		mb.setId(member.getId());
		mb.setUsername(member.getUsername());
		mb.setEmail(member.getEmail());
		mb.setCurrent(member.getCurrent());
		mb.setSocialType(member.getSocialType());
		mb.setCreatedDate(member.getCreatedDate());
		mb.setLastModifiedDate(member.getLastModifiedDate());
		return mb;
	}
}
