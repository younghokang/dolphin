package com.poseidon.dolphin.simulator.timeline.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.member.repository.MemberRepository;
import com.poseidon.dolphin.simulator.timeline.Activity;
import com.poseidon.dolphin.simulator.timeline.State;
import com.poseidon.dolphin.simulator.timeline.Timeline;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TimelineRepositoryTests {
	@Autowired
	private TimelineRepository timelineRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	private Member alice;
	
	@Before
	public void setUp() {
		alice = new Member();
		alice.setUsername("alice");
		alice.setCurrent(LocalDate.of(2019, 1, 1));
		memberRepository.saveAndFlush(alice);
	}
	
	@Test
	public void givenUnorderedListThenReturnOrderedList() {
		List<Timeline> timelines = createUnorderedList();
		
		assertThat(timelines.size()).isEqualTo(4);
		assertThat(timelines.get(0).getActiveDate()).isEqualTo(LocalDate.of(2019, 1, 1));
		assertThat(timelines.get(1).getActiveDate()).isEqualTo(LocalDate.of(2020, 1, 1));
		assertThat(timelines.get(2).getActiveDate()).isEqualTo(LocalDate.of(2019, 2, 1));
		assertThat(timelines.get(3).getActiveDate()).isEqualTo(LocalDate.of(2019, 3, 1));
		assertThat(timelines.get(0).getActivity()).isEqualTo(Activity.INSTALLMENT_SAVING_ACCOUNT_OPEN);
		assertThat(timelines.get(1).getActivity()).isEqualTo(Activity.INSTALLMENT_SAVING_ACCOUNT_CLOSE);
		assertThat(timelines.get(2).getActivity()).isEqualTo(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		assertThat(timelines.get(3).getActivity()).isEqualTo(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		
		timelineRepository.saveAll(timelines);
		List<Timeline> orderedTimelines = timelineRepository.findAllByMemberOrderByActiveDateAscStateAsc(alice);
		assertThat(orderedTimelines.size()).isEqualTo(4);
		assertThat(orderedTimelines.get(0).getActiveDate()).isEqualTo(LocalDate.of(2019, 1, 1));
		assertThat(orderedTimelines.get(1).getActiveDate()).isEqualTo(LocalDate.of(2019, 2, 1));
		assertThat(orderedTimelines.get(2).getActiveDate()).isEqualTo(LocalDate.of(2019, 3, 1));
		assertThat(orderedTimelines.get(3).getActiveDate()).isEqualTo(LocalDate.of(2020, 1, 1));
		assertThat(orderedTimelines.get(0).getActivity()).isEqualTo(Activity.INSTALLMENT_SAVING_ACCOUNT_OPEN);
		assertThat(orderedTimelines.get(1).getActivity()).isEqualTo(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		assertThat(orderedTimelines.get(2).getActivity()).isEqualTo(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		assertThat(orderedTimelines.get(3).getActivity()).isEqualTo(Activity.INSTALLMENT_SAVING_ACCOUNT_CLOSE);
	}

	private List<Timeline> createUnorderedList() {
		List<Timeline> timelines = new ArrayList<>();
		Timeline timeline = new Timeline();
		timeline.setMember(alice);
		timeline.setActiveDate(LocalDate.of(2019, 1, 1));
		timeline.setActivity(Activity.INSTALLMENT_SAVING_ACCOUNT_OPEN);
		timeline.setReferenceId(50l);
		timeline.setState(State.READY);
		timelines.add(timeline);
		
		timeline = new Timeline();
		timeline.setMember(alice);
		timeline.setActiveDate(LocalDate.of(2020, 1, 1));
		timeline.setActivity(Activity.INSTALLMENT_SAVING_ACCOUNT_CLOSE);
		timeline.setReferenceId(50l);
		timeline.setState(State.READY);
		timelines.add(timeline);
		
		timeline = new Timeline();
		timeline.setMember(alice);
		timeline.setActiveDate(LocalDate.of(2019, 2, 1));
		timeline.setActivity(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		timeline.setReferenceId(50l);
		timeline.setState(State.READY);
		timelines.add(timeline);
		
		timeline = new Timeline();
		timeline.setMember(alice);
		timeline.setActiveDate(LocalDate.of(2019, 3, 1));
		timeline.setActivity(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		timeline.setReferenceId(50l);
		timeline.setState(State.READY);
		timelines.add(timeline);
		return timelines;
	}
	
	@Test
	public void givenMemberAndStateThenReturnOnlyOneTimeline() {
		List<Timeline> timelines = new ArrayList<>();
		Timeline timeline = new Timeline();
		timeline.setMember(alice);
		timeline.setActiveDate(LocalDate.of(2019, 1, 1));
		timeline.setActivity(Activity.INSTALLMENT_SAVING_ACCOUNT_OPEN);
		timeline.setReferenceId(50l);
		timeline.setState(State.READY);
		timelines.add(timeline);
		
		timeline = new Timeline();
		timeline.setMember(alice);
		timeline.setActiveDate(LocalDate.of(2020, 1, 1));
		timeline.setActivity(Activity.INSTALLMENT_SAVING_ACCOUNT_CLOSE);
		timeline.setReferenceId(50l);
		timeline.setState(State.PROCESS);
		timelines.add(timeline);
		
		timelineRepository.saveAll(timelines);
		assertThat(timelineRepository.findByMemberAndState(alice, State.PROCESS)).isNotEmpty();
	}
	
	@Test
	public void givenMemberAndActiveDateThenReturnNextTimeline() {
		List<Timeline> unorderedTimelines = createUnorderedList();
		timelineRepository.saveAll(unorderedTimelines);
		
		LocalDate activeDate = LocalDate.of(2019, 2, 1);
		Timeline timeline = timelineRepository.findFirstByMemberAndStateAndActiveDateGreaterThanEqualOrderByActiveDateAscStateAsc(alice, State.READY, activeDate)
				.orElse(null);
		assertThat(timeline).isNotNull();
		assertThat(timeline.getActivity()).isEqualTo(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		assertThat(timeline.getActiveDate()).isEqualTo(activeDate);
		
		timeline.setState(State.DONE);
		timeline = timelineRepository.findFirstByMemberAndStateAndActiveDateGreaterThanEqualOrderByActiveDateAscStateAsc(alice, State.READY, activeDate)
				.orElse(null);
		assertThat(timeline).isNotNull();
		assertThat(timeline.getActivity()).isEqualTo(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		assertThat(timeline.getActiveDate()).isEqualTo(LocalDate.of(2019, 3, 1));
	}
	
	@Test
	public void givenMemberThenReturnCount() {
		long count = timelineRepository.countByMember(alice);
		assertThat(count).isEqualTo(0);
		
		Timeline timeline = new Timeline();
		timeline.setMember(alice);
		timeline.setActiveDate(LocalDate.now());
		timeline.setActivity(Activity.FIXED_DEPOSIT_ACCOUNT_CLOSE);
		timeline.setState(State.SKIP);
		timeline.setReferenceId(15l);
		Timeline savedTimeline = timelineRepository.save(timeline);
		assertThat(savedTimeline).isEqualTo(timeline);
		assertThat(savedTimeline.getId()).isPositive();
	}

}
