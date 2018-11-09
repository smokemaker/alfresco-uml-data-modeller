package ru.neodoc.content.modeller.tasks;

import java.util.ArrayList;

public class ExtendedTaskDescriptor extends TaskDescriptor {
	
	public ExtendedTaskDescriptor() {
		super();
		this.subtasks = new ArrayList<>();
	}

	public void prepare() {
		this.totalCount = 0;
		for (SubtaskDescriptor sd: this.subtasks)
			totalCount += sd.relativeCount;
	}
	
	public ExtendedTaskDescriptor add(ExtendedSubTask subTask) {
		this.subtasks.add(subTask);
		subTask.setExecutionContext(this.executionContext);
		return this;
	}
	
	
}
