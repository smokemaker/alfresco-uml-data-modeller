package ru.neodoc.content.codegen.sdoc2.extension.java.annotation;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Aspect;

public class AspectAnnotator extends ClassElementAnnotator {
	
	public AspectAnnotator() {
		super();
		this.coveredClasses.add(Aspect.class);
		this.annotationClassName = "alfresco.module.SdocCore.classes.annotation.Aspect";		
	}
	
}
