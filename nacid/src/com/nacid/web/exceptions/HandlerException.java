package com.nacid.web.exceptions;

public class HandlerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HandlerException() {
		super();
	}

	public HandlerException(String msg) {
		super(msg);
	}

}
