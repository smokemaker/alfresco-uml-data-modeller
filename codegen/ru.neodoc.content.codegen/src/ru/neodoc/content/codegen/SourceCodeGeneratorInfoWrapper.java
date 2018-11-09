package ru.neodoc.content.codegen;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

import ru.neodoc.eclipse.extensionpoints.IConfigurationWrapper;

public class SourceCodeGeneratorInfoWrapper implements IConfigurationWrapper<SourceCodeGeneratorInfo>{

	@Override
	public SourceCodeGeneratorInfo create(IExtension extension, IConfigurationElement element) {
		return new SourceCodeGeneratorInfo(extension, element);
	}
	
}
