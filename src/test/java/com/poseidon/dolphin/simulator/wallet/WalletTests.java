package com.poseidon.dolphin.simulator.wallet;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.poseidon.dolphin.simulator.member.Member;

public class WalletTests {
	
	private Member member;
	
	@Before
	public void setUp() {
		member = new Member();
		member.setUsername("afraid86");
	}
	
	@Test
	public void makeWallet() {
		long balance = 50000;
		
		Wallet wallet = new Wallet();
		wallet.setMember(member);
		wallet.setBalance(balance);
		wallet.getLogs().add(new WalletLog(balance, LocalDateTime.now(), TransferType.INSTALLMENT_SAVING_CLOSE, 50l));
		
		assertThat(wallet.getMember()).isEqualTo(member);
		assertThat(wallet.getBalance()).isEqualTo(balance);
		assertThat(wallet.getLogs().stream().mapToLong(log -> log.getBalance()).sum()).isEqualTo(balance);
	}

}
