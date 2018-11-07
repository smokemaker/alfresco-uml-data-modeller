package ru.neodoc.content.modeller.utils.uml.elements.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.Model;
import ru.neodoc.content.modeller.utils.uml.elements.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class ModelImpl extends BasePackageElementImpl implements Model {

	protected Stereotype modelStereotype;
	
	public ModelImpl(){
		super();
	}
	
	public ModelImpl(Element element) {
		super(element);
	}

	public ModelImpl(EObject eObject) {
		super(eObject);
	}
	
	@Override
	protected void init() {
		super.init();
		if (super.isValid())
			modelStereotype = AlfrescoUMLUtils.getModelStereotype(packageElement); 
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && (modelStereotype != null);
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.Model#getNamespaces()
	 */
	@Override
	public List<Namespace> getNamespaces(){
		List<Namespace> result = new ArrayList<>();
		if (!isValid())
			return result;
		
		List<Package> packages = AlfrescoUMLUtils.getNamespaces(packageElement);
		for (Package pack: packages) {
			NamespaceImpl namespace = new NamespaceImpl(pack);
			if (namespace.isValid())
				result.add(namespace);
		}
		
		return result;
	}
	@Override
	public List<Namespace> getImportedNamespaces() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Namespace> getRequiredNamespaces() {
		Map<String, Namespace> map = new HashMap<>();
		
		Set<String> ownedPrefixes = new HashSet<>();
		for (Namespace ns: getNamespaces())
			ownedPrefixes.add(ns.getPrefix());
		
		for (Namespace ns: getNamespaces()) {
			List<Namespace> required = ns.getRequiredNamespaces();
			for (Namespace rqns: required){
				if (map.containsKey(rqns.getPrefix()))
					continue;
				if (ownedPrefixes.contains(rqns.getPrefix()))
					continue;
				map.put(rqns.getPrefix(), rqns);
			}
		}
		
		List<Namespace> result = new ArrayList<>();
		result.addAll(map.values());
		
		return result;
	}
	
	@Override
	public String getAuthor() {
		if (!isValid())
			return "";
		Object result = AlfrescoUMLUtils.getStereotypeValue(
				modelStereotype.getName(), 
				packageElement, 
				AlfrescoProfile.ForPackage.Model.AUTHOR); 
		return (result==null?"":result.toString());
	}
	
	@Override
	public String getVersion() {
		if (!isValid())
			return "";
		Object result = AlfrescoUMLUtils.getStereotypeValue(
				modelStereotype.getName(), 
				packageElement, 
				AlfrescoProfile.ForPackage.Model.VERSION); 
		return (result==null?"":result.toString());
	}
}
