package ru.neodoc.content.modeller.xml2uml.structure;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

public class ModelObject<T extends Object> {
	
	public static boolean hasElement(ModelObject<?>...modelObjects) {
		if (modelObjects==null)
			return false;
		for (int i=0; i<modelObjects.length; i++)
			if ((modelObjects[i]==null) || (modelObjects[i].element==null))
				return false;
		return true;
	}
	
	public T source = null;
	public String name = "";
	public String pack = "";
	public String uri = "";
	public List<ModelObject<?>> imports = new ArrayList<>();
	public final List<ModelObject<?>> inners = new ArrayList<>();
	private Element element = null;
	public String location = "";
	public String model = "";

	public boolean noStore = false;
	
	public boolean isTransient = false;
	
	public Object createdBy;
	
	public Class<? extends Element> elementClass;
	
/*		public static ModelObject<? extends Object> newInstance(Object object){
		ModelObject<? extends Object> result;
		if (object instanceof Model)
			result = new ModelObject<Model>((Model)object);
	}
*/		
	public ModelObject(){
		
	}

	public ModelObject(String fullName){
		if (fullName != null) {
			String[] data = fullName.split(":");
			this.name = data[data.length-1];
			if (data.length>1)
				this.pack = data[data.length-2];
		}
	}
	
	public ModelObject(T object){
		this.source = object;
		load(object);
	}
	
	public void load(T object){
/*		if (object instanceof org.alfresco.model.dictionary._1.Model){
			this.name = ((org.alfresco.model.dictionary._1.Model)object).getName();
		} else 
*/		/*if (object instanceof org.alfresco.model.dictionary._1.Model.Namespaces.Namespace){
			this.name = ((org.alfresco.model.dictionary._1.Model.Namespaces.Namespace)object).getPrefix();
			this.uri = ((org.alfresco.model.dictionary._1.Model.Namespaces.Namespace)object).getUri();
		} else 
*/		/*if (object instanceof org.alfresco.model.dictionary._1.Model.DataTypes.DataType) {
			String typeName = ((org.alfresco.model.dictionary._1.Model.DataTypes.DataType)object).getName();
			int index = typeName.indexOf(":"); 
			if (index>0){
				this.name = typeName.substring(index+1);
				this.pack = typeName.substring(0, index);
			}
		} else 
*/		/*if (object instanceof org.alfresco.model.dictionary._1.Type){
			String typeName = ((org.alfresco.model.dictionary._1.Type)object).getName();
			int index = typeName.indexOf(":"); 
			if (index>0){
				this.name = typeName.substring(index+1);
				this.pack = typeName.substring(0, index);
			}
		} else 
		if (object instanceof org.alfresco.model.dictionary._1.Aspect){
			String typeName = ((org.alfresco.model.dictionary._1.Aspect)object).getName();
			int index = typeName.indexOf(":"); 
			if (index>0){
				this.name = typeName.substring(index+1);
				this.pack = typeName.substring(0, index);
			}
		}
*/	/*	if (object instanceof org.alfresco.model.dictionary._1.Constraint) {
			String typeName = ((org.alfresco.model.dictionary._1.Constraint)object).getName();
			PrefixedName pn = new PrefixedName(typeName);
			this.name = pn.getName();
			this.pack = pn.getPrefix();
		}*/
		
	}
	
	public boolean isModel(){
		return (this.source instanceof Model);
	}
	
	public boolean isPackage(){
		return (this.source instanceof org.alfresco.model.dictionary._1.Model.Namespaces.Namespace)
			|| (this.element instanceof Package) || (this.name!=null && (this.pack==null || this.pack.length()==0));
	}
	
	public boolean isVirtual(){
		return (this.source==null) && (this.element==null);
	}

	public String getName(){
		if (isPackage())
			return this.name;
		return this.pack + ":" + this.name;
	}
	
	public boolean isToCreate(){
		return this.source!=null;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
		if (this.element!=null)
			this.elementClass = element.getClass();
	}
	
}