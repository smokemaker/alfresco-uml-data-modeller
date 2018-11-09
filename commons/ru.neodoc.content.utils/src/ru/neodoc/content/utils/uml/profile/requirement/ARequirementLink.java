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
 * annotation to mark field or method which holds or returns
 * element or collection of elements, which requirements must 
 * be included in a collection of current element requirements
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
/**
 * @author starovoitenkov_sv
 *
 */
public @interface ARequirementLink {

}
