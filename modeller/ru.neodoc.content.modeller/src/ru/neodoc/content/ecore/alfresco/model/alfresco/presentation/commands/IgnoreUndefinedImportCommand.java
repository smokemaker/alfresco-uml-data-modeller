package ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandActionDelegate;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ru.neodoc.content.ecore.alfresco.model.AlfrescoModelHelper;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import;

public class IgnoreUndefinedImportCommand extends CompoundCommand implements
		CommandActionDelegate {

	 /**
	   * This value is used to indicate that an optional positional index
	   * indicator is unspecified.
	   * @deprecated As of EMF 2.0, use {@link CommandParameter#NO_INDEX}, whose
	   * value is equal to this, instead.
	   */
	  @Deprecated
	  protected static final int NO_INDEX = CommandParameter.NO_INDEX;
	
	  /**
	   * This is the editing domain in which this command operates.
	   */
	  protected EditingDomain domain;
	
	  /**
	   * This is the object to which the child will be added.
	   */
	  protected EObject owner;
	
	  /**
	   * This is the feature of the owner to which the child will be added.
	   */
	  protected EStructuralFeature feature;
	
	  /**
	   * This is the index for the new object's position under the feature.
	   */
	  protected int index;
	
	  /**
	   * This is the value to be returned by {@link #getAffectedObjects}. 
	   * The affected objects are different after an execute or redo from after
	   * an undo, so we record them.
	   */
	  protected Collection<?> affectedObjects;
	
	  /**
	   * This is the collection of objects that were selected when this command
	   * was created.  After an undo, these are considered the affected objects.
	   */
	  protected Collection<?> selection;
	
	
	protected List<AlfrescoModelHelper.LoadedModelInfo> modelInfoList = new ArrayList<AlfrescoModelHelper.LoadedModelInfo>();
	
	public static Command create(EditingDomain domain, Object owner, 
              Collection<?> selection){
		return domain.createCommand(IgnoreUndefinedImportCommand.class, 
				new CommandParameter(owner, null, null, new ArrayList<Object>(selection))
				);
	}
	
	public IgnoreUndefinedImportCommand(EditingDomain domain,
			EObject owner,
			EStructuralFeature feature,
			Object object,
			int index,
			Collection<?> selection){
		super();

		SetCommand updateImport = new SetCommand(domain, (Import)owner, 
				DictionariesPackage.Literals.IMPORT__IS_IGNORED, 
				new Boolean(true));
		append(updateImport);
	}

	public Object getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getToolTipText() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
