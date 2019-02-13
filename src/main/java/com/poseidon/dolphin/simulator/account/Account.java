package com.poseidon.dolphin.simulator.account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.simulator.product.Interest;
import com.poseidon.dolphin.simulator.product.InterestRate;
import com.poseidon.dolphin.simulator.product.InterestRateType;
import com.poseidon.dolphin.simulator.product.Product;
import com.poseidon.dolphin.simulator.product.ProductOption;
import com.poseidon.dolphin.simulator.product.TaxType;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@CreatedDate
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
	
	@OneToOne
	@JoinColumn(name="productId")
	private Product product;
	
	@OneToOne
	@JoinColumn(name="memberId")
	private Member member;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<AccountDetail> accountDetails = new ArrayList<>();
	
	private Contract contract;
	
	@Enumerated(EnumType.STRING)
	private State state;
	
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List<AccountDetail> getAccountDetails() {
		return accountDetails;
	}

	public void setAccountDetails(List<AccountDetail> accountDetails) {
		this.accountDetails = accountDetails;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public AccountDetail nextAccountDetail() {
		Contract contract = getContract();
		long maxTurn = contract.getPaymentFrequency().calculateTurns(contract.getContractDate(), contract.getExpiryDate());
		return getAccountDetails().stream()
				.max(Comparator.comparing(AccountDetail::getTurn))
				.filter(p -> p.getTurn() != maxTurn)
				.map(accountDetail -> {
					return nextAccountDetail(accountDetail, contract.getDateOfPayment());
				})
				.orElse(null);
	}
	
	private AccountDetail nextAccountDetail(AccountDetail ad, int dateOfPayment) {
		AccountDetail accountDetail = new AccountDetail();
		accountDetail.setTurn(ad.getTurn() + 1);
		accountDetail.setDepositDate(nextDate(dateOfPayment, ad.getDepositDate()));
		return accountDetail;
	}
	
	private LocalDate nextDate(int dateOfPayment, LocalDate current) {
		LocalDate next = current.plusMonths(1);
		int dayOfMonth = next.lengthOfMonth() < dateOfPayment ? next.lengthOfMonth() : dateOfPayment;
		next = next.withDayOfMonth(dayOfMonth);
		return next;
	}
	
	public static Contract writeUp(Product product, long balance, LocalDate current) {
		return Account.writeUp(product, balance, current, current.getDayOfMonth(), Product.DEFAULT_PERIOD, Product.DEFAULT_PERIOD_UNIT, PaymentFrequency.MONTH, TaxType.GENERAL, TaxType.GENERAL.getTaxRate());
	}
	
	public static Contract writeUp(Product product, long balance, LocalDate current, int dateOfPayment, int period, ChronoUnit periodUnit, PaymentFrequency paymentFrequency, TaxType taxType, double taxRate) {
		ProductOption productOption = product.getProductOptions().stream()
			.filter(option -> Long.compare(option.getId().longValue(), product.getFilteredOptionId().longValue()) == 0)
			.findAny()
			.orElseThrow(NullPointerException::new);
		
		InterestRate interestRate = productOption.getInterestRates().stream()
				.filter(ir -> ir.getContractPeriod() == period)
				.findAny()
				.orElseThrow(NullPointerException::new);
		
		Contract contract = new Contract();
		contract.setBalance(balance);
		contract.setContractDate(current);
		contract.setDateOfPayment(dateOfPayment);
		contract.setExpiryDate(current.plus(period, periodUnit));
		contract.setInterest(productOption.getInterestRateType() == InterestRateType.COMPOUND ? Interest.MONTH_COMPOUND : Interest.DAY_SIMPLE);
		contract.setInterestRate(interestRate.getRate());
		contract.setName(product.getName());
		contract.setPaymentFrequency(paymentFrequency);
		contract.setProductType(product.getProductType());
		contract.setReserveType(productOption.getReserveType());
		contract.setTaxRate(taxRate);
		contract.setTaxType(taxType);
		return contract;
	}
	
	public long getTotalAmountOfBalance() {
		return getAccountDetails().stream().mapToLong(mapper -> mapper.getBalance()).sum();
	}
	
	public long getDayOfContract() {
		return ChronoUnit.DAYS.between(getContract().getContractDate(), getContract().getExpiryDate());
	}
	
	public long getDayOfExpiryFromCurrent() {
		return ChronoUnit.DAYS.between(getMember().getCurrent(), getContract().getExpiryDate());
	}
	
	public long getDayOfCurrentFromContractDate() {
		return ChronoUnit.DAYS.between(getContract().getContractDate(), getMember().getCurrent());
	}
	
	public long getNumberOfPercentToExpiry() {
		return Math.round(((double)getDayOfCurrentFromContractDate() / getDayOfContract()) * 100);
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", product=" + product + ", contract=" + contract + ", details=" + accountDetails
				+ ", member=" + member + ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate
				+ ", state=" + state + "]";
	}

	
}
