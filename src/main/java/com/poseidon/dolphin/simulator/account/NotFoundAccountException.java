package com.poseidon.dolphin.simulator.account;

@SuppressWarnings("serial")
public class NotFoundAccountException extends RuntimeException {

	public NotFoundAccountException(long id) {
		super("account #" + id + " not found.");
	}
	
}
