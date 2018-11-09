package ru.neodoc.content.utils.uml.profile.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.uml.profile.EmptyProfile;

@Retention(RUNTIME)
@Target(TYPE)
public @interface AStereotype {
	
	@Retention(RUNTIME)
	public @interface AApplication {
		
		public enum Multiplicity {NONE, UNIQUE, DUPLICATE};
		
		public Class<? extends Element>[] classes() default {};
		public Multiplicity multiple() default Multiplicity.NONE;
		public Class<?>[] requires() default {};
	}
	
	public String name();
	public boolean isAbstract() default false;
	public AApplication[] applications() default {};
	public Class<?> profile() default EmptyProfile.class;

	public static final Class<?> EMPTY_PROFILE = EmptyProfile.class;
}
