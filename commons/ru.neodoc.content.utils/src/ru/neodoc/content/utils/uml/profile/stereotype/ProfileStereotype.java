package ru.neodoc.content.utils.uml.profile.stereotype;

import java.util.List;

import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.descriptor.ProfileDescriptor;

public interface ProfileStereotype extends StereotypedElement {
	
	@Deprecated
	public AbstractProfile getProfile();
	
	public ProfileDescriptor getProfileDescriptor();
	
	public List<StereotypedElement> getRequiredElements();
	public List<StereotypedElement> getRequiredElementsByProfile();
	
	public void setAttribute(String name, Object value);
}
