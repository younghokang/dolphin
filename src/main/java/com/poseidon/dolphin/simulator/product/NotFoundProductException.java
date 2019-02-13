package com.poseidon.dolphin.simulator.product;

@SuppressWarnings("serial")
public class NotFoundProductException extends RuntimeException {

	public NotFoundProductException(long id) {
		super("product #" + id + " not found.");
	}

}
