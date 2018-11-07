package ru.neodoc.content.codegen.sdoc.wrap;

import java.util.HashMap;
import java.util.Map;

import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;

public class WrapperRegistry {

	protected static Map<BaseElement, BaseWrapper> cache = new HashMap<>();
	
	protected static Map<String, BaseElement> cacheById = new HashMap<>();
	
	public static void clear(){
		cache.clear();
		cacheById.clear();
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends BaseWrapper, S extends BaseElement> T get(S element){
		if (cache.containsKey(element))
			return (T)cache.get(element);
		if (cacheById.containsKey(element.getUniqueId()))
			return (T) cache.get(cacheById.get(element.getUniqueId()));
		return null;
	}
	
	public static <T extends BaseWrapper> void put(T wrapper){
		BaseElement be = wrapper.getWrappedElement();
		cacheById.put(be.getUniqueId(), be);
		cache.put(wrapper.getWrappedElement(), wrapper);
	}
}
