package com.poseidon.dolphin.simulator.timeline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.dolphin.simulator.timeline.Timeline;

@Repository
public interface TimelineRepository extends JpaRepository<Timeline, Long> {
}
