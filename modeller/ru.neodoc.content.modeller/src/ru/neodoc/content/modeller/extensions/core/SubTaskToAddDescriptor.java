package ru.neodoc.content.modeller.extensions.core;

import ru.neodoc.content.modeller.tasks.ExtendedSubTask;
import ru.neodoc.content.modeller.tasks.SubtaskDescriptor;

public class SubTaskToAddDescriptor {
	
	protected SubtaskDescriptor previousTask = null;
	protected SubtaskDescriptor nextTask = null;
	protected ExtendedSubTask taskToAdd = null;
	
	protected ModellerExtension ownerExtension = null;
	
	public SubtaskDescriptor getPreviousTask(){
		return this.previousTask; 
	}

	public SubtaskDescriptor getNextTask() {
		return nextTask;
	}
	
	public ExtendedSubTask getTaskToAdd() {
		return this.taskToAdd;
	}
	
	public SubTaskToAddDescriptor previous(SubtaskDescriptor task) {
		this.previousTask = task;
		return this;
		
	}

	public SubTaskToAddDescriptor next(SubtaskDescriptor task) {
		this.nextTask = task;
		return this;
		
	}
	
	public SubTaskToAddDescriptor toAdd(ExtendedSubTask task) {
		this.taskToAdd = task;
		return this;
		
	}
	
	public SubTaskToAddDescriptor extension(ModellerExtension ext) {
		this.ownerExtension = ext;
		return this;
	}
}
