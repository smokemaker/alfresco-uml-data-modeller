package ru.neodoc.content.codegen;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import ru.neodoc.eclipse.extensionpoints.ExtensionsRegistry;
import ru.neodoc.eclipse.extensionpoints.IConfigurationWrapper;

public class CodegenHelper {
	
	public static List<SourceCodeGeneratorInfo> getAvailableGenerators(){
/*		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint(CodegenPlugin.PLUGIN_ID + "." + CodegenPlugin.EP_GENERATORS);
		IExtension[] extensions = ep.getExtensions();
		ArrayList<SourceCodeGeneratorInfo> contributors = new ArrayList<>();
		for (int i = 0; i < extensions.length; i++) {
			SourceCodeGeneratorInfo gi;
			IExtension ext = extensions[i];
			IConfigurationElement[] ce = ext.getConfigurationElements();
			for (int j = 0; j < ce.length; j++) {
				try {
					gi = new SourceCodeGeneratorInfo(ext, ce[j]);
					if (gi.isValid()){
						contributors.add(gi);
					}
				} catch (Exception e) {
					CodegenPlugin.getDefault().getLog().log(new Status(
							Status.ERROR, 
							CodegenPlugin.PLUGIN_ID, 
							e.getMessage()));
				}
			}
		}
		return contributors;*/
		return ExtensionsRegistry.getExtensions(
				CodegenPlugin.PLUGIN_ID + "." + CodegenPlugin.EP_GENERATORS, SourceCodeGeneratorInfoWrapper.class);
	}
	
}
