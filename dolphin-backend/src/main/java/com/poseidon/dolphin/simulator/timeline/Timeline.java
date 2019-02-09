package com.poseidon.dolphin.simulator.timeline;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.timeline.converter.ActivityConverter;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Timeline {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@CreatedDate
	private LocalDateTime createdDate;
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
	@OneToOne
	@JoinColumn(name="memberId")
	private Member member;
	private LocalDate activeDate;
	@Convert(converter=ActivityConverter.class)
	private Activity activity;
	private long referenceId;
	@Enumerated(EnumType.ORDINAL)
	private State state;
	
	public Timeline() {}
	
	public Timeline(Member member, LocalDate activeDate, Activity activity, long referenceId, State state) {
		this.member = member;
		this.activeDate = activeDate;
		this.activity = activity;
		this.referenceId = referenceId;
		this.state = state;
	}
	
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
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public LocalDate getActiveDate() {
		return activeDate;
	}
	public void setActiveDate(LocalDate activeDate) {
		this.activeDate = activeDate;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public long getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
	public static List<Timeline> makeTimelinesByFixedDeposit(Account account) {
		List<Timeline> timelines = Arrays.asList(
				new Timeline(account.getMember(), account.getContract().getContractDate(), Activity.FIXED_DEPOSIT_ACCOUNT_OPEN, account.getId(), State.DONE),
				new Timeline(account.getMember(), account.getContract().getExpiryDate(), Activity.FIXED_DEPOSIT_ACCOUNT_CLOSE, account.getId(), State.READY));
		return timelines;
	}
	
	public static List<Timeline> makeTimelinesByInstallmentSaving(Account account) {
		List<Timeline> timelines = new ArrayList<>();
		timelines.add(new Timeline(account.getMember(), account.getContract().getContractDate(), Activity.INSTALLMENT_SAVING_ACCOUNT_OPEN, account.getId(), State.DONE));
		long turns = account.getContract().getPaymentFrequency().calculateTurns(account.getContract().getContractDate(), account.getContract().getExpiryDate());
		LocalDate nextDate = account.getContract().getContractDate();
		for(long turn = 1; turn < turns; turn++) {
			nextDate = nextDate(account.getContract().getDateOfPayment(), nextDate);
			timelines.add(new Timeline(account.getMember(), nextDate, Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT, account.getId(), State.READY));
		}
		timelines.add(new Timeline(account.getMember(), account.getContract().getExpiryDate(), Activity.INSTALLMENT_SAVING_ACCOUNT_CLOSE, account.getId(), State.READY));
		return timelines;
	}
	
	private static LocalDate nextDate(int dateOfPayment, LocalDate current) {
		LocalDate next = current.plusMonths(1);
		int dayOfMonth = next.lengthOfMonth() < dateOfPayment ? next.lengthOfMonth() : dateOfPayment;
		next = next.withDayOfMonth(dayOfMonth);
		return next;
	}
	
	@Override
	public String toString() {
		return "Timeline [id=" + id + ", createdDate=" + createdDate + ", lastModifiedDate=" + lastModifiedDate
				+ ", member=" + member + ", activeDate=" + activeDate + ", activity=" + activity + ", referenceId="
				+ referenceId + ", state=" + state + "]";
	}
}
