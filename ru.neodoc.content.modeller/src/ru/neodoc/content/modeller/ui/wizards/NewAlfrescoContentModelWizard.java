/*****************************************************************************
on * Copyright (c) 2010 CEA LIST.
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
package ru.neodoc.content.modeller.ui.wizards;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.infra.core.resource.IModel;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.papyrus.uml.diagram.wizards.pages.SelectRepresentationKindPage;
import org.eclipse.papyrus.uml.diagram.wizards.wizards.CreateModelWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.uml2.uml.Model;

import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.modeller.ContentModellerPlugin;
import ru.neodoc.content.modeller.commands.create.FillAlfrescoFromUmlTemplateCommand;
import ru.neodoc.content.modeller.commands.create.InitFromTemplateCommand;
import ru.neodoc.content.modeller.commands.create.MarkTemplateModels;
import ru.neodoc.content.modeller.model.AlfrescoModel;
import ru.neodoc.content.modeller.model.css.ModelCSSHelper;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;


/**
 * The Class NewSysMLModelWizard.
 */
public class NewAlfrescoContentModelWizard extends CreateModelWizard 
	implements InitFromTemplateCommand.ResourceSetKeeper {

	protected ModelSet cachedResourceSet = null;
	
	protected ResourceSet keptResourceSet = null;
	
	protected SelectRepresentationKindPage localSelectRepresentationKindPage = null;
	
	protected ModelCSSHelper cssHelper;
	
	@Override
	public void set(ResourceSet resourceSet) {
		keptResourceSet = resourceSet;
	}
	
	/**
	 * Instantiates a new new sys ml model wizard.
	 */
	public NewAlfrescoContentModelWizard() {
		super();
//		setWindowTitle(newTitle);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		setWindowTitle("New Alfresco Content Model");
	}

	/**
	 * {@inheritDoc}
	 */
/*	@Override
	protected NewModelFilePage createNewModelFilePage(IStructuredSelection selection) {
		NewModelFilePage page = super.createNewModelFilePage(selection);
		if (page != null) {
			page.setTitle("Papyrus Alfresco Content Model");
			page.setDescription("Create a New Alfresco Content Model");
		}
		return page;
	}*/
	
	/**
	 * {@inheritDoc}
	 */
/*	@Override
	protected String[] getDiagramCategoryIds() {
		return new String[]{CreateAlfrescoContentModelCommand.COMMAND_ID, "uml"};
	}
*/
	/**
	 * {@inheritDoc}
	 */
/*	@Override
	protected SelectDiagramCategoryPage createSelectDiagramCategoryPage() {
		//here SysML is the only available category
		return null;
	}
*/	
	/**
	 * {@inheritDoc}
	 */
/*	@Override
	protected void saveDiagramCategorySettings() {
		//here SysML is the only available category
	}
*/

	/**
	 * SINCE 3.0 it's done via org.eclipse.papyrus.infra.architecture.commandProviders
	 */
/*	@Override
	protected void createEmptyDomainModel(ModelSet modelSet,
			String diagramCategoryId) {
		try {
			IModelCreationCommand creationCommand = getDiagramCategoryMap().get(diagramCategoryId).getCommand();
					new CreateAlfrescoContentModelCommand();
			creationCommand.createModel(modelSet);
		} catch (Exception e) {
			log.error(e);
		}
	}
*/	
	/**
	 * We don't register the model with extensiion point, as it will be applied to all created Papyrus models
	 * We just add the model "on the fly" 
	 */
	@Override
	protected void createPapyrusModels(ModelSet modelSet, URI newURI) {
		IModel model = new AlfrescoModel();
		modelSet.registerModel(model);
		// and load profile
		AlfrescoProfile.getProfile(modelSet);
		super.createPapyrusModels(modelSet, newURI);

		cachedResourceSet = modelSet;
	}

/*	protected SelectDiagramKindPage getSelectDiagramKindPage(){
		IWizardPage[] pages = getPages();
		for (int i=0; i<pages.length; i++)
			if (pages[i] instanceof SelectDiagramKindPage)
				return (SelectDiagramKindPage)pages[i];
		return null;
				
	}
*/	
	@Override
	protected SelectRepresentationKindPage doCreateSelectRepresentationKindPage() {
		localSelectRepresentationKindPage = super.doCreateSelectRepresentationKindPage();
		return localSelectRepresentationKindPage;
	}
	
	@Override
	protected void initDomainModel(ModelSet modelSet, String contextId, String[] viewpointIds) {
		
		cssHelper = new ModelCSSHelper(modelSet);
		
		super.initDomainModel(modelSet, contextId, viewpointIds);
		
		// apply CSS (empty, default or from template) to model
		// the CSS file is prepared in createEmptyDomainModel or initDomainModelFromTemplate
		cssHelper.setDefaultCSSToAlfrescoModel();
		
	}
	
	@Override
	protected void createEmptyDomainModel(ModelSet modelSet, String contextId, String[] viewpointIds) {
		super.createEmptyDomainModel(modelSet, contextId, viewpointIds);
		cssHelper.initEmptyModel();
	}
	
	@Override
	protected void initDomainModelFromTemplate(ModelSet modelSet) {
		// super.initDomainModelFromTemplate(modelSet);
		
		/* BEGIN: modified super.initDomainModelFromTemplate(modelSet) */
		SelectRepresentationKindPage page = localSelectRepresentationKindPage;
		
		InitFromTemplateCommand iftc = new InitFromTemplateCommand(modelSet.getTransactionalEditingDomain(), 
				modelSet, page.getTemplatePluginId(), 
				page.getTemplatePath(), 
				page.getNotationTemplatePath(), 
				page.getDiTemplatePath());
		iftc.setResourceSetKeeper(this);
		
		getCommandStack(modelSet).execute(iftc);
		/* END */

		Model umlModel = null;
		Alfresco alfModel = null;
		
		for (Resource res: modelSet.getResources()){
			EObject root = (res.getContents()==null)||(res.getContents().isEmpty())?null:res.getContents().get(0); 
			if (root instanceof Model)
				umlModel = (Model)root;
		}

		AlfrescoModel alfrescoModel = (AlfrescoModel)modelSet.getModel(AlfrescoModel.MODEL_ID); 
		if (alfrescoModel!=null) {
			try {
				alfModel = (Alfresco)alfrescoModel.getResource().getContents().get(0);
			} catch (Exception e) {
				ContentModellerPlugin.getDefault().log(e);
			}
		}
		
		
		if (umlModel!=null && alfModel!=null) {
			
			CompoundCommand composite = new CompoundCommand("initDomainModelFromTemplate");
			
			composite.append(
						new MarkTemplateModels(modelSet.getTransactionalEditingDomain(), umlModel)
					);
			
			composite.append(
					new FillAlfrescoFromUmlTemplateCommand(
							modelSet.getTransactionalEditingDomain(), 
							alfModel, 
							umlModel));
			
			getCommandStack(modelSet).execute(composite);
			
			cssHelper.initModelFromTemplate(
					page.getTemplatePluginId(),
					page.getTemplatePath().substring(0, page.getTemplatePath().lastIndexOf("."))					
				);
		}
	}

	 @Override
	protected void initDiagramModel(ModelSet modelSet, String categoryId) {
		super.initDiagramModel(modelSet, categoryId);
	}
	
	@Override
	protected void initDiagrams(ModelSet resourceSet, EObject root,
			String categoryId) {
		super.initDiagrams(resourceSet, root, categoryId);
		
/*		CSSNotationModel cssModel = null;
		IModel nm = resourceSet.getModel(NotationModel.MODEL_ID);
		if (nm instanceof CSSNotationModel)
			cssModel = (CSSNotationModel)nm;
*/
/*		getCommandStack(resourceSet).execute(
					new SetDefaultCSSCommand(resourceSet.getTransactionalEditingDomain(), resourceSet)
				);
		
			
		Alfresco alfresco = (Alfresco)AlfrescoModelUtils.getAlfrescoRootObject(resourceSet);
		if ((alfresco!=null) && (alfresco.getDefaultCss()!=null))
			getCommandStack(resourceSet).execute(
					new UpdateCssInDiagrams(
							resourceSet.getTransactionalEditingDomain(), 
							resourceSet, 
							alfresco.getDefaultCss())
					);
		
*/	}	

	@Override
	protected void openDiagram(URI newURI) {
		// here we can close cached resourceset
		if (this.keptResourceSet != null)
			EMFHelper.unload(this.keptResourceSet);
		
		super.openDiagram(newURI);
	}
	
}
