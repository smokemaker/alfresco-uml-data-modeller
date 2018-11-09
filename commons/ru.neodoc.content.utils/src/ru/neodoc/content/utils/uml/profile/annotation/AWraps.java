package ru.neodoc.content.utils.uml.profile.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

@Retention(RUNTIME)
@Target(TYPE)
public @interface AWraps {
	public Class<? extends ProfileStereotype> value();
}
