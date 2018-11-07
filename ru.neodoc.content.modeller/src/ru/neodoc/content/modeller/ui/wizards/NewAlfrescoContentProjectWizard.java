package ru.neodoc.content.modeller.ui.wizards;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.infra.core.resource.IModel;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationModel;
import org.eclipse.papyrus.infra.gmfdiag.css.resource.CSSNotationModel;
import org.eclipse.papyrus.uml.diagram.wizards.wizards.NewPapyrusProjectWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.uml2.uml.Model;

import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.modeller.commands.create.FillAlfrescoFromUmlTemplateCommand;
import ru.neodoc.content.modeller.model.AlfrescoModel;


/**
 * The Class NewSysMLProjectWizard.
 */
public class NewAlfrescoContentProjectWizard extends NewPapyrusProjectWizard {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		setWindowTitle("New Alfresco Content Project");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected WizardNewProjectCreationPage createNewProjectCreationPage() {
		WizardNewProjectCreationPage newProjectPage = super.createNewProjectCreationPage();
		newProjectPage.setTitle("Papyrus Alfresco Content Project");
		newProjectPage.setDescription("Create a New Alfresco Content Project");
		return newProjectPage;
	}

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
	@Override
	protected void saveDiagramCategorySettings() {
		//do nothing
		//here SysML is the only available category
	}

/*	*//**
	 * {@inheritDoc}
	 *//*
	@Override
	protected String[] getDiagramCategoryIds() {
		return new String[]{CreateAlfrescoContentModelCommand.COMMAND_ID, "uml"};
	}
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
/*	@Override
	protected void createPapyrusModels(ModelSet modelSet, URI newURI) {
		IModel model = new AlfrescoModel();
		modelSet.registerModel(model);
		
		// for a snippet
		//model.addModelSnippet(snippet);
		
		// for a set snippet
		//diResourceSet.addModelSetSnippet(snippet)
		
		// call the parent
		super.createPapyrusModels(modelSet, newURI);
	}*/
	
	@Override
	protected void createPapyrusModels(ModelSet modelSet, URI newURI) {
		// TODO Auto-generated method stub
		IModel model = new AlfrescoModel();
		modelSet.registerModel(model);

		super.createPapyrusModels(modelSet, newURI);
	}
	
	@Override
	protected void initDomainModelFromTemplate(ModelSet modelSet) {
		super.initDomainModelFromTemplate(modelSet);
		
		Model umlModel = null;
		Alfresco alfModel = null;
		
		for (Resource res: modelSet.getResources()){
			EObject root = res.getContents().get(0); 
			if (root instanceof Model)
				umlModel = (Model)root;
		}

		AlfrescoModel alfrescoModel = (AlfrescoModel)modelSet.getModel(AlfrescoModel.MODEL_ID); 
		if (alfrescoModel!=null) {
			if (alfrescoModel.getResource().getContents().isEmpty())
				alfrescoModel.getResource().getContents().add(alfrescoModel.createInitialModel());
			alfModel = (Alfresco)alfrescoModel.getResource().getContents().get(0);
		}
		
		if (umlModel!=null && alfModel!=null) {
			
			CompoundCommand composite = new CompoundCommand("initDomainModelFromTemplate");
			
			composite.append(
					new FillAlfrescoFromUmlTemplateCommand(
							modelSet.getTransactionalEditingDomain(), 
							alfModel, 
							umlModel));
			getCommandStack(modelSet).execute(composite);
			
		}
		
	}
	
	@Override
	protected void initDiagrams(ModelSet resourceSet, EObject root,
			String categoryId) {
		super.initDiagrams(resourceSet, root, categoryId);

		CSSNotationModel cssModel = null;
		IModel nm = resourceSet.getModel(NotationModel.MODEL_ID);
		if (nm instanceof CSSNotationModel)
			cssModel = (CSSNotationModel)nm;
		
		Alfresco alfModel = null;
		for (Resource res: resourceSet.getResources()){
			if (res.getContents()!=null){
				EObject rRoot = res.getContents().get(0); 
				if (rRoot instanceof Alfresco)
					alfModel = (Alfresco)rRoot;
			}
		}
		
/*		getCommandStack(resourceSet).execute(
				new UpdateCssInDiagrams(
						resourceSet.getTransactionalEditingDomain(), 
						resourceSet, 
						alfModel.getDefaultCss())
				);
*/		
	
	}
	
}
