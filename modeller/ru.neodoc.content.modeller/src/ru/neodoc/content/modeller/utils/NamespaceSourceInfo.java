package ru.neodoc.content.modeller.utils;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace;
import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;

public class NamespaceSourceInfo{
	
	public List<Namespace> namespaces = new ArrayList<Namespace>();
	public String dictionaryName;
	public String dictionaryLocation;
	public Model model;
	
	public NamespaceSourceInfo(){
		
	}

	public NamespaceSourceInfo(Dictionary dictionary){
		dictionaryName = dictionary.getName();
		dictionaryLocation = dictionary.getLocation();
	}
	
	public boolean loadModel(){
		
		model = null;
		if (dictionaryLocation==null || dictionaryLocation.trim().length()==0)
			return false;
		
		try {
			IPath path = new Path(dictionaryLocation);
			IPath fsPath = ResourcesPlugin.getWorkspace().getRoot().getFile(path).getRawLocation(); 
			IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(fsPath);
			
			if (files.length==0)
				return false;
			
			IFile f = (IFile)files[0];
			
			model = JaxbUtils.read(f);
		} catch (Exception e) {
			AlfrescoEditorPlugin.INSTANCE.log(e);
			return false;
		}
		
		return isLoaded();
	}
	
	public boolean isLoaded(){
		return model!=null;
	} 
}
