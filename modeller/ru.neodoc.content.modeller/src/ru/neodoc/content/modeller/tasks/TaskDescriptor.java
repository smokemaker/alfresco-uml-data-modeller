package ru.neodoc.content.modeller.tasks;

import java.util.List;

public class TaskDescriptor extends CommonTaskDescriptor {

	public static final SubtaskDescriptor FIRST_SUBTASK = new SubtaskDescriptor();
	public static final SubtaskDescriptor LAST_SUBTASK = new SubtaskDescriptor();

	public List<SubtaskDescriptor> subtasks;
	
	public <T extends SubtaskDescriptor> T  findSubTask(Class<T> clazz){
		for (SubtaskDescriptor subTask: subtasks)
			if (clazz.isAssignableFrom(subTask.getClass()))
				return clazz.cast(subTask); 
		return null;
	}
	
}
