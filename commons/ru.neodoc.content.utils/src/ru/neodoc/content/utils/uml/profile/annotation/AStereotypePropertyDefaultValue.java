package ru.neodoc.content.utils.uml.profile.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

@Retention(RUNTIME)
@Target(FIELD)
public @interface AStereotypePropertyDefaultValue {
	String propertyName();
	Class<? extends ProfileStereotype> owner();
	boolean setOnCreate() default false;
}
