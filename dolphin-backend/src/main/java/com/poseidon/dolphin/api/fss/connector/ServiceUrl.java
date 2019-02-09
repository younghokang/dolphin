package com.poseidon.dolphin.api.fss.connector;

public enum ServiceUrl {
	COMPANY_SEARCH("companySearch"),
	SAVING_PRODUCTS_SEARCH("savingProductsSearch"),
	DEPOSIT_PRODUCTS_SEARCH("depositProductsSearch");
	
	private final String url;
	ServiceUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	
}
