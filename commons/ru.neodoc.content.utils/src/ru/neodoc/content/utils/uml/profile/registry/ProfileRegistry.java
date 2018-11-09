package ru.neodoc.content.utils.uml.profile.registry;

import java.util.List;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.utils.uml.profile.UMLProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotype.AApplication;
import ru.neodoc.content.utils.uml.profile.descriptor.AbstractDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.ProfileDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.PropertyDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.StereotypeDescriptor;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

public interface ProfileRegistry {

	public static final ResourceSet innerResourceSet = new ResourceSetImpl();
	
	public static final ProfileRegistry INSTANCE = new ProfileRegistryImpl(); 
	
	public <T extends AbstractDescriptor<?, ?>> T getDescriptor(Class<T> descriptorClass, String name);
	public <T extends AbstractDescriptor<E, ?>, E> T getDescriptor(Class<T> descriptorClass, E javaElement);
	
	public ProfileDescriptor registerProfile(UMLProfile umlProfile);
	public void registerDescriptor(AbstractDescriptor<?, ?> descriptor);
	
	// profile access
	public ProfileDescriptor getProfileDescriptor(String profileName);
	public ProfileDescriptor getProfileDescriptor(Class<?> profileClass);
	public ProfileDescriptor getProfileDescriptor(Profile umlProfile);
	public ProfileDescriptor getProfileDescriptorByStereotype(Stereotype umlStereotype);
	public ProfileDescriptor getProfileDescriptorByStereotype(Class<? extends ProfileStereotype> profileStereotypeClass);
	
	// stereotype access
	public Class<? extends ProfileStereotype> findStereotypeClass(Stereotype stereotype);
	public StereotypeDescriptor findStereotypeDescriptor(Stereotype stereotype);
	public StereotypeDescriptor findStereotypeDescriptor(Class<?> stereotypeClass);
	
	// property access
	public PropertyDescriptor findPropertyDescriptor(String propertyName, StereotypeDescriptor stereotypeDescriptor);
	
	// utils
	public void collectAApplications(java.lang.Class<?> clazz, List<AApplication> list);
}
