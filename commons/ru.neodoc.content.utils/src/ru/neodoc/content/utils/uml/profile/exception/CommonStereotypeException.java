package ru.neodoc.content.utils.uml.profile.exception;

import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

public class CommonStereotypeException extends ClassAwareException {

	protected Class<? extends ProfileStereotype> stereotypeClass;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9163512086992851064L;

	public CommonStereotypeException(Class<? extends ProfileStereotype> clazz) {
		super(clazz);
		this.stereotypeClass = clazz;
	}
	
	protected String getStereotypeInfo() {
		return stereotypeClass.getName();
	}
}
