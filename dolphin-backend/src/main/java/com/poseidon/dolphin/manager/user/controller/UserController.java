package com.poseidon.dolphin.manager.user.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidon.dolphin.manager.user.Authority;
import com.poseidon.dolphin.manager.user.UserCommand;
import com.poseidon.dolphin.manager.user.repository.UserRepository;
import com.poseidon.dolphin.manager.user.service.UserService;

@Controller
@RequestMapping("/manager/user")
public class UserController {
	private final UserRepository userRepository;
	private final UserService userService;
	
	@Autowired
	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}
	
	@ModelAttribute("allAuthorities")
	public List<Authority> allAuthorities() {
		return Arrays.asList(new Authority("ROLE_USER"));
	}

	@GetMapping("/list")
	public String list(Pageable pageable, Model model) {
		model.addAttribute("list", userRepository.findAll(pageable));
		return "manager/user/list";
	}
	
	@GetMapping("/create")
	public String create(Model model, UserCommand userCommand) {
		model.addAttribute("userCommand", userCommand);
		return "manager/user/create";
	}
	
	@PostMapping("/create")
	public String createSubmit(@ModelAttribute @Valid UserCommand userCommand) {
		userService.save(userCommand);
		return "redirect:/manager/user/" + userCommand.getUsername();
	}
	
	@GetMapping("/{username}")
	public String detail(@PathVariable("username") String username, Model model, UserCommand userCommand) {
		userCommand = userRepository.findById(username)
			.map(UserCommand::from)
			.orElseThrow(NullPointerException::new);
		model.addAttribute("userCommand", userCommand);
		return "manager/user/detail";
	}
	
	@PostMapping("/{username}")
	public String detail(@PathVariable("username") String username, @ModelAttribute @Valid UserCommand userCommand) {
		userService.save(userCommand);
		return "redirect:/manager/user/" + username;
	}
	
	
}
