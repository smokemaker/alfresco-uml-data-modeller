package ru.neodoc.content.utils.uml.profile.exception;

import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

public class StereotypeNotImplementedException extends CommonStereotypeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5728883979264880342L;

	public StereotypeNotImplementedException(Class<? extends ProfileStereotype> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMessage() {
		return "Stereotype not implemented: [" + getStereotypeInfo() + "]";
	}
	
}
