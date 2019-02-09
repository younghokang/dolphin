package com.poseidon.dolphin.api.fss.company.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.api.fss.company.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
	Optional<Company> findByFinanceCompanyNumberAndDisclosureMonth(String financeCompanyNumber, String disclosureMonth);
	
	@Query(value="SELECT MAX(disclosureMonth) FROM FSSCompany", nativeQuery=true)
	String getMaxDisclosureMonth();
	
	List<Company> findAllByDisclosureMonth(String disclosureMonth);

}
