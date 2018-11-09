package ru.neodoc.content.modeller.xml2uml.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Element;

public class HelperRegistry {

	protected final Map<Class<? extends AbstractHelper<?, ?>>, List<Class<? extends AbstractHelper<?, ?>>>> subhelperMap = new HashMap<>();
	protected final Map<Class<?>, Class<? extends AbstractHelper<?, ?>>> classToHelperMap = new HashMap<>();
	protected final List<Class<? extends AbstractHelper<?, ?>>> helperList = new ArrayList<>();
	
	protected final Map<Class<? extends Element>, List<Class<? extends AbstractHelper<?, ?>>>> elementHelperMap = new HashMap<>();
	
	protected final Map<Class<?>, Class<AbstractHelper<?, ?>>> helperClassCache = new HashMap<>();
	protected final Map<Class<? extends AbstractHelper<?, ?>>, AbstractHelper<?, ?>> helperCache = new HashMap<>();
	
	protected void register(Class<? extends AbstractHelper<?, ?>> helperClass) {
		if (!helperList.contains(helperClass))
			helperList.add(helperClass);
		if (!subhelperMap.containsKey(helperClass))
			subhelperMap.put((Class<? extends AbstractHelper<?, ?>>)helperClass, new ArrayList<Class<? extends AbstractHelper<?, ?>>>());
	}
	
	public void addElementHelper(Class<? extends Element> elementClass, Class<? extends AbstractHelper<?, ?>> helper) {
		if (!elementHelperMap.containsKey(elementClass))
			elementHelperMap.put(elementClass, new ArrayList<Class<? extends AbstractHelper<?, ?>>>());
		if (!elementHelperMap.get(elementClass).contains(helper))
			elementHelperMap.get(elementClass).add(helper);
	}
	
	@SuppressWarnings("unchecked")
	public void register(Class<? extends AbstractHelper<?, ?>> helperClass, Class<?>...elementClasses) {
		register(helperClass);
		if (elementClasses!=null)
			for (int i=0; i<elementClasses.length; i++) {
				if(!Element.class.equals(elementClasses[i]))
					classToHelperMap.put(elementClasses[i], helperClass);
				if (Element.class.isAssignableFrom(elementClasses[i]))
					addElementHelper((Class<? extends Element>)elementClasses[i], helperClass);
			}
	}
	
	public void register(Class<? extends AbstractHelper<?, ?>> helperClass, 
			Class<? extends AbstractHelper<?, ?>> ownerHelperClass, Class<?>...elementClasses) {
		register(helperClass, elementClasses);
		register(ownerHelperClass);
		if (!subhelperMap.get(ownerHelperClass).contains(helperClass))
			subhelperMap.get(ownerHelperClass).add(helperClass);
	}	
	
	public List<Class<? extends AbstractHelper<?, ?>>> getSubhelpers(Class<? extends AbstractHelper<?, ?>> ownerHelperClass){
		if (ownerHelperClass==null)
			return Collections.emptyList();
		if (!subhelperMap.containsKey(ownerHelperClass))
			return Collections.emptyList();
		return subhelperMap.get(ownerHelperClass);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Class<? extends AbstractHelper<T, ?>> getProcessorClassFor(T object){
		return getProcessorClassFor((Class<T>)object.getClass());
	}
	
	@SuppressWarnings("unchecked")
	public <T> Class<? extends AbstractHelper<T, ?>> getProcessorClassFor(Class<T> objectClass){
		return (Class<? extends AbstractHelper<T, ?>>)classToHelperMap.get(objectClass);
	}
	
	@SuppressWarnings("unchecked")
	public <T> AbstractHelper<T, ?> getProcessorFor(T object){
		return (AbstractHelper<T, ?>) getProcessorFor(object.getClass());
	}
	
	@SuppressWarnings("unchecked")
	public <T> AbstractHelper<T, ?> getProcessorFor(Class<T> objectClass){
		@SuppressWarnings("unchecked")
		Class<? extends AbstractHelper<?, ?>> clazz = helperClassCache.get(objectClass);
		if (clazz == null) {
			clazz = (Class<? extends AbstractHelper<T, ?>>)getProcessorClassFor(objectClass);
			if (clazz==null)
				return null;
			helperClassCache.put(objectClass, (Class<AbstractHelper<?, ?>>) clazz);
		}
		return getHelperByClass(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public <T> AbstractHelper<T, ?> getHelperByClass(Class<? extends AbstractHelper<?, ?>> helperClass){
		AbstractHelper<T, ?> result = (AbstractHelper<T, ?>) helperCache.get(helperClass);
		if (result == null) {
			try {
				result = (AbstractHelper<T, ?>)helperClass.newInstance();
				helperCache.put(helperClass, result);
			} catch (Exception e) {
				
			}
		}
		return result;
	}
	
	public List<AbstractHelper<?, ?>> getElementProcessors(Class<? extends Element> elementClass){
		List<AbstractHelper<?, ?>> result = new ArrayList<>();
		if (!elementHelperMap.containsKey(elementClass))
			return new ArrayList<>();
		for (Class<? extends AbstractHelper<?, ?>> helperClass: elementHelperMap.get(elementClass)) {
			AbstractHelper<?, ?> helper = getHelperByClass(helperClass);
			if (helper!=null)
				result.add(helper);
		}
		return result;
	}
	
}
