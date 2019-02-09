package com.poseidon.dolphin.simulator.wallet.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.wallet.Wallet;
import com.poseidon.dolphin.simulator.wallet.WalletLog;
import com.poseidon.dolphin.simulator.wallet.repository.WalletRepository;

@Service
public class WalletService {
	private final WalletRepository walletRepository;
	
	@Autowired
	public WalletService(WalletRepository walletRepository) {
		this.walletRepository = walletRepository;
	}

	@CacheEvict(value="wallet", key="#member.username")
	public Wallet save(Member member, long balance, WalletLog walletLog) {
		assert member != null;
		assert walletLog != null;
		return walletRepository.findByMember(member)
				.map(mapper -> {
					mapper.setBalance(mapper.getBalance() + balance);
					mapper.getLogs().add(walletLog);
					return walletRepository.save(mapper);
				}).orElseGet(() -> {
					Wallet wallet = new Wallet();
					wallet.setBalance(balance);
					wallet.setMember(member);
					wallet.getLogs().add(walletLog);
					return walletRepository.save(wallet);
				});
	}
	
	@Cacheable(value="wallet", key="#member.username")
	public Wallet getMyWallet(Member member) {
		Optional<Wallet> wallet = walletRepository.findByMember(member);
		return wallet.orElse(null);
	}
	
}
