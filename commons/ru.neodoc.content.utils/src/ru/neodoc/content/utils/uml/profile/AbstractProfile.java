package ru.neodoc.content.utils.uml.profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.papyrus.uml.extensionpoints.profile.IRegisteredProfile;
import org.eclipse.papyrus.uml.extensionpoints.profile.RegisteredProfile;
import org.eclipse.papyrus.uml.tools.utils.PackageUtil;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.utils.uml.profile.annotation.ALibrary;
import ru.neodoc.content.utils.uml.profile.annotation.AProfile;
import ru.neodoc.content.utils.uml.profile.descriptor.ProfileDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.StereotypeDescriptor;
import ru.neodoc.content.utils.uml.profile.library.ProfileLibrary;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.registry.ProfileRegistry;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public abstract class AbstractProfile implements UMLProfile {

/*	protected static Map<String, AbstractProfile> profileRegistry = new HashMap<>();
	protected static Map<Class<?>, AbstractProfile> profileClassRegistry = new HashMap<>();
	
	protected static Map<Class<?>, StereotypeDescriptor> sharedStereotypeDescriptorRegistry = new HashMap<>();
	
	protected static ResourceSet innerResourceSet = new ResourceSetImpl();
*/	
	public static class  StereotypeHelper<T extends ProfileStereotype, S extends Element> {

		protected Class<T> stereotypeClass = null;
		
		public T getFor(S element) {
			if (stereotypeClass==null)
				initStereotypeClass();
			if (stereotypeClass==null)
				return null;
			return (T)asType(element, stereotypeClass);
		}
		
		public boolean is(Element element) {
			if (stereotypeClass==null)
				initStereotypeClass();
			if (stereotypeClass==null)
				return false;
			return isType(element, stereotypeClass);
		}
		
		@SuppressWarnings("unchecked")
		public T findNearestFor(Element element) {
			Element current = element;
			while (current!=null)
				if (is(current))
					return getFor((S)current);
				else {
					EObject container = current.eContainer();
					if (container instanceof Element)
						current = (Element)container;
				}
			return null;
		}

		public T findNearestFor(StereotypedElement se) {
			if (se==null)
				return null;
			return findNearestFor((Element)se.getElement());
		}

		public List<S> getElementList(List<T> stereotypedList){
			if (stereotypedList==null)
				return Collections.emptyList();
			List<S> result = new ArrayList<>();
			for (T stereotypedElement: stereotypedList)
				if (stereotypedElement.getElement()!=null)
					result.add(stereotypedElement.getElement());
			return result;
		}

		public List<T> getStereotypedList(List<S> elementList){
			if (elementList==null)
				return Collections.emptyList();
			List<T> result = new ArrayList<>();
			for (S element: elementList) {
				T stereotyped = getFor(element);
				if (stereotyped!=null)
					result.add(stereotyped);
			}
			return result;
		}
		
		
		@SuppressWarnings("unchecked")
		protected void initStereotypeClass() {
			Class<?> current = this.getClass();
			while (stereotypeClass==null && current!=null)
				if (ProfileStereotype.class.isAssignableFrom(current))
					stereotypeClass = (Class<T>)current;
				else
					current = current.getDeclaringClass();
		}
		
	}
	
	public static class StereotypeFactory<P, R extends ProfileStereotype> {
		
		private Map<P, Class<? extends R>> classMap = new HashMap<>();
		private Map<String, P> stringMap = new HashMap<>();
		
		public StereotypeFactory(List<P> keys, List<Class<? extends R>> values) {
			super();
			if ((keys==null) || (values==null))
				return;
			int min = Math.min(keys.size(), values.size());
			for (int i=0; i<min; i++) {
				if (keys.get(i)==null)
					continue;
				classMap.put(keys.get(i), values.get(i));
				stringMap.put(keys.get(i).toString(), keys.get(i));
			}
		}
		
		public Class<? extends R> get(P key){
			return classMap.get(key);
		}
		
		public R get(StereotypedElement se, P key) {
			Class<? extends R> clazz = get(key);
			if (clazz!=null)
				return se.get(clazz);
			return null;
		}
		
		public R getOrCreate(StereotypedElement se, P key) {
			Class<? extends R> clazz = get(key);
			if (clazz!=null)
				return se.getOrCreate(clazz);
			return null;
		}
		
		public Class<? extends R> get(String key){
			if (!stringMap.containsKey(key))
				return null;
			return classMap.get(stringMap.get(key));
		}
		
		public R get(StereotypedElement se, String key) {
			Class<? extends R> clazz = get(key);
			if (clazz!=null)
				return se.get(clazz);
			return null;
		}
		
		public R getOrCreate(StereotypedElement se, String key) {
			Class<? extends R> clazz = get(key);
			if (clazz!=null)
				return se.getOrCreate(clazz);
			return null;
		}
	}

	
	/*
	 * STATIC
	 */
/*	
	public static AbstractProfile get(String name) {
		return profileRegistry.get(name);
	}

	public static AbstractProfile get(Class<?> clazz) {
		return profileClassRegistry.get(clazz);
	}

	public static AbstractProfile get(Profile profile) {
		return profile==null?null:profileRegistry.get(profile.getName());
	}
	
	public static AbstractProfile getProfile(Stereotype stereotype) {
		if (stereotype==null)
			return null;
		AbstractProfile profile = get(stereotype.getProfile().getName());
		return profile;
	}
	
	public static Class<? extends ProfileStereotype> findStereotypeClass(Stereotype stereotype){
		AbstractProfile profile = getProfile(stereotype);
		if (profile==null)
			return null;
		return profile.getStereotypeClass(stereotype);
	}

	public static StereotypeDescriptor findStereotypeDescriptor(Stereotype stereotype) {
		AbstractProfile profile = getProfile(stereotype);
		if (profile==null)
			return null;
		return profile.getStereotypeDescriptor(stereotype);
	}
	
	public static void addSharedStereotypeDescriptor(Class<?> stereotypeClass, StereotypeDescriptor stereotypeDescriptor) {
		sharedStereotypeDescriptorRegistry.put(stereotypeClass, stereotypeDescriptor);
		if (stereotypeDescriptor.getImplementationClass()!=null) {
			sharedStereotypeDescriptorRegistry.put(stereotypeDescriptor.getImplementationClass(), stereotypeDescriptor);
		}
	}
*/	
	public static <T extends ProfileStereotype> T asType(Element element, java.lang.Class<? extends T> clazz) {
		if (element==null)
			return null;
		CompositeMetaObject cmo = getFor(element);
		return cmo.getOrCreate(clazz);
	}

	public static boolean isType(Element element, Class<? extends ProfileStereotype> clazz) {
		if (element == null)
			return false;
		CompositeMetaObject cmo = getFor(element);
		return cmo.has(clazz);
	}
	
	public static StereotypedElement asUntyped(Element element) {
		return getFor(element);
	}
	
	protected static CompositeMetaObject getFor(Element element) {
		return CompositeMetaObject.getFor(element);
	}
	
	/*
	 * DYNAMIC
	 */
	
	protected final Map<String, java.lang.Class<? extends ProfileStereotype>> stereotypeRegistry = new HashMap<>();
	protected final Map<String, StereotypeDescriptor> stereotypeDescriptorRegistry = new HashMap<>();

	protected final Map<String, java.lang.Class<? extends ProfileLibrary>> libraryRegistry = new HashMap<>();
	protected final Map<Class<?>, java.lang.Class<? extends ProfileLibrary>> libraryClassesRegistry = new HashMap<>();
	
	protected AProfile profileAnnotation;
	protected ALibrary libraryAnnotation;
	protected String name;
	
	protected URI umlProfileUri;
	protected Profile umlProfile;
	
	protected AbstractProfile() {
		this.profileAnnotation = this.getClass().getAnnotation(AProfile.class);
		this.libraryAnnotation = this.getClass().getAnnotation(ALibrary.class);
		defineName();

		ProfileRegistry.INSTANCE.registerProfile(this);
		
	}

	protected StereotypeDescriptor findStereotypeDescriptor(Stereotype stereotype) {
		return ProfileRegistry.INSTANCE.findStereotypeDescriptor(stereotype);
	}
	
	protected StereotypeDescriptor findStereotypeDescriptor(Class<?> clazz) {
		return ProfileRegistry.INSTANCE.getDescriptor(StereotypeDescriptor.class, clazz);
	}

	public ProfileApplication apply(Model model) {
		ProfileApplication pa = getProfileApplication(model);
		if (pa==null) {
			model.applyProfile(getUMLProfile());
			pa = getProfileApplication(model);
		}
		return pa;
	}
	
	public void unapply(Model model) {
		ProfileApplication pa = getProfileApplication(model);
		if (pa!=null)
			model.unapplyProfile(pa.getAppliedProfile());
	}
	
	public boolean isApplied(Element element) {
		EObject current = element;
		while (current!=null) {
			if (current instanceof Model)
				return isApplied((Model)current);
			current = current.eContainer();
		}
		return false;
	}
	
	public boolean isApplied(Model model) {
		return (getProfileApplication(model)!=null);
	}
	
	public ProfileApplication getProfileApplication(Model model) {
		Profile p = getUMLProfile();
		for (ProfileApplication pa: model.getAllProfileApplications()) {
			if (pa.getAppliedProfile().getName().equals(p.getName()) && pa.getAppliedProfile().getURI().equals(p.getURI()))
				return pa;
		}
		
		return null;
	}
	
	public void checkRegistration() {
		if (ProfileRegistry.INSTANCE.getProfileDescriptor(this.getClass())==null){
			ProfileRegistry.INSTANCE.registerProfile(this);
		}
	}
	
	public Class<? extends ProfileStereotype> getStereotypeClass(Stereotype stereotype) {
		return getStereotypeClass(stereotype.getName());
	}
	
	public Class<? extends ProfileStereotype> getStereotypeClass(String name) {
		return stereotypeRegistry.get(name);
	}

	public StereotypeDescriptor getStereotypeDescriptor(Stereotype stereotype) {
		return getStereotypeDescriptor(stereotype.getName());
	}
	
	public StereotypeDescriptor getStereotypeDescriptor(String name) {
		return stereotypeDescriptorRegistry.get(name);
	}
	
	public String getName(){
		return this.name;
	}
	
	public URI getUMLProfileUri() {
		if (this.umlProfileUri==null) {
			synchronized (this.umlProfileUri) {
				IRegisteredProfile registeredProfile = RegisteredProfile.getRegisteredProfile(this.getName());
				if (registeredProfile == null)
					return null;
				
				String path = registeredProfile.getPath();
				if (path == null)
					return null;
				if (path.trim().length()==0)
					return null;
				
				this.umlProfileUri = URI.createURI(path);
			}
		}
		return this.umlProfileUri;
	}
	
	public Profile getUMLProfile() {
		return ProfileRegistry.INSTANCE.getProfileDescriptor(this.getClass()).getProfile();
	}

	public Profile getUMLProfile(ResourceSet resourceSet) {
		Profile result = (Profile)PackageUtil.loadPackage(getUMLProfileUri(), resourceSet);
		return result;		
	}
	
	@Override
	public AProfile getProfileAnnotation() {
		return this.profileAnnotation;
	}

	@Override
	public ALibrary getLibraryAnnotation() {
		return this.libraryAnnotation;
	}

	@Override
	public ProfileDescriptor getDescriptor() {
		return ProfileRegistry.INSTANCE.getProfileDescriptor(this.getClass());
	}
	
	@Override
	public List<StereotypeDescriptor> getStereotypeDescriptors() {
		return null;
	}	
	
	public boolean isLibrary() {
		return this.libraryAnnotation!=null;
	}
	
	protected void defineName() {
		this.name = "<UNDEFINED>";
		if (this.profileAnnotation!=null)
			this.name = this.profileAnnotation.name();
	}

}
