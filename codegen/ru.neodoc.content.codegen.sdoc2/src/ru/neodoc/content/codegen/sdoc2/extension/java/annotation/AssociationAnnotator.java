package ru.neodoc.content.codegen.sdoc2.extension.java.annotation;

import java.util.Arrays;

import ru.neodoc.content.codegen.sdoc2.generator.annotation.AnnotationInfo;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.AssociationSolid;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.ChildAssociation;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;

public class AssociationAnnotator extends ElementAnnotator<AssociationSolid> {
	
	public AssociationAnnotator() {
		super();
		this.coveredClasses.addAll(Arrays.asList(
					Association.class,
					ChildAssociation.class
				));
		this.annotationClassName = "alfresco.module.SdocCore.classes.annotation.TypeAssociation";
	}

	@Override
	public AnnotationInfo getAnnotation(AssociationSolid element) {
		AnnotationInfo result = new AnnotationInfo();

		StringBuffer value = new StringBuffer();
		value.append(getDefaultAnnotation())
			.append("(")
			.append("title=\"")
			.append(element.getTitle())
			.append("\", target=\"")
			.append(Namespace._HELPER.findNearestFor(element.getTarget()).getPrefix())
			.append(":")
			.append(element.getTarget().getName())
			.append("\")");
		
		result.annotation = value.toString();
		result.addImport(annotationClassName);
		
		return result;
	}

	
	
}
