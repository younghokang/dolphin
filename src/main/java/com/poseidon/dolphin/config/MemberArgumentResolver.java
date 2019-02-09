package com.poseidon.dolphin.config;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.member.service.MemberService;

public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
	private final MemberService memberService;
	
	public MemberArgumentResolver(MemberService memberService) {
		this.memberService = memberService;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return Member.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null) {
			return memberService.loadByUsername(authentication.getName());
		}
		return null;
	}

}
