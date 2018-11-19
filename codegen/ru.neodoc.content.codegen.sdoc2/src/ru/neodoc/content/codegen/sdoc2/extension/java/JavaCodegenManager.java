package ru.neodoc.content.codegen.sdoc2.extension.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import ru.neodoc.content.codegen.CodegenPlugin;
import ru.neodoc.content.codegen.sdoc2.SdocCodegenPlugin;
import ru.neodoc.content.codegen.sdoc2.config.Configurable;
import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.extension.java.annotation.AnnotationFactoryInfo;

public class JavaCodegenManager implements Configurable {

	public static final String PROP_NAME = JavaCodegenManager.class.getName();

	protected Configuration configuration = null;
	
	public JavaCodegenManager(Configuration configuration) {
		super();
		setConfiguration(configuration);
		if (configuration!=null)
			configuration.setValue(PROP_NAME, this);
	}
	
	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}
	
	public List<AnnotationFactoryInfo> getAnnotationFactoryInfo(){
		List<AnnotationFactoryInfo> result = new ArrayList<>();
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint(SdocCodegenPlugin.EP_CODEGEN_ANNOTATION_FACTORY.ID);
		IExtension[] extensions = ep.getExtensions();
		for (int i=0; i<extensions.length; i++) {
			IExtension ext = extensions[i];
			IConfigurationElement[] ce = ext.getConfigurationElements();
			for (int j = 0; j < ce.length; j++) {
				try {
					result.add(new AnnotationFactoryInfo(ext, ce[j]));
				} catch (Exception e) {
					CodegenPlugin.getDefault().getLog().log(new Status(
							Status.ERROR, 
							CodegenPlugin.PLUGIN_ID, 
							e.getMessage()));
				}
			}
		}
		
		return result;
	}
}
