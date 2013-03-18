package com.dummi.common.command;

public class ResolverNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4191722791965346804L;

	public ResolverNotFoundException(int id) {
		super("Resolver " + id + " not found(check your registration).");
	}
	
}
