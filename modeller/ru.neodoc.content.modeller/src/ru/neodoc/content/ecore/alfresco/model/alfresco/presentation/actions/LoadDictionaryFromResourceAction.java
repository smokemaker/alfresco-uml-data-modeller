package ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.StaticSelectionCommandAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;

import ru.neodoc.content.ecore.alfresco.model.AlfrescoModelHelper;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesImpl;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionaryImpl;
import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;
import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.commands.LoadDictionaryFromResourceCommand;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class LoadDictionaryFromResourceAction extends StaticSelectionCommandAction {

	protected IEditorPart editorPart;
	protected ISelection selection;
	protected List<Model> modelsToLoad = new ArrayList<Model>();
	protected List<IFile> modelSources = new ArrayList<IFile>();
	protected Object selectedObject = null;
	
	public LoadDictionaryFromResourceAction(){
		super();
	}
	
	public LoadDictionaryFromResourceAction(IEditorPart editorPart, ISelection selection){
	    this((IWorkbenchPart)editorPart, selection);
	}

	public LoadDictionaryFromResourceAction(IWorkbenchPart workbenchPart, ISelection selection){
		super(workbenchPart);
	}
	  
	@Override
	public void run() {
		this.modelsToLoad.clear();
		this.modelSources.clear();
		Shell parentShell = editorPart.getSite().getShell();
//		MessageBox mb = new MessageBox(parentShell);

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		IPath relativeRoot = root.getFullPath();
		if (editorPart!=null) {
			IEditorInput editorInput = editorPart.getEditorInput();
			if (editorInput instanceof IFileEditorInput){
				relativeRoot = ((IFileEditorInput)editorInput).getFile().getProject().getFullPath();
			}
				
		}
		
		Object[] objects = new Object[]{};

		if (!(selection instanceof IStructuredSelection))
			return;
		
		IStructuredSelection ssel = (IStructuredSelection) selection;
		
		if (ssel.getFirstElement() instanceof DictionaryImpl){
			
			ResourceListSelectionDialog dialog = new ResourceListSelectionDialog(parentShell, root, IResource.FILE);
			
			dialog.open();
			objects = dialog.getResult();
		} else if (ssel.getFirstElement() instanceof DictionariesImpl){
			ResourceSelectionDialog dialog = new ResourceSelectionDialog(parentShell, root, "Select resources to load:");
			dialog.open();
			objects = dialog.getResult();
		}
		
		if (objects.length<1)
			return;
		for (Object obj: objects){
			IFile f = (IFile)obj;
			Model model;
			try {
				model = JaxbUtils.read(f);
				this.modelsToLoad.add(model);
				this.modelSources.add(f);
			} catch (Exception e) {
				AlfrescoEditorPlugin.INSTANCE.log(e);
			}
		}
		
		configureAction(selection);
		super.run();
	}
	
	@Override
	public String getText() {
		/* FIXME externalize */
		return "Load from resource \u2026";
	}

	public IEditorPart getEditorPart() {
		return editorPart;
	}

	public void setEditorPart(IEditorPart editorPart) {
		this.editorPart = editorPart;
	}

	public ISelection getSelection() {
		return selection;
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
	}

	@Override
	protected Command createActionCommand(EditingDomain editingDomain, 
			Collection<?> collection) {
	    if (collection.size() == 1)
	    {
	      Object owner = collection.iterator().next();
	      return LoadDictionaryFromResourceCommand.create(editingDomain, owner, 
	    		  AlfrescoModelHelper.getLoadedModels(modelsToLoad, modelSources), 
	    		  collection);
	    }
	    return UnexecutableCommand.INSTANCE;	
	}

	public Object getSelectedObject() {
		return selectedObject;
	}

	public void setSelectedObject(Object selectedObject) {
		this.selectedObject = selectedObject;
	}
	
}
