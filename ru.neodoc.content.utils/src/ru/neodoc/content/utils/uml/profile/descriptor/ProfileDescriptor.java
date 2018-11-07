package ru.neodoc.content.utils.uml.profile.descriptor;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.papyrus.uml.extensionpoints.profile.IRegisteredProfile;
import org.eclipse.papyrus.uml.extensionpoints.profile.RegisteredProfile;
import org.eclipse.papyrus.uml.tools.utils.PackageUtil;
import org.eclipse.uml2.uml.Profile;

import ru.neodoc.content.utils.uml.profile.UMLProfile;
import ru.neodoc.content.utils.uml.profile.annotation.ALibrary;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotype;
import ru.neodoc.content.utils.uml.profile.descriptor.scanner.ClassInClassScanner;
import ru.neodoc.content.utils.uml.profile.registry.ProfileRegistry;

public class ProfileDescriptor extends AbstractDescriptor<Class, Profile> {
	
	protected UMLProfile umlProfile;
	protected Class<?> profileClass;
	protected URI umlProfileURI;
	
/*	protected Map<String, StereotypeDescriptor> stereotypeDescriptorsByName = new HashMap<>();
	protected Map<String, StereotypeDescriptor> stereotypeDescriptorsByClass = new HashMap<>();
	
	protected Map<String, LibraryDescriptor> libraryDescriptors = new HashMap<>();
*/	
	
	
	protected ProfileDescriptor(Class<?> origin) {
		super(origin);
	}

	public ProfileDescriptor(UMLProfile umlProfile) {
		this(umlProfile.getClass());
		this.umlProfile = umlProfile;
	}

	/*
	 * parent methods
	 */
	
	@Override
	protected void initInnerStorages() {
		super.initInnerStorages();
		innerStorages.put(StereotypeDescriptor.class, new InnerStorage<StereotypeDescriptor, Class>());
		innerStorages.put(LibraryDescriptor.class, new InnerStorage<LibraryDescriptor, Class>());
	}
	
	@Override
	public void init() {
		super.init();
		loadProfileURI();
	}

	@Override
	protected void doScan(Class javaElement) {
		ClassInClassScanner scanner = new ClassInClassScanner();
		scanner.add(AStereotype.class, StereotypeDescriptor.class);
		scanner.add(ALibrary.class, LibraryDescriptor.class);
		scanner.scan(this, javaElement);
	}
	
	@Override
	public boolean isValid() {
		return true;
	}
	
	/*
	 * specific methods
	 */
	
	protected synchronized void loadProfileURI() {
		if (this.umlProfileURI!=null)
			return;
		IRegisteredProfile registeredProfile = RegisteredProfile.getRegisteredProfile(umlProfile.getName());
		if (registeredProfile == null)
			return;
		
		String path = registeredProfile.getPath();
		if (path == null)
			return;
		if (path.trim().length()==0)
			return;
		
		this.umlProfileURI = URI.createURI(path);			
	}
	
	public URI getUmlProfileURI() {
		loadProfileURI();
		return umlProfileURI;
	}

	public String getName() {
		return this.umlProfile.getName();
	}
	
/*	protected void scan(java.lang.Class<?> clazz) {
		check(clazz);
		java.lang.Class<?>[] declared = clazz.getDeclaredClasses();
		for (int i = 0; i < declared.length; i++) {
			java.lang.Class<?> class1 = declared[i];
			scan(class1);
		}
	}

	protected void check(java.lang.Class<?> clazz) {
		checkForStereotype(clazz);
		checkForLibrary(clazz);
	}

	protected void checkForStereotype(java.lang.Class<?> clazz) {
		if (!ProfileStereotype.class.isAssignableFrom(clazz))
			return;
		AStereotype aStereotype = clazz.getAnnotation(AStereotype.class);
		if (aStereotype==null)
			return;
		StereotypeDescriptor sd = new StereotypeDescriptor(clazz, this);
		if (sd.getProfile()==null)
			sd.setProfile(this.getClass());
		if (!sd.isValid())
			return;
		
		this.stereotypeDescriptorsByName.put(aStereotype.name(), sd);
		
	}
	
	protected void checkForLibrary(java.lang.Class<?> clazz) {
		if (!ProfileLibrary.class.isAssignableFrom(clazz))
			return;
		ALibrary aLibrary = clazz.getAnnotation(ALibrary.class);
		if (aLibrary==null)
			return;
		String name = aLibrary.name();
		if (!CommonUtils.isValueable(name))
			name = clazz.getSimpleName();
		this.libraryDescriptors.put(name, new LibraryDescriptor(clazz, this));
	}*/
	
	public List<StereotypeDescriptor> getStereotypeDescriptors(){
		return getAll(StereotypeDescriptor.class);
	}
	
	public StereotypeDescriptor getStereotypeDescriptor(Class<?> stereotypeClass) {
		return getByElement(stereotypeClass, StereotypeDescriptor.class);
	}
	
	public StereotypeDescriptor getStereotypeDescriptor(String name) {
		return getByName(name, StereotypeDescriptor.class);
	}
	
	public List<LibraryDescriptor> getLibraryDescriptors(){
		return getAll(LibraryDescriptor.class);
	}
	
	public LibraryDescriptor getLibraryDescriptor(Class<?> libraryClass) {
		return getByElement(libraryClass, LibraryDescriptor.class);
	}
	
	public LibraryDescriptor getLibraryDescriptor(String name) {
		return getByName(name, LibraryDescriptor.class);
	}
	
	public Class<?> getProfileClass(){
		return (Class<?>)getOriginElement();
	}
	
	public Profile getProfile() {
		return getProfile(ProfileRegistry.innerResourceSet);
	}
	
	public Profile getProfile(ResourceSet resourceSet) {
		return (Profile)PackageUtil.loadPackage(getUmlProfileURI(), resourceSet);
	}

	public UMLProfile getUmlProfile() {
		return umlProfile;
	}

	@Override
	protected Profile loadUMLElement(ResourceSet resourceSet) {
		Resource resource = resourceSet.getResource(getUmlProfileURI(), true);
		return (Profile)resource.getContents().get(0);
	}
	
}
