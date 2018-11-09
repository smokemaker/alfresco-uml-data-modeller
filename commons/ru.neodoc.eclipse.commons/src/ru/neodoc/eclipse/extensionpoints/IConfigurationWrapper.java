package ru.neodoc.eclipse.extensionpoints;

import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

public interface IConfigurationWrapper<T extends IRegisteredExtension> {
	public List<T> create(IExtension extension, IConfigurationElement element);
}
