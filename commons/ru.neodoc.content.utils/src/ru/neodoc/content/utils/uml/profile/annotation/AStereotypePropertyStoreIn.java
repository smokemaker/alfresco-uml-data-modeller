package ru.neodoc.content.utils.uml.profile.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

@Retention(RUNTIME)
@Target({ FIELD, METHOD, PARAMETER })
public @interface AStereotypePropertyStoreIn {
	Class<? extends ProfileStereotype> storageOwner();
	String storagePropertyName();
}
