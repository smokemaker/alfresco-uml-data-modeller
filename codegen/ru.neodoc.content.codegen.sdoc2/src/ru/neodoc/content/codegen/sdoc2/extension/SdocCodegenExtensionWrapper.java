package ru.neodoc.content.codegen.sdoc2.extension;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

import ru.neodoc.eclipse.extensionpoints.IConfigurationWrapper;

public class SdocCodegenExtensionWrapper implements IConfigurationWrapper<SdocCodegenExtensionInfo> {

	@Override
	public List<SdocCodegenExtensionInfo> create(IExtension extension, IConfigurationElement element) {
		return Arrays.asList(new SdocCodegenExtensionInfo[] { new SdocCodegenExtensionInfo(extension, element)});
	}

}
