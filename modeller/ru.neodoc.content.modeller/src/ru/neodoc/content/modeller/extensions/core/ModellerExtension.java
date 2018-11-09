package ru.neodoc.content.modeller.extensions.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

import ru.neodoc.content.modeller.tasks.ExecutionContext;
import ru.neodoc.content.modeller.tasks.Executor;
import ru.neodoc.content.utils.common.MapOfList;

public class ModellerExtension implements IModellerExtension {

	protected MapOfList<String, ExecutorExtension> executorExtensions = new MapOfList<>();
	
	public void load(IConfigurationElement configurationElement) {
		IConfigurationElement[] executorExtensionConfigurations = configurationElement.getChildren(IModellerExtension.EXECUTOR_EXTENSION_NAME);
		for (int i = 0; i < executorExtensionConfigurations.length; i++) {
			IConfigurationElement iConfigurationElement = executorExtensionConfigurations[i];
			try {
				ExecutorExtension executorExtension = (ExecutorExtension)iConfigurationElement.createExecutableExtension("component");
				executorExtension.setOwnerExtension(this);
				executorExtension.setExecutorId(iConfigurationElement.getAttribute("executorId"));
				executorExtension.initialize();
				executorExtensions.add(executorExtension.getExecutorId(), executorExtension);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean isEnabled(ExecutionContext executionContext) {
		return false;
	}
	
	@Override
	public List<SubTaskToAddDescriptor> getTasksToAdd(Executor executor){
		List<SubTaskToAddDescriptor> result = new ArrayList<>();
		for (ExecutorExtension ext: executorExtensions.getOrEmpty(executor.getExecutorId()))
				result.addAll(ext.getTasksToAdd(executor));
		for (ExecutorExtension ext: executorExtensions.getOrEmpty(ExecutorExtension.ANY_EXECUTORS_WILDCARD))
			result.addAll(ext.getTasksToAdd(executor));
		return result;
	}

	@Override
	public List<ExecutorExtension> getExecutorExtensions(Executor executor) {
		List<ExecutorExtension> result = new ArrayList<>(); 
		result.addAll(executorExtensions.getOrEmpty("*"));
		result.addAll(executorExtensions.getOrEmpty(executor.getExecutorId()));
		return result;
	}

}
