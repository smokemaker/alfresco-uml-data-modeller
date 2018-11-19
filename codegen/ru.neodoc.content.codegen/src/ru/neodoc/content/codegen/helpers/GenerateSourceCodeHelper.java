package ru.neodoc.content.codegen.helpers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.codegen.CodegenSubject;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class GenerateSourceCodeHelper implements CodegenSubject {
	
	protected EObject currentObject = null;
	protected Element currentElement = null;
	protected Model umlRoot = null;
	
	
	protected StereotypedElement profileElement = null;
	
	public GenerateSourceCodeHelper(EObject currentObject){
		super();
		this.currentObject = currentObject; 
		this.currentElement = (Element)currentObject;
		this.umlRoot = AlfrescoUMLUtils.getUMLRoot(currentObject);
		this.profileElement = AlfrescoProfile.asUntyped(this.currentElement);
	}

	/* (non-Javadoc)
	 * @see ru.neodoc.content.codegen.helpers.CodegenSubject#isPackage()
	 */
	@Override
	public boolean isPackage(){
		return (currentElement!=null) && (currentElement instanceof Package);
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.codegen.helpers.CodegenSubject#isNamespace()
	 */
	@Override
	public boolean isNamespace(){
		return isPackage() && this.profileElement.has(Namespace.class); //AlfrescoUMLUtils.isNamespace(currentElement);
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.codegen.helpers.CodegenSubject#isModel()
	 */
	@Override
	public boolean isModel(){
		return isPackage() && this.profileElement.has(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model.class); //AlfrescoUMLUtils.isModel(currentElement);
	}

	@Override
	public boolean isAlfresco() {
		return isPackage() && this.profileElement.has(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForModel.Alfresco.class); //AlfrescoUMLUtils.isAlfresco(currentElement);
	}	
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.codegen.helpers.CodegenSubject#asNamespace()
	 */
	@Override
	public Package asNamespacePackage() {
		if (isNamespace())
			return (Package)currentElement;
		return (Package)null;
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.codegen.helpers.CodegenSubject#asModel()
	 */
	@Override
	public Package asModelPackage() {
		if (isModel())
			return (Package)currentElement;
		return (Package)null;
	}
	
	@Override
	public Package asAlfrescoPackage() {
		if (isAlfresco())
			return (Package)currentElement;
		return (Package)null;
	}
	
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.codegen.helpers.CodegenSubject#getCurrentObject()
	 */
	@Override
	public EObject getCurrentObject() {
		return currentObject;
	}

	public void setCurrentObject(EObject currentObject) {
		this.currentObject = currentObject;
	}

	/* (non-Javadoc)
	 * @see ru.neodoc.content.codegen.helpers.CodegenSubject#getUmlRoot()
	 */
	@Override
	public Model getUmlRoot() {
		return umlRoot;
	}

	public void setUmlRoot(Model umlRoot) {
		this.umlRoot = umlRoot;
	}

	@Override
	public ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForModel.Alfresco asAlfresco() {
		return this.profileElement.get(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForModel.Alfresco.class);
	}

	@Override
	public ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model asModel() {
		return this.profileElement.get(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model.class);
	}

	@Override
	public Namespace asNamespace() {
		return this.profileElement.get(Namespace.class);
	}
	
}
