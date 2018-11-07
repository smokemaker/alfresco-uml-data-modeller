package ru.neodoc.content.utils.uml.profile.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD, METHOD, PARAMETER })
public @interface AStereotypeProperty {

	String name() default "";
	String externalName() default "";
	boolean isReadOnly() default false;
	Class<?> type();
	Class<?> exposedType() default Void.class;
	@Deprecated
	String defaultValue() default "";
}
