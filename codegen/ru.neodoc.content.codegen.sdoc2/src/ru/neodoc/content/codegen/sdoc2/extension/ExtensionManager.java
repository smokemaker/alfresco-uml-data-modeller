package ru.neodoc.content.codegen.sdoc2.extension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import ru.neodoc.content.codegen.sdoc2.SdocCodegenPlugin;
import ru.neodoc.eclipse.extensionpoints.ExtensionsRegistry;

public class ExtensionManager {

	private static boolean isLoaded = false;

	private static List<SdocCodegenExtension> extensions = new LinkedList<>();
	
	public static boolean isLoaded() {
		return isLoaded;
	}
	
	public static boolean load() {
		if (!isLoaded) {
			reload();
		}
		return isLoaded();
	}
	
	public static boolean reload() {
		clear();
		Map<String, SdocCodegenExtensionInfo> mapInfo = readExtensions();
		storeExtensions(mapInfo);
		isLoaded = true;
		return isLoaded();
	}
	
	private static void clear() {
		extensions.clear();
	}
	
	private static Map<String, SdocCodegenExtensionInfo> readExtensions(){
		Map<String, SdocCodegenExtensionInfo> result = new HashMap<>();
/*		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint(SdocCodegenPlugin.EP_CODEGEN_EXTENSION.ID);
		IExtension[] extensions = ep.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			SdocCodegenExtensionInfo ei;
			IExtension ext = extensions[i];
			IConfigurationElement[] ce = ext.getConfigurationElements();
			for (int j = 0; j < ce.length; j++) {
				try {
					ei = new SdocCodegenExtensionInfo(ext, ce[j]);
					if (ei.isValid()){
						result.put(ei.getId(),ei);
					}
				} catch (Exception e) {
					SdocCodegenPlugin.getDefault().getLog().log(new Status(
							Status.ERROR, 
							SdocCodegenPlugin.PLUGIN_ID, 
							e.getMessage()));
				}
			}
		}*/
		for (SdocCodegenExtensionInfo ei: ExtensionsRegistry.getExtensions(SdocCodegenPlugin.EP_CODEGEN_EXTENSION.ID, SdocCodegenExtensionWrapper.class))
			result.put(ei.getId(), ei);
		return result;
	}
	
	private static void storeExtensions(Map<String, SdocCodegenExtensionInfo> infoMap) {
		List<SdocCodegenExtensionInfo> first = new ArrayList<>();
		List<SdocCodegenExtensionInfo> firstConditional = new ArrayList<>();
		List<SdocCodegenExtensionInfo> middle = new ArrayList<>();
		List<SdocCodegenExtensionInfo> lastConditional = new ArrayList<>();
		List<SdocCodegenExtensionInfo> last = new ArrayList<>();
		
		List<List<SdocCodegenExtensionInfo>> all = new ArrayList<>();
		all.add(first);
		all.add(firstConditional);
		all.add(middle);
		all.add(lastConditional);
		all.add(last);
		
		for (SdocCodegenExtensionInfo ei: infoMap.values())
			if (ei.isUseFirst())
				if (ei.getUseBefore().isEmpty() && ei.getUseAfter().isEmpty())
					first.add(ei);
				else
					firstConditional.add(ei);
			else if (ei.isUseLast()) 
				if (ei.getUseBefore().isEmpty() && ei.getUseAfter().isEmpty())
					last.add(ei);
				else
					lastConditional.add(ei);
			else middle.add(ei);
		
		firstConditional = sortGroup(firstConditional, infoMap);
		middle = sortGroup(middle, infoMap);
		lastConditional = sortGroup(lastConditional, infoMap);
		
		for (List<SdocCodegenExtensionInfo> list: all)
			for (SdocCodegenExtensionInfo ei: list)
				extensions.add(ei.getCodegenExtension());
	}
	
	private static List<SdocCodegenExtensionInfo> sortGroup(List<SdocCodegenExtensionInfo> group, final Map<String, SdocCodegenExtensionInfo> registry) {
		List<SdocCodegenExtensionInfo> result = new ArrayList<>();
		final List<SdocCodegenExtensionInfo> localGroup = group;
		
		Predicate<String> predicate = new Predicate<String>() {

			@Override
			public boolean test(String t) {
				return !localGroup.contains(registry.get(t));
			}
			
		};
		
		// remove outer links
		for (SdocCodegenExtensionInfo ei: group) {
			ei.getUseAfter().removeIf(predicate);
			ei.getUseBefore().removeIf(predicate);
		}
		
		// fill all the links
		for (SdocCodegenExtensionInfo ei: group) {
			for (String s: ei.getUseBefore()) {
				SdocCodegenExtensionInfo eiNext = registry.get(s);
				// prevent cycles
				if (eiNext.getUseBefore().contains(s))
					continue;
				
				eiNext.getUseAfter().add(s);
				eiNext.getUseAfter().addAll(ei.getUseAfter());
			}

			for (String s: ei.getUseAfter()) {
				SdocCodegenExtensionInfo eiPrev = registry.get(s);
				// prevent cycles
				if (eiPrev.getUseAfter().contains(s))
					continue;
				
				eiPrev.getUseBefore().add(s);
				eiPrev.getUseBefore().addAll(ei.getUseBefore());
			}
		}
		
		// sort
		result.addAll(localGroup); 
		result.sort(new Comparator<SdocCodegenExtensionInfo>() {

			@Override
			public int compare(SdocCodegenExtensionInfo o1, SdocCodegenExtensionInfo o2) {
				if (o1.getUseAfter().contains(o2.getId()))
					return 1;
				if (o1.getUseBefore().contains(o2.getId()))
					return -1;
				return 0;
			}
		});
		
		return result;
	}
	
	public static List<SdocCodegenExtension> getExtensions() {
		load();
		return extensions;
	}
}
