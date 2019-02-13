package com.poseidon.dolphin.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.member.repository.MemberRepository;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	
	@Autowired
	public MemberServiceImpl(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	@Override
	@CacheEvict(cacheNames="members", key="#member.username")
	public Member saveChanges(Member member) {
		Assert.notNull(member, "member must not be null");
		return memberRepository.findByUsername(member.getUsername())
				.map(mapper -> {
					if(!StringUtils.isEmpty(member.getEmail())) {
						mapper.setEmail(member.getEmail());
					}
					mapper.setSocialType(member.getSocialType());
					mapper.setCurrent(member.getCurrent());
					return memberRepository.save(mapper);
				}).orElseGet(() -> {
					return memberRepository.save(member);
				});
	}
	
	@Override
	@Cacheable("members")
	public Member loadByUsername(String username) {
		Assert.notNull(username, "username must not be null");
		return memberRepository.findByUsername(username)
				.orElseThrow(IllegalArgumentException::new);
	}

}
