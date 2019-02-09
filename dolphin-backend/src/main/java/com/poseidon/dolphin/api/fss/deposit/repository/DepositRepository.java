package com.poseidon.dolphin.api.fss.deposit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.api.fss.deposit.Deposit;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
	Optional<Deposit> findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth(String financeCompanyNumber, String financeProductCode, String disclosureMonth);

	@Query(value="SELECT MAX(disclosureMonth) FROM FSSDeposit", nativeQuery=true)
	String getMaxDisclosureMonth();
	
	List<Deposit> findAllByDisclosureMonth(String disclosureMonth);

}
