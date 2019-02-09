package com.poseidon.dolphin.simulator.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.member.SocialType;
import com.poseidon.dolphin.simulator.member.repository.MemberRepository;

@RunWith(SpringRunner.class)
public class MemberServiceTests {
	private MemberService memberService;
	
	@MockBean
	private MemberRepository memberRepository;
	
	@Before
	public void setUp() {
		memberService = new MemberService(memberRepository);
	}
	
	@Test
	public void givenMemberAndDateThenSave() {
		long id = 1l;
		LocalDate current = LocalDate.of(2019, 1, 1);
		
		Member member = new Member();
		member.setId(id);
		member.setCurrent(current);
		member.setUsername("afraid86");
		
		given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
		given(memberRepository.save(any(Member.class))).willReturn(member);
		
		LocalDate changeCurrent = LocalDate.of(201, 2, 1);
		Member savedMember = memberService.changeCurrent(member, changeCurrent);
		assertThat(savedMember.getCurrent()).isEqualTo(changeCurrent);
		
		verify(memberRepository, times(1)).findById(anyLong());
		verify(memberRepository, times(1)).save(any(Member.class));
	}
	
	@Test
	public void givenMemberThenSaveChanges() {
		Member member = new Member();
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
		} catch(IllegalArgumentException e) {}
		
		verify(memberRepository, times(2)).findByUsername(anyString());
	}
	
}
