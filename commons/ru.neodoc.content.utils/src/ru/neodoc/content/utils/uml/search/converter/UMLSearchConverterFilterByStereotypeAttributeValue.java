package ru.neodoc.content.utils.uml.search.converter;

import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

public class UMLSearchConverterFilterByStereotypeAttributeValue implements UMLSearchConverterFilter<ProfileStereotype> {

	protected String attributeName;
	protected Object attributeValue;
	
	protected UMLSearchConverterFilterByStereotypeAttributeValue(String name, Object value) {
		this.attributeName = name;
		this.attributeValue = value;
	}
	
	public static UMLSearchConverterFilterByStereotypeAttributeValue create(String name, Object value) {
		return new UMLSearchConverterFilterByStereotypeAttributeValue(name, value);
	}

	@Override
	public boolean matches(ProfileStereotype element) {
		Object obj = element.getAttribute(attributeName);
		return (obj==null && attributeValue==null) || (obj!=null && obj.equals(attributeValue));
	}
	
	
}
