package ru.neodoc.content.modeller.uml2xml.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.neodoc.content.utils.common.MapOfList;

public interface ObjectContainer<T> {
	
	static public class FACTORY {
		
		static private final Map<Object, ObjectContainer<?>> cache = new HashMap<>();
		
		@SuppressWarnings("unchecked")
		static public <T> ObjectContainer<T> getOrCreate(T object){
			if (!cache.containsKey(object)) {
				ObjectContainer<T> result = createNew(object);
				if (result.hasObject())
					cache.put(object, result);
				return result;
			}
			return (ObjectContainer<T>)cache.get(object);
		}
		
		static public <T> ObjectContainer<T> createNew(T object){
			return new DefaultObjectContainer<>(object);
		}
		
		static public List<ObjectContainer<?>> createList(List<?> objects){
			List<ObjectContainer<?>> result = new ArrayList<>();
			for (Object object: objects)
				result.add(getOrCreate(object));
			return result;
		}
		
		static public <T> List<ObjectContainer<T>> createListTyped(List<T> objects){
			List<ObjectContainer<T>> result = new ArrayList<>();
			for (T object: objects)
				result.add(getOrCreate(object));
			return result;
		}
		
		static public List<?> extract(List<ObjectContainer<?>> containers){
			List<Object> result = new ArrayList<>();
			for (ObjectContainer<?> objectContainer: containers)
				result.add((Object)objectContainer.getObject());
			return result;
		}
		
		static public <T> List<T> extractTyped(List<ObjectContainer<T>> containers){
			List<T> result = new ArrayList<>();
			for (ObjectContainer<T> objectContainer: containers)
				result.add(objectContainer.getObject());
			return result;
		}
		
		static protected class DefaultObjectContainer<T> implements ObjectContainer<T> {
			
			protected ObjectContainer<?> parent = null;
			protected MapOfList<Class<?>, ObjectContainer<?>> children = new MapOfList<>();
			
			protected T object = null;
			
			protected final Map<String, Object> data = new HashMap<>();
			
			protected DefaultObjectContainer(T object) {
				super();
				setObject(object);
			}
			
			@Override
			public ObjectContainer<?> getParent() {
				return this.parent;
			}

			@Override
			public List<ObjectContainer<?>> getHierarchy() {
				List<ObjectContainer<?>> result = new ArrayList<>();
				ObjectContainer<?> current = getParent();
				while (current!=null) {
					result.add(0, current);
					current = current.getParent();
				}
				return result;
			}
			
			@Override
			public <T> ObjectContainer<T> findParent(Class<T> parentClass) {
				ObjectContainer<?> current = getParent();
				if (current!=null) {
					if (current.hasObject())
						if (parentClass.isAssignableFrom(current.getObject().getClass()))
							return (ObjectContainer<T>)current;
					return current.findParent(parentClass);
				}
				return null;
			}
			
			@Override
			public List<ObjectContainer<?>> getChildren() {
				return children.allValuesDistinct();
			}

			@Override
			public List<ObjectContainer<?>> getChildren(Class<?> childClass) {
				return children.get(childClass);
			}

			@Override
			public void setParent(ObjectContainer<?> parent) {
				this.parent = parent;
			}

			@Override
			public void addChild(ObjectContainer<?> child) {
				if (child==null)
					return;
				if (child.getObject()==null)
					return;
				children.addUnique(child.getObject().getClass(), child);
				child.setParent(this);
			}

			@Override
			public T getObject() {
				return this.object;
			}

			@Override
			public void setObject(T object) {
				this.object = object;
			}
			
			@Override
			public boolean hasObject() {
				return this.object!=null;
			}
			
			@Override
			public <T> T get(String name) {
				return (T)data.get(name);
			}
			
			@Override
			public void put(String name, Object object) {
				data.put(name, object);
			}
		}
	}
	
	public ObjectContainer<?> getParent();
	public List<ObjectContainer<?>> getHierarchy();
	public <T> ObjectContainer<T> findParent(Class<T> parentClass);
	public List<ObjectContainer<?>> getChildren();
	public List<ObjectContainer<?>> getChildren(Class<?> childClass);
	
	public void setParent(ObjectContainer<?> parent);
	public void addChild(ObjectContainer<?> child);
	
	public T getObject();
	public void setObject(T object);
	public boolean hasObject();
	
	public <T> T get(String name);
	public void put(String name, Object object);
	public default void put(Object object) {
		if (object!=null)
			put(object.getClass().getName(), object);
	};
}
