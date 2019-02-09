package com.poseidon.dolphin.api.fss.saving.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.api.fss.saving.Saving;

@Repository
public interface SavingRepository extends JpaRepository<Saving, Long> {
	Optional<Saving> findByFinanceCompanyNumberAndFinanceProductCodeAndDisclosureMonth(String financeCompanyNumber, String financeProductCode, String disclosureMonth);

	@Query(value="SELECT MAX(disclosureMonth) FROM FSSSaving", nativeQuery=true)
	String getMaxDisclosureMonth();
	
	List<Saving> findAllByDisclosureMonth(String disclosureMonth);
	
}
