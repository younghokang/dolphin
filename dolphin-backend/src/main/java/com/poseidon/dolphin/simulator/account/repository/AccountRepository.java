package com.poseidon.dolphin.simulator.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.simulator.account.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
