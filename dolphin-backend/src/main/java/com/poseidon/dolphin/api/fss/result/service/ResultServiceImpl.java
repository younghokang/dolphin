package com.poseidon.dolphin.api.fss.result.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poseidon.dolphin.api.fss.result.Result;
import com.poseidon.dolphin.api.fss.result.repository.ResultRepository;

@Service
@Transactional
public class ResultServiceImpl implements ResultService {
	private final ResultRepository resultRepository;
	
	@Autowired
	public ResultServiceImpl(ResultRepository resultRepository) {
		this.resultRepository = resultRepository;
	}

	@Override
	public Result save(Result result) {
		return resultRepository.save(result);
	}

}
