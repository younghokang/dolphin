package com.poseidon.dolphin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.poseidon.dolphin.comment.CommentCommand;
import com.poseidon.dolphin.comment.repository.CommentRepository;

@Controller
public class HomeController {
	private final CommentRepository commentRepository;
	
	@Autowired
	HomeController(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	@GetMapping("/")
	public String index(Model model, Pageable pageable, CommentCommand commentCommand) {
		model.addAttribute("commentCommand", commentCommand);
		model.addAttribute("comments", commentRepository.findAll(pageable));
		return "index";
	}
	
}
