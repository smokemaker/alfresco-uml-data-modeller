package ru.neodoc.content.codegen.sdoc.generator.java.annotation.factory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.neodoc.content.codegen.sdoc.generator.java.annotation.AnnotationInfo;
import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;

public abstract class  ElementAnnotator<T extends BaseElement> {

	protected List<Class<? extends BaseElement>> coveredClasses;
	
	protected String annotationClassName;
	
	public ElementAnnotator(){
		super();
		coveredClasses = new ArrayList<>();
	}
	
	public abstract AnnotationInfo getAnnotation(T element);
	
	public List<Class<? extends BaseElement>> getCoveredClasses(){
		return this.coveredClasses;
	}
	
	protected Set<String> getDefaultImports(){
		Set<String> result = new HashSet<>();
		
		if ((annotationClassName!=null) && (annotationClassName.length()>0))
			result.add(annotationClassName);
		
		return result;
	}

	protected String getDefaultAnnotation(){
		if (annotationClassName==null)
			return null;
		String[] splitted = annotationClassName.split("\\.");
		if (splitted==null)
			return null;
		if (splitted.length==0)
			return null;
		return splitted[splitted.length-1];
	}
}
