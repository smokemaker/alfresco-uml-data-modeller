package ru.neodoc.content.modeller.extensions.core;

import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.modeller.tasks.Executor;
import ru.neodoc.content.modeller.tasks.ExtendedSubTask;
import ru.neodoc.content.modeller.tasks.SubtaskDescriptor;
import ru.neodoc.content.modeller.tasks.TaskDescriptor;

public abstract class ExecutorExtension {

	public static final String ANY_EXECUTORS_WILDCARD = "*";
	
	public static final class SubTasksHelper {
		
		protected Executor executor = null;
		protected ModellerExtension modellerExtension = null;
		
		protected TaskDescriptor rootTask = null;
		
		protected final List<SubTaskToAddDescriptor> subtaskDescriptors = new ArrayList<>();
		
		public SubTasksHelper(Executor executor, ModellerExtension modellerExtension) {
			super();
			this.executor = executor;
			this.modellerExtension = modellerExtension;
			this.rootTask = this.executor.getRootTask();
		}
		
		protected ExtendedSubTask findExtendedSubTask(Class<? extends SubtaskDescriptor> taskClass) {
			SubtaskDescriptor subtaskDescriptor = rootTask.findSubTask(taskClass);
			if (subtaskDescriptor==null)
				return null;
			if (subtaskDescriptor instanceof ExtendedSubTask)
				return (ExtendedSubTask)subtaskDescriptor;
			return null;
		}
		
		protected boolean internalInsertAfter(SubtaskDescriptor beforeTask, ExtendedSubTask addedTask) {
			SubTaskToAddDescriptor subtaskDescriptor = ((new SubTaskToAddDescriptor()))
					.extension(this.modellerExtension)
					.previous(beforeTask)
					.toAdd(addedTask);
			this.subtaskDescriptors.add(subtaskDescriptor);
			return true;
		}
		
		public boolean insertOnlyAfter(Class<? extends SubtaskDescriptor> taskClass, ExtendedSubTask extendedSubTask) {
			ExtendedSubTask task = findExtendedSubTask(taskClass);
			if (task==null)
				return false;
			return internalInsertAfter(task, extendedSubTask);
		}

		public boolean insertAfter(Class<? extends SubtaskDescriptor> taskClass, ExtendedSubTask extendedSubTask) {
			return internalInsertAfter(findExtendedSubTask(taskClass), extendedSubTask);
		}

		protected boolean internalInsertBefore(SubtaskDescriptor afterTask, ExtendedSubTask addedTask) {
			SubTaskToAddDescriptor subtaskDescriptor = ((new SubTaskToAddDescriptor()))
					.extension(this.modellerExtension)
					.next(afterTask)
					.toAdd(addedTask);
			this.subtaskDescriptors.add(subtaskDescriptor);
			return true;
		}

		public boolean insertOnlyBefore(Class<? extends SubtaskDescriptor> taskClass, ExtendedSubTask extendedSubTask) {
			ExtendedSubTask task = findExtendedSubTask(taskClass);
			if (task==null)
				return false;
			return internalInsertBefore(task, extendedSubTask);
		}

		public boolean insertBefore(Class<? extends SubtaskDescriptor> taskClass, ExtendedSubTask extendedSubTask) {
			return internalInsertBefore(findExtendedSubTask(taskClass), extendedSubTask);
		}
		
		public boolean insertAsFirst(ExtendedSubTask extendedSubTask) {
			return internalInsertBefore(TaskDescriptor.FIRST_SUBTASK, extendedSubTask);
		}
		
		public boolean toTheEnd(ExtendedSubTask extendedSubTask) {
			this.subtaskDescriptors.add(
						(new SubTaskToAddDescriptor())
						.extension(this.modellerExtension)
						.toAdd(extendedSubTask)
					);
			return true;
		}
		
		public List<SubTaskToAddDescriptor> getList(){
			return this.subtaskDescriptors;
		}
		
	}
	
	protected String executorId;
	protected ModellerExtension ownerExtension;
	
	public void initialize() {
		
	}
	
	public String getExecutorId() {
		return executorId;
	}

	public void setExecutorId(String executorId) {
		this.executorId = executorId;
	}
	
	public ModellerExtension getOwnerExtension() {
		return ownerExtension;
	}

	public void setOwnerExtension(ModellerExtension ownerExtension) {
		this.ownerExtension = ownerExtension;
	}

	
	
	public abstract List<SubTaskToAddDescriptor> getTasksToAdd(Executor executor);
	
}
