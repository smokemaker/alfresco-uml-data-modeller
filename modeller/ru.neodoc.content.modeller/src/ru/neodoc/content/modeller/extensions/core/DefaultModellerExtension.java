package ru.neodoc.content.modeller.extensions.core;

import ru.neodoc.content.modeller.tasks.ExecutionContext;

public class DefaultModellerExtension extends ModellerExtension {

	@Override
	public boolean isEnabled(ExecutionContext executionContext) {
		return true;
	}
	
}
