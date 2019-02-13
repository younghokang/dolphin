package com.poseidon.dolphin.simulator.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.member.SocialType;
import com.poseidon.dolphin.member.repository.MemberRepository;
import com.poseidon.dolphin.member.service.MemberService;
import com.poseidon.dolphin.member.service.MemberServiceImpl;

@RunWith(SpringRunner.class)
public class MemberServiceTests {
	private MemberService memberService;
	
	@MockBean
	private MemberRepository memberRepository;
	
	@Before
	public void setUp() {
		memberService = new MemberServiceImpl(memberRepository);
	}
	
	@Test
	public void givenMemberThenSaveChanges() {
		Member member = null;
		
		try {
			memberService.saveChanges(member);
			fail();
		} catch(IllegalArgumentException e) {}
		
		member = new Member();
		member.setUsername("username");
		member.setSocialType(SocialType.NAVER);
		member.setEmail("afraid86@gmail.com");
		given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));
		given(memberRepository.save(any(Member.class))).willReturn(member);
		
		memberService.saveChanges(member);
		
		verify(memberRepository, times(1)).findByUsername(anyString());
		verify(memberRepository, times(1)).save(any(Member.class));
	}
	
	@Test
	public void givenUsernameThenReturnMember() {
		Member member = new Member();
		member.setUsername("username");
		given(memberRepository.findByUsername(anyString())).willReturn(Optional.of(member));
		
		assertThat(memberService.loadByUsername("username")).isEqualTo(member);
		
		given(memberRepository.findByUsername(anyString())).willReturn(Optional.empty());
		
		try {
			memberService.loadByUsername("username");
			fail();
		} catch(UsernameNotFoundException e) {}
		
		verify(memberRepository, times(2)).findByUsername(anyString());
	}
	
}
