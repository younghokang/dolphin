package com.poseidon.dolphin.comment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poseidon.dolphin.comment.Comment;
import com.poseidon.dolphin.comment.repository.CommentRepository;

@Service
@Transactional
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
