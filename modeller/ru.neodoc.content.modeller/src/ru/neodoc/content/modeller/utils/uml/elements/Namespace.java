package ru.neodoc.content.modeller.utils.uml.elements;

import java.util.List;

public interface Namespace extends BasePackageElement {

	Model getModel();
	
	String getPrefix();

	List<Type> getTypes();

	List<Aspect> getAspects();

	List<DataTypeElement> getDataTypes();
	
	List<Namespace> getImportedNamespaces();
	
	List<Namespace> getRequiredNamespaces();
	List<Type> getRequiredTypes();
	List<Aspect> getRequiredAspects();
	List<DataTypeElement> getRequiredDataTypes();

}