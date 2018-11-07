package ru.neodoc.content.modeller.tasks;

public class ExecutionResultByException extends ExecutionResultImpl {
	
	public ExecutionResultByException(Exception e) {
		super();
		ok(false);
		message(e.getMessage());
		errorObject(e);
	}
	
}
