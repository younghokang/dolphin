package com.poseidon.dolphin.manager.comment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidon.dolphin.manager.comment.Comment;
import com.poseidon.dolphin.manager.comment.CommentCommand;
import com.poseidon.dolphin.manager.comment.repository.CommentRepository;

@Controller
@RequestMapping("/manager/comment")
public class CommentController {
	private final CommentRepository commentRepository;
	
	@Autowired
	public CommentController(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	@GetMapping("/list")
	public String list(Model model, Pageable pageable, CommentCommand commentCommand) {
		Comment comment = new Comment();
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnoreCase()
				.withIgnoreNullValues()
				.withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
		if(!StringUtils.isEmpty(commentCommand.getContents())) {
			comment.setContents(commentCommand.getContents());
		}
		
		Example<Comment> example = Example.of(comment, matcher);
		model.addAttribute("list", commentRepository.findAll(example, pageable));
		model.addAttribute("commentCommand", commentCommand);
		return "manager/comment/list";
	}
	
}
