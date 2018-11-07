package ru.neodoc.content.codegen.sdoc.generator.java.annotation.factory;

import java.util.Arrays;

import ru.neodoc.content.codegen.sdoc.generator.java.annotation.AnnotationInfo;
import ru.neodoc.content.modeller.utils.uml.elements.BaseAssociationElement;
import ru.neodoc.content.modeller.utils.uml.elements.ChildAssociation;
import ru.neodoc.content.modeller.utils.uml.elements.PeerAssociation;

public class AssociationAnnotator extends ElementAnnotator<BaseAssociationElement> {
	
	public AssociationAnnotator() {
		super();
		this.coveredClasses.addAll(Arrays.asList(
					PeerAssociation.class,
					ChildAssociation.class
				));
		this.annotationClassName = "alfresco.module.SdocCore.classes.annotation.TypeAssociation";
	}

	@Override
	public AnnotationInfo getAnnotation(BaseAssociationElement element) {
		AnnotationInfo result = new AnnotationInfo();

		StringBuffer value = new StringBuffer();
		value.append(getDefaultAnnotation())
			.append("(")
			.append("title=\"")
			.append(element.getTitle())
			.append("\", target=\"")
			.append(element.getTarget().getNamespace().getPrefix())
			.append(":")
			.append(element.getTarget().getName())
			.append("\")");
		
		result.annotation = value.toString();
		result.addImport(annotationClassName);
		
		return result;
	}

	
	
}
