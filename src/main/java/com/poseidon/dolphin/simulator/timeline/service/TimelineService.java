package com.poseidon.dolphin.simulator.timeline.service;

import java.util.List;

import com.poseidon.dolphin.member.Member;
import com.poseidon.dolphin.simulator.timeline.Timeline;

public interface TimelineService {
	Timeline next(Member member);
	List<Timeline> saveAll(List<Timeline> timelines);
	Timeline done(Member member);
	boolean availableRegulaPayment(Member member);
}
