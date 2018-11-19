package ru.neodoc.content.codegen.sdoc2.extension.java.annotation;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

import ru.neodoc.content.codegen.sdoc2.SdocCodegenPlugin;
import ru.neodoc.content.codegen.sdoc2.generator.annotation.SdocAnnotationFactory;

public class AnnotationFactoryInfo {

	protected String id;
	protected SdocAnnotationFactory component;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SdocAnnotationFactory getComponent() {
		return component;
	}

	public void setComponent(SdocAnnotationFactory component) {
		this.component = component;
	}

	public AnnotationFactoryInfo(IExtension extension, IConfigurationElement element){
		super();
		try {
			Object obj = element.createExecutableExtension(SdocCodegenPlugin.EP_CODEGEN_ANNOTATION_FACTORY.PROPERTIES.COMPONENT);
			if (SdocAnnotationFactory.class.isAssignableFrom(obj.getClass())){
				setComponent((SdocAnnotationFactory)obj);
			} else {
				throw new IllegalArgumentException();
			}
			
			String id = element.getAttribute(SdocCodegenPlugin.EP_CODEGEN_ANNOTATION_FACTORY.PROPERTIES.ID);
			setId(id);
		} catch (Exception e) {
			
		}
	}
	
}
