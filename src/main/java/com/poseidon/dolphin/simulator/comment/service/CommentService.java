package com.poseidon.dolphin.simulator.comment.service;

import com.poseidon.dolphin.simulator.comment.Comment;

public interface CommentService {
	Comment save(Comment comment);
	void remove(Comment comment);
}
