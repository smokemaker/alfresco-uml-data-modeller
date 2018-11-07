package ru.neodoc.content.codegen.sdoc.generator.java.writer;

import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;

public interface AnnotationProvider {
	
	public String getAnnotation(BaseWrapper baseWrapper);
	
}
