package ru.neodoc.content.codegen;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

import ru.neodoc.eclipse.extensionpoints.IConfigurationWrapper;

public class SourceCodeGeneratorInfoWrapper implements IConfigurationWrapper<SourceCodeGeneratorInfo>{

	@Override
	public List<SourceCodeGeneratorInfo> create(IExtension extension, IConfigurationElement element) {
		return Arrays.asList(new SourceCodeGeneratorInfo[] { new SourceCodeGeneratorInfo(extension, element)});
	}
	
}
