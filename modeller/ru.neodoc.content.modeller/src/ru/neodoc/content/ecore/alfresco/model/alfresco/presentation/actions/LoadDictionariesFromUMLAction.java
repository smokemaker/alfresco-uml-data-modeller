package ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.actions;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.StaticSelectionCommandAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.uml2.uml.Model;

import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.commands.LoadDictionariesFromUMLCommand;
import ru.neodoc.content.modeller.model.AlfrescoModel;

public class LoadDictionariesFromUMLAction extends StaticSelectionCommandAction {

//	protected TransactionalEditingDomain domain = null;
	protected AlfrescoModel alfModel;
	
	protected Alfresco alfrescoRoot;
	protected Model umlModel;
	
	protected ISelection selection;
	
	@Override
	protected Command createActionCommand(EditingDomain editingDomain, Collection<?> collection) {
		if (prepareData()){
			return LoadDictionariesFromUMLCommand.create(editingDomain, collection.iterator().next(), 
					alfrescoRoot, umlModel, collection);
		}
		return UnexecutableCommand.INSTANCE;
	}

	protected boolean prepareData(){
		if (alfrescoRoot!=null && umlModel!=null)
			return true;
		
		ModelSet modelSet = findModelSet();
		if (modelSet == null)
			return false;
		
/*		if (domain == null) {
			domain = modelSet.getTransactionalEditingDomain();
		}
*/		
		for (Resource res: modelSet.getResources()){
			if (res.getContents()==null || res.getContents().size()==0)
				continue;
			EObject root = res.getContents().get(0); 
			if (umlModel == null && root instanceof Model)
				umlModel = (Model)root;
			else if (alfrescoRoot==null && root instanceof Alfresco)
				alfrescoRoot = (Alfresco)root;
		}
		
		return alfrescoRoot!=null && umlModel!=null;
	}
	
	protected ModelSet findModelSet(){
		ModelSet result = null;
		
		if (alfModel != null)
			result = alfModel.getModelSet();
		if (result != null)
			return result;
		
		if (umlModel != null) {
			if (umlModel.eResource().getResourceSet() instanceof ModelSet)
				result = (ModelSet)umlModel.eResource().getResourceSet();
		}
		if (result != null)
			return result;
		
		if (alfrescoRoot!=null){
			if (alfrescoRoot.eResource().getResourceSet() instanceof ModelSet)
				result = (ModelSet)umlModel.eResource().getResourceSet();
			if (result != null)
				return result;
			
			alfModel = AlfrescoModel.getModel(alfrescoRoot.eResource().getURI());
			if (alfModel != null)
				result = alfModel.getModelSet();
			if (result != null)
				return result;
		}
		
		return result;
	}
	
/*	public void setDomain(TransactionalEditingDomain domain) {
		this.domain = domain;
	}
*/
	public void setAlfModel(AlfrescoModel alfModel) {
		this.alfModel = alfModel;
	}

	public void setAlfrescoRoot(Alfresco alfrescoRoot) {
		this.alfrescoRoot = alfrescoRoot;
	}

	public void setUmlModel(Model umlModel) {
		this.umlModel = umlModel;
	}

	@Override
	public String getText() {
		return "Load dictionaries from UML model";
	}

	public ISelection getSelection() {
		return selection;
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
	}
	
	@Override
	public void run() {
		configureAction(selection);
		super.run();
	}
	
}
