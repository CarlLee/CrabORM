package com.carllee.androidorm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataField {
	public static enum FieldType {
		INTEGER, TEXT, BLOB, REAL, DEFAULT /*
											 * derive from field declearation
											 */

	};

	public boolean unique() default false;

	public boolean primaryKey() default false;

	public FieldType filedType() default FieldType.DEFAULT;
}
