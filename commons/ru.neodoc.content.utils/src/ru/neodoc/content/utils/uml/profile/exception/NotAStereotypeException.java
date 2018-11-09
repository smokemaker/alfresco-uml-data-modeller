package ru.neodoc.content.utils.uml.profile.exception;

public class NotAStereotypeException extends ClassAwareException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3127903331769319060L;

	public NotAStereotypeException(Class<?> clazz) {
		super(clazz);
	}

}
