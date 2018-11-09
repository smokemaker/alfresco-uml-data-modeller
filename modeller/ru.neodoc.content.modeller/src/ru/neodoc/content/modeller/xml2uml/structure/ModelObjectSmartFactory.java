package ru.neodoc.content.modeller.xml2uml.structure;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.PrefixedName;

public class ModelObjectSmartFactory {
	ObjectRegistry objReg;
	
	public ModelObjectSmartFactory(ObjectRegistry or) {
		this.objReg = or;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> ModelObject<T> getObject(String fullName){
		
		ModelObject<T> result = (ModelObject<T>)this.objReg.get(fullName);
		if (result==null) {
			PrefixedName pName = new PrefixedName(fullName);
			if (pName.isPrefixed())
				result = (ModelObject<T>)this.objReg.get(pName.getName());
		}
		
		if (result == null) {
			result = new ModelObject<>(fullName);
		}
		return result;
	}

	public <T extends Object> ModelObject<T> getObject(String fullName, Class<? extends Element> elementClass){
		ModelObject<T> modelObject = getObject(fullName);
		if (modelObject.getElement()==null)
			modelObject.elementClass = elementClass;
		return modelObject;
	}

	public <T extends Object> ModelObject<T> getObject(Object source){
		return (ModelObject<T>)this.objReg.get(source);
	}
}
