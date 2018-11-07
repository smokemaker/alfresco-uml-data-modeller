package ru.neodoc.content.codegen.sdoc.generator.java.annotation.factory;

import ru.neodoc.content.codegen.sdoc.generator.java.annotation.AnnotationInfo;
import ru.neodoc.content.modeller.utils.uml.elements.Namespace;

public class NamespaceAnnotator extends ElementAnnotator<Namespace> {

/*	static {
		DefaultAnnotationFactory.addAnnotator(NamespaceAnnotator.class);
	}
*/	
	public NamespaceAnnotator() {
		super();
		this.coveredClasses.add(Namespace.class);
	}
	
	@Override
	public AnnotationInfo getAnnotation(Namespace element) {
		return null;
	}

}
