package ru.neodoc.content.utils.uml.profile.meta;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.uml2.uml.Element;

public class MetaObjectCache {

	private static Map<Element, CompositeMetaObject> cache = new HashMap<>();
	
	public static CompositeMetaObject get(Element element) {
		return cache.get(element);
	}
	
	public static void put(CompositeMetaObject metaObject) {
		cache.put(metaObject.getElement(), metaObject);
	}
}
