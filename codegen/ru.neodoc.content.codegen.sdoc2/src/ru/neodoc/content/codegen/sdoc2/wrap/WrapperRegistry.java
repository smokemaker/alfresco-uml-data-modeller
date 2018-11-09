package ru.neodoc.content.codegen.sdoc2.wrap;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class WrapperRegistry {

	protected static Map<Element, AbstractWrapper> cache = new HashMap<>();
	
	protected static Map<String, Element> cacheById = new HashMap<>();
	
	public static void clear(){
		cache.clear();
		cacheById.clear();
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends AbstractWrapper, S extends StereotypedElement> T get(S ps){
		if (cache.containsKey(ps.getElement()))
			return (T)cache.get(ps.getElement());
		if (cacheById.containsKey(ps.getUniqueId()))
			return (T) cache.get(cacheById.get(ps.getUniqueId()));
		return null;
	}
	
	public static <T extends AbstractWrapper> void put(T wrapper){
		ProfileStereotype ps = wrapper.getWrappedElement(); 
		Element be = ps.getElement();
		cacheById.put(ps.getUniqueId(), be);
		cache.put(be, wrapper);
	}
}
