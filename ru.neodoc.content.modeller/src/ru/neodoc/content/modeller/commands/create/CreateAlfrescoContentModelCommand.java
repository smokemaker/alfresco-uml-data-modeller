/*****************************************************************************
 * Copyright (c) 2010 CEA LIST.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Tatiana Fesenko (CEA LIST) - Initial API and implementation
 *
 *****************************************************************************/
package ru.neodoc.content.modeller.commands.create;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.papyrus.infra.core.resource.IModel;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.uml.diagram.common.commands.ModelCreationCommandBase;
import org.eclipse.papyrus.uml.tools.utils.PackageUtil;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLFactory;

import ru.neodoc.content.modeller.model.AlfrescoModel;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;


/**
 * The Class CreateAlfrescoContentModelCommand.
 */
public class CreateAlfrescoContentModelCommand extends ModelCreationCommandBase {

	public static final String COMMAND_ID = "alfresco";

	/**
	 * @see org.eclipse.papyrus.infra.core.extension.commands.ModelCreationCommandBase#createRootElement()
	 * 
	 * @return
	 */

	@Override
	protected EObject createRootElement() {
		return UMLFactory.eINSTANCE.createModel();
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.extension.commands.ModelCreationCommandBase#initializeModel(org.eclipse.emf.ecore.EObject)
	 * 
	 * @param owner
	 */

	@Override
	protected void initializeModel(EObject owner) {
		super.initializeModel(owner);
		
		ResourceSet rs = owner.eResource().getResourceSet();
		ModelSet ms = rs instanceof ModelSet?(ModelSet)rs:null; 
		
		if (ms == null)
			return;
		
		IModel imodel = ms.getModel(AlfrescoModel.MODEL_ID);
		AlfrescoModel model = null;
		if ((imodel!=null) && (imodel instanceof AlfrescoModel))
			model = (AlfrescoModel)imodel;
		
		if (model == null)
			return;
		
		// create empty content for alfresco model
		model.getResource().getContents().add(model.createInitialModel());
		
		((org.eclipse.uml2.uml.Package)owner).setName(getModelName());

		// Retrieve Alfresco profile and apply with Sub-profile
		Profile alfresco = AlfrescoProfile.getProfile(owner.eResource().getResourceSet());
		if(alfresco != null) {
			PackageUtil.applyProfile(((org.eclipse.uml2.uml.Package)owner), alfresco, true);
			org.eclipse.uml2.uml.Model umlModel = (org.eclipse.uml2.uml.Model)owner; 
			umlModel.applyStereotype(
				umlModel.getAppliedProfile("alfresco").getOwnedStereotype("alfresco")	
			);
//			owner.
		}
	}

	/**
	 * Gets the model name.
	 * 
	 * @return the model name
	 */
	protected String getModelName() {
		return "AlfrescoContentModel";
	}
}
