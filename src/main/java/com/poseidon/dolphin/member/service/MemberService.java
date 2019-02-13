package com.poseidon.dolphin.member.service;

import com.poseidon.dolphin.member.Member;

public interface MemberService {
	Member saveChanges(Member member);
	Member loadByUsername(String username);
}
