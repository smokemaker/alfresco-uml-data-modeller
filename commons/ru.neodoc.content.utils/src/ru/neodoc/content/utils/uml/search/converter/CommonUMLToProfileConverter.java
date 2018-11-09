package ru.neodoc.content.utils.uml.search.converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class CommonUMLToProfileConverter<SourceClass extends Element, TargetClass extends ProfileStereotype>
		implements UMLSearchConverter<SourceClass, TargetClass> {
	
	protected java.lang.Class<TargetClass> targetClass; 
	
	protected List<UMLSearchConverterFilter<? super TargetClass>> filters = new ArrayList<>();
	
	public CommonUMLToProfileConverter(java.lang.Class<TargetClass> targetClass) {
		this.targetClass = targetClass;
	}
	
	@Override
	public <SC extends SourceClass> boolean isConvertible(SC origin) {
		boolean result = AbstractProfile.isType(origin, this.targetClass);
		return result; 
	}
	
	@Override
	public <TC extends TargetClass, SC extends SourceClass> TC convert(SC origin) {
		StereotypedElement ps = AbstractProfile.asUntyped(origin);
		@SuppressWarnings("unchecked")
		TC result = (TC)ps.get(this.targetClass); 

		boolean checked = (result!=null);
		Iterator<UMLSearchConverterFilter<? super TargetClass>>	iterator = this.filters.iterator();
		while (checked && iterator.hasNext())
			checked = checked && iterator.next().matches(result);
				
		return checked?result:null;
	}
	
	@Override
	public UMLSearchConverter<SourceClass, TargetClass>  addFilter(UMLSearchConverterFilter<? super TargetClass> filter) {
		this.filters.add(filter);
		return this;
	}
}
