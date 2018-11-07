package ru.neodoc.content.codegen.sdoc2.extension.java.annotation;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Type;

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
