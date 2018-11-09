package ru.neodoc.content.modeller.xml2uml.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ObjectRegistry{
	Map<String, ModelObject<?>> objects = new HashMap<>();
	Map<Object, ModelObject<?>> objectsBySource = new HashMap<>();
	Map<String, List<String>> classifiedObjects = new HashMap<>();
	
	public <T extends Object> void add(ModelObject<T> object){
		add(object, false);
	}
	@SuppressWarnings("unchecked")
	public <T extends Object> void add(ModelObject<T> object, boolean reload){
		if ((object==null) || object.noStore)
			return;
		String name = object.getName();
		
		// check if the namespace exists and define it if not
		String pack = object.pack;
		if (pack!=null 
				&& pack.trim().length()>0 
				&& this.objects.get(pack)==null){
			this.objects.put(pack, new ModelObject<>(pack));
		}
		
		ModelObject<Object> storedObject = (ModelObject<Object>)this.objects.get(name);
		
		if (object.equals(storedObject))
			return;
		
		if (storedObject==null) {
			if (object.source!=null)
				storedObject = (ModelObject<Object>)this.objectsBySource.get(object.source);
		}
		if (storedObject==null) {
			this.objects.put(object.getName(), (ModelObject<Object>)object);
			
			if (object.source != null) {
				String className = object.source.getClass().getName();
				if (!this.classifiedObjects.containsKey(className))
					this.classifiedObjects.put(className, new ArrayList<String>());
				
				if (!this.classifiedObjects.get(className).contains(object.getName()))
					this.classifiedObjects.get(className).add(object.getName());
			}
			return;
		}
		
		if (reload && (object.source!=null))
			storedObject.load(object.source);
		
		if (storedObject.source == null){
			storedObject.source = object.source;

			if (object.source != null){
				String className = object.source.getClass().getName();
				if (!this.classifiedObjects.containsKey(className))
					this.classifiedObjects.put(className, new ArrayList<String>());
				
				if (!this.classifiedObjects.get(className).contains(object.getName()))
					this.classifiedObjects.get(className).add(object.getName());
			}
		}
		if (storedObject.getElement() == null)
			storedObject.setElement(object.getElement());
		if (storedObject.location == null || 
				storedObject.location.trim().length()==0)
			storedObject.location = object.location;
		if (storedObject.model == null || 
				storedObject.model.trim().length()==0)
			storedObject.model = object.model;
		
		storedObject.createdBy = object.createdBy;
		storedObject.inners.clear();
		storedObject.inners.addAll(object.inners);
		storedObject.imports = object.imports;
	}
	
	public ModelObject<?> get(Object source){
		return this.objectsBySource.get(source);
	}
	
	public ModelObject<?> get(String name){
		return this.objects.get(name);
	}
	
	public Collection<ModelObject<?>> getObjects(){
		return this.objects.values();
	}
	
	public int size(){
		return this.objects.values().size();
	}
	
	public List<String> getObjectsByClass(java.lang.Class<?> clazz){
		return getObjectsByClass(clazz.getName());
	}
	
	public List<String> getObjectsByClass(String className){
		return this.classifiedObjects.get(className);
	}
	
	public void remove(String name) {
		ModelObject<?> mo = get(name);
		if (mo!=null)
			if (mo.source!=null)
				objectsBySource.remove(mo.source);
		this.objects.remove(name);
	}
}
