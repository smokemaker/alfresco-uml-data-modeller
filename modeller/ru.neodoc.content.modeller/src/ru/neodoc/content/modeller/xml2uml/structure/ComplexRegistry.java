package ru.neodoc.content.modeller.xml2uml.structure;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.uml2.uml.Package;

public class ComplexRegistry {

	public static final String PROP_ROOT_OBJECT = "rootObject";
	public static final String PROP_CREATE_DIAGRAMS = "createDiagrams";
	
	protected ObjectRegistry objectRegistry;
	protected RelationRegistry relationRegistry;
	protected ModelObjectSmartFactory objectSmartFactory;
	
	protected Package umlRoot;
	
	protected Map<Class<?>, Object> singletons = new HashMap<>();
	protected Map<String, Object> properties = new HashMap<>();
	
	public ComplexRegistry(ObjectRegistry objectRegistry, RelationRegistry dependencyRegistry, ModelObjectSmartFactory objectSmartFactory) {
		this.objectRegistry = objectRegistry;
		this.relationRegistry = dependencyRegistry;
		this.objectSmartFactory = objectSmartFactory;
	}

	public ObjectRegistry getObjectRegistry() {
		return objectRegistry;
	}

	public RelationRegistry getRelationRegistry() {
		return relationRegistry;
	}

	public ModelObjectSmartFactory getObjectSmartFactory() {
		return objectSmartFactory;
	}

	public Package getUmlRoot() {
		return umlRoot;
	}

	public void setUmlRoot(Package umlRoot) {
		this.umlRoot = umlRoot;
	}
	
	public void put(Object obj) {
		singletons.put(obj.getClass(), obj);
	}
	
	public <T> T get(Class<T> clazz){
		return (T)singletons.get(clazz);
	}
	
	public void put(String name, Object value) {
		properties.put(name, value);
	}
	
	public Object get(String name) {
		return properties.get(name);
	}
}
