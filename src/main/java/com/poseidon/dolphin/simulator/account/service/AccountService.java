package com.poseidon.dolphin.simulator.account.service;

import java.util.List;
import java.util.Optional;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.product.Product;

public interface AccountService {
	Account openAccount(Member member, Product product, Contract contract);
	List<Account> findAllByMember(Member member);
	boolean regularyPay(long id, Member member);
	Account closeAccount(long id);
	Optional<Account> findInstallmentSavingAccountByMember(Member member);
}
