package ru.neodoc.content.modeller.extensionpoints;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import ru.neodoc.content.profile.alfresco.ui.ExtensionPoints;

public class RegisteredModelRegistry {

	private static IRegisteredModel[] registeredModels;
	
	public static IRegisteredModel[] getRegisteredModels() {
		//if (registeredModels==null) {
			registeredModels = new IRegisteredModel[]{};
			
			List<IRegisteredModel> list = new ArrayList<>();
			IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(ExtensionPoints.REGISTERED_MODEL.id());
			for (int i = 0; i < elements.length; i++) {
				IConfigurationElement iConfigurationElement = elements[i];
				RegisteredModel registeredModel = new RegisteredModel(iConfigurationElement);
				if (registeredModel.isValid)
					list.add(registeredModel);
			}
			
			registeredModels = list.toArray(registeredModels);
		//}
		return registeredModels;
	}
	
}
