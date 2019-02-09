package com.poseidon.dolphin.simulator.timeline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.timeline.Activity;
import com.poseidon.dolphin.simulator.timeline.State;
import com.poseidon.dolphin.simulator.timeline.Timeline;
import com.poseidon.dolphin.simulator.timeline.repository.TimelineRepository;

@Service
public class TimelineService {
	private final TimelineRepository timelineRepository;
	
	@Autowired
	public TimelineService(TimelineRepository timelineRepository) {
		this.timelineRepository = timelineRepository;
	}
	
	public Timeline next(Member member) {
		return timelineRepository.findByMemberAndState(member, State.PROCESS)
				.orElseGet(() -> {
					return timelineRepository.findFirstByMemberAndStateAndActiveDateGreaterThanEqualOrderByActiveDateAscStateAsc(member, State.READY, member.getCurrent())
							.map(timeline -> {
								timeline.setState(State.PROCESS);
								return timelineRepository.save(timeline);
							})
							.orElse(null);
				});
	}

	public List<Timeline> saveAll(List<Timeline> timelines) {
		return timelineRepository.saveAll(timelines);
	}
	
	public Timeline done(Member member) {
		return timelineRepository.findByMemberAndState(member, State.PROCESS)
				.map(timeline -> {
					timeline.setState(State.DONE);
					return timelineRepository.save(timeline);
				}).orElseThrow(NullPointerException::new);
	}
	
	public boolean availableRegulaPayment(Member member) {
		return timelineRepository.findByMemberAndState(member, State.PROCESS).map(mapper -> {
			return mapper.getActiveDate().isEqual(member.getCurrent()) && 
				mapper.getActivity().equals(Activity.INSTALLMENT_SAVING_REGULARY_PAYMENT);
		}).orElse(false);
	}
	
}
