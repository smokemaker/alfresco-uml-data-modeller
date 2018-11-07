package ru.neodoc.content.modeller.tasks;

public interface Executor {

	public String getExecutorId();
	
	public TaskDescriptor getRootTask();
	public void prepare(TaskDescriptor task);
	
	public SubtaskDescriptor prepare(SubtaskDescriptor subtask);
	public ExecutionResult execute(SubtaskDescriptor subtask, ExecutionCallback callback);
	
}
