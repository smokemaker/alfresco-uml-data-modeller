package ru.neodoc.content.modeller.extensions.common;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.extensions.ModellingExtensionsProfile;
import ru.neodoc.content.modeller.tasks.ExecutionContext;

public interface CommonModellerExtensions {

	public static final String EXTENSIONS_PROPERTY_PREFIX = "extensions.common.";
	
	public static final String EXTENSIONS_OBJECT = EXTENSIONS_PROPERTY_PREFIX + "object";
	
	public static boolean extensionsEnabled(ExecutionContext executionContext) {
		Object object = executionContext.get(EXTENSIONS_OBJECT);
		if (object==null)
			object = executionContext.getContextObject();
		if (object==null)
			return false;
		if (!(object instanceof Element))
			return false;
		return ModellingExtensionsProfile._INSTANCE.isApplied((Element)object);
	}
	
	public static void setExtensionsObject(ExecutionContext executionContext, Element object) {
		executionContext.put(EXTENSIONS_OBJECT, object);
	}
	
	
	
}
