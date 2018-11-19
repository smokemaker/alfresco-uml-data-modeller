package ru.neodoc.content.utils.uml.profile.descriptor;

import java.lang.reflect.Field;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.UMLUtils;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypeProperty;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypePropertyStoreIn;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class PropertyDescriptor extends AbstractOwnedDescriptor<Field, Property, StereotypeDescriptor> {


	protected boolean isReadOnly = false;
	protected Class<?> type;
	protected Class<?> exposedType;
	
	protected AStereotypeProperty annotation; 
	
	protected AStereotypePropertyStoreIn storageAnnotation;
	
	protected Stereotype stereotype;

	protected CustomDataTypeClassDescriptor customDataTypeClassDescriptor = null;
	
	public PropertyDescriptor(Field origin, StereotypeDescriptor owner) {
		super(origin, owner);
		
		annotation = origin.getAnnotation(AStereotypeProperty.class);
		if (annotation==null)
			return;
		setType(annotation.type());
		if (Void.class.equals(annotation.exposedType()))
			setExposedType(annotation.type());
		else
			setExposedType(annotation.exposedType());
		setReadOnly(annotation.isReadOnly());
		
		storageAnnotation = origin.getAnnotation(AStereotypePropertyStoreIn.class);
	}

	public boolean isValid() {
		return CommonUtils.isValueable(this.getName()) && (this.type!=null);
	}
	
	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	@Override
	public String getName() {
		try {
			return CommonUtils.isValueable(annotation.name())
					?annotation.name()
					:originElement.get(null)!=null
						?originElement.get(null).toString()
						:originElement.getName();
		} catch (Exception e) {
			return originElement.getName();
		}
	}

	public String getExternalName() {
		if (CommonUtils.isValueable(annotation.externalName()))
			return annotation.externalName();
		return getName();
	}
	
	public boolean hasExternalName() {
		return getName().equals(getExternalName());
	}
	
	public String getOtherName(String thisName) {
		if (!CommonUtils.isValueable(thisName))
			return null;
		if (thisName.equals(getName()))
			return getExternalName();
		if (thisName.equals(getExternalName()))
			return getName();
		return null;
	}
	
	public Class<?> _getOwner() {
		return null;//owner;
	}

/*	public void setOwner(Class<?> owner) {
		this.owner = owner;
	}
*/
	public boolean isReadOnly() {
		return isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}
	
	public Stereotype getStereotype() {
		return stereotype;
	}

/*	public void setStereotype(Stereotype stereotype) {
		this.stereotype = stereotype;
		if (this.stereotype==null)
			this.stereotypeDescriptor = null;
		else
			this.stereotypeDescriptor = AbstractProfile.findStereotypeDescriptor(this.stereotype);
	}
*/	
	public Class<?> getExposedType() {
		return exposedType;
	}

	public void setExposedType(Class<?> exposedType) {
		this.exposedType = exposedType;
	}

	public StereotypeDescriptor getStereotypeDescriptor() {
		return this.ownerDescriptor;
	}

	public void setValue(StereotypedElement stereotypedElement, Object value) 
			throws IllegalArgumentException {
		PrefixedName pn = new PrefixedName("");
		pn.setPrefix(this.ownerDescriptor.getProfileName());
		pn.setName(this.ownerDescriptor.getStereotypeName());
		if (stereotypedElement.getElement().getAppliedStereotype(pn.getFullName())==null)
			throw new IllegalArgumentException("Stereotype " + pn.getFullName() + " not applied");
		
		// convert value
		
		Object convertedValue = value;
		
		
		stereotypedElement.getElement().setValue(
				UMLUtils.getStereotype(stereotypedElement.getElement(), ownerDescriptor.getStereotypeName()), 
				this.getName(), 
				convertedValue);
	}
	
	public CustomDataTypeClassDescriptor getCustomTypeDescriptor() {
		if (customDataTypeClassDescriptor==null)
			customDataTypeClassDescriptor = CustomDataTypeClassDescriptor.find(
						this.getType().isArray()
						?this.getType().getComponentType()
						:this.getType()
					);
		return customDataTypeClassDescriptor;
	}

	public boolean isTypeCustom() {
		return getCustomTypeDescriptor()!=null;
	}
	
	@Override
	protected Property getFromParent(Element parentElement) {
		if (!(parentElement instanceof Stereotype))
			return null;
		for (Property property: ((Stereotype)parentElement).getOwnedAttributes())
			if (property.getName().equals(getName()))
				return property;
		return null;
	}
	
	public boolean isStoreInOther() {
		return (this.storageAnnotation!=null);
	}
	
	public Class<? extends ProfileStereotype> getStorageOwner(){
		if (!isStoreInOther())
			return null;
		return storageAnnotation.storageOwner();
	}
	
	public String getStoragePropertyName() {
		if (!isStoreInOther())
			return null;
		return storageAnnotation.storagePropertyName();
	}
}
