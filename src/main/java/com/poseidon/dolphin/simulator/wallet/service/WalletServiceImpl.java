package com.poseidon.dolphin.simulator.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.simulator.wallet.Wallet;
import com.poseidon.dolphin.simulator.wallet.WalletLog;
import com.poseidon.dolphin.simulator.wallet.repository.WalletRepository;

@Service
@Transactional
public class WalletServiceImpl implements WalletService {
	private final WalletRepository walletRepository;
	
	@Autowired
	public WalletServiceImpl(WalletRepository walletRepository) {
		this.walletRepository = walletRepository;
	}

	@Override
	@CacheEvict(cacheNames="wallet", key="#member.username")
	public Wallet save(Member member, long balance, WalletLog walletLog) {
		Assert.notNull(member, "member must not be null");
		Assert.notNull(walletLog, "walletLog must not be null");
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
	
	@Override
	@Cacheable(cacheNames="wallet", key="#member.username")
	public Wallet getMyWallet(Member member) {
		return walletRepository.findByMember(member)
				.orElse(null);
	}
	
}
