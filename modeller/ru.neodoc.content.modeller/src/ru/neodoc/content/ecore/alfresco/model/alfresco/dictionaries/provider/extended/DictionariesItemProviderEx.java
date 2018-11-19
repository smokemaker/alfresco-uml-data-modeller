package ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.provider.extended;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.provider.DictionariesItemProvider;
import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.commands.LoadDictionariesFromUMLCommand;
import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.commands.LoadDictionaryFromResourceCommand;

public class DictionariesItemProviderEx extends DictionariesItemProvider {
	
	public DictionariesItemProviderEx(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	
	@Override
	public Command createCommand(Object object, EditingDomain domain,
			Class<? extends Command> commandClass,
			CommandParameter commandParameter) {

		Command result = UnexecutableCommand.INSTANCE;
		
		CommandParameter oldCommandParameter = commandParameter;
	    commandParameter = unwrapCommandValues(commandParameter, commandClass);

	    if (commandClass == LoadDictionaryFromResourceCommand.class) {
//	    	CommandParameter loadFromResourceParameter = (CommandParameter)commandParameter.getValue();
	    	CommandParameter loadFromResourceParameter = commandParameter;
	        result = 
	        		createLoadDictionaryFromResourceCommand
	                  (domain,
	                   commandParameter.getEOwner(), 
	                   loadFromResourceParameter.getEStructuralFeature(), 
	                   loadFromResourceParameter.getValue(),
	                   loadFromResourceParameter.getIndex(),
	                   commandParameter.getCollection());      
	        return wrapCommand(result, object, commandClass, commandParameter, oldCommandParameter);	    	
		} else 
	    if (commandClass == LoadDictionariesFromUMLCommand.class) {
	    	CommandParameter loadFromUMLParameter = commandParameter;
	    	result = createLoadDictionariesFromUMLCommand(
	    			domain, 
	    			commandParameter.getEOwner(), 
	    			loadFromUMLParameter.getEStructuralFeature(), 
	    			loadFromUMLParameter.getValue(),
	    			loadFromUMLParameter.getIndex(),
	                commandParameter.getCollection());
	    	return wrapCommand(result, object, commandClass, commandParameter, oldCommandParameter);
	    }

	    return super.createCommand(object, domain, commandClass, commandParameter);
	}
	
	  @SuppressWarnings("unchecked")
	protected Command createLoadDictionaryFromResourceCommand
	    (EditingDomain domain, EObject owner, EStructuralFeature feature, Object value, int index, Collection<?> collection)
	  {
	    if (feature instanceof EReference && value instanceof EObject)
	    {
	      return createLoadDictionaryFromResourceCommand(domain, owner, (EReference)feature, (EObject)value, index, collection);
	    }
	    return new LoadDictionaryFromResourceCommand(domain, owner, feature, (List<Object>)value, index, collection);
	  }

	  @SuppressWarnings("unchecked")
	protected Command createLoadDictionariesFromUMLCommand
	    (EditingDomain domain, EObject owner, EStructuralFeature feature, Object value, int index, Collection<?> collection)
	  {
	    if (feature instanceof EReference && value instanceof EObject)
	    {
	      return createLoadDictionariesFromUMLCommand(domain, owner, (EReference)feature, (EObject)value, index, collection);
	    }
	    return new LoadDictionariesFromUMLCommand(domain, owner, feature, (List<Object>)value, index, collection);
	  }
	

}
