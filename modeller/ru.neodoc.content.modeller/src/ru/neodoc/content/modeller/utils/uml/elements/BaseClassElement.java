package ru.neodoc.content.modeller.utils.uml.elements;

import java.util.List;

public interface BaseClassElement extends BaseTypeElement {

	Namespace getNamespace();

	BaseClassElement getParent();

	List<Property> getProperties();

	List<PeerAssociation> getPeerAssociations();
	List<ChildAssociation> getChildAssociations();
	List<MandatoryAspect> getMandatoryAspects();

	List<Namespace> getRequiredNamespaces();

	List<Type> getRequiredTypes();
	List<Aspect> getRequiredAspects();
	List<DataTypeElement> getRequiredDataTypes();
	
}