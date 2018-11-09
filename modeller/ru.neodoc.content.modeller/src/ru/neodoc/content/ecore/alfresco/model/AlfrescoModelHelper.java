package ru.neodoc.content.ecore.alfresco.model;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.eclipse.core.resources.IFile;

import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionaries;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesFactory;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace;

public class AlfrescoModelHelper {
	
	public static String DEFAULT_CSS_LOCATION = "model/alfresco.css";
	
	public static class LoadedModelInfo{
		protected Model model;
		protected IFile source;
		
		public LoadedModelInfo(Model model, IFile source){
			LoadedModelInfo.this.model = model;
			LoadedModelInfo.this.source = source;
		}

		public Model getModel() {
			return model;
		}

		public IFile getSource() {
			return source;
		}
		
	}
	
	public static List<LoadedModelInfo> getLoadedModels(List<Model> models, List<IFile> sources){
		List<LoadedModelInfo> result = new ArrayList<AlfrescoModelHelper.LoadedModelInfo>();
		int count = Math.min(models.size(), sources.size());
		for (int i=0; i<count; i++) {
			result.add(new LoadedModelInfo(models.get(i), sources.get(i)));
		}
		return result;
	}
	
	public static AlfrescoPackage getAlfrescoPackage(){
		return AlfrescoPackage.eINSTANCE;
	}
	
	public static DictionariesPackage getDictionariesPackage(){
		return DictionariesPackage.eINSTANCE;
	}
	
	public static DictionariesFactory getDictionariesFactory(){
		return getDictionariesPackage().getDictionariesFactory();
	}
	
	public static Dictionaries getDictinaries(Alfresco alfresco){
		if (alfresco.getDictionaries() == null){
			Dictionaries dict = getDictionariesFactory().createDictionaries();
			alfresco.setDictionaries(dict);
		}
		return alfresco.getDictionaries();
	}
	
	public static Dictionary newDictionary(){
		return getDictionariesFactory().createDictionary();
	}

	public static Dictionary newDictionary(Alfresco alfresco){
		Dictionaries dicts = getDictinaries(alfresco);
		Dictionary d = getDictionariesFactory().createDictionary();
		dicts.getDictionaries().add(d);
		return d;
	}
	
	
	public static Namespace newNamespace(){
		return getDictionariesFactory().createNamespace();
	}

	public static Namespace newNamespace(Dictionary owner){
		Namespace ns = newNamespace();
		owner.getNamespaces().add(ns);
		return ns;
	}
	
	public static Namespace newNamespace(Dictionary owner, String alias, String url){
		Namespace result = (owner==null?newNamespace():newNamespace(owner));
		result.setAlias(alias);
		result.setUrl(url);
		return result;
	}
	
	public static Import newImport(){
		return getDictionariesFactory().createImport();
	}
	public static Import newImport(Dictionary owner){
		Import imp = newImport();
		owner.getImports().add(imp);
		return imp;
	}
	
	public static Import newImport(Dictionary owner, String alias, String url){
		Import result = (owner==null?newImport():newImport(owner));
		result.setAlias(alias);
		result.setUrl(url);
		return result;
	}
	
	public static List<Namespace> extractNamespaces(Object[] objects){
		List<Namespace> result = new ArrayList<Namespace>();
		
		for (int i=0; i<objects.length; i++)
			if (objects[i] instanceof Namespace)
				result.add((Namespace)objects[i]);
		
		return result;
	}
	
	public static List<Import> findUndefinedImports(List<Import> imports, List<Namespace> namespaces){
		List<String> namespaceStrings = new ArrayList<String>();
		for (Namespace namespace: namespaces){
			if (!namespaceStrings.contains(namespace.getUrl()))
				namespaceStrings.add(namespace.getUrl());
			if (!namespaceStrings.contains(namespace.getAlias()))
				namespaceStrings.add(namespace.getAlias());
		}
		
		return findUndefinedImportsForStrings(imports, namespaceStrings);
	}

	public static List<Import> findUndefinedImportsForStrings(List<Import> imports, List<String> namespaces){
		List<Import> undefinedImports = new ArrayList<Import>();

		for (Import imp: imports)
			if (!namespaces.contains(imp.getUrl()) &&  !namespaces.contains(imp.getAlias()))
				undefinedImports.add(imp);
		
		return undefinedImports;
	}

	public static void collectImportsAndNamespaces(Dictionaries dictionaries, 
			List<Dictionary> excludeDictionaries, List<Import> foundImports, 
			List<Namespace> foundNamespaces){
		
		for (Dictionary dictionary: dictionaries.getDictionaries())
			if (!excludeDictionaries.contains(dictionary)){
				for (Namespace namespace: dictionary.getNamespaces())
					if (!foundNamespaces.contains(namespace))
						foundNamespaces.add(namespace);
				for (Import imp: dictionary.getImports())
					if (!foundImports.contains(imp))
						foundImports.add(imp);
			}	
	}
	
	
	public static void collectImportsAndNamespaceUrls(Dictionaries dictionaries, 
			List<Dictionary> excludeDictionaries, List<Import> foundImports, 
			List<String> foundUrls){
		
		for (Dictionary dictionary: dictionaries.getDictionaries())
			if (!excludeDictionaries.contains(dictionary)){
				for (Namespace namespace: dictionary.getNamespaces())
					if ((namespace.getUrl()!=null) && !foundUrls.contains(namespace.getUrl()))
						foundUrls.add(namespace.getUrl());
				for (Import imp: dictionary.getImports())
					if (!foundImports.contains(imp))
						foundImports.add(imp);
			}	
	}
	
}
