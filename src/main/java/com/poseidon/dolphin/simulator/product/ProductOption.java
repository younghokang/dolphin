package com.poseidon.dolphin.simulator.product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.CollectionTable;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OrderBy;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.poseidon.dolphin.simulator.product.converter.InterestRateTypeConverter;
import com.poseidon.dolphin.simulator.product.converter.ReserveTypeConverter;

/**
 * 이자유형: 단리/복리
 * 적립유형:자유적립식/정액적립식
	이자율: {
		계약기간
		이자율
		우대이율
	}
 * */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ProductOption {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@CreatedDate
	private LocalDateTime createdDate;
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
	@Convert(converter=InterestRateTypeConverter.class)
	private InterestRateType interestRateType;
	@Convert(converter=ReserveTypeConverter.class)
	private ReserveType reserveType;
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="ProductOptionInterestRate", joinColumns=@JoinColumn(name="productOptionId"))
	@OrderBy("contractPeriod")
	private List<InterestRate> interestRates = new ArrayList<>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public InterestRateType getInterestRateType() {
		return interestRateType;
	}
	public void setInterestRateType(InterestRateType interestRateType) {
		this.interestRateType = interestRateType;
	}
	public ReserveType getReserveType() {
		return reserveType;
	}
	public void setReserveType(ReserveType reserveType) {
		this.reserveType = reserveType;
	}
	public List<InterestRate> getInterestRates() {
		return interestRates;
	}
	public void setInterestRates(List<InterestRate> interestRates) {
		this.interestRates = interestRates;
	}
	
	public InterestRate applyWithDayOfContractPeriod(final int dayOfContractPeriod) {
		Comparator<InterestRate> comparator = Comparator.comparing(InterestRate::getContractPeriod);
		return getInterestRates().stream()
			.sorted(comparator.reversed())
			.filter(interestRate -> {
				return dayOfContractPeriod >= interestRate.getContractPeriod();
			})
			.findFirst()
			.orElseThrow(NoSuchElementException::new);
	}
	
	@Override
	public String toString() {
		return "ProductOption [id=" + id + ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate
				+ ", interestRateType=" + interestRateType + ", reserveType=" + reserveType + ", interestRates="
				+ interestRates + "]";
	}
}
