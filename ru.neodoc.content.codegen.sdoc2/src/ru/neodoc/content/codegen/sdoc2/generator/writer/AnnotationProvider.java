package ru.neodoc.content.codegen.sdoc2.generator.writer;

import java.util.Set;

import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public interface AnnotationProvider {
	
	public String getAnnotation(AbstractWrapper baseWrapper);
	public String getAnnotation(StereotypedElement stereotypedElement);
	
	public Set<String> getAnnotationImports(AbstractWrapper baseWrapper);
	public Set<String> getAnnotationImports(StereotypedElement stereotypedElement);
	
}
