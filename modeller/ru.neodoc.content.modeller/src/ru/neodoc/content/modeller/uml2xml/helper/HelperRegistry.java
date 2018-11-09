package ru.neodoc.content.modeller.uml2xml.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.common.MapOfList;

public class HelperRegistry {

	protected final MapOfList<Class<? extends AbstractHelper<?, ?, ?>>, Class<? extends AbstractHelper<?, ?, ?>>> subhelperMap = 
			new MapOfList<>();
		/*Map<Class<? extends AbstractHelper<?, ?, ?>>, List<Class<? extends AbstractHelper<?, ?, ?>>>> subhelperMap = new HashMap<>();*/
	protected final Map<Class<?>, Class<? extends AbstractHelper<?, ?, ?>>> classToHelperMap = new HashMap<>();
	protected final List<Class<? extends AbstractHelper<?, ?, ?>>> helperList = new ArrayList<>();
	
	protected final MapOfList<Class<? extends Element>, Class<? extends AbstractHelper<?, ?, ?>>> elementHelperMap = 
			new MapOfList<>();
		/*Map<Class<? extends Element>, List<Class<? extends AbstractHelper<?, ?, ?>>>> elementHelperMap = new HashMap<>();*/
	
	protected final Map<Class<?>, Class<AbstractHelper<?, ?, ?>>> helperClassCache = new HashMap<>();
	protected final Map<Class<? extends AbstractHelper<?, ?, ?>>, AbstractHelper<?, ?, ?>> helperCache = new HashMap<>();
	
	protected final MapOfList<Class<? extends AbstractHelper<?, ?, ?>>, Class<? extends AbstractHelper<?, ?, ?>>> containerMap = 
			new MapOfList<>();
		/*Map<Class<? extends AbstractHelper<?, ?, ?>>, List<Class<? extends AbstractHelper<?, ?, ?>>>> containerMap = new HashMap<>();*/
	
	public class RegistrationInfo {
		
		protected Class<? extends AbstractHelper<?, ?, ?>> helperClass;
		protected Class<? extends AbstractHelper<?, ?, ?>> subHelperClass;
		
		public RegistrationInfo(Class<? extends AbstractHelper<?, ?, ?>> helperClass, 
				Class<? extends AbstractHelper<?, ?, ?>> subHhelperClass) {
			super();
			this.helperClass = helperClass;
			this.subHelperClass = subHhelperClass;
		}
		
		public void asContained() {
			if (helperClass==null)
				return;
			if (subHelperClass==null)
				return;
/*			if (!containerMap.containsKey(helperClass))
				containerMap.put(helperClass, new ArrayList<Class<? extends AbstractHelper<?, ?, ?>>>());
			containerMap.get(helperClass).add(subHelperClass);*/
			containerMap.add(helperClass, subHelperClass);
		}
		
	}
	
	protected RegistrationInfo register(Class<? extends AbstractHelper<?, ?, ?>> helperClass) {
		if (!helperList.contains(helperClass))
			helperList.add(helperClass);
/*		if (!subhelperMap.containsKey(helperClass))
			subhelperMap.put((Class<? extends AbstractHelper<?, ?, ?>>)helperClass, new ArrayList<Class<? extends AbstractHelper<?, ?, ?>>>());
*/		return new RegistrationInfo(helperClass, null);
	}
	
	public void addElementHelper(Class<? extends Element> elementClass, Class<? extends AbstractHelper<?, ?, ?>> helper) {
/*		if (!elementHelperMap.containsKey(elementClass))
			elementHelperMap.put(elementClass, new ArrayList<Class<? extends AbstractHelper<?, ?, ?>>>());
		if (!elementHelperMap.get(elementClass).contains(helper))
			elementHelperMap.get(elementClass).add(helper);*/
		elementHelperMap.addUnique(elementClass, helper);
	}
	
	@SuppressWarnings("unchecked")
	public RegistrationInfo register(Class<? extends AbstractHelper<?, ?, ?>> helperClass, Class<?>...elementClasses) {
		register(helperClass);
		if (elementClasses!=null)
			for (int i=0; i<elementClasses.length; i++) {
				if(!Element.class.equals(elementClasses[i]))
					classToHelperMap.put(elementClasses[i], helperClass);
				if (Element.class.isAssignableFrom(elementClasses[i]))
					addElementHelper((Class<? extends Element>)elementClasses[i], helperClass);
			}
		return new RegistrationInfo(helperClass, null);
	}
	
	public RegistrationInfo register(Class<? extends AbstractHelper<?, ?, ?>> helperClass, 
			Class<? extends AbstractHelper<?, ?, ?>> ownerHelperClass, Class<?>...elementClasses) {
		register(helperClass, elementClasses);
		register(ownerHelperClass);
		/*if (!subhelperMap.get(ownerHelperClass).contains(helperClass))
			subhelperMap.get(ownerHelperClass).add(helperClass);*/
		subhelperMap.addUnique(ownerHelperClass, helperClass);
		return new RegistrationInfo(ownerHelperClass, helperClass);
	}	
	
	public List<Class<? extends AbstractHelper<?, ?, ?>>> getSubhelperClasses(Class<? extends AbstractHelper<?, ?, ?>> ownerHelperClass){
		if (ownerHelperClass==null)
			return Collections.emptyList();
		if (!subhelperMap.containsKey(ownerHelperClass))
			return Collections.emptyList();
		return subhelperMap.get(ownerHelperClass);
	}

	public <ContainerElementType extends Element, JAXBContainerType> List<AbstractSubHelper<ContainerElementType, ?, JAXBContainerType, ?, ?>> 
			getSubHelpers(Class<? extends AbstractHelper<ContainerElementType, JAXBContainerType, ?>> ownerClass){
		List<Class<? extends AbstractHelper<?, ?, ?>>> helperClasses = getSubhelperClasses(ownerClass);
		List<AbstractSubHelper<ContainerElementType, ?, JAXBContainerType, ?, ?>> result = new ArrayList<>();
		
		for (Class<? extends AbstractHelper<?, ?, ?>> helperClass: helperClasses) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends AbstractSubHelper<ContainerElementType, ?, JAXBContainerType, ?, ?>> subHelperClass = 
						(Class<? extends AbstractSubHelper<ContainerElementType, ?, JAXBContainerType, ?, ?>>)helperClass;
				if (subHelperClass==null)
					continue;
				AbstractSubHelper<ContainerElementType, ?, JAXBContainerType, ?, ?> subHelper = subHelperClass.newInstance();
				result.add(subHelper);
			} catch (Exception e) {
				
			}
		}
		return result;
	}
	
	public List<Class<? extends AbstractHelper<?, ?, ?>>> getContainedSubhelperClasses(Class<? extends AbstractHelper<?, ?, ?>> ownerHelperClass){
		if (ownerHelperClass==null)
			return Collections.emptyList();
		if (!containerMap.containsKey(ownerHelperClass))
			return Collections.emptyList();
		return containerMap.get(ownerHelperClass);
	}

	
}
