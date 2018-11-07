package ru.neodoc.content.utils.uml.profile.descriptor;

import java.lang.reflect.Field;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypePropertyDefaultValue;

public class PropertyDefaultValueDescriptor extends AbstractOwnedDescriptor<Field, Property, StereotypeDescriptor> {

	private static class UnsetValue {
		
	}
	
	private static UnsetValue UNSET_VALUE = new UnsetValue();
	
	protected Class<?> type;
	protected Object value = UNSET_VALUE;
	protected String name;
	
	public PropertyDefaultValueDescriptor(Field annotatedField, StereotypeDescriptor stereotypeDescriptor) {
		super(annotatedField, stereotypeDescriptor);
		AStereotypePropertyDefaultValue annotation = annotatedField.getAnnotation(AStereotypePropertyDefaultValue.class);
		if (annotation==null)
			return;
	
		setName(annotation.propertyName());
		
		PropertyDescriptor pd = null;
		if (this.ownerDescriptor.isValid())
			pd = this.ownerDescriptor.getPropertyDescriptor(name);
		
		setType(annotatedField.getType());
		
		if (pd!=null) {
			Class<?> propertyType = pd.getType();
			if (propertyType!=null)
				if (!propertyType.isAssignableFrom(type))
					try {
						propertyType.cast(annotatedField.get(null));
					} catch (Exception e) {
						// not castable
						return;
					}
		}
		
		try {
			setValue(annotatedField.get(null));
		} catch (IllegalAccessException iae) {
			return;
		}
	}

	public boolean isValid() {
		return CommonUtils.isValueable(this.name)
			&& !UNSET_VALUE.equals(this.value)
			&& (this.type!=null)
			&& (this.value==null?true:this.type.isAssignableFrom(this.value.getClass()));
	}
	
	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	
}
