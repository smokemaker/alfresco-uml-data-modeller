package ru.neodoc.content.modeller.tasks;

public abstract class ExtendedSubTask extends SubtaskDescriptor {
	
	public ExtendedSubTask() {
		super();
		// XML2UMLGenerationManager.this.mainTask.subtasks.add(this);
	}
	
	public void prepare() {
		
	}
	
	public ExtendedSubTask name(String value) {
		this.name = value;
		return this;
	}
	
	public ExtendedSubTask totalCount(int value) {
		this.totalCount = value;
		return this;
	}
	
	public ExtendedSubTask relativeCount(int value) {
		this.relativeCount = value;
		return this;
	}
	
	public abstract ExecutionResult execute(ExecutionCallback callback);
}