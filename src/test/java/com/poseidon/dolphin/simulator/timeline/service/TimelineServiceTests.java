package com.poseidon.dolphin.simulator.timeline.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.timeline.Activity;
import com.poseidon.dolphin.simulator.timeline.State;
import com.poseidon.dolphin.simulator.timeline.Timeline;
import com.poseidon.dolphin.simulator.timeline.repository.TimelineRepository;

@RunWith(SpringRunner.class)
public class TimelineServiceTests {
	private TimelineService timelineService;
	
	@MockBean
	private TimelineRepository timelineRepository;
	
	private Member member;
	
	@Before
	public void setUp() {
		timelineService = new TimelineService(timelineRepository);
		
		member = new Member();
		member.setUsername("afraid86@gmail.com");
	}
	
	@Test
	public void whenNextTimelineExistsProcessStateThenReturnProcessTimeline() {
		Member alice = new Member();
		alice.setUsername("alice");
		alice.setCurrent(LocalDate.of(2019, 2, 1));
		
		Member bob = new Member();
		bob.setUsername("bob");
		bob.setCurrent(LocalDate.of(2019, 2, 1));
		
		Timeline timeline = new Timeline();
		timeline.setMember(alice);
		timeline.setState(State.PROCESS);
		
		given(timelineRepository.findByMemberAndState(eq(alice), any(State.class))).willReturn(Optional.of(timeline));
		given(timelineRepository.findFirstByMemberAndStateAndActiveDateGreaterThanEqualOrderByActiveDateAscStateAsc(eq(alice), any(State.class), any(LocalDate.class))).willReturn(Optional.empty());
		
		assertThat(timelineService.next(alice)).isEqualTo(timeline);
		
		given(timelineRepository.findByMemberAndState(bob, State.PROCESS)).willReturn(Optional.empty());
		given(timelineRepository.findFirstByMemberAndStateAndActiveDateGreaterThanEqualOrderByActiveDateAscStateAsc(any(Member.class), any(State.class), any(LocalDate.class))).willReturn(Optional.of(new Timeline()));
		given(timelineRepository.save(any(Timeline.class))).willReturn(timeline);
		
		assertThat(timelineService.next(bob)).isNotNull();
		
		given(timelineRepository.findFirstByMemberAndStateAndActiveDateGreaterThanEqualOrderByActiveDateAscStateAsc(any(Member.class), any(State.class), any(LocalDate.class))).willReturn(Optional.empty());
		
		assertThat(timelineService.next(bob)).isNull();
		
		verify(timelineRepository, times(3)).findByMemberAndState(any(Member.class), any(State.class));
		verify(timelineRepository, times(2)).findFirstByMemberAndStateAndActiveDateGreaterThanEqualOrderByActiveDateAscStateAsc(any(Member.class), any(State.class), any(LocalDate.class));
	}
	
	@Test
	public void givenTimelineDoneByMemberThenReturnUpdated() {
		given(timelineRepository.findByMemberAndState(any(Member.class), any(State.class))).willThrow(NullPointerException.class);
		
		try {
			timelineService.done(member);
			fail();
		} catch(NullPointerException e) {}
		
		Timeline timeline = new Timeline();
		timeline.setState(State.PROCESS);
		
		given(timelineRepository.findByMemberAndState(any(Member.class), eq(State.PROCESS))).willReturn(Optional.of(timeline));
		given(timelineRepository.save(any(Timeline.class))).willReturn(timeline);
		
		assertThat(timelineService.done(member).getState()).isEqualTo(State.DONE);
		
		verify(timelineRepository, times(2)).findByMemberAndState(any(Member.class), any(State.class));
	}
	
	@Test
	public void givenMemberThenAvailableRegularPayment() {
		given(timelineRepository.findByMemberAndState(any(Member.class), any(State.class))).willReturn(Optional.empty());
		
		assertThat(timelineService.availableRegulaPayment(member)).isFalse();
		
		Timeline timeline = new Timeline();
		timeline.setActiveDate(LocalDate.now());
		timeline.setActivity(Activity.FIXED_DEPOSIT_ACCOUNT_CLOSE);
		
		member.setCurrent(LocalDate.now());
		given(timelineRepository.findByMemberAndState(any(Member.class), any(State.class))).willReturn(Optional.of(timeline));
		
		assertThat(timelineService.availableRegulaPayment(member)).isFalse();
		
		timeline.setActivity(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		given(timelineRepository.findByMemberAndState(any(Member.class), any(State.class))).willReturn(Optional.of(timeline));
		
		assertThat(timelineService.availableRegulaPayment(member)).isTrue();
		
		verify(timelineRepository, times(3)).findByMemberAndState(any(Member.class), any(State.class));
	}
	
}
