package com.poseidon.dolphin.simulator.wallet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.wallet.TransferType;
import com.poseidon.dolphin.simulator.wallet.Wallet;
import com.poseidon.dolphin.simulator.wallet.WalletLog;
import com.poseidon.dolphin.simulator.wallet.repository.WalletRepository;

@RunWith(SpringRunner.class)
public class WalletServiceTests {
	private WalletService walletService;
	
	@MockBean
	private WalletRepository walletRepository;
	
	private Member member;
	
	@Before
	public void setUp() {
		walletService = new WalletService(walletRepository);
		
		member = new Member();
		member.setUsername("alice");
	}
	
	@Test
	public void givenWalletAndWalletLogThenSaveWallet() {
		long balance = 50000;
		long referenceId = 50l;
		
		Wallet wallet = new Wallet();
		wallet.setBalance(balance);
		wallet.setMember(member);
		
		WalletLog walletLog = new WalletLog(balance, LocalDateTime.now(), TransferType.FIXED_DEPOSIT_CLOSE, referenceId);
		wallet.getLogs().add(walletLog);
		
		given(walletRepository.findByMember(any(Member.class))).willReturn(Optional.empty());
		given(walletRepository.save(any(Wallet.class))).willReturn(wallet);
		
		Wallet savedWallet = walletService.save(member, balance, walletLog);
		assertThat(savedWallet.getBalance()).isEqualTo(balance);
		assertThat(savedWallet.getLogs().size()).isEqualTo(1);
		
		long newBalance = -5000;
		
		Wallet changedWallet = new Wallet();
		changedWallet.setMember(member);
		changedWallet.setBalance(wallet.getBalance() + newBalance);
		changedWallet.getLogs().addAll(wallet.getLogs());
		
		walletLog = new WalletLog(newBalance, LocalDateTime.now(), TransferType.FIXED_DEPOSIT_CLOSE, referenceId);
		changedWallet.getLogs().add(walletLog);
		
		given(walletRepository.findByMember(any(Member.class))).willReturn(Optional.of(wallet));
		given(walletRepository.save(any(Wallet.class))).willReturn(changedWallet);
		
		savedWallet = walletService.save(member, newBalance, walletLog);
		assertThat(savedWallet.getBalance()).isEqualTo(changedWallet.getBalance());
		assertThat(savedWallet.getLogs().size()).isEqualTo(2);
		
		verify(walletRepository, times(2)).findByMember(any(Member.class));
		verify(walletRepository, times(2)).save(any(Wallet.class));
	}
	
}
