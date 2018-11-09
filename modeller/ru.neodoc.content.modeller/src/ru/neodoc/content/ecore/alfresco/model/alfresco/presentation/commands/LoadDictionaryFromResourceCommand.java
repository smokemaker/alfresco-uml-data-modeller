package ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Namespaces.Namespace;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandActionDelegate;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ru.neodoc.content.ecore.alfresco.model.AlfrescoModelHelper;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionaries;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import;

public class LoadDictionaryFromResourceCommand extends CompoundCommand implements
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
              List<AlfrescoModelHelper.LoadedModelInfo> modelsInfo,
              Collection<?> selection){
		return domain.createCommand(LoadDictionaryFromResourceCommand.class, 
				new CommandParameter(owner, null, modelsInfo, new ArrayList<Object>(selection))
				);
	}
	
	public LoadDictionaryFromResourceCommand(EditingDomain domain,
			EObject owner,
			EStructuralFeature feature,
			List<Object> modelInfo,
			int index,
			Collection<?> selection){
		super();

		this.domain = domain;
	    this.owner = owner;
	    this.feature = feature;
	    this.index = index;
	    this.selection = selection == null ? Collections.EMPTY_LIST : selection;
		
	    for (Object obj: modelInfo){
	    	if (obj instanceof AlfrescoModelHelper.LoadedModelInfo)
	    		this.modelInfoList.add((AlfrescoModelHelper.LoadedModelInfo)obj);
	    }
		
	    // If we're creating a child under an object in a feature map, the selection will be the feature map entry.
	    // We want to replace it with the model object.
	    //
	    if (this.selection.size() == 1) {
	    	Object selObject = this.selection.iterator().next();
	    	if (selObject instanceof FeatureMap.Entry && ((FeatureMap.Entry)selObject).getValue() == owner) {
	    		this.selection = Collections.singletonList(owner);
	    	}
	    }
		
	    if (owner instanceof Dictionary)
	    	fillCommandListForDictionary();
	    else if (owner instanceof Dictionaries)
	    	fillCommandListForDictionaries();
	    
	}

	protected void fillCommandListForDictionaries(){
		Dictionaries dictionaries = (Dictionaries)owner;
		List<Dictionary> dictionariesToAdd = new ArrayList<Dictionary>();
		Dictionary dictionary = null;
		
		ArrayList<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace> namespacesToAdd = 
				new ArrayList<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace>();
		ArrayList<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import> importsToAdd = 
				new ArrayList<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import>();
		
		for (AlfrescoModelHelper.LoadedModelInfo modelInfo: modelInfoList) {
			Model modelToLoad = modelInfo.getModel();
			IFile modelFile = modelInfo.getSource();
			
			// Create and fill Dictionary object
			dictionary = AlfrescoModelHelper.newDictionary();
			dictionary.setLocation(modelFile.getFullPath().toString());
			dictionary.setName(modelToLoad.getName()!=null && modelToLoad.getName().trim().length()>0
						?modelToLoad.getName()
						:modelFile.getName());
			
			dictionariesToAdd.add(dictionary);
			
			// Add new Namespace objects
			if (modelToLoad.getNamespaces()!=null) {
				for (Namespace ns: modelToLoad.getNamespaces().getNamespace()){
					namespacesToAdd.add(AlfrescoModelHelper.newNamespace(dictionary, ns.getPrefix(), ns.getUri()));
				}
			}
			
			// Add new Import objects
			if (modelToLoad.getImports()!=null) {
				for (org.alfresco.model.dictionary._1.Model.Imports.Import imp: modelToLoad.getImports().getImport()){
					importsToAdd.add(AlfrescoModelHelper.newImport(dictionary, imp.getPrefix(), imp.getUri()));
				}
			};
			
		}


		for (Dictionary dict: dictionariesToAdd){
			
			// Delete the same dictionary if exists
			for (Dictionary d: dictionaries.getDictionaries())
				if (d.getLocation().equals(dict.getLocation())) {
					DeleteCommand delete = new DeleteCommand(domain, Collections.singletonList(d));
					append(delete);
				}
		}

		// Add dictionaries
		AddCommand add = new AddCommand(domain, dictionaries, 
				DictionariesPackage.Literals.DICTIONARIES__DICTIONARIES, 
				dictionariesToAdd);
		
		append(add);
		
		List<Import> allImports = new ArrayList<Import>();  
		List<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace> allNamespaces 
			= new ArrayList<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace>();
		
		AlfrescoModelHelper.collectImportsAndNamespaces((Dictionaries)owner, 
				Collections.<Dictionary>emptyList(), 
				allImports, 
				allNamespaces);
		
		allImports.addAll(importsToAdd);
		allNamespaces.addAll(namespacesToAdd);
		
		List<Import> undefinedImports = AlfrescoModelHelper.findUndefinedImports(allImports, allNamespaces);
		SetCommand updateImport;
		for (Import importToChange: allImports) {
			if (undefinedImports.contains(importToChange)){
				if (importsToAdd.contains(importToChange))
					importToChange.setIsUndefined(true);
				else if (!importToChange.isIsUndefined()) 
				{
					updateImport = new SetCommand(domain, importToChange, 
							DictionariesPackage.Literals.IMPORT__IS_UNDEFINED, 
							new Boolean(true));
					append(updateImport);
				}
					
			} else if (!importsToAdd.contains(importToChange) && importToChange.isIsUndefined()) {
				updateImport = new SetCommand(domain, importToChange, 
						DictionariesPackage.Literals.IMPORT__IS_UNDEFINED, 
						new Boolean(false));
				append(updateImport);
			}
		}
	
	
	}
	
	
	protected void fillCommandListForDictionary(){
		Dictionary dictionary = (Dictionary)owner;
		for (AlfrescoModelHelper.LoadedModelInfo modelInfo: modelInfoList){
			
			Model modelToLoad = modelInfo.getModel();
			IFile modelFile = modelInfo.getSource();
			// Create delete commands for existing Namespace objects
			for (ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace ns: dictionary.getNamespaces()){
				DeleteCommand delete = new DeleteCommand(domain, Collections.singletonList(ns));
				append(delete);
			}
	
			// Create delete commands for existing Import objects
			for (ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import imp: dictionary.getImports()){
				DeleteCommand delete = new DeleteCommand(domain, Collections.singletonList(imp));
				append(delete);
			}
			
			
			// Add new Namespace objects
			ArrayList<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace> namespacesToAdd = 
					new ArrayList<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace>();
			if (modelToLoad.getNamespaces()!=null) {
				for (Namespace ns: modelToLoad.getNamespaces().getNamespace()){
					namespacesToAdd.add(AlfrescoModelHelper.newNamespace(null, ns.getPrefix(), ns.getUri()));
				}
				if (namespacesToAdd.size() > 0) {
					AddCommand add = new AddCommand(domain, dictionary, 
							DictionariesPackage.Literals.DICTIONARY__NAMESPACES, 
							namespacesToAdd);
					append(add);
				}
			}
			// Add new Import objects
			ArrayList<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import> importsToAdd = 
					new ArrayList<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import>();
			if (modelToLoad.getImports()!=null) {
				for (org.alfresco.model.dictionary._1.Model.Imports.Import imp: modelToLoad.getImports().getImport()){
					importsToAdd.add(AlfrescoModelHelper.newImport(null, imp.getPrefix(), imp.getUri()));
				}
				if (importsToAdd.size() > 0) {
					AddCommand add = new AddCommand(domain, dictionary, 
							DictionariesPackage.Literals.DICTIONARY__IMPORTS, 
							importsToAdd);
					append(add);
				}
			};
			
			List<Import> allImports = new ArrayList<Import>();  
			List<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace> allNamespaces 
				= new ArrayList<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace>();
			AlfrescoModelHelper.collectImportsAndNamespaces((Dictionaries)owner.eContainer(), 
					Collections.singletonList((Dictionary)owner), 
					allImports, 
					allNamespaces);
			
			allImports.addAll(importsToAdd);
			allNamespaces.addAll(namespacesToAdd);
			
			List<Import> undefinedImports = AlfrescoModelHelper.findUndefinedImports(allImports, allNamespaces);
			SetCommand updateImport;
			for (Import importToChange: allImports) {
				if (undefinedImports.contains(importToChange)){
					if (importsToAdd.contains(importToChange))
						importToChange.setIsUndefined(true);
					else if (!importToChange.isIsUndefined()) 
					{
						updateImport = new SetCommand(domain, importToChange, 
								DictionariesPackage.Literals.IMPORT__IS_UNDEFINED, 
								new Boolean(true));
						append(updateImport);
					}
						
				} else if (!importsToAdd.contains(importToChange) && importToChange.isIsUndefined()) {
					updateImport = new SetCommand(domain, importToChange, 
							DictionariesPackage.Literals.IMPORT__IS_UNDEFINED, 
							new Boolean(false));
					append(updateImport);
				}
			}
				
			
			// Set Dictionary properties
			SetCommand set = new SetCommand(domain, dictionary,
					DictionariesPackage.Literals.DICTIONARY__LOCATION, 
					modelFile.getFullPath().toString());
			append(set);
			set = new SetCommand(domain, dictionary,
					DictionariesPackage.Literals.DICTIONARY__NAME, 
					(modelToLoad.getName()!=null && modelToLoad.getName().trim().length()>0
						?modelToLoad.getName()
						:modelFile.getName())
					);
			append(set);
		
		}
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
	
/*
	Dictionary current = (Dictionary)((IStructuredSelection)selection).getFirstElement();
	current.setLocation(f.getLocationURI().toString());
	current.setName(f.getName());
	
	current.getNamespaces().clear();
	for (Namespace ns: model.getNamespaces().getNamespace()){
		AlfrescoModelHelper.newNamespace(current, ns.getPrefix(), ns.getUri());
	}
	*/
	
}
