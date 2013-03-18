package com.dummi.common.command;

public class ResolverInvokeException extends RuntimeException {

	private static final long serialVersionUID = 4191722791965346802L;

	public ResolverInvokeException(int id, Throwable reason) {
		super("Resolver " + id + " failed.", reason);
	}
	
}
