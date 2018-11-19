package ru.neodoc.content.codegen;

import java.util.List;

import ru.neodoc.eclipse.extensionpoints.ExtensionsRegistry;

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
