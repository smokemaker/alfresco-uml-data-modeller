package ru.neodoc.eclipse.extensionpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

public class ExtensionsRegistry {
	
	private static final Map<String, List<IExtension>> extensions = new HashMap<>();
	private static final Map<String, List<IConfigurationElement>> configurationElements = new HashMap<>();
	
	private static void load(String extensionPointId) {
		List<IExtension> listExt = new ArrayList<>();
		List<IConfigurationElement> list = new ArrayList<>();
		IExtension[] exts = Platform.getExtensionRegistry().getExtensionPoint(extensionPointId).getExtensions();
		for (int i = 0; i < exts.length; i++) {
			listExt.add(exts[i]);
			IConfigurationElement[] elements = exts[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				list.add(elements[j]);
			}
		}
		extensions.put(extensionPointId, listExt);
		configurationElements.put(extensionPointId, list);
	}
	
	public static List<IExtension> getExtensions(String extensionPointId){
		if (!extensions.containsKey(extensionPointId))
			load(extensionPointId);
		return extensions.get(extensionPointId);
	}
	
	public static List<IConfigurationElement> getConfigurationElements(String extensionPointId){
		if (!configurationElements.containsKey(extensionPointId))
			load(extensionPointId);
		return configurationElements.get(extensionPointId);
	}
	
	public static <T extends IRegisteredExtension> List<T> getExtensions(String extensionPointId, Class<? extends IConfigurationWrapper<T>> wrapperClass){
		List<T> list = new ArrayList<>();
		if (wrapperClass!=null)
			try {
				IConfigurationWrapper<T> instance = wrapperClass.newInstance();
				for (IExtension ie: getExtensions(extensionPointId)) {
					IConfigurationElement[] elements = ie.getConfigurationElements(); 
					for (int i=0; i<elements.length; i++) {
						IConfigurationElement ic = elements[i];
						list.addAll(instance.create(ie, ic));
					}
					
				}
			} catch (Exception e) {
				
			}
		return list;
	}
	
}
