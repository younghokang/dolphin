package com.poseidon.dolphin.simulator.calculator.strategy;

import com.poseidon.dolphin.simulator.account.Account;

public interface InterestStrategy {
	long getInterestBeforeTax(Account account);
}
