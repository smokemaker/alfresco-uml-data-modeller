package ru.neodoc.content.utils.uml.profile.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObject;


@Retention(RUNTIME)
@Target({ TYPE, FIELD })
public @interface AImplemented {
	public Class<? extends ImplementationMetaObject> value();
}
