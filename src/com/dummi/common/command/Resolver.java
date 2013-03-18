package com.dummi.common.command;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * The Method is to be invoked by Command invocation
 */
@Target(value={ElementType.METHOD})
@Retention(value=java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Resolver {
	int id();
}
