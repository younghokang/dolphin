package com.poseidon.dolphin.simulator.company.controller;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.simulator.TestState;
import com.poseidon.dolphin.simulator.company.FinanceCompany;
import com.poseidon.dolphin.simulator.company.FinanceCompanyCommand;
import com.poseidon.dolphin.simulator.company.repository.FinanceCompanyRepository;
import com.poseidon.dolphin.simulator.company.service.FinanceCompanyService;

@Controller
@RequestMapping("/simulator/company")
public class CompanyController {
	private final FinanceCompanyRepository financeCompanyRepository;
	private final FinanceCompanyService financeCompanyService;
	
	@Autowired
	public CompanyController(FinanceCompanyRepository financeCompanyRepository, FinanceCompanyService financeCompanyService) {
		this.financeCompanyRepository = financeCompanyRepository;
		this.financeCompanyService = financeCompanyService;
	}
	
	@ModelAttribute("allFinanceGroups")
	public Set<FinanceGroup> allFinanceGroups() {
		return Arrays.stream(FinanceGroup.values()).collect(Collectors.toSet());
	}
	
	@ModelAttribute("allTestStates")
	public Set<TestState> allTestStates() {
		return Arrays.stream(TestState.values()).sorted(Comparator.comparing(TestState::ordinal).reversed()).collect(Collectors.toSet());
	}
	
	@GetMapping("/list")
	public String list(Model model, Pageable pageable, FinanceCompanyCommand financeCompanyCommand) {
		FinanceCompany financeCompany = new FinanceCompany();
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnoreCase()
				.withIgnoreNullValues()
				.withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
		if(!StringUtils.isEmpty(financeCompanyCommand.getName())) {
			financeCompany.setName(financeCompanyCommand.getName());
		}
		if(!StringUtils.isEmpty(financeCompanyCommand.getCode())) {
			financeCompany.setCode(financeCompanyCommand.getCode());
		}
		if(financeCompanyCommand.getFinanceGroup() != null) {
			financeCompany.setFinanceGroup(financeCompanyCommand.getFinanceGroup());
		}
		if(financeCompanyCommand.getTestState() != null) {
			financeCompany.setTestState(financeCompanyCommand.getTestState());
		}
		
		Example<FinanceCompany> example = Example.of(financeCompany, matcher);
		model.addAttribute("list", financeCompanyRepository.findAll(example, pageable));
		model.addAttribute("financeCompanyCommand", financeCompanyCommand);
		return "simulator/company/list";
	}
	
	@GetMapping("/{id}")
	public String detail(@PathVariable long id, Model model, FinanceCompanyCommand financeCompanyCommand) {
		financeCompanyCommand = FinanceCompanyCommand.from(financeCompanyRepository.findById(id).orElseThrow(NullPointerException::new));
		model.addAttribute("financeCompanyCommand", financeCompanyCommand);
		return "simulator/company/detail";
	}
	
	@PostMapping("/{id}")
	public String detailPost(@PathVariable long id, Model model, @ModelAttribute @Valid FinanceCompanyCommand financeCompanyCommand, Errors errors, RedirectAttributes ra) {
		if(errors.hasErrors()) {
			return "simulator/company/detail";
		}
		
		financeCompanyService.save(financeCompanyCommand);
		ra.addFlashAttribute("message", "saveSuccess");
		return "redirect:/simulator/company/list";
	}
	
}
