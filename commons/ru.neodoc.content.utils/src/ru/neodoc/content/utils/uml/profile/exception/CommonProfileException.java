package ru.neodoc.content.utils.uml.profile.exception;

import ru.neodoc.content.utils.uml.profile.AbstractProfile;

public class CommonProfileException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2990892202271697125L;

	protected AbstractProfile profile;
	
	public CommonProfileException(AbstractProfile profile) {
		this.profile = profile;
	}
	
}
