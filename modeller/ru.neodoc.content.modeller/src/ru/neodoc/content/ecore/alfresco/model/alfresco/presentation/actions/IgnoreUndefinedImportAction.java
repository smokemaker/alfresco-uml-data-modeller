package ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.actions;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.StaticSelectionCommandAction;
import org.eclipse.jface.viewers.ISelection;

import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import;
import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.commands.IgnoreUndefinedImportCommand;

public class IgnoreUndefinedImportAction extends StaticSelectionCommandAction {

	protected Import selectedObject;
	protected ISelection selection;
	
	@Override
	protected Command createActionCommand(EditingDomain editingDomain,
			Collection<?> collection) {
	    if (collection.size() == 1)
	    {
	      Object owner = collection.iterator().next();
	      return IgnoreUndefinedImportCommand.create(editingDomain, owner, collection);
	    }
	    return UnexecutableCommand.INSTANCE;	}

	@Override
	public void run() {
		configureAction(selection);
		super.run();
	}
	
	public Import getSelectedObject() {
		return selectedObject;
	}

	public void setSelectedObject(Import selectedObject) {
		this.selectedObject = selectedObject;
	}

	public ISelection getSelection() {
		return selection;
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return "Ignore undefined import";
	}
	
}
