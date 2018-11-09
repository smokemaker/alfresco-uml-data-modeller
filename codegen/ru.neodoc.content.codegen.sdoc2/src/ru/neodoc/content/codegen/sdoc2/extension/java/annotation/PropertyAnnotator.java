package ru.neodoc.content.codegen.sdoc2.extension.java.annotation;

import ru.neodoc.content.codegen.sdoc2.extension.java.WrapperJavaExtension;
import ru.neodoc.content.codegen.sdoc2.generator.annotation.AnnotationInfo;
import ru.neodoc.content.codegen.sdoc2.wrap.DataTypeWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.WrapperFactory;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property;

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
			value.append(WrapperJavaExtension.get(dtw).getFullTargetJavaName() );
		
		value.append(")");
		
		result.annotation = value.toString();
		result.addImport(annotationClassName);
		
		return result;
	}

}
