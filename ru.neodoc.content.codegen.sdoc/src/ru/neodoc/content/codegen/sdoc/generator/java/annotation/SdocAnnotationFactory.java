package ru.neodoc.content.codegen.sdoc.generator.java.annotation;

import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;
import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;

public interface SdocAnnotationFactory {

	public AnnotationInfo getAnnotation(BaseWrapper wrapper);
	public AnnotationInfo getAnnotation(BaseElement element);
	
}
