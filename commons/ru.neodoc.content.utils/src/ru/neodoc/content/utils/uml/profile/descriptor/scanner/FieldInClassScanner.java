package ru.neodoc.content.utils.uml.profile.descriptor.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class FieldInClassScanner extends AbstractScanner<Class, Field> {

	public FieldInClassScanner() {
		super(Class.class, Field.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected List<Field> getChildren(Class parent) {
		return Arrays.asList(parent.getDeclaredFields());
	}

	@Override
	protected Annotation getAnnotation(Field child, Class<? extends Annotation> annotationClass) {
		return child.getAnnotation(annotationClass);
	}

}
