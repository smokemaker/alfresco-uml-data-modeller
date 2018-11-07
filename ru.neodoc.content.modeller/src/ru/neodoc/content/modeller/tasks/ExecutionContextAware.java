package ru.neodoc.content.modeller.tasks;

public interface ExecutionContextAware {
	
	public static void setContext(ExecutionContext executionContext, Object object) {
		if (object == null)
			return;
		if (object instanceof ExecutionContextAware)
			((ExecutionContextAware)object).setExecutionContext(executionContext);
	}
	
	public void setExecutionContext(ExecutionContext executionContext);
	
	default public ExecutionContext getExecutionContext() {
		return null;
	};
	
}
