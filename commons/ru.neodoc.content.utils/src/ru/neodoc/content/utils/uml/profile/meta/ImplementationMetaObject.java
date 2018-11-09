package ru.neodoc.content.utils.uml.profile.meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.UMLUtils;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.UMLProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotype;
import ru.neodoc.content.utils.uml.profile.dataconverter.DataConverterRegistry;
import ru.neodoc.content.utils.uml.profile.descriptor.CustomDataTypeClassDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.ProfileDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.PropertyDefaultValueDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.PropertyDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.StereotypeDescriptor;
import ru.neodoc.content.utils.uml.profile.exception.NotAStereotypeException;
import ru.neodoc.content.utils.uml.profile.exception.StereotypeNotImplementedException;
import ru.neodoc.content.utils.uml.profile.registry.ProfileRegistry;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public abstract class ImplementationMetaObject extends MetaObject implements ProfileStereotype {

	protected CompositeMetaObject composite;
	protected List<ImplementationMetaObject> subImplementors = new ArrayList<>();
	
	protected ImplementationMetaObject ownerImplementor = null;
	
	protected Class<? extends ProfileStereotype> implementedStereotype;
	protected AStereotype stereotypeAnnotation;
	protected Stereotype stereotype; 
	protected StereotypeDescriptor stereotypeDescriptor;
	
	protected EObject stereotypeApplication;
	
	public ImplementationMetaObject(CompositeMetaObject composite) {
		this.composite = composite;
		this.element = composite.getElement();
		init();
		setDefaults();
	}

	protected void addSubimplemetor(ImplementationMetaObject imo) {
		this.subImplementors.add(imo);
		imo.setOwnerImplementor(this);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends ProfileStereotype> T createAndRegisterSubimplementor(Class<T> clazz) {
		StereotypeDescriptor descriptor = ProfileRegistry.INSTANCE.findStereotypeDescriptor(clazz);
		try {
			if (!descriptor.isValid())
				throw new NotAStereotypeException(clazz);
			if (!descriptor.isImplemented())
				throw new StereotypeNotImplementedException((Class<ProfileStereotype>)clazz);
			
			Class<? extends ImplementationMetaObject> cImo = descriptor.getImplementationClass();
			ImplementationMetaObject imo = cImo.getConstructor(CompositeMetaObject.class).newInstance(this.composite);
			addSubimplemetor(imo);
			return (T)imo;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	protected void init() {
		AImplements aImplements = this.getClass().getAnnotation(AImplements.class);
		if (aImplements==null)
			return;
		this.implementedStereotype = aImplements.value();
		
		this.stereotypeAnnotation = this.implementedStereotype.getAnnotation(AStereotype.class);
		if (this.stereotypeAnnotation==null)
			return;
		
		// this.stereotype = this.composite.getProfile().getOwnedStereotype(this.stereotypeAnnotation.name());
		Profile p = UMLUtils.getProfile(
				this.element, 
				ProfileRegistry.INSTANCE.getProfileDescriptorByStereotype((this.implementedStereotype)).getName());
		if (p!=null)
			this.stereotype = p.getOwnedStereotype(this.stereotypeAnnotation.name());
		
		if (this.stereotype!=null)
			this.stereotypeApplication = this.element.getStereotypeApplication(this.stereotype);
		
		this.stereotypeDescriptor = ProfileRegistry.INSTANCE.findStereotypeDescriptor(this.implementedStereotype);
	}
	
	protected void setDefaults() {
		if (this.stereotypeDescriptor==null)
			return;
		
		List<PropertyDefaultValueDescriptor> defaults = this.stereotypeDescriptor.getPropertyDefaultValueDescriptors();
		for (PropertyDefaultValueDescriptor descriptor: defaults) {
			Class<?> owner = this.stereotypeDescriptor.getOriginElement();
			if (owner==null)
				owner = this.implementedStereotype;
			try {
				@SuppressWarnings("unchecked")
				Class<? extends ProfileStereotype> psClass = (Class<? extends ProfileStereotype>)owner;
				ImplementationMetaObject imo = (ImplementationMetaObject)get(psClass);
				imo.setAttribute(descriptor.getName(), descriptor.getValue());
			} catch (Exception e) {
				continue;
			}
			
		}
	}
	
	protected EClass getAttributeEClass(String propertyName) {
		try {
			return (EClass) this.stereotypeApplication.eClass().getEStructuralFeature(propertyName).getEType();
		} catch (Exception e) {
			// NOOP
		}
		try {
			return (EClass) this.getTopOwnerImplementor().getStereotypeApplication().eClass().getEStructuralFeature(propertyName).getEType();
		} catch (Exception e) {
			// NOOP
		}
		return null;
	}
	
	public void beforeRemove() {
		
	}
	
	public ProfileStereotype cast() {
		return this.implementedStereotype.cast(this);
	}
	
	public Class<? extends ProfileStereotype> getImplementedStereotype(){
		return this.implementedStereotype;
	}
	
	public List<Class<? extends ProfileStereotype>> getAllImplementedStereotypes(){
		List<Class<? extends ProfileStereotype>> result = new ArrayList<>();
		result.add(this.implementedStereotype);
		for (ImplementationMetaObject si: this.subImplementors)
			result.addAll(si.getAllImplementedStereotypes());
		return result;
	}

	public boolean isOwned() {
		return this.ownerImplementor!=null;
	}
	
	public ImplementationMetaObject getTopOwnerImplementor() {
		ImplementationMetaObject implementor = this;
		while (implementor!=null && implementor.isOwned())
			implementor = implementor.getOwnerImplementor();
		return implementor;
	}
	
	public Stereotype getStereotype() {
		return this.stereotype;
	}

	public StereotypeDescriptor getStereotypeDescriptor() {
		if (this.stereotypeDescriptor==null)
			if (this.implementedStereotype!=null)
				this.stereotypeDescriptor = StereotypeDescriptor.find(this.implementedStereotype);
		return this.stereotypeDescriptor;
	}
	
	public EObject getStereotypeApplication() {
		return stereotypeApplication;
	}

	public ImplementationMetaObject getOwnerImplementor() {
		return this.ownerImplementor;
	}

	public void setOwnerImplementor(ImplementationMetaObject ownerImplementor) {
		this.ownerImplementor = ownerImplementor;
	}

	public void apply() {
		this.stereotypeApplication = this.element.applyStereotype(this.stereotype);
	}
	
	@Override
	public boolean has(Class<? extends ProfileStereotype> clazz) {
		return this.composite.has(clazz);
	}

	@Override
	public void remove(Class<? extends ProfileStereotype> clazz) {
		this.composite.remove(clazz);
	}

	@Override
	public void removeAll(Class<? extends ProfileStereotype> clazz) {
		this.composite.removeAll(clazz);
	}

	@Override
	public <T extends ProfileStereotype> T get(Class<T> clazz) {
		return this.composite.get(clazz);
	}

	@Override
	public <T extends ProfileStereotype> T getOrCreate(Class<T> clazz) {
		return this.composite.getOrCreate(clazz);
	}

	@Override
	public <T> List<T> getAll(Class<? extends ProfileStereotype> clazz) {
		return this.composite.getAll(clazz);
	}

	@Override
	public <T> T append(Class<? extends ProfileStereotype> clazz) {
		return this.composite.append(clazz);
	}
	
	@Override
	@Deprecated
	public AbstractProfile getProfile() {
		ProfileDescriptor pd = getProfileDescriptor(); 
		if (pd==null)
			return null;
		return (AbstractProfile)pd.getUmlProfile();
	}
	
	@Override
	public ProfileDescriptor getProfileDescriptor() {
		ProfileDescriptor pd = ProfileRegistry.INSTANCE
				.getProfileDescriptorByStereotype(this.implementedStereotype);
		return pd;
	}
	
	@Override
	public List<UMLProfile> getAppliedProfiles() {
		return this.composite.getAppliedProfiles();
	}
	
	@Override
	public List<ProfileStereotype> getAll(String profileName) {
		return this.composite.getAll(profileName);
	}
	
	@Override
	public List<ProfileStereotype> getAll(UMLProfile profile) {
		return this.composite.getAll(profile);
	}
	
	public void setAttribute(String name, boolean value) {
		setAttribute(name, new Boolean(value));
	}
	
	public void setAttribute(String name, Boolean value) {
		if (value==null)
			setAttribute(name, (Object)new Boolean(false));
		else
			setAttribute(name, (Object)value);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setAttribute(String name, Object value) {
		Object valueToSet = value;
		if (this.element==null)
			return;
		ImplementationMetaObject top = getTopOwnerImplementor();
		if (top == null)
			return;
		Stereotype st = top.getStereotype();
		if (st == null)
			return;
		StereotypeDescriptor descriptor = top.getStereotypeDescriptor();
		if (descriptor!=null) {
			PropertyDescriptor propertyDescriptor = descriptor.findPropertyDescriptor(name);
			if (propertyDescriptor!=null) {
				valueToSet = convertValueToSet(propertyDescriptor, valueToSet);
				/*Class<?> targetElementClass = propertyDescriptor.getType();
				if (targetElementClass.isArray())
					targetElementClass = targetElementClass.getComponentType();
				CustomDataTypeClassDescriptor cdtcd = CustomDataTypeClassDescriptor.find(targetElementClass);*/
				if ((/*cdtcd!=null*/propertyDescriptor.isTypeCustom()) && (valueToSet!=null)) {
					
					EClass eClass = getAttributeEClass(name);
					
					if (List.class.isAssignableFrom(valueToSet.getClass()) || valueToSet.getClass().isArray())
						valueToSet = convert(valueToSet, DynamicEObjectImpl[].class, eClass);
					else
						valueToSet = convert(valueToSet, DynamicEObjectImpl.class, eClass);
				}
			}
		}
		Object current = this.element.getValue(st, name);
		if (current!=null)
			if (AbstractEList.class.isAssignableFrom(current.getClass())){
				AbstractEList eList = (AbstractEList<?>)current;
				eList.clear();
				if (valueToSet!=null) {
					if (List.class.isAssignableFrom(valueToSet.getClass()))
						eList.addAll((List)valueToSet);
					else if (valueToSet.getClass().isArray()) {
						Object[] temp = (Object[])valueToSet;
						for (int i = 0; i < temp.length; i++) {
							Object object = temp[i];
							eList.add(object);	
						}
					} else
						eList.add(valueToSet);
				}
				return;
			} 
		this.element.setValue(st, name, valueToSet);
		
		// sync data in redefined properties
		// TODO make it more beautiful
		PropertyDescriptor propertyDescriptor = descriptor.findPropertyDescriptor(name);
		if ((propertyDescriptor!=null) && (propertyDescriptor.hasExternalName())) {
			String otherName = propertyDescriptor.getOtherName(name);
			if (CommonUtils.isValueable(otherName))
				this.element.setValue(st, otherName, valueToSet);
		}
	}
	
	protected Object convertValueToSet(PropertyDescriptor propertyDescriptor, Object originValue) {
		Object temp = convert(originValue, propertyDescriptor.getExposedType());
		temp = convert(temp, propertyDescriptor.getType());
		return temp;
	}
	
	protected Object convert(Object origin, Class<?> targetClass, Object...objects) {
		if (targetClass==null)
			return origin;
		if (origin==null)
			return targetClass.cast(null);
		if (targetClass.isAssignableFrom(origin.getClass()))
			return origin;
		
		return DataConverterRegistry.INSTANCE.convert(origin, targetClass, objects);
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name) {
		Object result = null;
		if (this.element==null)
			return null;
		ImplementationMetaObject top = getTopOwnerImplementor();
		if (top == null)
			return null;
		Stereotype st = top.getStereotype();
		if (st == null)
			return null;
		result = this.element.getValue(st, name);
		StereotypeDescriptor descriptor = top.getStereotypeDescriptor();
		if (descriptor!=null) {
			PropertyDescriptor propertyDescriptor = descriptor.findPropertyDescriptor(name);
			if (propertyDescriptor!=null) {
				result = synchronizeValues(propertyDescriptor, result);
				result = convertValueToGet(propertyDescriptor, result);
			}
		}
		return (T)result;
	}

	// TODO make it more beautiful
	protected Object synchronizeValues(PropertyDescriptor descriptor, Object currentValue) {
		if (descriptor.getName().equals(descriptor.getExternalName()))
			return currentValue;
		Stereotype s = getTopOwnerImplementor().getStereotype();
		Object externalValue = this.element.getValue(s, descriptor.getExternalName());
		if (externalValue!=null)
			this.element.setValue(s, descriptor.getName(), externalValue);
		return externalValue;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name, T defaultValue) {
		T result = getAttribute(name); 
		return result==null?defaultValue:result;
	}

	protected Object convertValueToGet(PropertyDescriptor propertyDescriptor, Object originValue) {
		Object temp = convert(originValue, propertyDescriptor.getType(), getAttributeEClass(propertyDescriptor.getName()));
		temp = convert(temp, propertyDescriptor.getExposedType());
		return temp;
	}
	
	protected boolean getBoolean(String name) {
		Boolean b = getAttribute(name);
		if (b == null)
			return false;
		return b.booleanValue();
	}
	
	protected boolean getBoolean(String name, Boolean defaultValue) {
		Boolean b = getAttribute(name, defaultValue);
		if (b == null)
			return false;
		return b.booleanValue();
	}
	
	protected String getString(String name) {
		Object obj = getAttribute(name);
		return obj==null?null:obj.toString();
	}

	protected String getString(String name, String defaultValue) {
		Object obj = getAttribute(name, defaultValue);
		return obj==null?null:obj.toString();
	}
	
	@Override
	public List<StereotypedElement> getAllRequiredElements() {
		return this.composite.getAllRequiredElements();
	}
	
	@Override
	public List<StereotypedElement> getRequiredElements(String profileName) {
		return this.composite.getRequiredElements(profileName);
	}
	
	@Override
	public List<StereotypedElement> getRequiredElements(UMLProfile profile) {
		return this.composite.getRequiredElements(profile);
	}
	
	@Override
	public List<StereotypedElement> getRequiredElementsByProfile() {
		return getRequiredElements(getProfile());
	}
	
	@Override
	public List<StereotypedElement> getRequiredElements() {
		return (new RequirementsResolver(this)).resolveAll();
	}
}
