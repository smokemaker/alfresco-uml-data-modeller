package ru.neodoc.content.codegen.sdoc2.generator.writer;

import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

public interface LookupProvider {
	
	public String lookupTargetName(ProfileStereotype element);
	
}
