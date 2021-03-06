package ru.neodoc.content.codegen;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public interface CodegenSubject {

	boolean isPackage();

	boolean isNamespace();

	boolean isModel();

	boolean isAlfresco();
	
	
	Package asNamespacePackage();

	Package asModelPackage();

	Package asAlfrescoPackage();
	
	AlfrescoProfile.ForModel.Alfresco asAlfresco();
	AlfrescoProfile.ForPackage.Model asModel();
	AlfrescoProfile.ForPackage.Namespace asNamespace();
	
	EObject getCurrentObject();

	Model getUmlRoot();

}