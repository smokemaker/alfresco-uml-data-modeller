package ru.neodoc.content.utils.uml.profile.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.uml.tools.databinding.AbstractStereotypeListener;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.utils.uml.profile.UMLProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplemented;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotype;
import ru.neodoc.content.utils.uml.profile.descriptor.ProfileDescriptor;
import ru.neodoc.content.utils.uml.profile.exception.StereotypeNotImplementedException;
import ru.neodoc.content.utils.uml.profile.registry.ProfileRegistry;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class CompositeMetaObject extends MetaObject {

	// protected Profile profile;
	
	protected static class ImplementationRegistry {
		
		protected Map<Class<? extends ProfileStereotype>, Implementation> implementations = new HashMap<>();
		
		protected Map<ImplementationMetaObject, List<Implementation>> implementors = new HashMap<>();
		
		protected class Implementation {
			public Class<? extends ProfileStereotype> stereotypeInteface;
			public List<ImplementationMetaObject> stereotypeImplementors = new ArrayList<>();
			
			public boolean isEmpty() {
				return this.stereotypeImplementors==null || this.stereotypeImplementors.isEmpty();
			}
			
			public int size() {
				return this.stereotypeImplementors==null?0:this.stereotypeImplementors.size();
			}
		}
		
		public static class ImplementorsFilter {
			public List<UMLProfile> profiles = new ArrayList<>();
			public boolean topOnly = false;
			
			public ImplementorsFilter() {
				
			}
			
			public ImplementorsFilter(boolean topOnly) {
				this();
				this.topOnly = topOnly;
			}
			
			public ImplementorsFilter(boolean topOnly, UMLProfile... profiles) {
				this(topOnly);
				fillProfiles(profiles);
			}
			
			public ImplementorsFilter(boolean topOnly, String... profileNames) {
				this(topOnly);
				fillProfiles(findProfiles(profileNames));
			}
			
			public ImplementorsFilter(UMLProfile... profiles) {
				this();
				fillProfiles(profiles);
			}
			
			public ImplementorsFilter(ProfileDescriptor... profiles) {
				this();
			}
			
			public ImplementorsFilter(String... profileNames) {
				this();
				fillProfiles(findProfiles(profileNames));
			}
			
			protected void fillProfiles(UMLProfile[] profiles) {
				for (int i = 0; i < profiles.length; i++) {
					if (profiles[i]!=null)
						this.profiles.add(profiles[i]);
				}
			}
			
			protected UMLProfile[] findProfiles(String[] profileNames) {
				Set<UMLProfile> set = new HashSet<>();
				for (int i = 0; i < profileNames.length; i++) {
					String string = profileNames[i];
					UMLProfile ap = ProfileRegistry.INSTANCE
							.getProfileDescriptor(string)
							.getUmlProfile();
					if (ap!=null)
						set.add(ap);
						
				}
				return (new ArrayList<>(set)).toArray(new UMLProfile[]{});
			}
		}
		
		public Iterator<ImplementationMetaObject> implementorsIterator(){
			return this.implementors.keySet().iterator();
		}
		
		public List<ImplementationMetaObject> getImplementors(){
			return getImplementors(new ImplementorsFilter());
		}
		
		public List<ImplementationMetaObject> getImplementors(ImplementorsFilter filter){
			List<ImplementationMetaObject> result = new ArrayList<>();
			Iterator<ImplementationMetaObject> iter = implementorsIterator();
			while (iter.hasNext()) {
				ImplementationMetaObject imo = iter.next();
				if (filter.topOnly && imo.isOwned())
					continue;
				if (!filter.profiles.isEmpty() && !filter.profiles.contains(imo.getProfile()))
					continue;
				result.add(imo);
			}
			return result;
		}
		
		public boolean isImplemented(Class<? extends ProfileStereotype> clazz) {
			return this.implementations.containsKey(clazz) && !this.implementations.get(clazz).isEmpty();
		}
		
		public boolean isUnique(Class<? extends ProfileStereotype> clazz) {
			return isImplemented(clazz) && (this.implementations.get(clazz).size()==1);
		}
		
		public void delete(Class<? extends ProfileStereotype> clazz) {
			Implementation implementation = this.implementations.get(clazz);
			if (implementation!=null && !implementation.isEmpty()) {
				for (ImplementationMetaObject mo: implementation.stereotypeImplementors) {
					mo.beforeRemove();
					if (this.implementors.containsKey(mo))
						for (Implementation imp: this.implementors.get(mo)) {
							// we DO NOT remove implementor from currently traversed list
							if (imp == implementation)
								continue;
							imp.stereotypeImplementors.remove(mo);
							if (imp.isEmpty())
								this.implementations.remove(imp.stereotypeInteface);
						}
				}
			}
			this.implementations.remove(clazz);
		}
		
		@SuppressWarnings("unchecked")
		public <T extends ProfileStereotype> T getFirstImplementation(Class<T> clazz) {
			Implementation imp = this.implementations.get(clazz);
			if (imp==null || imp.isEmpty())
				return null;
			for (ImplementationMetaObject mo: imp.stereotypeImplementors)
				if (mo!=null)
					return (T) mo;
			return null;
		}
		
		public ProfileStereotype add(ImplementationMetaObject metaObject) {
			Class<? extends ProfileStereotype> clazz = metaObject.getImplementedStereotype();
			if (clazz==null)
				return null;
			List<Class<? extends ProfileStereotype>> stereotypes = metaObject.getAllImplementedStereotypes();
			List<Implementation> imps = this.implementors.get(metaObject);
			if (imps==null)
				imps = new ArrayList<>();
			for (Class<? extends ProfileStereotype> stereotype: stereotypes) {
				Implementation impl = this.implementations.get(stereotype);
				if (impl==null) {
					impl = new Implementation();
					impl.stereotypeInteface = stereotype;
					this.implementations.put(stereotype, impl);
				}
				impl.stereotypeImplementors.add(metaObject);
				imps.add(impl);
			}
			this.implementors.put(metaObject, imps);
			return metaObject.get(clazz);
		}
	}
	
	protected class StereotypeListener extends AbstractStereotypeListener {
		
		protected CompositeMetaObject metaObject;
		
		public StereotypeListener(CompositeMetaObject owner) {
			super(owner.getElement());
			this.metaObject = owner;
		}
		
		@Override
		protected void handleAppliedStereotype(EObject appliedStereotype) {
			Stereotype s = getStereotypeByStereotypeApplication(appliedStereotype);
			if (s!=null)
				this.metaObject.processStereotypeApplied(s);
		}
		
		@Override
		protected void handleUnappliedStereotype(EObject unappliedStereotype) {
			ImplementationMetaObject imo = getImplementorByStereotypeApplication(unappliedStereotype);
			if (imo!=null)
				this.metaObject.processStereotypeUnapplied(imo.getStereotype());
		}
		
		protected Stereotype getStereotypeByStereotypeApplication(EObject stereotypeApplication) {
			if (stereotypeApplication == null)
				return null;
			for (Stereotype stereotype: this.metaObject.getElement().getAppliedStereotypes())
				if (stereotypeApplication.equals(this.metaObject.getElement().getStereotypeApplication(stereotype)))
					return stereotype;
			return null;
		}
		
		protected ImplementationMetaObject getImplementorByStereotypeApplication(EObject stereotypeApplication) {
			if (stereotypeApplication == null)
				return null;
			for (ImplementationMetaObject imo: this.metaObject.registry.getImplementors())
				if (stereotypeApplication.equals(imo.getStereotypeApplication()))
					return imo;
			return null;
		}
	}
	
	static public CompositeMetaObject getFor(Element element) {
		CompositeMetaObject result = MetaObjectCache.get(element);
		if (result == null) {
			result = new CompositeMetaObject(element);
			MetaObjectCache.put(result);
		}
		return result;
	}

	/**
	 * class fields
	 */

	protected ImplementationRegistry registry = new ImplementationRegistry();
	
	protected StereotypeListener stereotypeListener; 
	
	
	
	/**
	 * protected constructor
	 * @param element Wrapped element
	 */
	protected CompositeMetaObject(Element element) {
		this.element = element;
		//profile = AlfrescoProfile.getProfile(this.element);
		doInitialScan();
		this.stereotypeListener = new StereotypeListener(this);
	}

	protected void doInitialScan() {
		List<Stereotype> applied = this.element.getAppliedStereotypes();
		for (Stereotype st: applied) {
			processStereotypeApplied(st);
		}
	}
	
	protected void processStereotypeApplied(Stereotype stereotype) {
		Class<? extends ProfileStereotype> stereotypeClass =
				ProfileRegistry.INSTANCE.findStereotypeClass(stereotype);
		if ((stereotypeClass!=null) && (get(stereotypeClass)==null) ) {
			AImplemented impl = stereotypeClass.getAnnotation(AImplemented.class);
			if (impl!=null) {
				// implementation class is found
				Class<? extends ImplementationMetaObject> metaClass = impl.value();
				try {
					ImplementationMetaObject mo = metaClass.getConstructor(CompositeMetaObject.class).newInstance(this);
					if (mo!=null) {
						this.registry.add(mo);
					}
				} catch (Exception e) {
					// TODO implement
				}
			}
		}
	}

	protected void processStereotypeUnapplied(Stereotype stereotype) {
		Class<? extends ProfileStereotype> stereotypeClass =
				ProfileRegistry.INSTANCE.findStereotypeClass(stereotype);
		if ((stereotypeClass!=null) && (get(stereotypeClass)!=null) ) {
			try {
				this.registry.delete(stereotypeClass);
			} catch (Exception e) {
				
			}
		}
		
	}
	
	
	/*
	 * BEGIN
	 * IMPLEMENTATION of ru.neodoc.content.profile.ProfileStereotype
	 */
	
	
	/*
	 * (non-Javadoc)
	 * @see ru.neodoc.content.profile.ProfileStereotype#has(java.lang.Class)
	 */
	@Override
	public boolean has(Class<? extends ProfileStereotype> clazz) {
		return this.registry.isImplemented(clazz);
	}

	@Override
	public void remove(Class<? extends ProfileStereotype> clazz) {
		if (!has(clazz) || !isRemovable(clazz) || !this.registry.isUnique(clazz))
			return;
		this.registry.delete(clazz);
	}

	@Override
	public void removeAll(Class<? extends ProfileStereotype> clazz) {
		if (has(clazz) && isRemovable(clazz))
			this.registry.delete(clazz);
	}

	@Override
	public <T extends ProfileStereotype> T get(Class<T> clazz) {
		if (has(clazz))
			return this.registry.getFirstImplementation(clazz);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ProfileStereotype> T getOrCreate(Class<T> clazz) {
		T result = get(clazz);
		if (result == null && isApplicable(clazz)) {
			AImplemented impl = clazz.getAnnotation(AImplemented.class);
			try {
				if (impl==null)
					throw new StereotypeNotImplementedException((Class<ProfileStereotype>)clazz);
				// implementation class is found
				Class<? extends ImplementationMetaObject> metaClass = impl.value();
					ImplementationMetaObject mo = metaClass.getConstructor(CompositeMetaObject.class).newInstance(this);
					if (mo!=null) {
						result = (T)this.registry.add(mo);
						mo.apply();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return result;
	}

	@Override
	public <T> List<T> getAll(Class<? extends ProfileStereotype> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProfileStereotype> getAll(String profileName) {
		return getAll(ProfileRegistry.INSTANCE.getProfileDescriptor(profileName).getUmlProfile());
	}
	
	@Override
	public List<ProfileStereotype> getAll(UMLProfile profile) {
		List<ImplementationMetaObject> list = this.registry.getImplementors(new ImplementationRegistry.ImplementorsFilter(profile));
		List<ProfileStereotype> result = new ArrayList<>();
		for (ImplementationMetaObject imo: list)
			result.add(imo.cast());
		return result;
	}
	
/*	public List<ProfileStereotype> getAll(ProfileDescriptor profile) {
		
	}
*/	
	@Override
	public <T> T append(Class<? extends ProfileStereotype> clazz) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public <T> T getAttribute(String name) {
		T result = null;
		Iterator<ImplementationMetaObject> iterator = this.registry.implementorsIterator();
		while (result==null && iterator.hasNext()) {
			try {
				result = iterator.next().getAttribute(name);
			} catch (Exception e) {
				
			}
		}
		return result;
	}
	/*
	 * END
	 */

	/*
	 * BEGIN ROUTINES
	 */
	
	protected boolean isRemovable(Class<? extends ProfileStereotype> clazz) {
		if (clazz==null)
			return false;
		AStereotype aStereotype = clazz.getAnnotation(AStereotype.class);
		if (aStereotype==null)
			return false;
		if (aStereotype.isAbstract())
			return false;
		return true;
	}
	
	/*
	 * END ROUTINES
	 */
	
	/*
	 * Setters & getters
	 */
	
	@SuppressWarnings("unchecked")
	public Element getElement() {
		return this.element;
	}

	@Override
	public List<UMLProfile> getAppliedProfiles() {
		Set<UMLProfile> result = new HashSet<>();
		Iterator<ImplementationMetaObject> iter = this.registry.implementorsIterator();
		while (iter.hasNext())
			try {
				result.add(iter.next().getProfileDescriptor().getUmlProfile());
			} catch (Exception e) {
				// NOOP
			}
		return new ArrayList<>(result);
	}

	@Override
	public List<StereotypedElement> getAllRequiredElements() {
		List<StereotypedElement> result = new ArrayList<>();
		for (UMLProfile ap: getAppliedProfiles())
			result.addAll(getRequiredElements(ap));
		return result;
	}

	@Override
	public List<StereotypedElement> getRequiredElements(String profileName) {
		return getRequiredElements(ProfileRegistry.INSTANCE.getProfileDescriptor(profileName).getUmlProfile());
	}

	@Override
	public List<StereotypedElement> getRequiredElements(UMLProfile profile) {
		List<ImplementationMetaObject> list = this.registry.getImplementors(
				new ImplementationRegistry.ImplementorsFilter(true, profile));
		Set<StereotypedElement> set = new HashSet<>();
		
		for (ImplementationMetaObject imo: list) {
			set.addAll(imo.getRequiredElements());
		}
		
		set.remove(this);
		return new ArrayList<>(set);
	}
	
/*	public Profile getProfile() {
		return profile;
	}
*/	
}
