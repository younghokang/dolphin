package com.poseidon.dolphin.simulator.account.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.AccountDetail;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.account.NotFoundAccountException;
import com.poseidon.dolphin.simulator.account.State;
import com.poseidon.dolphin.simulator.account.repository.AccountRepository;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductType;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	private final AccountRepository accountRepository;
	
	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	@Override
	public Account openAccount(Member member, Product product, Contract contract) {
		Account account = new Account();
		account.setMember(member);
		account.setProduct(product);
		account.setContract(contract);
		account.getAccountDetails().add(new AccountDetail(1, contract.getBalance(), contract.getContractDate()));
		account.setState(State.ACTIVE);
		return accountRepository.save(account);
	}

	@Override
	public List<Account> findAllByMember(Member member) {
		return accountRepository.findAllByMemberAndState(member, State.ACTIVE);
	}

	@Override
	public boolean regularyPay(long id, Member member) {
		return accountRepository.findById(id)
			.filter(p -> p.getMember().equals(member) && p.getState().equals(State.ACTIVE))
			.map(account -> {
				AccountDetail accountDetail = account.nextAccountDetail();
				if(accountDetail != null) {
					accountDetail.setBalance(account.getContract().getBalance());
					account.getAccountDetails().add(accountDetail);
					accountRepository.save(account);
					return true;
				}
				return false;
			})
			.orElse(Boolean.FALSE.booleanValue());
	}

	@Override
	public Account closeAccount(long id) {
		return accountRepository.findById(id)
			.map(account -> {
				account.setState(State.CLOSE);
				return accountRepository.save(account);
			}).orElseThrow(() -> new NotFoundAccountException(id));
	}
	
	@Override
	public Optional<Account> findInstallmentSavingAccountByMember(Member member) {
		return accountRepository.findAllByMemberAndState(member, State.ACTIVE).stream()
			.filter(p -> p.getContract().getProductType().equals(ProductType.INSTALLMENT_SAVING))
			.findAny();
	}
	
}
