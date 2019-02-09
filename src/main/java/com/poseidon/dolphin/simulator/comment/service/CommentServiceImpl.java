package com.poseidon.dolphin.simulator.comment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.dolphin.simulator.comment.Comment;
import com.poseidon.dolphin.simulator.comment.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {
	private final CommentRepository repository;
	
	@Autowired
	CommentServiceImpl(CommentRepository repository) {
		this.repository = repository;
	}

	@Override
	public Comment save(Comment comment) {
		return repository.save(comment);
	}

	@Override
	public void remove(Comment comment) {
		repository.delete(comment);
	}

}
