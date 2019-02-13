package com.poseidon.dolphin.comment;

@SuppressWarnings("serial")
public class NotFoundCommentException extends RuntimeException {

	public NotFoundCommentException(long id) {
		super("comment #" + id + " not found.");
	}
}
