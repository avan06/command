package com.dummi.common.command;

import java.lang.reflect.Method;

public class ResolverDelegateIdentifierException extends RuntimeException {

	private static final long serialVersionUID = 4191722791965346802L;

	public ResolverDelegateIdentifierException(int id, Method m1, Method m2) {
		super("Resolver " + id + " need by " + m2.getName() + " have been taken by " + m1.getName());
	}
	
}
