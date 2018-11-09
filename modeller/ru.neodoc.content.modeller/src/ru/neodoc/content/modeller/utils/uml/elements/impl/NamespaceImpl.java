package ru.neodoc.content.modeller.utils.uml.elements.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.Aspect;
import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.DataTypeElement;
import ru.neodoc.content.modeller.utils.uml.elements.ElementFactory;
import ru.neodoc.content.modeller.utils.uml.elements.Model;
import ru.neodoc.content.modeller.utils.uml.elements.Namespace;
import ru.neodoc.content.modeller.utils.uml.elements.Type;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;

public class NamespaceImpl extends BasePackageElementImpl implements Namespace {
	
	protected Model parentModel;
	
	protected Stereotype namespaceStereotype;
	
	public NamespaceImpl(){
		super();
	}

	public NamespaceImpl(Element element) {
		super(element);
	}

	public NamespaceImpl(EObject eObject) {
		super(eObject);
	}
	
	@Override
	protected void init() {
		super.init();
		if (super.isValid())
			namespaceStereotype = AlfrescoUMLUtils.getNamespaceStereotype(packageElement);
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && (namespaceStereotype != null);
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.Namespace#getPrefix()
	 */
	@Override
	public String getPrefix(){
		return getName();
	}
	
	protected List<org.eclipse.uml2.uml.Type> getTypeList(String stereotype) {
		@SuppressWarnings("unchecked")
		List<org.eclipse.uml2.uml.Type> foundTypes = 
			(List<org.eclipse.uml2.uml.Type>)ru.neodoc.content.utils.uml.search.helper.SearchHelperFactory.getTypeSearcher()
				.stopOnStereotype(stereotype)
				.target(stereotype)
				.startWith(packageElement)
				.search();
		return foundTypes;
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.Namespace#getTypes()
	 */
	@Override
	public List<Type> getTypes(){
		List<Type> result = new ArrayList<>();
		
		List<org.eclipse.uml2.uml.Type> foundTypes = getTypeList(AlfrescoProfile.ForClass.Type._NAME); 
						
		for (org.eclipse.uml2.uml.Type foundType: foundTypes) {
			TypeImpl t = new TypeImpl(foundType);
			if (t.isValid())
				result.add(t);
		}
		
		return result;
		
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.Namespace#getAspects()
	 */
	@Override
	public List<Aspect> getAspects(){
		List<Aspect> result = new ArrayList<>();
		
		List<org.eclipse.uml2.uml.Type> foundTypes = getTypeList(AlfrescoProfile.ForClass.Aspect._NAME); 
						
		for (org.eclipse.uml2.uml.Type foundType: foundTypes) {
			AspectImpl t = new AspectImpl(foundType);
			if (t.isValid())
				result.add(t);
		}
		
		return result;
	}
	
	@Override
	public List<Namespace> getImportedNamespaces() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Aspect> getRequiredAspects() {
		return getRequiredBaseClassElements(false);
	}
	
	@Override
	public List<Type> getRequiredTypes() {
		return getRequiredBaseClassElements(true);
	}

	protected <T extends BaseClassElement> List<T> getRequiredBaseClassElements(boolean findTypes){
		Map<String, T> result = new HashMap<>();
		
		for (Type tp: getTypes()){
			@SuppressWarnings("unchecked")
			List<T> tmp = (List<T>) (findTypes
					?tp.getRequiredTypes()
					:tp.getRequiredAspects());
			for (T item: tmp){
				org.eclipse.uml2.uml.Package p = AlfrescoUMLUtils.getNearestNamespace(item.getElement());
				if (p == getElement())
					continue;
				String key = p.getName()+"$" + p.getURI() + item.getName();
				if (result.containsKey(key))
					continue;
				result.put(key, item);
			}
				
		}

		for (Aspect asp: getAspects()){
			@SuppressWarnings("unchecked")
			List<T> tmp = (List<T>) (findTypes
					?asp.getRequiredTypes()
					:asp.getRequiredAspects());
			for (T item: tmp){
				org.eclipse.uml2.uml.Package p = AlfrescoUMLUtils.getNearestNamespace(item.getElement());
				if (p == getElement())
					continue;
				String key = p.getName()+"$" + p.getURI() + item.getName();
				if (result.containsKey(key))
					continue;
				result.put(key, item);
			}
				
		}
		
		
		return new ArrayList<>(result.values());
	}
	
	@Override
	public List<DataTypeElement> getDataTypes() {
		List<? extends org.eclipse.uml2.uml.Type> types = AlfrescoSearchHelperFactory.getDataTypeSearcher()
			.startWith(packageElement)
			.search();
		List<DataTypeElement> result = new ArrayList<>();
		for (org.eclipse.uml2.uml.Type t: types){
			DataTypeElement dte = new DataTypeElementImpl(t);
			if (dte.isValid())
				result.add(dte);
		}
		return result;
	}
	
	@Override
	public List<DataTypeElement> getRequiredDataTypes() {
		Map<String, DataTypeElement> result = new HashMap<>();
		
		for (Type t: getTypes()) {
			List<DataTypeElement> tmp = t.getRequiredDataTypes();
			for (DataTypeElement item: tmp){
				Package p = AlfrescoUMLUtils.getNearestNamespace(item.getElement());
				if (p == getElement())
					continue;
				String key = p.getName()+"$" + p.getURI() + "$" + item.getName();
				if (result.containsKey(key))
					continue;
				result.put(key, item);
			}
		}
			
		for (Aspect t: getAspects()) {
			List<DataTypeElement> tmp = t.getRequiredDataTypes();
			for (DataTypeElement item: tmp){
				Package p = AlfrescoUMLUtils.getNearestNamespace(item.getElement());
				if (p == getElement())
					continue;
				String key = p.getName()+"$" + p.getURI() + "$" + item.getName();
				if (result.containsKey(key))
					continue;
				result.put(key, item);
			}
		}
			
		
		return new ArrayList<>(result.values());
	}
	
	@Override
	public List<Namespace> getRequiredNamespaces() {
		Map<String, Namespace> map = new HashMap<>();
		
		for (Type tp: getTypes()) {
			List<Namespace> required = tp.getRequiredNamespaces();
			for (Namespace rqns: required)
				addNamespaceToMap(rqns, map);
		}
		
		for (Aspect as: getAspects()) {
			List<Namespace> required = as.getRequiredNamespaces();
			for (Namespace rqns: required)
				addNamespaceToMap(rqns, map);
		}
		
		
		List<Namespace> result = new ArrayList<>();
		result.addAll(map.values());
		
		return result;
	}
	
	private void addNamespaceToMap(Namespace ns, Map<String, Namespace> map) {
		if (ns.getPrefix().equals(this.getPrefix()))
			return;
		if (map.containsKey(ns.getPrefix()))
			return;
		map.put(ns.getPrefix(), ns);
	}
	
	@Override
	public Model getModel() {
		if (this.parentModel == null) {
			Package p = AlfrescoUMLUtils.getModel(packageElement);
			if (p!=null)
				parentModel = ElementFactory.createModelElement(p);
		}
		return parentModel;
	}
}
