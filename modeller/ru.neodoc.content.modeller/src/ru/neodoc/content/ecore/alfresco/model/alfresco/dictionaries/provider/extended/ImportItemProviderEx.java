package ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.provider.extended;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.provider.ImportItemProvider;
import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.commands.IgnoreUndefinedImportCommand;

public class ImportItemProviderEx extends ImportItemProvider {

	public ImportItemProviderEx(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	

	@Override
	public Command createCommand(Object object, EditingDomain domain,
			Class<? extends Command> commandClass,
			CommandParameter commandParameter) {

		Command result = UnexecutableCommand.INSTANCE;
		
		CommandParameter oldCommandParameter = commandParameter;
	    commandParameter = unwrapCommandValues(commandParameter, commandClass);

	    if (commandClass == IgnoreUndefinedImportCommand.class) {
//	    	CommandParameter loadFromResourceParameter = (CommandParameter)commandParameter.getValue();
	    	CommandParameter ignoreUndefinedImportParameter = commandParameter;
	        result = 
	        		createIgnoreUndefinedImportCommand
	                  (domain,
	                   commandParameter.getEOwner(), 
	                   ignoreUndefinedImportParameter.getEStructuralFeature(), 
	                   ignoreUndefinedImportParameter.getValue(),
	                   ignoreUndefinedImportParameter.getIndex(),
	                   commandParameter.getCollection());      
	        return wrapCommand(result, object, commandClass, commandParameter, oldCommandParameter);	    	
		}
		
		
		return super.createCommand(object, domain, commandClass, commandParameter);
	}

	  protected Command createIgnoreUndefinedImportCommand
	    (EditingDomain domain, EObject owner, EStructuralFeature feature, Object value, int index, Collection<?> collection)
	  {
	    if (feature instanceof EReference && value instanceof EObject)
	    {
	      return createIgnoreUndefinedImportCommand(domain, owner, (EReference)feature, (EObject)value, index, collection);
	    }
	    return new IgnoreUndefinedImportCommand(domain, owner, feature, value, index, collection);
	  }
	
	
}
