package ru.neodoc.content.utils.uml.profile.descriptor.scanner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ClassInClassScanner extends AbstractScanner<Class, Class> {

	public ClassInClassScanner() {
		super(Class.class, Class.class);
	}

	@Override
	protected List<Class> getChildren(Class parent) {
		Class<?>[] children = parent.getDeclaredClasses();
		return Arrays.asList(children);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Annotation getAnnotation(Class child, Class<? extends Annotation> annotationClass) {
		return child.getAnnotation(annotationClass);
	}

	
	
}
