package com.poseidon.dolphin.comment;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poseidon.dolphin.comment.repository.CommentRepository;
import com.poseidon.dolphin.comment.service.CommentService;
import com.poseidon.dolphin.member.Member;

@Controller
@RequestMapping("/comment")
public class CommentController {
	private final CommentRepository commentRepository;
	private final CommentService commentService;
	
	@Autowired
	public CommentController(CommentRepository commentRepository, CommentService commentService) {
		this.commentRepository = commentRepository;
		this.commentService = commentService;
	}
	
	@PostMapping("/write")
	public String commentWrite(Member member, @ModelAttribute @Valid CommentCommand commentCommand, Errors errors, RedirectAttributes ra, Model model, Pageable pageable) {
		if(errors.hasErrors()) {
			model.addAttribute("comments", commentRepository.findAll(pageable));
			return "index";
		}
		
		Comment comment = new Comment();
		comment.setContents(commentCommand.getContents());
		comment.setMember(member);
		commentService.save(comment);
		
		ra.addFlashAttribute("message", "comment.saveSuccess");
		return "redirect:/";
	}
	
	@GetMapping("/remove/{id}")
	public String commentRemove(@PathVariable("id") long id, Member member, RedirectAttributes ra) {
		Comment comment = commentRepository.findById(id)
				.orElseThrow(() -> new NotFoundCommentException(id));
		if(!comment.getMember().getUsername().equals(member.getUsername())) {
			ra.addFlashAttribute("message", "comment.removeFail");
			return "redirect:/";
		}
		commentService.remove(comment);
		ra.addFlashAttribute("message", "comment.removeSuccess");
		return "redirect:/";
	}
	
	@ExceptionHandler(NotFoundCommentException.class)
	public ModelAndView notFoundCommentException(NotFoundCommentException exception) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", exception);
		mav.setViewName("error");
		return mav;
	}

}
