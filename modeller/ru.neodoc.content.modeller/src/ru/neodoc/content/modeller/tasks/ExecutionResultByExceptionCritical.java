package ru.neodoc.content.modeller.tasks;

public class ExecutionResultByExceptionCritical extends ExecutionResultByException {

	public ExecutionResultByExceptionCritical(Exception e) {
		super(e);
		ignorable(false);
	}

}
