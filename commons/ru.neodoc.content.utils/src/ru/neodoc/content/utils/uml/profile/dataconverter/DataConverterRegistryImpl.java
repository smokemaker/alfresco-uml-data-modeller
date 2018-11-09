package ru.neodoc.content.utils.uml.profile.dataconverter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;

public class DataConverterRegistryImpl extends DefaultDirectedGraph<ClassVertex, DataConverterEdge> 
		implements DataConverterRegistry {

	@SuppressWarnings("unchecked")
	public final void register(Class<? extends DataConverter<?, ?>>...converterClasses) {
		if (converterClasses==null)
			return;
		for (int i=0; i<converterClasses.length; i++) {
			try {
				addConverter(converterClasses[i].newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3233363338923101767L;

	protected Map<Class<?>, ClassVertex> vertexMap = new HashMap<>();
	
	protected final List<DataConverterRegistryListener> listeners = new ArrayList<>();
	
	protected final List<DataConverterRegistryListener> listenersToAdd = new ArrayList<>();
	
	protected DataConverterRegistryImpl(Class<? extends DataConverterEdge> edgeClass) {
		super(edgeClass);
		addConverter(DataConverter.DEFAULT_CONVERTER);
	}

	public void addListener(DataConverterRegistryListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(DataConverterRegistryListener listener) {
		this.listeners.remove(listener);
	}
	
	public ClassVertex getVertex(Class<?> objectClass) {
		return vertexMap.get(objectClass);
	}
	
	public ClassVertex getOrCreateVertex(Class<?> objectClass) {
		if (!vertexMap.containsKey(objectClass)) {
			vertexMap.put(objectClass, new ClassVertex(objectClass));
			addVertex(getVertex(objectClass));
			notifyOnClass(objectClass);
		}
		return getVertex(objectClass);
	}

	protected void notifyOnClass(Class<?> clazz) {
		for (DataConverterRegistryListener listener: this.listeners)
			listener.onClassAdded(clazz, this);
	}
	
	@Override
	public void addConverter(DataConverter<?, ?> dataConverter) {
		addConverter(dataConverter.getSourceClass(), dataConverter.getTargetClass(), dataConverter);
	}
	
	@Override
	public void addConverter(Class<?> sourceClass, Class<?> targetClass, DataConverter<?, ?> dataConverter) {
		ClassVertex from = getOrCreateVertex(sourceClass);
		ClassVertex to = getOrCreateVertex(targetClass);
		DataConverterEdge edge = new DataConverterEdge(dataConverter);
		super.addEdge(from, to, edge);
		if (dataConverter instanceof DataConverterRegistryListener)
			if (!this.listeners.contains(dataConverter))
				this.addListener((DataConverterRegistryListener)dataConverter);
	}
	
	protected void notifyOnConverter(DataConverter<?, ?> dataConverter) {
		for (DataConverterRegistryListener listener: this.listeners)
			listener.onConverterAdded(dataConverter, this);
	}
	
	public DataConverter<?, ?> getConverter(Class<?> sourceClass, Class<?> targetClass){
		GraphPath<ClassVertex, DataConverterEdge> path = null;
		
		// make sure the vertices exist
		// if not, they'll be created and listeners would be notified
		getOrCreateVertex(sourceClass);
		getOrCreateVertex(targetClass);
		try {
			path = DijkstraShortestPath.findPathBetween(
						this, 
						getVertex(sourceClass), 
						getVertex(targetClass));
		} catch (Exception e) {
			
		}
		if (path==null)
			return null;
		CompositeDataConverter<?, ?> result = new CompositeDataConverter<>(sourceClass, targetClass);
		for (DataConverterEdge dce: path.getEdgeList()) {
			result.add(dce.getDataConverter());
		}
		return result;
	}
	
	public DataConverter<?, ?> findConverter(Class<?> sourceClass, Class<?> targetClass){
		return findConverter(sourceClass, targetClass, true);
	}
	
	public DataConverter<?, ?> findConverter(Class<?> sourceClass, Class<?> targetClass, boolean recoursive){
		DataConverter<?, ?> result = null; 
		result = getConverter(sourceClass, targetClass);
		if ((result==null) && recoursive)
			for (Class<?> intrfc: sourceClass.getInterfaces()) {
				result = findConverter(intrfc, targetClass, recoursive);
				if (result!=null)
					return result;
			}
		if (result!=null)
			return result;
		if ((result==null) && recoursive) {
			Class<?> superClass = sourceClass.getSuperclass();
			if (superClass!=null)
				result = findConverter(superClass, targetClass, recoursive);
		}
		
		return result;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object convert(Object source, Class<?> targetClass, Object...objects) {
		Object result = source;
		if (source==null)
			return null;
		if (targetClass==null)
			return source;
		Class<?> sourceClass = source.getClass();
		Class<?> sourceClassToSearch = sourceClass;
		Class<?> targetClassToSearch = targetClass;
		
		DataConverter converter = findConverter(sourceClassToSearch, targetClassToSearch, false);
		if (converter!=null)
			return converter.convert(source, targetClassToSearch, objects);
		
		boolean sourceIsList = List.class.isAssignableFrom(sourceClass);
		boolean sourceIsArray = sourceClass.isArray(); 
		
		if (sourceIsArray)
			sourceClassToSearch = sourceClass.getComponentType();
		
		if (targetClass.isArray())
			targetClassToSearch = targetClass.getComponentType();
		
		boolean targetIsList = List.class.isAssignableFrom(targetClass);
		
		if (sourceIsList || sourceIsArray) {
			List<Object> tempList = new ArrayList<>();
			List sourceList = null;
			if (sourceIsList)
				sourceList = (List)source;
			else
				sourceList = Arrays.asList((Object[])source);
			
			for (Object element: sourceList)
				tempList.add(targetIsList?element:convert(element, targetClassToSearch, objects));
			if (targetClass.isArray()) {
				Object[] array = (Object[]) Array.newInstance(targetClassToSearch, tempList.size());
				for (int i=0; i<tempList.size(); i++)
					array[i] = tempList.get(i);
				return array;
			} else 
				if (targetIsList)
					return tempList;
				else {
					if (tempList.isEmpty())
						return null;
					else
						return tempList.get(0);
				}
			
		} else {
			converter = findConverter(sourceClassToSearch, targetClassToSearch);
			if (converter!=null)
				return converter.convert(source, objects);
			else if (Enum.class.isAssignableFrom(targetClassToSearch)) {
				converter = findConverter(sourceClassToSearch, Enum.class);
				if (converter!=null)
					return converter.convert(source, targetClassToSearch, objects);
			}
				
		}
		
		return result;
	}
}
