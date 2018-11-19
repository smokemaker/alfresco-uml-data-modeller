package ru.neodoc.content.utils.uml.profile.descriptor;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.profile.annotation.ACustomDataTypeClass;
import ru.neodoc.content.utils.uml.profile.registry.ProfileRegistry;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.search.UMLSearchUtils;

@SuppressWarnings("rawtypes")
public class CustomDataTypeClassDescriptor extends AbstractOwnedDescriptor<
		Class, 
		org.eclipse.uml2.uml.DataType, 
		LibraryDescriptor> {

	public static CustomDataTypeClassDescriptor find(Class<?> clazz) {
		return ProfileRegistry.INSTANCE.getDescriptor(CustomDataTypeClassDescriptor.class, clazz);
	}
	
	protected ACustomDataTypeClass annotation;
	
	public CustomDataTypeClassDescriptor(Class origin, LibraryDescriptor owner) {
		super(origin, owner);
		annotation = ((Class<?>)origin).getAnnotation(ACustomDataTypeClass.class);
	}

	@Override
	protected org.eclipse.uml2.uml.DataType getFromParent(Element parentElement) {
		return UMLSearchUtils.dataTypeByName((Package)parentElement, getName());
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String getName() {
		String name = annotation.name();
		if (!CommonUtils.isValueable(name))
			name = originElement.getSimpleName();
		return name;
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends ProfileStereotype> getWrapper(){
		Class<?> wrapper = annotation.wrapper();
		if (wrapper == null)
			return null;
		if (!ProfileStereotype.class.isAssignableFrom(wrapper))
			return (Class<? extends ProfileStereotype>)wrapper;
		return null;
	}

	@SuppressWarnings("unchecked")
	public DynamicEObjectImpl convertToEObject(Object value) {
		if (!originElement.isAssignableFrom(value.getClass()))
			return new DynamicEObjectImpl();
		DynamicEObjectImpl result;
		if (umlElement instanceof EClass)
			result = new DynamicEObjectImpl((EClass)umlElement);
		else
			result = new DynamicEObjectImpl();
		return result;
	}
	
}
