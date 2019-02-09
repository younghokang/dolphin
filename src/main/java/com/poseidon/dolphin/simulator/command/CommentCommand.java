package com.poseidon.dolphin.simulator.command;

import javax.validation.constraints.Size;

public class CommentCommand {
	private Long id;
	@Size(min=1, max=100)
	private String contents;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
}
