package ru.neodoc.content.modeller.tasks;

import java.util.List;

import ru.neodoc.content.modeller.extensions.core.ExecutorExtension;
import ru.neodoc.content.modeller.extensions.core.IModellerExtension;
import ru.neodoc.content.modeller.extensions.core.SubTaskToAddDescriptor;

public abstract class ExtendedExecutor implements Executor {

	protected ExtendedTaskDescriptor mainTask;
	
	public ExtendedExecutor() {
		super();
		initialize();
		this.mainTask = createMainTask();
		if (this.mainTask!=null) {
			populateMainTask();
			populateFromExtensions();
		}
	}

	protected void initialize() {
		
	}
	
	protected abstract ExtendedTaskDescriptor createMainTask();
	
	protected void populateMainTask() {
		
	};

	protected void populateFromExtensions() {
		for (IModellerExtension extension: IModellerExtension.getAll()) {
			extension.initialize();
			for (ExecutorExtension executorExtension: extension.getExecutorExtensions(this)) {
				List<SubTaskToAddDescriptor> tasksToAdd = executorExtension.getTasksToAdd(this);
				for (SubTaskToAddDescriptor taskToAdd: tasksToAdd) {
					ExtendedSubTaskWrapper wrapper = new ExtendedSubTaskWrapper(taskToAdd.getTaskToAdd(), extension);
					wrapper.setExecutionContext(this.mainTask.getExecutionContext());
					if (taskToAdd.getPreviousTask()!=null)
						if (this.mainTask.subtasks.contains(taskToAdd.getPreviousTask())) {
							this.mainTask.subtasks.add(this.mainTask.subtasks.indexOf(taskToAdd.getPreviousTask())+1, wrapper);
							continue;
						}
					if (taskToAdd.getNextTask()!=null)
						if (TaskDescriptor.FIRST_SUBTASK.equals(taskToAdd.getNextTask())) {
							this.mainTask.subtasks.add(0, wrapper);
							continue;
						} else 
							if (this.mainTask.subtasks.contains(taskToAdd.getNextTask())) {
								this.mainTask.subtasks.add(this.mainTask.subtasks.indexOf(taskToAdd.getNextTask()), wrapper);
								continue;
							}
					this.mainTask.subtasks.add(wrapper);
				}
			}
		}
	}
	
	@Override
	public String getExecutorId() {
		return this.getClass().getName();
	}
	
	
/* BEGIN :: interaction with caller */
	
	@Override
	public TaskDescriptor getRootTask() {
		return this.mainTask;
	}
	@Override
	public void prepare(TaskDescriptor task) {
		this.mainTask.prepare();
	}
	@Override
	public SubtaskDescriptor prepare(SubtaskDescriptor subtask) {
		if (subtask instanceof ExtendedSubTask) {
			((ExtendedSubTask)subtask).prepare();
		}
		return subtask;
	}
	@Override
	public ExecutionResult execute(SubtaskDescriptor subtask, ExecutionCallback callback) {
		if (subtask instanceof ExtendedSubTask) 
			return ((ExtendedSubTask)subtask).execute(callback);
		return ExecutionResult.RESULT_OK;
	}
	
/* END :: interaction with caller */

}
