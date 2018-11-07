package ru.neodoc.content.codegen.sdoc.generator.java.writer;

import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;

public interface LookupProvider {
	
	public String lookupTargetName(BaseElement element);
	
}
