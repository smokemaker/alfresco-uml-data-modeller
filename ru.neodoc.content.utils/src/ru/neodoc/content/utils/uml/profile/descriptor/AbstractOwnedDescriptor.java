package ru.neodoc.content.utils.uml.profile.descriptor;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.Element;

public abstract class AbstractOwnedDescriptor<
		JavaElementClass,
		UMLElementClass extends Element,
		OwnerDescriptorClass extends AbstractDescriptor<?, ?>> extends AbstractDescriptor<JavaElementClass, UMLElementClass>{

	protected OwnerDescriptorClass ownerDescriptor;
	
	public AbstractOwnedDescriptor(JavaElementClass origin, OwnerDescriptorClass owner) {
		super(origin);
		setOwnerDescriptor(owner);
		scan(originElement);
	}

	protected OwnerDescriptorClass getOwnerDescriptor() {
		return ownerDescriptor;
	}

	protected void setOwnerDescriptor(OwnerDescriptorClass ownerDescriptor) {
		this.ownerDescriptor = ownerDescriptor;
	}
	
	protected void scan(JavaElementClass elementToScan) {
		
	}
	
	@Override
	protected UMLElementClass loadUMLElement(ResourceSet resourceSet) {
		Element parentElement = ownerDescriptor.getUMLElement(resourceSet);
		if (parentElement==null)
			return null;
		return getFromParent(parentElement);
	}
	
	protected abstract UMLElementClass getFromParent(Element parentElement);
}
