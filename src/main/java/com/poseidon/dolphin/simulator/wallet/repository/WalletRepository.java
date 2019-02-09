package com.poseidon.dolphin.simulator.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.wallet.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
	Optional<Wallet> findByMember(Member member);

}
