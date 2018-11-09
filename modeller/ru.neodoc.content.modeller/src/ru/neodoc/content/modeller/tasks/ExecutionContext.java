package ru.neodoc.content.modeller.tasks;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;

import ru.neodoc.content.utils.common.MapOfList;

public interface ExecutionContext {

	public static final String CONTEXT_OBJECT = "contextObject";
	
	public static final String UML_CONTEXT_ROOT_MODEL = "umlContextRootModel";
	
	public static interface Listener {
		public void changed(String name, Object value, ExecutionContext executionContext);
	}
	
	public class DefaultExecutionContext implements ExecutionContext {

		protected final MapOfList<String, Listener> listeners = new MapOfList<>();
		
		public DefaultExecutionContext() {
			super();
		}
		
		public DefaultExecutionContext(Model umlRootModel) {
			this();
			setUMLContextRootModel(umlRootModel);
		}
		
		public abstract class ValueAdapter<T> {
			
			protected String name = "";
			
			public void setName(String name) {
				this.name = name;
			}
			
			@SuppressWarnings("unchecked")
			public T get() {
				return (T) DefaultExecutionContext.this.get(this.name);
			}
			
			public T put(T value) {
				DefaultExecutionContext.this.put(this.name, value);
				return get();
			}
		}
		
		protected final Map<String, Object> storage = new HashMap<>();

		protected final Map<String, ValueAdapter<?>> adapters = new HashMap<>();
		
		public DefaultExecutionContext adapter(String name, ValueAdapter<?> adapter) {
			adapter.setName(name);
			this.adapters.put(name, adapter);
			return this;
		}
		
		@Override
		public void put(String name, Object object) {
			this.storage.put(name, object);
			for (Listener listener: listeners.getOrEmpty(name))
				listener.changed(name, object, this);
		}

		@Override
		public Object get(String name) {
			return this.storage.get(name);
		}
		
		public <T> T adapted(String name) {
			@SuppressWarnings("unchecked")
			ValueAdapter<T> adapter = (ValueAdapter<T>)this.adapters.get(name);
			return adapter.get();
		}
		
		public <T> T adapted(String name, T value) {
			@SuppressWarnings("unchecked")
			ValueAdapter<T> adapter = (ValueAdapter<T>)this.adapters.get(name);
			return adapter.put(value);
		}
		
		@Override
		public void addListener(String name, Listener listener) {
			listeners.addUnique(name, listener);
		}
		
		@Override
		public void removeListener(Listener listener) {
			listeners.removeValue(listener);
		}
	}
	
	static public ExecutionContext create() {
		return new DefaultExecutionContext();
	}
	
	static public ExecutionContext create(Model umlRootModel) {
		return new DefaultExecutionContext(umlRootModel);
	}
	
	default public void put(Object object) {
		if (object!=null)
			put(object.getClass().getName(), object);
	};
	public void put(String name, Object object);
	
	default public <T> T get(Class<T> clazz) {
		try {
			return (T)get(clazz.getName());
		} catch (Exception e) {
			return null;
		}
	};
	
	default public void setContextObject(Object object) {
		if (object instanceof Element) {
			setUMLContextRootModel(((Element)object).getModel());
		}
		put(CONTEXT_OBJECT, object);
	}
	
	default public Object getContextObject() {
		return get(CONTEXT_OBJECT);
	}
	
	default public void setUMLContextRootModel(Model model) {
		put(UML_CONTEXT_ROOT_MODEL, model);
	}
	
	default public Model getUMLContextRootModel() {
		return (Model)get(UML_CONTEXT_ROOT_MODEL);
	}
	
	public Object get(String name);
	
	public void addListener(String name, Listener listener);
	
	public void removeListener(Listener listener);
}
