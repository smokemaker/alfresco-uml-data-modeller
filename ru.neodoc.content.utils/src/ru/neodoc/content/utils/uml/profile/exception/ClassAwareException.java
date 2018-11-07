package ru.neodoc.content.utils.uml.profile.exception;

public abstract class ClassAwareException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2043137538375437L;
	
	protected Class<?> exceptedClass;
	
	public ClassAwareException(Class<?> clazz) {
		this.exceptedClass = clazz;
	}
}
