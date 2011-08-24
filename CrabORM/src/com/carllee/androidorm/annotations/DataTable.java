package com.carllee.androidorm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataTable {
	public static final String DEFAULT_TABLE_NAME = "__default__";
	
	String name() default DEFAULT_TABLE_NAME;
}
