package ru.neodoc.content.modeller.tasks;

import ru.neodoc.content.modeller.extensions.core.IModellerExtension;

public class ExtendedSubTaskWrapper extends ExtendedSubTask {

	protected ExtendedSubTask wrappedTask = null;
	
	protected IModellerExtension modellerExtension = null;
	
	public ExtendedSubTaskWrapper(ExtendedSubTask wrappedTask, IModellerExtension extension) {
		super();
		this.wrappedTask = wrappedTask;
		this.modellerExtension = extension;
	}
	
	@Override
	public void prepare() {
		super.prepare();
		if (modellerExtension.isEnabled(this.executionContext))
			wrappedTask.prepare();
	}
	
	@Override
	public ExecutionResult execute(ExecutionCallback callback) {
		if (modellerExtension.isEnabled(this.executionContext)) {
			ExecutionResult result = wrappedTask.execute(callback); 
			return result==null?ExecutionResult.RESULT_OK:result;
		}
		// just skip execution
		return ExecutionResult.RESULT_OK;
	}

	@Override
	public void setExecutionContext(ExecutionContext executionContext) {
		super.setExecutionContext(executionContext);
		wrappedTask.setExecutionContext(getExecutionContext());
	}
}
