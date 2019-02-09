package com.poseidon.dolphin.simulator.timeline;

import java.util.Comparator;

public class TimelineComparator implements Comparator<Timeline> {

	@Override
	public int compare(Timeline o1, Timeline o2) {
		int activeDate = o1.getActiveDate().compareTo(o2.getActiveDate());
		if(activeDate == 0) {
			return o1.getState().compareTo(o2.getState());
		}
		return activeDate;
	}

}
