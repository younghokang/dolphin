package com.poseidon.dolphin.simulator.wallet.service;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.simulator.wallet.Wallet;
import com.poseidon.dolphin.simulator.wallet.WalletLog;

public interface WalletService {
	Wallet save(Member member, long balance, WalletLog walletLog);
	Wallet getMyWallet(Member member);
}
