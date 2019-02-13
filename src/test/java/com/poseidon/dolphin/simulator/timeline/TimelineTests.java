package com.poseidon.dolphin.simulator.timeline;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.simulator.account.Account;
import com.poseidon.dolphin.simulator.account.Contract;
import com.poseidon.dolphin.simulator.account.PaymentFrequency;

public class TimelineTests {
	private Member member;
	
	@Before
	public void setUp() {
		member = new Member();
		member.setUsername("afraid86@gmail.com");
	}
	
	@Test
	public void createTimeline() {
		Timeline timeline = new Timeline();
		timeline.setId(1l);
		timeline.setMember(member);
		timeline.setActiveDate(LocalDate.of(2019, 1, 1));
		timeline.setActivity(Activity.INSTALLMENT_SAVING_ACCOUNT_OPEN);
		timeline.setReferenceId(50l);
		timeline.setState(State.READY);
		
		assertThat(timeline.getActivity()).isEqualTo(Activity.INSTALLMENT_SAVING_ACCOUNT_OPEN);
	}
	
	@Test
	public void testTimelineCompareTo() {
		TimelineComparator comparator = new TimelineComparator();
		Timeline o1 = new Timeline();
		o1.setActiveDate(LocalDate.of(2019, 1, 1));
		o1.setState(State.READY);
		
		Timeline o2 = new Timeline();
		o2.setActiveDate(LocalDate.of(2019, 2, 1));
		o2.setState(State.READY);
		assertThat(comparator.compare(o1, o2)).isNegative();
		
		o2.setActiveDate(LocalDate.of(2019, 1, 1));
		assertThat(comparator.compare(o1, o2)).isZero();
		
		o1.setState(State.DONE);
		assertThat(comparator.compare(o1, o2)).isNegative();
	}
	
	@Test
	public void testTimelineListOrdering() {
		List<Timeline> timelines = createDummyList();
		timelines.stream().forEach(item -> {
			System.out.printf("id: %d, date: %s, activity: %s, state: %s\n", item.getId(), item.getActiveDate(), item.getActivity().getDescription(), item.getState());
		});
		
		Timeline timeline = new Timeline();
		timeline.setId(16l);
		timeline.setActiveDate(LocalDate.of(2020, 1, 1));
		timeline.setActivity(Activity.INSTALLMENT_SAVING_ACCOUNT_OPEN);
		timeline.setState(State.READY);
		timelines.add(timeline);
		assertThat(timelines.get(timelines.size() - 1).getId()).isEqualTo(16l);
		
		System.out.println();
		timelines.stream().forEach(item -> {
			System.out.printf("id: %d, date: %s, activity: %s, state: %s\n", item.getId(), item.getActiveDate(), item.getActivity().getDescription(), item.getState());
		});
		Collections.sort(timelines, new TimelineComparator());
		
		assertThat(timelines.get(14)).isEqualTo(timeline);
		
		System.out.println();
		timelines.stream().forEach(item -> {
			System.out.printf("id: %d, date: %s, activity: %s, state: %s\n", item.getId(), item.getActiveDate(), item.getActivity().getDescription(), item.getState());
		});
	}
	
	private List<Timeline> createDummyList() {
		List<Timeline> timelines = new ArrayList<>();
		long id = 1l;
		LocalDate activeDate = LocalDate.of(2019, 1, 1);
		Timeline timeline = new Timeline();
		timeline.setId(id);
		timeline.setActiveDate(activeDate);
		timeline.setActivity(Activity.INSTALLMENT_SAVING_ACCOUNT_OPEN);
		timeline.setState(State.DONE);
		timelines.add(timeline);
		
		for(int i=1; i<=11; i++) {
			timeline = new Timeline();
			timeline.setId(++id);
			timeline.setActiveDate(activeDate = activeDate.plusMonths(1));
			timeline.setActivity(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
			timeline.setState(State.DONE);
			timelines.add(timeline);
		}
		
		timeline = new Timeline();
		timeline.setId(++id);
		timeline.setActiveDate(activeDate = activeDate.plusMonths(1));
		timeline.setActivity(Activity.INSTALLMENT_SAVING_ACCOUNT_CLOSE);
		timeline.setState(State.DONE);
		timelines.add(timeline);
		
		timeline = new Timeline();
		timeline.setId(++id);
		timeline.setActiveDate(activeDate);
		timeline.setActivity(Activity.FIXED_DEPOSIT_ACCOUNT_OPEN);
		timeline.setState(State.READY);
		timelines.add(timeline);
		
		timeline = new Timeline();
		timeline.setId(++id);
		timeline.setActiveDate(activeDate.plusYears(1));
		timeline.setActivity(Activity.FIXED_DEPOSIT_ACCOUNT_CLOSE);
		timeline.setState(State.READY);
		timelines.add(timeline); 
		return timelines;
	}
	
	@Test
	public void givenFixedDepositAccountThenMakeTimelines() {
		Account account = new Account();
		account.setId(10l);
		account.setMember(member);
		
		Contract contract = new Contract();
		contract.setContractDate(LocalDate.of(2019, 1, 1));
		contract.setExpiryDate(LocalDate.of(2020, 1, 1));
		account.setContract(contract);
		
		List<Timeline> timelines = Arrays.asList(
				new Timeline(account.getMember(), account.getContract().getContractDate(), Activity.FIXED_DEPOSIT_ACCOUNT_OPEN, account.getId(), State.DONE),
				new Timeline(account.getMember(), account.getContract().getExpiryDate(), Activity.FIXED_DEPOSIT_ACCOUNT_CLOSE, account.getId(), State.READY));
		
		assertThat(Timeline.makeTimelinesByFixedDeposit(account).get(0)).isEqualToComparingFieldByField(timelines.get(0));
		assertThat(Timeline.makeTimelinesByFixedDeposit(account).get(1)).isEqualToComparingFieldByField(timelines.get(1));
	}
	
	@Test
	public void givenInstallmentSavingAccountThenMakeTimelines() {
		Account account = new Account();
		account.setId(10l);
		account.setMember(member);
		
		Contract contract = new Contract();
		contract.setContractDate(LocalDate.of(2019, 1, 1));
		contract.setExpiryDate(LocalDate.of(2020, 1, 1));
		contract.setPaymentFrequency(PaymentFrequency.MONTH);
		contract.setDateOfPayment(25);
		account.setContract(contract);
		
		List<Timeline> timelines = Arrays.asList(
				new Timeline(account.getMember(), account.getContract().getContractDate(), Activity.INSTALLMENT_SAVING_ACCOUNT_OPEN, account.getId(), State.DONE),
				new Timeline(account.getMember(), LocalDate.of(2019, 2, 25), Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT, account.getId(), State.READY),
				new Timeline(account.getMember(), LocalDate.of(2019, 3, 25), Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT, account.getId(), State.READY),
				new Timeline(account.getMember(), LocalDate.of(2019, 4, 25), Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT, account.getId(), State.READY),
				new Timeline(account.getMember(), LocalDate.of(2019, 5, 25), Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT, account.getId(), State.READY),
				new Timeline(account.getMember(), LocalDate.of(2019, 6, 25), Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT, account.getId(), State.READY),
				new Timeline(account.getMember(), LocalDate.of(2019, 7, 25), Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT, account.getId(), State.READY),
				new Timeline(account.getMember(), LocalDate.of(2019, 8, 25), Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT, account.getId(), State.READY),
				new Timeline(account.getMember(), LocalDate.of(2019, 9, 25), Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT, account.getId(), State.READY),
				new Timeline(account.getMember(), LocalDate.of(2019, 10, 25), Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT, account.getId(), State.READY),
				new Timeline(account.getMember(), LocalDate.of(2019, 11, 25), Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT, account.getId(), State.READY),
				new Timeline(account.getMember(), LocalDate.of(2019, 12, 25), Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT, account.getId(), State.READY),
				new Timeline(account.getMember(), account.getContract().getExpiryDate(), Activity.INSTALLMENT_SAVING_ACCOUNT_CLOSE, account.getId(), State.READY));
		
		List<Timeline> expected = Timeline.makeTimelinesByInstallmentSaving(account);
		for(int index=0; index<expected.size(); index++) {
			assertThat(expected.get(index)).isEqualToComparingFieldByField(timelines.get(index));
		}
	}

}
