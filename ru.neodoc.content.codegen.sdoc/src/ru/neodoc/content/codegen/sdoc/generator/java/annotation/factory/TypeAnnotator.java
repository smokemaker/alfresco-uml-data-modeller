package ru.neodoc.content.codegen.sdoc.generator.java.annotation.factory;

import ru.neodoc.content.modeller.utils.uml.elements.Type;

public class TypeAnnotator extends ClassElementAnnotator {

/*	static {
		DefaultAnnotationFactory.addAnnotator(TypeAnnotator.class);
	}
*/	
	public TypeAnnotator() {
		super();
		this.coveredClasses.add(Type.class);
		this.annotationClassName = "alfresco.module.SdocCore.classes.annotation.Type";
	}
	
}
