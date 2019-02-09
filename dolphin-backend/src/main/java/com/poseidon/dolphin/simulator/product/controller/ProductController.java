package com.poseidon.dolphin.simulator.product.controller;

import java.time.temporal.ChronoUnit;
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
import com.poseidon.dolphin.api.fss.common.JoinDeny;
import com.poseidon.dolphin.simulator.TestState;
import com.poseidon.dolphin.simulator.product.InterestPaymentType;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductCommand;
import com.poseidon.dolphin.simulator.product.State;
import com.poseidon.dolphin.simulator.product.repository.ProductRepository;
import com.poseidon.dolphin.simulator.product.service.ProductService;

@Controller
@RequestMapping("/simulator/product")
public class ProductController {
	private final ProductRepository productRepository;
	private final ProductService productService;
	
	@Autowired
	public ProductController(ProductRepository productRepository, ProductService productService) {
		this.productRepository = productRepository;
		this.productService = productService;
	}
	
	@ModelAttribute("allFinanceGroups")
	public Set<FinanceGroup> allFinanceGroups() {
		return Arrays.stream(FinanceGroup.values()).collect(Collectors.toSet());
	}
	
	@ModelAttribute("allTestStates")
	public Set<TestState> allTestStates() {
		return Arrays.stream(TestState.values()).sorted(Comparator.comparing(TestState::ordinal).reversed()).collect(Collectors.toSet());
	}
	
	@ModelAttribute("allJoinDenies")
	public Set<JoinDeny> allJoinDenies() {
		return Arrays.stream(JoinDeny.values()).collect(Collectors.toSet());
	}
	
	@ModelAttribute("allPeriodUnits")
	public Set<ChronoUnit> allPeriodUnits() {
		return Arrays.asList(ChronoUnit.DAYS, ChronoUnit.MONTHS, ChronoUnit.YEARS).stream().collect(Collectors.toSet());
	}
	
	@ModelAttribute("allInterestPaymentTypes")
	public Set<InterestPaymentType> allInterestPaymentTypes() {
		return Arrays.stream(InterestPaymentType.values()).collect(Collectors.toSet());
	}
	
	@ModelAttribute("allStates")
	public Set<State> allStates() {
		return Arrays.stream(State.values()).collect(Collectors.toSet());
	}
	
	@GetMapping("/list")
	public String list(Model model, Pageable pageable, ProductCommand productCommand) {
		Product product = new Product();
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withIgnoreCase()
				.withIgnoreNullValues()
				.withIgnorePaths("minPeriod", "maxPeriod", "depositProtect", "minBalance", "maxBalance", "minAge", "maxAge", "nonTaxable")
				.withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
		if(!StringUtils.isEmpty(productCommand.getName())) {
			product.setName(productCommand.getName());
		}
		if(productCommand.getFinanceGroup() != null) {
			product.setFinanceGroup(productCommand.getFinanceGroup());
		}
		if(productCommand.getTestState() != null) {
			product.setTestState(productCommand.getTestState());
		}
		if(!StringUtils.isEmpty(productCommand.getCompanyName())) {
			product.setCompanyName(productCommand.getCompanyName());
		}
		
		Example<Product> example = Example.of(product, matcher);
		model.addAttribute("list", productRepository.findAll(example, pageable));
		model.addAttribute("productCommand", productCommand);
		return "simulator/product/list";
	}
	
	@GetMapping("/{id}")
	public String detail(@PathVariable long id, Model model, ProductCommand productCommand) {
		productCommand = ProductCommand.from(productRepository.findById(id).orElseThrow(NullPointerException::new));
		model.addAttribute("productCommand", productCommand);
		return "simulator/product/detail";
	}
	
	@PostMapping("/{id}")
	public String detailSaveChanges(@PathVariable long id, @ModelAttribute @Valid ProductCommand productCommand, Errors errors, RedirectAttributes ra) {
		if(errors.hasErrors()) {
			return "simulator/product/detail";
		}
		
		productService.save(productCommand);
		
		ra.addFlashAttribute("message", "saveSuccess");
		return "redirect:/simulator/product/list";
	}

}
