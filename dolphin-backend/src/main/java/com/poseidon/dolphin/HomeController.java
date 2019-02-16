package com.poseidon.dolphin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poseidon.dolphin.api.cache.FrontendCacheClient;
import com.poseidon.dolphin.simulator.product.ProductType;

@Controller
public class HomeController {
	private final FrontendCacheClient cacheClient;
	
	@Autowired
	public HomeController(FrontendCacheClient cacheClient) {
		this.cacheClient = cacheClient;
	}
	
	@ModelAttribute("allProductTypes")
	public Set<ProductType> allProductTypes() {
		return Arrays.stream(ProductType.values()).collect(Collectors.toSet());
	}
	
	@GetMapping("/")
	public String index(Model model) {
		return "index";
	}
	
	@GetMapping("/cache/products/{productType}")
	public String frontendProductsCachePut(@PathVariable("productType") ProductType productType, RedirectAttributes ra) throws URISyntaxException, ClientProtocolException, IOException {
		HttpStatus httpStatus = cacheClient.productsCachePut(productType);
		ra.addFlashAttribute("message", "frontend httpStatus: " + httpStatus);
		return "redirect:/";
	}

}
