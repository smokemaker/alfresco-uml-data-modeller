package ru.neodoc.content.codegen.sdoc2.extension.java.annotation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.neodoc.content.codegen.sdoc2.generator.annotation.AnnotationInfo;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;

public class ClassElementAnnotator extends ElementAnnotator<ClassMain> {

	// protected Map<String, String> annotationContent = new LinkedHashMap<String, String>();
	
	@Override
	public AnnotationInfo getAnnotation(ClassMain element) {
		AnnotationInfo result = new AnnotationInfo();
		StringBuffer sb = new StringBuffer();
		
		 Map<String, String> annotationContent = new LinkedHashMap<String, String>();
		
		fillAnnotationContent(annotationContent, element);
		
		sb.append(getAnnotationElement()).append("(");
		
		boolean first = true;
		for (Map.Entry<String, String> entry: annotationContent.entrySet()){
			if (!first) 
				sb.append(", ");
			sb.append(entry.getKey()).append("=").append(entry.getValue());
			first = false;
		}
		
		sb.append(")");
		
		result.annotation = sb.toString();
		result.addImports(getDefaultImports());
		
		return result;
	}

	protected void fillAnnotationContent(Map<String, String> annotationContent, ClassMain element){
		fillTitle(annotationContent, element);
		fillParent(annotationContent, element);
		fillMandatoryAspects(annotationContent, element);
	}
	
	protected void fillTitle(Map<String, String> annotationContent, ClassMain element){
		annotationContent.put("title", "\"" + element.getTitle() + "\"");
	}
	
	protected void fillParent(Map<String, String> annotationContent, ClassMain element){
		ClassMain parent = element.getInherits().size()==0?null:element.getInherits().get(0).getGeneral();
		if (parent == null)
			return;
		annotationContent.put("parent", 
						"\"" 
						+ Namespace._HELPER.findNearestFor(parent).getPrefix() 
						+ ":"
						+ parent.getName()
						+ "\"");
	}
	
	protected void fillMandatoryAspects(Map<String, String> annotationContent, ClassMain element){
		
		List<MandatoryAspect> mandatoryAspects = element.getMandatoryAspects();
		if (mandatoryAspects.isEmpty())
			return;
		
		StringBuffer value = new StringBuffer();
		value.append("{");
		
		boolean first = true;
		for (MandatoryAspect ma: mandatoryAspects){
			if (ma.getTarget()==null)
				continue;
			if (!first)
				value.append(", ");
			first = false;
			value.append("\"")
				.append(Namespace._HELPER.findNearestFor(ma.getTarget()).getPrefix())
				.append(":")
				.append(ma.getTarget().getName())
				.append("\"");
		}
		
		value.append("}");
		
		annotationContent.put("mandatoryAspects", value.toString());
	}
	
	protected String getAnnotationElement(){
		return getDefaultAnnotation();
	}
}
