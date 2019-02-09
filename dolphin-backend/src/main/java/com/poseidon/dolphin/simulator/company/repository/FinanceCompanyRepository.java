package com.poseidon.dolphin.simulator.company.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.simulator.company.FinanceCompany;

@Repository
public interface FinanceCompanyRepository extends JpaRepository<FinanceCompany, Long> {
	Optional<FinanceCompany> findByCode(String code);
}
