package ru.neodoc.content.modeller.extensions.common;

import ru.neodoc.content.modeller.common.Enablable;
import ru.neodoc.content.modeller.tasks.ExecutionContextAware;

public interface ExtensionManagedObject extends ExecutionContextAware, Enablable {

	@Override
	default boolean isEnabled() {
		return CommonModellerExtensions.extensionsEnabled(((ExecutionContextAware)this).getExecutionContext());
	}
	
}
