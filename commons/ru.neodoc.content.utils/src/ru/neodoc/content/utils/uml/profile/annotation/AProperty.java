package ru.neodoc.content.utils.uml.profile.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface AProperty {
	public String name();
	public Class<?> type() default String.class;
}
