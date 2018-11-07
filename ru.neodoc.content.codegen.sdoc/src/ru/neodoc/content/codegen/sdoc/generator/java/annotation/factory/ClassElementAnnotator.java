package ru.neodoc.content.codegen.sdoc.generator.java.annotation.factory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ru.neodoc.content.codegen.sdoc.generator.java.annotation.AnnotationInfo;
import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.MandatoryAspect;

public class ClassElementAnnotator extends ElementAnnotator<BaseClassElement> {

	// protected Map<String, String> annotationContent = new LinkedHashMap<String, String>();
	
	@Override
	public AnnotationInfo getAnnotation(BaseClassElement element) {
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

	protected void fillAnnotationContent(Map<String, String> annotationContent, BaseClassElement element){
		fillTitle(annotationContent, element);
		fillParent(annotationContent, element);
		fillMandatoryAspects(annotationContent, element);
	}
	
	protected void fillTitle(Map<String, String> annotationContent, BaseClassElement element){
		annotationContent.put("title", "\"" + element.getTitle() + "\"");
	}
	
	protected void fillParent(Map<String, String> annotationContent, BaseClassElement element){
		BaseClassElement parent = element.getParent();
		if (parent == null)
			return;
		annotationContent.put("parent", 
						"\"" 
						+ parent.getNamespace().getPrefix() 
						+ ":"
						+ parent.getName()
						+ "\"");
	}
	
	protected void fillMandatoryAspects(Map<String, String> annotationContent, BaseClassElement element){
		
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
				.append(ma.getTarget().getNamespace().getPrefix())
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
