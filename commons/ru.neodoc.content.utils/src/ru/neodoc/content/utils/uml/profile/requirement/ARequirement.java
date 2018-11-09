/**
 * 
 */
package ru.neodoc.content.utils.uml.profile.requirement;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/*
 * annotation to mark fields or methods that holds or returns 
 * element or a collection of elements, which are required by 
 * the element, which has the annotation
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
/**
 * @author starovoitenkov_sv
 *
 */
public @interface ARequirement {

}
