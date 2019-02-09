package com.poseidon.dolphin.simulator.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.State;
import com.poseidon.dolphin.simulator.member.Member;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	List<Account> findAllByMemberAndState(Member member, State state);

}
