package ru.neodoc.content.codegen.sdoc.generator.java.annotation.factory;

import ru.neodoc.content.modeller.utils.uml.elements.Aspect;

public class AspectAnnotator extends ClassElementAnnotator {
	
	public AspectAnnotator() {
		super();
		this.coveredClasses.add(Aspect.class);
		this.annotationClassName = "alfresco.module.SdocCore.classes.annotation.Aspect";		
	}
	
}
