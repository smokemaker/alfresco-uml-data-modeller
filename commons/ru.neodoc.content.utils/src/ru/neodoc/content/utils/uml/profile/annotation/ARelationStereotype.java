package ru.neodoc.content.utils.uml.profile.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

@Retention(RUNTIME)
@Target(TYPE)
public @interface ARelationStereotype {
	
	@Retention(RUNTIME)
	public @interface ARelation {
		Class<? extends ProfileStereotype>[] source() default {};
		Class<? extends ProfileStereotype>[] notSource() default {};
		Class<? extends ProfileStereotype>[] target() default {};
		Class<? extends ProfileStereotype>[] notTarget() default {};
		boolean sourceIsMultiple() default true;
		boolean targetIsMultiple() default true;
	}
	
	ARelation[] relations() default {};
	
}
