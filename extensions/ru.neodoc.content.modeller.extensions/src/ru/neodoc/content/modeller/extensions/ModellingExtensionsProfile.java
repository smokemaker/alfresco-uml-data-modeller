package ru.neodoc.content.modeller.extensions;

import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AProfile;

@AProfile(name="alfresco-modelling-extensions")
public class ModellingExtensionsProfile extends AbstractProfile {
	
	public static final AbstractProfile _INSTANCE = new ModellingExtensionsProfile();

	protected ModellingExtensionsProfile() {
		super();
	}
	
}
