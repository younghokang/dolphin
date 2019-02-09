package com.poseidon.dolphin.simulator.account.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.AccountDetail;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.account.State;
import com.poseidon.dolphin.simulator.account.repository.AccountRepository;
import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;

@Service
public class AccountService {
	private final AccountRepository accountRepository;
	
	@Autowired
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	public Account openAccount(Member member, Product product, Contract contract) {
		Account account = new Account();
		account.setMember(member);
		account.setProduct(product);
		account.setContract(contract);
		account.getAccountDetails().add(new AccountDetail(1, contract.getBalance(), contract.getContractDate()));
		account.setState(State.ACTIVE);
		return accountRepository.save(account);
	}

	public List<Account> findAllByMember(Member member) {
		return accountRepository.findAllByMemberAndState(member, State.ACTIVE);
	}

	public boolean regularyPay(long id, Member member) {
		return accountRepository.findById(id)
			.filter(p -> p.getMember().equals(member) && p.getState().equals(State.ACTIVE))
			.map(account -> {
				AccountDetail accountDetail = account.nextAccountDetail();
				if(accountDetail != null) {
					accountDetail.setBalance(account.getContract().getBalance());
					account.getAccountDetails().add(accountDetail);
					return true;
				}
				return false;
			})
			.orElse(Boolean.FALSE.booleanValue());
	}

	public Account closeAccount(long id) {
		return accountRepository.findById(id)
			.map(account -> {
				account.setState(State.CLOSE);
				return accountRepository.save(account);
			}).orElseThrow(NullPointerException::new);
	}
	
	public Optional<Account> findInstallmentSavingAccountByMember(Member member) {
		return accountRepository.findAllByMemberAndState(member, State.ACTIVE).stream()
			.filter(p -> p.getContract().getProductType().equals(ProductType.INSTALLMENT_SAVING))
			.findAny();
	}
	
}
