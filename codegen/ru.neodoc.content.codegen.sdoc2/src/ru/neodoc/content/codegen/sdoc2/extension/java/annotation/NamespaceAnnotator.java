package ru.neodoc.content.codegen.sdoc2.extension.java.annotation;

import ru.neodoc.content.codegen.sdoc2.generator.annotation.AnnotationInfo;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;

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
