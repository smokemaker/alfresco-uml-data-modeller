package ru.neodoc.content.codegen.sdoc2.wrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.NamedElement;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.DdTextualDescription;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

public abstract class AbstractWrapper {

	protected ProfileStereotype wrappedElement = null;
	
	public ProfileStereotype getWrappedElement() {
		return wrappedElement;
	}

	protected AbstractWrapper owner;
	
	public AbstractWrapper getOwner() {
		return owner;
	}

	public void setOwner(AbstractWrapper owner) {
		this.owner = owner;
	}

	protected List<AbstractWrapper> children = new ArrayList<>();
	
	protected Map<String, AbstractWrapperExtension> extensions = new HashMap<>();
	
	protected AbstractWrapper(ProfileStereotype wrappedElement) {
		super();
		this.wrappedElement = wrappedElement;
	}
	
	public void addChild(AbstractWrapper child){
		this.children.add(child);
	}
	
	public boolean hasChild(AbstractWrapper child){
		return this.children.contains(child);
	}
	
	public List<AbstractWrapper> getChildren(){
		return this.children;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractWrapper> List<T> getChildren(Class<? extends AbstractWrapper> clazz){
		List<T> result = new ArrayList<>();
		for (AbstractWrapper bw: this.children)
			if (clazz.isAssignableFrom(bw.getClass()))
				result.add((T)bw);
		return result;
	}

	public String getName(){
		NamedElement ne = null;
		try {
			ne = wrappedElement.getElement();
		} catch (Exception e) {
			// NOOP
		}
		if (ne!=null){
			String name = ne.getName();
			// TODO throw exception or somehow register the error
			if (name == null)
				return "";
			PrefixedName pn = new PrefixedName(name);
			return pn.getName();
		}
		return "";
	};
	
	public String getPrefixedName() {
		NamedElement ne = null;
		try {
			ne = wrappedElement.getElement();
		} catch (Exception e) {
			// NOOP
		}
		if (ne!=null){
			String name = ne.getName();
			PrefixedName pn = new PrefixedName(name);
			Namespace ns = Namespace._HELPER.findNearestFor(ne);
			if (ns!=null)
				pn.setPrefix(ns.getPrefix());
			return pn.getFullName();
		}
		return "";
	}
	
	public String getTitle(){
		AlfrescoProfile.Internal.DdTextualDescription ddTd = wrappedElement.get(DdTextualDescription.class);
		if (ddTd!=null)
			return ddTd.getTitle();
		return "";
	}
	
	public String getDescription(){
		AlfrescoProfile.Internal.DdTextualDescription ddTd = wrappedElement.get(DdTextualDescription.class);
		if (ddTd!=null)
			return ddTd.getDescription();
		return "";
	}
	
	public String getKey(){
		return null;
	}
	
	public void extend(AbstractWrapperExtension awe) {
		String id = awe.getId();
		extensions.put(id, awe);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractWrapperExtension> T getExtension(String id) {
		return (T)extensions.get(id);
	}
}
