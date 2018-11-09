package ru.neodoc.content.codegen.sdoc2.extension;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

import ru.neodoc.eclipse.extensionpoints.IConfigurationWrapper;

public class SdocCodegenExtensionWrapper implements IConfigurationWrapper<SdocCodegenExtensionInfo> {

	@Override
	public SdocCodegenExtensionInfo create(IExtension extension, IConfigurationElement element) {
		return new SdocCodegenExtensionInfo(extension, element);
	}

}
