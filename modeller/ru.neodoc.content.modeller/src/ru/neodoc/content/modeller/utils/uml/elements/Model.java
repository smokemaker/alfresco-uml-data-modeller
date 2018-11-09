package ru.neodoc.content.modeller.utils.uml.elements;

import java.util.List;

public interface Model extends BasePackageElement {

	String getAuthor();
	
	String getVersion();
	
	List<Namespace> getNamespaces();
	
	List<Namespace> getImportedNamespaces();
	List<Namespace> getRequiredNamespaces();
	
}