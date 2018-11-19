package ru.neodoc.content.utils.uml.profile.stereotype;

import java.util.List;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.uml.profile.UMLProfile;

public interface StereotypedElement {
	
	@Override
	public boolean equals(Object obj);
	
	public boolean has(Class<? extends ProfileStereotype> clazz);
	
	public void remove(Class<? extends ProfileStereotype> clazz);
	public void removeAll(Class<? extends ProfileStereotype> clazz);
	
	public <T extends ProfileStereotype> T get(Class<T> clazz);
	public <T extends ProfileStereotype> T getOrCreate(Class<T> clazz);
	@Deprecated
	public <T> List<T> getAll(Class<? extends ProfileStereotype> clazz);
	public List<ProfileStereotype> getAll(UMLProfile profile);
	public List<ProfileStereotype> getAll(String profileName);
	
	public <T> T append(Class<? extends ProfileStereotype> clazz);
	
	public <T extends Element> T getElement();
	
	public List<UMLProfile> getAppliedProfiles();
	
	public <T> T getAttribute(String name);
	
	public List<StereotypedElement> getAllRequiredElements();
	public List<StereotypedElement> getRequiredElements(String profileName);
	public List<StereotypedElement> getRequiredElements(UMLProfile profile);
	
	public String getUniqueId();

}
