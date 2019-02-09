package com.poseidon.dolphin.api.fss.result;

import java.time.LocalDateTime;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.poseidon.dolphin.api.fss.common.FinanceGroup;
import com.poseidon.dolphin.api.fss.common.converter.FinanceGroupConverter;
import com.poseidon.dolphin.api.fss.company.json.FSSCompanyResult;
import com.poseidon.dolphin.api.fss.deposit.json.FSSDepositResult;
import com.poseidon.dolphin.api.fss.result.converter.ErrorCodeConverter;
import com.poseidon.dolphin.api.fss.result.converter.ProductDivisionConverter;

@Entity
@Table(name="FSSResult")
@EntityListeners(AuditingEntityListener.class)
public class Result {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Convert(converter=FinanceGroupConverter.class)
	private FinanceGroup financeGroup;
	@Convert(converter=ProductDivisionConverter.class)
	private ProductDivision productDivision;
	@Convert(converter=ErrorCodeConverter.class)
	private ErrorCode errorCode;
	private String errorMessage;
	private int totalCount;
	private int maxPageNumber;
	private int nowPageNumber;
	private LocalDateTime connectionTime;
	
	@CreatedDate
	private LocalDateTime createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FinanceGroup getFinanceGroup() {
		return financeGroup;
	}

	public void setFinanceGroup(FinanceGroup financeGroup) {
		this.financeGroup = financeGroup;
	}

	public ProductDivision getProductDivision() {
		return productDivision;
	}

	public void setProductDivision(ProductDivision productDivision) {
		this.productDivision = productDivision;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getMaxPageNumber() {
		return maxPageNumber;
	}

	public void setMaxPageNumber(int maxPageNumber) {
		this.maxPageNumber = maxPageNumber;
	}

	public int getNowPageNumber() {
		return nowPageNumber;
	}

	public void setNowPageNumber(int nowPageNumber) {
		this.nowPageNumber = nowPageNumber;
	}

	public LocalDateTime getConnectionTime() {
		return connectionTime;
	}

	public void setConnectionTime(LocalDateTime connectionTime) {
		this.connectionTime = connectionTime;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	
	public static Result from(FSSCompanyResult fssCompanyResult) {
		Result result = new Result();
		result.setProductDivision(ProductDivision.findByCode(fssCompanyResult.getPrdt_div()));
		result.setErrorCode(ErrorCode.findByCode(fssCompanyResult.getErr_cd()));
		result.setErrorMessage(fssCompanyResult.getErr_msg());
		result.setTotalCount(fssCompanyResult.getTotal_count());
		result.setMaxPageNumber(fssCompanyResult.getMax_page_no());
		result.setNowPageNumber(fssCompanyResult.getNow_page_no());
		return result;
	}
	
	public static Result from(FSSDepositResult fssDepositResult) {
		Result result = new Result();
		result.setProductDivision(ProductDivision.findByCode(fssDepositResult.getPrdt_div()));
		result.setErrorCode(ErrorCode.findByCode(fssDepositResult.getErr_cd()));
		result.setErrorMessage(fssDepositResult.getErr_msg());
		result.setTotalCount(fssDepositResult.getTotal_count());
		result.setMaxPageNumber(fssDepositResult.getMax_page_no());
		result.setNowPageNumber(fssDepositResult.getNow_page_no());
		return result;
	}
	
	public static <T> Result from(T result) {
		if(result.getClass().isAssignableFrom(FSSCompanyResult.class)) {
			return from((FSSCompanyResult) result);
		}
		return from((FSSDepositResult) result);
	}

	@Override
	public String toString() {
		return "Result [id=" + id + ", financeGroup=" + financeGroup + ", productDivision=" + productDivision
				+ ", errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", totalCount=" + totalCount
				+ ", maxPageNumber=" + maxPageNumber + ", nowPageNumber=" + nowPageNumber + ", connectionTime="
				+ connectionTime + ", createdDate=" + createdDate + "]";
	}

}
