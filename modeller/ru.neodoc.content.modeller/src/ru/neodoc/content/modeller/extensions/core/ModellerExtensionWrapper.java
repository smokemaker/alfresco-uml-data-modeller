package ru.neodoc.content.modeller.extensions.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

import ru.neodoc.eclipse.extensionpoints.IConfigurationWrapper;

public class ModellerExtensionWrapper implements IConfigurationWrapper<IModellerExtension> {

	@Override
	public List<IModellerExtension> create(IExtension extension, IConfigurationElement element) {
		List<IModellerExtension> result = new ArrayList<>();
		if (element==null)
			return Collections.emptyList();
		try {
			ModellerExtension modellerExtension = null;
			try {
				// in case "component" attribute is set
				modellerExtension = (ModellerExtension)element.createExecutableExtension("component"); 
			} catch (Exception e) {
				
			}
			if (modellerExtension == null)
				// default in case "component" attribute is NOT set
				modellerExtension = new DefaultModellerExtension();
			modellerExtension.load(element);
			result.add(modellerExtension);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
