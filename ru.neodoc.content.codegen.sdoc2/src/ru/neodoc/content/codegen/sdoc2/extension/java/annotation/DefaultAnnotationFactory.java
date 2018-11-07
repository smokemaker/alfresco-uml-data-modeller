package ru.neodoc.content.codegen.sdoc2.extension.java.annotation;

import java.util.HashMap;
import java.util.Map;

import ru.neodoc.content.codegen.sdoc2.generator.annotation.AnnotationInfo;
import ru.neodoc.content.codegen.sdoc2.generator.annotation.SdocAnnotationFactory;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

public class DefaultAnnotationFactory implements SdocAnnotationFactory {

	@SuppressWarnings("rawtypes")
	protected static Map<Class<? extends ProfileStereotype>, ElementAnnotator> annotators = new HashMap<>();
	
	static {
		try {
			DefaultAnnotationFactory.addAnnotator(NamespaceAnnotator.class);
			DefaultAnnotationFactory.addAnnotator(TypeAnnotator.class);
			DefaultAnnotationFactory.addAnnotator(AspectAnnotator.class);
			DefaultAnnotationFactory.addAnnotator(AssociationAnnotator.class);
			DefaultAnnotationFactory.addAnnotator(PropertyAnnotator.class);
		} catch (Exception e) {
			
		}
	}
	
	public static void addAnnotator(Class<? extends ElementAnnotator<? extends ProfileStereotype>> clazz){
		try {
			ElementAnnotator<? extends ProfileStereotype> ea = clazz.newInstance();
			for (Class<? extends ProfileStereotype> cl: ea.getCoveredClasses())
				annotators.put(cl, ea);
				
		} catch (Exception e) {
			// do nothing
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public AnnotationInfo getAnnotation(ProfileStereotype element) {
		AnnotationInfo ai = null;
		if (annotators.containsKey(element.getClass()))
			ai = annotators.get(element.getClass()).getAnnotation(element);
		else for (Class<? extends ProfileStereotype> clazz: annotators.keySet())
			if (element.has(clazz)) {
				annotators.put(element.getClass(), annotators.get(clazz));
				ai = annotators.get(clazz).getAnnotation(element);
				break;
			}
		return (ai==null?(new AnnotationInfo()):ai);
	}

	@Override
	public AnnotationInfo getAnnotation(AbstractWrapper wrapper) {
		return getAnnotation(wrapper.getWrappedElement());
	}


}
