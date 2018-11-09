package ru.neodoc.content.modeller.extensions.core;

import java.util.List;

import ru.neodoc.content.modeller.ContentModellerPlugin;
import ru.neodoc.content.modeller.tasks.ExecutionContext;
import ru.neodoc.content.modeller.tasks.Executor;
import ru.neodoc.eclipse.extensionpoints.ExtensionsRegistry;
import ru.neodoc.eclipse.extensionpoints.IRegisteredExtension;

public interface IModellerExtension extends IRegisteredExtension {

	public static final String EXTENSION_POINT_ID = ContentModellerPlugin.PLUGIN_ID + ".modellerExtension";
	public static final String EXECUTOR_EXTENSION_NAME = "executorExtension";
	
	public static List<IModellerExtension> getAll() {
		return ExtensionsRegistry.getExtensions(EXTENSION_POINT_ID, ModellerExtensionWrapper.class);
	}
	
	default public void initialize() {};
	
	public boolean isEnabled(ExecutionContext executionContext);
	
	public List<SubTaskToAddDescriptor> getTasksToAdd(Executor executor);
	
	public List<ExecutorExtension> getExecutorExtensions(Executor executor);
	
	//public void populateMainTask(Executor executor);
	
}
