package com.poseidon.dolphin.simulator.member.controller;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.member.MemberCommand;
import com.poseidon.dolphin.simulator.member.SocialType;
import com.poseidon.dolphin.simulator.member.repository.MemberRepository;

@Controller
@RequestMapping("/simulator/member")
public class MemberController {
	private final MemberRepository memberRepository;
	
	@Autowired
	public MemberController(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	@ModelAttribute("allSocialTypes")
	public Set<SocialType> allSocialTypes() {
		return Arrays.stream(SocialType.values()).collect(Collectors.toSet());
	}
	
	@GetMapping("/list")
	public String list(Model model, Pageable pageable, MemberCommand memberCommand) {
		Member member = new Member();
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnoreCase()
				.withIgnoreNullValues()
				.withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
		if(!StringUtils.isEmpty(memberCommand.getEmail())) {
			member.setEmail(memberCommand.getEmail());
		}
		if(memberCommand.getSocialType() != null) {
			member.setSocialType(memberCommand.getSocialType());
		}
		
		Example<Member> example = Example.of(member, matcher);
		model.addAttribute("list", memberRepository.findAll(example, pageable));
		model.addAttribute("memberCommand", memberCommand);
		return "simulator/member/list";
	}
	
	@GetMapping("/{id}")
	public String detail(@PathVariable long id, Model model, MemberCommand memberCommand) {
		memberCommand = MemberCommand.from(memberRepository.findById(id).orElseThrow(NullPointerException::new));
		model.addAttribute("memberCommand", memberCommand);
		return "simulator/member/detail";
	}

}
