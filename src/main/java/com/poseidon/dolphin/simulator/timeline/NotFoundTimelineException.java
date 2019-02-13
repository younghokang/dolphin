package com.poseidon.dolphin.simulator.timeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class NotFoundTimelineException extends RuntimeException {
	Logger log = LoggerFactory.getLogger(this.getClass());
	public NotFoundTimelineException(String username, State state) {
		super("username: " + username + ", state: " + state.toString());
		log.debug("notFoundTimeline username: " + username + ", state: " + state);
	}
	
}
