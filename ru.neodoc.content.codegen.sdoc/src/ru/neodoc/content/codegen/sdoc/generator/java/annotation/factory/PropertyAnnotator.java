package ru.neodoc.content.codegen.sdoc.generator.java.annotation.factory;

import ru.neodoc.content.codegen.sdoc.generator.java.annotation.AnnotationInfo;
import ru.neodoc.content.codegen.sdoc.wrap.DataTypeWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.WrapperFactory;
import ru.neodoc.content.modeller.utils.uml.elements.Property;

public class PropertyAnnotator extends ElementAnnotator<Property> {

	public PropertyAnnotator() {
		super();
		this.coveredClasses.add(Property.class);
		this.annotationClassName = "alfresco.module.SdocCore.classes.annotation.TypeAttribute";
	}
	
	@Override
	public AnnotationInfo getAnnotation(Property element) {
		AnnotationInfo result = new AnnotationInfo();
		
		StringBuffer value = new StringBuffer();
		value.append(getDefaultAnnotation())
			.append("(")
			.append("title=\"")
			.append(element.getTitle())
			.append("\", type=");
		
		DataTypeWrapper dtw = WrapperFactory.get(element.getDataType());
		if (dtw == null)
			value.append("null");
		else
			value.append(dtw.getFullTargetJavaName());
		
		value.append(")");
		
		result.annotation = value.toString();
		result.addImport(annotationClassName);
		
		return result;
	}

}
