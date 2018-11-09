package ru.neodoc.content.utils.uml.profile.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ui.internal.views.markers.AllMarkersSeverityAndDescriptionConfigurationArea;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.utils.uml.profile.UMLProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotype;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotype.AApplication;
import ru.neodoc.content.utils.uml.profile.descriptor.AbstractDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.AbstractOwnedDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.ProfileDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.PropertyDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.StereotypeDescriptor;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

public class ProfileRegistryImpl implements ProfileRegistry {
	
	protected final Map<String, ProfileDescriptor> profilesByName = new HashMap<>();
	protected final Map<Class<?>, ProfileDescriptor> profilesByClass = new HashMap<>();
	
	protected final Map<Class<?>, ProfileDescriptor> profilesByStereotypeClass = new HashMap<>();
	protected final Map<Stereotype, ProfileDescriptor> profilesByStereotype = new HashMap<>();
	protected final Map<String, ProfileDescriptor> profilesByStereotypeName = new HashMap<>();
	
	protected final Map<String, Map<Class<? extends AbstractDescriptor<?, ?>>, AbstractDescriptor<?, ?>>> allDescriptors
			= new HashMap<>();
	protected final Map<Object, AbstractDescriptor<?, ?>> allDescriptorsByElement = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractDescriptor<?, ?>> T getDescriptor(Class<T> descriptorClass, String name) {
		if (!allDescriptors.containsKey(name))
			return null;
		Map<Class<? extends AbstractDescriptor<?, ?>>, AbstractDescriptor<?, ?>> map = 
				allDescriptors.get(name);
		if (map==null)
			return null;
		AbstractDescriptor<?, ?> ad = map.get(descriptorClass);
		try {
			return (T)ad;
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractDescriptor<E, ?>, E> T getDescriptor(Class<T> descriptorClass, E javaElement) {
		try {
			return (T)allDescriptorsByElement.get(javaElement);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public ProfileDescriptor registerProfile(UMLProfile umlProfile) {
		if (!profilesByName.containsKey(umlProfile.getName())) {
			ProfileDescriptor profileDescriptor = new ProfileDescriptor(umlProfile);
			profileDescriptor.init();
			profilesByName.put(profileDescriptor.getName(), profileDescriptor);
			profilesByClass.put(profileDescriptor.getProfileClass(), profileDescriptor);
			
			for (StereotypeDescriptor stereotypeDescriptor: profileDescriptor.getStereotypeDescriptors()) {
				Stereotype st = stereotypeDescriptor.getStereotype(innerResourceSet);
				profilesByStereotypeClass.put(stereotypeDescriptor.getStereotypeClass(), profileDescriptor);
				profilesByStereotypeClass.put(stereotypeDescriptor.getImplementationClass(), profileDescriptor);
				profilesByStereotype.put(st, profileDescriptor);
				profilesByStereotypeName.put(st.getQualifiedName(), profileDescriptor);
			}
			
		}
		return profilesByName.get(umlProfile.getName());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void registerDescriptor(AbstractDescriptor<?, ?> descriptor) {
		Map<Class<? extends AbstractDescriptor<?, ?>>, AbstractDescriptor<?, ?>> map = allDescriptors.get(descriptor.getName());
		if (map == null) {
			map = new HashMap<>();
			allDescriptors.put(descriptor.getName(), map);
		}
		map.put((Class<? extends AbstractDescriptor<?, ?>>) descriptor.getClass(), descriptor);
		allDescriptorsByElement.put(descriptor.getOriginElement(), descriptor);
	}
	
	/*
	 * Access to profile
	 */
	@Override
	public ProfileDescriptor getProfileDescriptor(Class<?> profileClass) {
		return profilesByClass.get(profileClass);
	}
	
	@Override
	public ProfileDescriptor getProfileDescriptor(String profileName) {
		return profilesByName.get(profileName);
	}
	
	@Override
	public ProfileDescriptor getProfileDescriptor(Profile umlProfile) {
		return getProfileDescriptor(umlProfile.getName());
	}
	
	@Override
	public ProfileDescriptor getProfileDescriptorByStereotype(Stereotype umlStereotype) {
		if (umlStereotype==null)
			return null;
		ProfileDescriptor result = profilesByStereotype.get(umlStereotype);
		if (result!=null)
			return result;
		return profilesByStereotypeName.get(umlStereotype.getQualifiedName());
	};
	
	@Override
	public ProfileDescriptor getProfileDescriptorByStereotype(Class<? extends ProfileStereotype> profileStereotypeClass) {
		return profilesByStereotypeClass.get(profileStereotypeClass);
	};

	
	/*
	 * Access to stereotype 
	 */
	
	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends ProfileStereotype> findStereotypeClass(Stereotype stereotype){
		StereotypeDescriptor stereotypeDescriptor = findStereotypeDescriptor(stereotype);
		if (stereotypeDescriptor==null)
			return null;
		return (Class<? extends ProfileStereotype>)stereotypeDescriptor.getOriginElement();
	};
	
	@Override
	public StereotypeDescriptor findStereotypeDescriptor(Stereotype stereotype) {
		ProfileDescriptor profileDescriptor = getProfileDescriptorByStereotype(stereotype);
		if (profileDescriptor==null)
			return null;
		StereotypeDescriptor stereotypeDescriptor = profileDescriptor.getStereotypeDescriptor(stereotype.getName());
		return stereotypeDescriptor;
	};
	
	@Override
	public StereotypeDescriptor findStereotypeDescriptor(Class<?> stereotypeClass) {
		ProfileDescriptor profileDescriptor = profilesByStereotypeClass.get(stereotypeClass);
		if (profileDescriptor==null)
			return null;
		StereotypeDescriptor stereotypeDescriptor = profileDescriptor.getStereotypeDescriptor(stereotypeClass);
		return stereotypeDescriptor;
		
	};

	
	/*
	 * Access to property
	 */
	@Override
	public PropertyDescriptor findPropertyDescriptor(String propertyName, StereotypeDescriptor stereotypeDescriptor) {
		Set<Class<?>> hierarchyClasses = getFullStereotypeHierarchy(stereotypeDescriptor);
		for (Class<?> clazz: hierarchyClasses) {
			StereotypeDescriptor sd = findStereotypeDescriptor(clazz);
			if (sd!=null) {
				PropertyDescriptor pd = sd.getPropertyDescriptor(propertyName);
				if (pd!=null)
					return pd;
			}
		}
		return null;
	};

	/*
	 * utils
	 */
	
	public void collectAApplications(java.lang.Class<?> clazz, List<AApplication> list) {
		if (!list.isEmpty())
			return;
		AStereotype astereotype = clazz.getAnnotation(AStereotype.class);
		if ((astereotype == null) || astereotype.applications().length==0) {
			List<java.lang.Class<?>> parents = new ArrayList<>();
			if (clazz.getSuperclass() != null)
				parents.add(clazz.getSuperclass());
			java.lang.Class<?>[] interfaces = clazz.getInterfaces();
			if (interfaces!=null)
				for (int i=0; i<interfaces.length; i++)
					parents.add(interfaces[i]);
			for (java.lang.Class<?> cl: parents)
				collectAApplications(cl, list);
		} else {
			for (int i=0; i<astereotype.applications().length; i++)
				list.add(astereotype.applications()[i]);
		}
	}
	
	
	public static Set<Class<?>> getFullStereotypeHierarchy(StereotypeDescriptor stereotypeDescriptor){
		Class<?> stereotypeClass = stereotypeDescriptor.getOwnerClass();
		Set<Class<?>> result = new HashSet<>();
		analyzeForHierarchy(stereotypeClass, result);
		return result;
	}
	
	protected static void analyzeForHierarchy(Class<?> clazz, final Set<Class<?>> resultSet) {
		if (resultSet.contains(clazz) || (clazz==null))
			return;
		if (clazz.getAnnotation(AStereotype.class)!=null)
			resultSet.add(clazz);
		analyzeForHierarchy(clazz.getSuperclass(), resultSet);
		for (Class<?> intrfc: clazz.getInterfaces())
			analyzeForHierarchy(intrfc, resultSet);
	}
	
	public Set<Class<?>> getFullStereotypeHierarchy(Stereotype stereotype){
		StereotypeDescriptor sd = findStereotypeDescriptor(stereotype);
		if (sd==null)
			return Collections.emptySet();
		return getFullStereotypeHierarchy(sd);
	}
	
	public Set<Class<?>> getFullStereotypeHierarchy(Class<?> stereotypeClass){
		StereotypeDescriptor sd = findStereotypeDescriptor(stereotypeClass);
		if (sd==null)
			return Collections.emptySet();
		return getFullStereotypeHierarchy(sd);
	}

}
