package com.poseidon.dolphin.api.fss.result.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.api.fss.result.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
	
}
