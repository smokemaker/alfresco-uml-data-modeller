package ru.neodoc.content.codegen.sdoc.generator.java.annotation.factory;

import java.util.HashMap;
import java.util.Map;

import ru.neodoc.content.codegen.sdoc.generator.java.annotation.AnnotationInfo;
import ru.neodoc.content.codegen.sdoc.generator.java.annotation.SdocAnnotationFactory;
import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;
import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;

public class DefaultAnnotationFactory implements SdocAnnotationFactory {

	@SuppressWarnings("rawtypes")
	protected static Map<Class<? extends BaseElement>, ElementAnnotator> annotators = new HashMap<>();
	
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
	
	public static void addAnnotator(Class<? extends ElementAnnotator<? extends BaseElement>> clazz){
		try {
			ElementAnnotator<? extends BaseElement> ea = clazz.newInstance();
			for (Class<? extends BaseElement> cl: ea.getCoveredClasses())
				annotators.put(cl, ea);
				
		} catch (Exception e) {
			// do nothing
		}
	}
	
	@Override
	public AnnotationInfo getAnnotation(BaseWrapper wrapper) {
		return getAnnotation(wrapper.getWrappedElement());
	}

	@SuppressWarnings("unchecked")
	@Override
	public AnnotationInfo getAnnotation(BaseElement element) {
		AnnotationInfo ai = null;
		if (annotators.containsKey(element.getClass()))
			ai = annotators.get(element.getClass()).getAnnotation(element);
		else for (Class<? extends BaseElement> clazz: annotators.keySet())
			if (clazz.isAssignableFrom(element.getClass())) {
				annotators.put(element.getClass(), annotators.get(clazz));
				ai = annotators.get(clazz).getAnnotation(element);
				break;
			}
		return (ai==null?(new AnnotationInfo()):ai);
	}

}
