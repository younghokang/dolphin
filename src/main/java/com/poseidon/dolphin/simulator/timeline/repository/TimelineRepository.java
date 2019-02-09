package com.poseidon.dolphin.simulator.timeline.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.simulator.member.Member;
import com.poseidon.dolphin.simulator.timeline.State;
import com.poseidon.dolphin.simulator.timeline.Timeline;

@Repository
public interface TimelineRepository extends JpaRepository<Timeline, Long> {
	List<Timeline> findAllByMemberOrderByActiveDateAscStateAsc(Member member);
	Optional<Timeline> findByMemberAndState(Member member, State state);
	Optional<Timeline> findFirstByMemberAndStateAndActiveDateGreaterThanEqualOrderByActiveDateAscStateAsc(Member member, State state, LocalDate activeDate);
	long countByMember(Member member);
}
