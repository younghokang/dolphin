package com.poseidon.dolphin.comment.service;

import com.poseidon.dolphin.comment.Comment;

public interface CommentService {
	Comment save(Comment comment);
	void remove(Comment comment);
}
