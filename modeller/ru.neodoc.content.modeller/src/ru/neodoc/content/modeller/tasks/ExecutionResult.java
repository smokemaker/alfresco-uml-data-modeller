package ru.neodoc.content.modeller.tasks;

import java.util.Collections;
import java.util.List;

public interface ExecutionResult {

	static final ExecutionResult RESULT_OK = new ExecutionResult() {

		@Override
		public boolean isOk() {
			return true;
		}

		@Override
		public boolean isIgnorable() {
			return false;
		}

		@Override
		public String getMessage() {
			return "";
		}

		@Override
		public String getDescription() {
			return "";
		}

		@Override
		public List<Object> getResultObjects() {
			return Collections.<Object>emptyList();
		}

		@Override
		public List<Object> getErrorObjects() {
			return Collections.<Object>emptyList();
		}
		
	};
	
	boolean isOk();
	
	boolean isIgnorable();
	String getMessage();
	String getDescription();
	
	List<Object> getResultObjects();
	List<Object> getErrorObjects();
	
}
