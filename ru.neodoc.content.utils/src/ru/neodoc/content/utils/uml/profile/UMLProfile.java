package ru.neodoc.content.utils.uml.profile;

import java.util.List;

import org.eclipse.emf.common.util.URI;

import ru.neodoc.content.utils.uml.profile.annotation.ALibrary;
import ru.neodoc.content.utils.uml.profile.annotation.AProfile;
import ru.neodoc.content.utils.uml.profile.descriptor.ProfileDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.StereotypeDescriptor;

public interface UMLProfile {

	public String getName();
	public AProfile getProfileAnnotation();
	public ALibrary getLibraryAnnotation();
	
	public ProfileDescriptor getDescriptor();
	
	public List<StereotypeDescriptor> getStereotypeDescriptors();
	public StereotypeDescriptor getStereotypeDescriptor(String stereptypeName);
}
