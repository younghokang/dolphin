package com.poseidon.dolphin.simulator.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.simulator.member.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
