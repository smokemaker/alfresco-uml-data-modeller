package ru.neodoc.content.utils.uml.profile.descriptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.utils.uml.profile.annotation.AImplemented;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotype;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotype.AApplication;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypeProperties;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypeProperty;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypePropertyDefaultValue;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypePropertyDefaults;
import ru.neodoc.content.utils.uml.profile.annotation.AWrapped;
import ru.neodoc.content.utils.uml.profile.descriptor.scanner.AbstractScanner.ScannerValidator;
import ru.neodoc.content.utils.uml.profile.descriptor.scanner.ClassInClassScanner;
import ru.neodoc.content.utils.uml.profile.descriptor.scanner.FieldInClassScanner;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObject;
import ru.neodoc.content.utils.uml.profile.registry.ProfileRegistry;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

@SuppressWarnings("rawtypes")
public class StereotypeDescriptor extends AbstractOwnedDescriptor<Class, Stereotype, ProfileDescriptor> {
	
	
	/*
	 * STATIC
	 */
	
	public static StereotypeDescriptor find(Class<?> clazz) {
		return ProfileRegistry.INSTANCE.findStereotypeDescriptor(clazz);
	}
	
	public static StereotypeDescriptor find(String name) {
		return ProfileRegistry.INSTANCE.getDescriptor(StereotypeDescriptor.class, name);
	}
	
	/*
	 * DYNAMIC
	 */
	
	protected AStereotype annotation;
	
	protected AImplemented implementation;
	protected Class<? extends ImplementationMetaObject> implementationClass = null;
	
	protected AWrapped wrap;
	protected Class<? extends ImplementationMetaObject> wrapClass = null;
	
	protected RelationDescriptor relationDescriptor = null;
	
/*	protected Class<?> ownerClass;
	
	protected Stereotype stereotype;
*/	
	public StereotypeDescriptor(Class<?> clazz, String profileName) {
		this(clazz, ProfileRegistry.INSTANCE.getProfileDescriptor(profileName));
	}
	
	public StereotypeDescriptor(Class<?> clazz, ProfileDescriptor profileDescriptor) {
		super(clazz, profileDescriptor);

		annotation = clazz.getAnnotation(AStereotype.class);
		
		implementation = clazz.getAnnotation(AImplemented.class);
		if (implementation!=null)
			implementationClass = implementation.value();
		
		wrap = clazz.getAnnotation(AWrapped.class);
		if (wrap!=null)
			wrapClass = wrap.value();
		
		relationDescriptor = RelationDescriptor.create(clazz);
	}
	
	public String getProfileName() {
		return this.ownerDescriptor.getName();
	}

	public ProfileDescriptor getProfileDescriptor() {
		return this.ownerDescriptor;
	}

	@Override
	public List<Class> getKeys() {
		List<Class> result = super.getKeys();
		if (implementationClass!=null)
			result.add(implementationClass);
		return result;
	}
	
	@Override
	protected void initInnerStorages() {
		super.initInnerStorages();
		this.<PropertyDescriptor, Field>innerStorage(PropertyDescriptor.class);
		this.<PropertyDefaultValueDescriptor, Field>innerStorage(PropertyDefaultValueDescriptor.class);
	}
	
	@Override
	protected void doScan(Class javaElement) {
		ClassInClassScanner scanner = new ClassInClassScanner();
		scanner.add(AStereotypeProperties.class, null);
		scanner.add(AStereotypePropertyDefaults.class, null);
		Map<Class<? extends Annotation>, List<Class>> result = scanner.scan(ownerDescriptor, javaElement);
		scanForProperties(result.get(AStereotypeProperties.class));
		scanForPropertyDefaults(result.get(AStereotypePropertyDefaults.class));
	}

	protected List<Field> scanForFields(
			Class<? extends Annotation> annotationClass, 
			List<Class> toScan,
			Class<? extends AbstractOwnedDescriptor<Field, ?, ? extends AbstractDescriptor<Class, ?>>> classToCreate){
		
		if ((toScan==null) || toScan.isEmpty())
			return Collections.emptyList();
		
		FieldInClassScanner scanner = new FieldInClassScanner();
		scanner.validator(new ScannerValidator<Field>() {
			
			@Override
			public boolean isValid(Field object) {
				return Modifier.isStatic(object.getModifiers())
						&& Modifier.isFinal(object.getModifiers());
			}
		});
		scanner.add(annotationClass, classToCreate);
		List<Field> result = new ArrayList<>();
		for (Class clazz: toScan) {
			Map<Class<? extends Annotation>, List<Field>> scanned = scanner.scan(this, clazz);
			if (scanned.get(annotationClass)!=null)
				result.addAll(scanned.get(annotationClass));
		}
		return result;
	}
	
	protected List<Field> scanForProperties(List<Class> toScan) {
		return scanForFields(AStereotypeProperty.class, toScan, PropertyDescriptor.class);
	}

	protected List<Field> scanForPropertyDefaults(List<Class> toScan) {
		return scanForFields(AStereotypePropertyDefaultValue.class, toScan, PropertyDefaultValueDescriptor.class);
	}
	
/*	protected boolean scanForProperties(java.lang.Class<?> clazz, java.lang.Class<?> owner) {
		AStereotypeProperties propertiesAnnotation = clazz.getAnnotation(AStereotypeProperties.class);
		if (propertiesAnnotation!=null) {
			loadProperties(clazz, owner);
			return true;
		}
		java.lang.Class<?>[] declared = clazz.getDeclaredClasses();
		for (int i = 0; i < declared.length; i++) {
			java.lang.Class<?> subclass = declared[i];
			if (scanForProperties(subclass, owner))
				return true;
		}
		return false;
	}
	
	protected void loadProperties(java.lang.Class<?> clazz, java.lang.Class<?> owner) {
		Field[] fields = clazz.getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			Field field = fields[i];
			if (field.isAccessible() 
					&& Modifier.isStatic(field.getModifiers())
					&& Modifier.isFinal(field.getModifiers())){
				PropertyDescriptor descriptor = new PropertyDescriptor(field, this);
				if (descriptor.isValid())
					addPropertyDescriptor(descriptor);
			}
		}
	}
	
	protected boolean scanForDefaults(java.lang.Class<?> clazz) {
		AStereotypePropertyDefaults propertiesAnnotation = clazz.getAnnotation(AStereotypePropertyDefaults.class);
		if (propertiesAnnotation!=null) {
			loadDefaults(clazz);
			return true;
		}
		java.lang.Class<?>[] declared = clazz.getDeclaredClasses();
		for (int i = 0; i < declared.length; i++) {
			java.lang.Class<?> subclass = declared[i];
			if (scanForDefaults(subclass))
				return true;
		}
		return false;
	}
	
	protected void loadDefaults(java.lang.Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			Field field = fields[i];
			if (field.isAccessible() 
					&& Modifier.isStatic(field.getModifiers())
					&& Modifier.isFinal(field.getModifiers())){
				PropertyDefaultValueDescriptor descriptor = new PropertyDefaultValueDescriptor(field);
				if (descriptor.isValid())
					addPropertyDefaultValueDescriptor(descriptor);
			}
		}
	}
	
*//*	protected void addPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
		propertyDescriptor.setStereotypeDescriptor(this);
		this.propertyRegistry.put(propertyDescriptor.getName(), propertyDescriptor);
	}
	
	protected void addPropertyDefaultValueDescriptor(PropertyDefaultValueDescriptor propertyDefaultValueDescriptor) {
		this.propertyDefaultValueRegistry.put(propertyDefaultValueDescriptor.getName(), propertyDefaultValueDescriptor);
	}
*/	
	public List<PropertyDescriptor> getPropertyDescriptors(){
		return getAll(PropertyDescriptor.class);
	}
	
	public PropertyDescriptor getPropertyDescriptor(String propertyName) {
		return getByName(propertyName, PropertyDescriptor.class);
	}
	
	public PropertyDescriptor findPropertyDescriptor(String propertyName) {
		return ProfileRegistry.INSTANCE.findPropertyDescriptor(propertyName, this);
	}
	
	public List<PropertyDefaultValueDescriptor> getPropertyDefaultValueDescriptors(){
		return getAll(PropertyDefaultValueDescriptor.class);
	}
	
	public PropertyDefaultValueDescriptor getPropertyDefaultValueDescriptor(String propertyName) {
		return getByName(propertyName, PropertyDefaultValueDescriptor.class);
	}
	
	public Class<?> getOwnerClass() {
		return originElement;
	}
	
	public Class<?> getStereotypeClass(){
		return this.getOwnerClass();
	}
	
	@Override
	public boolean isValid() {
		return (annotation!=null) && (ProfileStereotype.class.isAssignableFrom(originElement));
	}
	
	public boolean isImplemented() {
		return implementationClass!=null;
	}
	
	public String getStereotypeName() {
		if (annotation==null) return null;
		return annotation.name();
	}
	
	public Stereotype getStereotype(ResourceSet resourceSet) {
		if (this.ownerDescriptor==null)
			return null;
		Profile profile = this.ownerDescriptor.getProfile(resourceSet);
		if (profile==null)
			return null;
		return profile.getOwnedStereotype(getStereotypeName());
	}

	public Class<? extends ImplementationMetaObject> getImplementationClass() {
		return implementationClass;
	}
	
	public boolean isAbstract() {
		if (annotation==null) return false;
		return annotation.isAbstract();
	}
	
	public List<AApplication> getOwnedApplications(){
		List<AApplication> result = new ArrayList<>();
		if (annotation==null)
			return result;
		result.addAll(Arrays.asList(annotation.applications()));
		return result;
	}
	
	public List<AApplication> getEffectiveApplications(){
		List<AApplication> result = getOwnedApplications();
		if (!result.isEmpty())
			return result;
		
		ProfileRegistry.INSTANCE.collectAApplications(getOwnerClass(), result);
		return result;
	}
	
	public Class<?> getProfile(){
		if (this.ownerDescriptor.getOriginElement()!=null)
			return this.ownerDescriptor.getOriginElement();
		
		if (annotation==null)
			return null;
		
		return annotation.profile().equals(AStereotype.EMPTY_PROFILE)?null:annotation.profile();
	}
	
	public Class<?> getExactSetProfile(){
		if (annotation==null)
			return null;
		return annotation.profile();
	}

	public RelationDescriptor getRelationDescriptor() {
		return relationDescriptor;
	}
	
	@Override
	public String getName() {
		return this.annotation.name();
	}

	@Override
	protected Stereotype getFromParent(Element parentElement) {
		if (!(parentElement instanceof Profile))
			return null;
		return ((Profile)parentElement).getOwnedStereotype(getName());
	}
}
