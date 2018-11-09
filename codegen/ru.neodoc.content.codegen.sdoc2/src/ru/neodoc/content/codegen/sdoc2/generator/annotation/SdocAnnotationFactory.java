package ru.neodoc.content.codegen.sdoc2.generator.annotation;

import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

public interface SdocAnnotationFactory {

	public static final String PROP_NAME = SdocAnnotationFactory.class.getName(); 
	
	public AnnotationInfo getAnnotation(AbstractWrapper wrapper);
	public AnnotationInfo getAnnotation(ProfileStereotype element);
	
}
