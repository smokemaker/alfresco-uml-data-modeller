package ru.neodoc.content.modeller.tasks;

public class CommonTaskDescriptor {

	public int totalCount;
	public String name;

	protected ExecutionContext executionContext = ExecutionContext.create();

	public ExecutionContext getExecutionContext() {
		return executionContext;
	}

	public void setExecutionContext(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}
	
	
}
