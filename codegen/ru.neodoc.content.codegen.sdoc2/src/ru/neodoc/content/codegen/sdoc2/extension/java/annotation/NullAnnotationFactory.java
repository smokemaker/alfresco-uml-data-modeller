package ru.neodoc.content.codegen.sdoc2.extension.java.annotation;

import ru.neodoc.content.codegen.sdoc2.generator.annotation.AnnotationInfo;
import ru.neodoc.content.codegen.sdoc2.generator.annotation.SdocAnnotationFactory;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

public class NullAnnotationFactory implements SdocAnnotationFactory {

	@Override
	public AnnotationInfo getAnnotation(AbstractWrapper wrapper) {
		return null;
	}

	@Override
	public AnnotationInfo getAnnotation(ProfileStereotype element) {
		return null;
	}

}
