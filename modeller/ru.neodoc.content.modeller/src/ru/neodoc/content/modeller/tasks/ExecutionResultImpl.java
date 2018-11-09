package ru.neodoc.content.modeller.tasks;

import java.util.ArrayList;
import java.util.List;

public class ExecutionResultImpl implements ExecutionResult {

	protected boolean isOk = true;
	protected boolean isIgnorable = true;
	
	protected String message = "";
	protected String description = "";
	
	protected final List<Object> resultObjects = new ArrayList<>();
	protected final List<Object> errorObjects = new ArrayList<>();
	
	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	public void setIgnorable(boolean isIgnorable) {
		this.isIgnorable = isIgnorable;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean isOk() {
		return this.isOk;
	}

	@Override
	public boolean isIgnorable() {
		return this.isIgnorable;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public List<Object> getResultObjects() {
		return this.resultObjects;
	}

	@Override
	public List<Object> getErrorObjects() {
		return this.errorObjects;
	}

	public ExecutionResultImpl ok(boolean value) {
		setOk(value);
		return this;
	}
	
	public ExecutionResultImpl ignorable(boolean value) {
		setIgnorable(value);
		return this;
	}
	
	public ExecutionResultImpl message(String value) {
		setMessage(value);
		return this;
	}
	
	public ExecutionResultImpl description(String value) {
		setDescription(value);
		return this;
	}
	
	public ExecutionResultImpl errorObjects(List<?> list) {
		this.errorObjects.clear();
		this.errorObjects.addAll(list);
		return this;
	}
	
	public ExecutionResultImpl errorObject(Object object) {
		this.errorObjects.add(object);
		return this;
	}

	public ExecutionResultImpl resultObjects(List<?> list) {
		this.resultObjects.clear();
		this.resultObjects.addAll(list);
		return this;
	}
	
	public ExecutionResultImpl resultObject(Object object) {
		this.resultObjects.add(object);
		return this;
	}
}
