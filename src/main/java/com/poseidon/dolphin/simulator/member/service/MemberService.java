package com.poseidon.dolphin.simulator.member.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.member.repository.MemberRepository;

@Service
public class MemberService {
	private final MemberRepository memberRepository;
	
	@Autowired
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	@CacheEvict(value="member", allEntries=true)
	public Member changeCurrent(Member member, LocalDate current) {
		assert current != null;
		return memberRepository.findById(member.getId())
			.map(mapper -> {
				mapper.setCurrent(current);
				return memberRepository.save(mapper);
			}).orElseThrow(NullPointerException::new);
	}
	
	@CacheEvict(value="member", key="#member.username")
	public Member saveChanges(Member member) {
		return memberRepository.findByUsername(member.getUsername())
				.map(mapper -> {
					if(!StringUtils.isEmpty(member.getEmail())) {
						mapper.setEmail(member.getEmail());
					}
					mapper.setSocialType(member.getSocialType());
					return memberRepository.save(mapper);
				}).orElseGet(() -> {
					return memberRepository.save(member);
				});
	}
	
	@Cacheable(value="member", key="#username")
	public Member loadByUsername(String username) {
		return memberRepository.findByUsername(username)
				.orElseThrow(IllegalArgumentException::new);
	}

}
