package ru.neodoc.content.modeller.utils.uml.elements.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.Alfresco;
import ru.neodoc.content.modeller.utils.uml.elements.Model;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class AlfrescoImpl extends BasePackageElementImpl implements Alfresco {
	
	protected Stereotype alfrescoStereotype;
	
	public AlfrescoImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AlfrescoImpl(Element element) {
		super(element);
		// TODO Auto-generated constructor stub
	}

	public AlfrescoImpl(EObject eObject) {
		super(eObject);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		
		if (!super.isValid())
			return;
		
		if (AlfrescoUMLUtils.isAlfresco(packageElement))
			alfrescoStereotype = AlfrescoUMLUtils.getStereotype(packageElement, AlfrescoProfile.ForModel.Alfresco._NAME);
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && (alfrescoStereotype != null);
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.Alfresco#getModels()
	 */
	@Override
	public List<Model> getModels(){
		if (!isValid())
			return new ArrayList<>();
		
		List<Package> packages = AlfrescoUMLUtils.getModels(packageElement);
		List<Model> result = new ArrayList<>();
		
		for (Package pack: packages) {
			ModelImpl model = new ModelImpl(pack);
			if (model.isValid())
				result.add(model);
		}
		
		return result;
	}
	
}
