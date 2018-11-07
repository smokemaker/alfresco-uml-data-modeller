package ru.neodoc.content.codegen.sdoc.wrap;

import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;
import ru.neodoc.content.modeller.utils.uml.elements.BaseNamedElement;
import ru.neodoc.content.modeller.utils.uml.elements.BaseTitledElement;

public abstract class BaseWrapper {
	
	protected BaseElement wrappedElement;
	
	protected BaseWrapper owner;
	
	protected List<BaseWrapper> children = new ArrayList<>();
	
	protected String targetJavaName;
	
	protected String targetJavaPackage;
	
	protected String targetJavaLocation;
	
	protected String targetJavaScriptName;
	
	protected String targetJavaScriptLocation;
	
	public String getTargetJavaScriptName() {
		return targetJavaScriptName;
	}
	
	public String getFullTargetJavaScriptName(){
		String result = getTargetJavaScriptName();
		if ((result==null) || (result.length()==0))
			if (owner!=null)
				return owner.getFullTargetJavaScriptName();
			else
				return "";
		if (owner != null){
			String parentResult = owner.getFullTargetJavaScriptName();
			if ((parentResult!=null) && (parentResult.length()>0))
				return parentResult + "." + result;
		}
		return result;
	}

	public void setTargetJavaScriptName(String targetJavaScriptName) {
		this.targetJavaScriptName = targetJavaScriptName;
	}

	public String getTargetJavaScriptLocation() {
		return targetJavaScriptLocation;
	}

	public void setTargetJavaScriptLocation(String targetJavaScriptLocation) {
		this.targetJavaScriptLocation = targetJavaScriptLocation;
	}

	public String getTargetJavaName() {
		return targetJavaName;
	}

	public String getFullTargetJavaName(){
		String result = getTargetJavaName();
		if ((result==null) || (result.length()==0))
			if (owner!=null)
				return owner.getFullTargetJavaName();
			else
				return "";
		if (owner != null){
			String parentResult = owner.getFullTargetJavaName();
			if ((parentResult!=null) && (parentResult.length()>0))
				return parentResult + "." + result;
		}
		return result;
	}
	
	public void setTargetJavaName(String targetJavaName) {
		this.targetJavaName = targetJavaName;
	}

	public String getTargetJavaPackage() {
		return targetJavaPackage;
	}

	public String getFinalJavaPackage(){
		String result = getTargetJavaPackage();
		if (result==null)
			result = "";
		String parentResult = "";
		if (owner!=null)
			parentResult = owner.getFinalJavaPackage();
		if (result.length()>0)
			if (parentResult.length()>0)
				return parentResult + "." + result;
			else
				return result;
		else
			return parentResult;
	}
	
	public void setTargetJavaPackage(String targetJavaPackage) {
		this.targetJavaPackage = targetJavaPackage;
	}

	public String getTargetJavaLocation() {
		return targetJavaLocation==null?"":targetJavaLocation;
	}

	public void setTargetJavaLocation(String targetJavaLocation) {
		this.targetJavaLocation = targetJavaLocation;
	}

	public BaseElement getWrappedElement() {
		return wrappedElement;
	}

	public void setWrappedElement(BaseElement wrappedElement) {
		this.wrappedElement = wrappedElement;
	}

	public void addChild(BaseWrapper child){
		this.children.add(child);
	}
	
	public boolean hasChild(BaseWrapper child){
		return this.children.contains(child);
	}
	
	public List<BaseWrapper> getChildren(){
		return this.children;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseWrapper> List<T> getChildren(Class<? extends BaseWrapper> clazz){
		List<T> result = new ArrayList<>();
		for (BaseWrapper bw: this.children)
			if (clazz.isAssignableFrom(bw.getClass()))
				result.add((T)bw);
		return result;
	}
	
	public String getName(){
		if (wrappedElement instanceof BaseNamedElement){
			String name = ((BaseNamedElement)wrappedElement).getName();
			// TODO throw exception or somehow register the error
			if (name == null)
				return "";
			String[] splitted = name.split(":");
			name = splitted[splitted.length - 1];
			return name;
		}
		return "";
	};
	
	public String getPrefixedName(){
		String result = getName();
		String namespacePrefix = null;
		BaseWrapper parent = this.owner;
		while (parent!=null) {
			if (parent instanceof NamespaceWrapper) {
				namespacePrefix = ((NamespaceWrapper)parent).getNamespace().getPrefix();
				break;
			}
			parent = parent.getOwner();
		}
		if (namespacePrefix!=null)
			result = namespacePrefix + ":" + result;
		return result;
	}
	
	public BaseWrapper getOwner() {
		return owner;
	}

	public void setOwner(BaseWrapper parent) {
		this.owner = parent;
	}

	public String getTitle(){
		if (wrappedElement instanceof BaseTitledElement)
			return ((BaseTitledElement)wrappedElement).getTitle();
		return "";
	}
	
	public String getDescription(){
		if (wrappedElement instanceof BaseTitledElement)
			return ((BaseTitledElement)wrappedElement).getDescription();
		return "";
	}
	
	public String getKey(){
		return null;
	}
}
