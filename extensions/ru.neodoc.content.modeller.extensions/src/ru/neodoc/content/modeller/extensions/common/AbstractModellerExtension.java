package ru.neodoc.content.modeller.extensions.common;

import ru.neodoc.content.modeller.extensions.core.DefaultModellerExtension;
import ru.neodoc.content.modeller.tasks.ExecutionContext;

public abstract class AbstractModellerExtension extends DefaultModellerExtension {

	@Override
	public boolean isEnabled(ExecutionContext executionContext) {
		return CommonModellerExtensions.extensionsEnabled(executionContext);
	}
	
}
