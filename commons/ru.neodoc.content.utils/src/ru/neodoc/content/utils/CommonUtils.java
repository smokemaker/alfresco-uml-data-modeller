package ru.neodoc.content.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommonUtils {

	public static interface ListComparator<T> {
		public boolean equals(T item1, T item2);
		public String itemHash(T item);
	}
	
	public static abstract class BaseListComparator<T> implements ListComparator<T> {
		@Override
		public boolean equals(T item1, T item2) {
			String h1 = itemHash(item1);
			String h2 = itemHash(item2);
			return (h1==null && h2==null) || (h1.equals(h2));
		}
	}
	
	public static interface ItemUpdater<T> {
		public void updateItem(T origin, T updated);
	}
	
	public static <T> List<T> updateList(List<T> toList, List<T> fromList, ListComparator<T> comparator) {
		return updateList(toList, fromList, comparator, false);
	}
	
	public static <T> List<T> updateList(List<T> toList, List<T> fromList, ListComparator<T> comparator, boolean replace) {
		Map<String, T> toMap = new LinkedHashMap<>();
		Map<String, T> fromMap = new LinkedHashMap<>();
		for (T item: toList) {
			toMap.put(comparator.itemHash(item), item);
		}
		for (T item: fromList) {
			fromMap.put(comparator.itemHash(item), item);
		}
		
		List<T> toRemove = new ArrayList<>();
		
		for (String key: toMap.keySet()) {
			if (fromMap.containsKey(key)){
				if (replace || !comparator.equals(toMap.get(key), fromMap.get(key)))
					toMap.put(key, fromMap.get(key));
				fromMap.remove(key);
			}else
				toRemove.add(toMap.get(key));
		}
		
		List<T> result = new ArrayList<>(toMap.values());
		for (T item: toRemove)
			result.remove(item);
		
		for (T item: fromMap.values())
			result.add(item);
		
		return result;
	} 
	
	public static <T> List<T> applyUpdatesToList(List<T> sourceList, List<T> updatedList,
			ListComparator<T> comparator){
		return applyUpdatesToList(sourceList, updatedList, comparator, new ItemUpdater<T>() {

			@Override
			public void updateItem(T origin, T updated) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public static <T> List<T> applyUpdatesToList(List<T> sourceList, List<T> updatedList,
			ListComparator<T> comparator, ItemUpdater<T> itemUpdater){
		Map<String, T> sourceMap = new LinkedHashMap<>();
		Map<String, T> updatedMap = new LinkedHashMap<>();
		for (T item: sourceList) {
			sourceMap.put(comparator.itemHash(item), item);
		}
		for (T item: updatedList) {
			updatedMap.put(comparator.itemHash(item), item);
		}
		for (String sourceHash: sourceMap.keySet())
			if (!updatedMap.containsKey(sourceHash))
				sourceList.remove(sourceMap.get(sourceHash));
		for (String updatedHash: updatedMap.keySet()) {
			if (!sourceMap.containsKey(updatedHash))
				sourceList.add(updatedMap.get(updatedHash));
			else 
				itemUpdater.updateItem(
						sourceList.get(sourceList.indexOf(sourceMap.get(updatedHash))),
						updatedMap.get(updatedHash));
		}
		return sourceList;
	}
	
	public static <T> void updateAndApply(
			List<T> toList, 
			List<T> fromList,
			ListComparator<T> comparator, 
			ItemUpdater<T> itemUpdater) {
		
		List<T> updated = updateList(toList, fromList, comparator, false);
		applyUpdatesToList(toList, updated, comparator, itemUpdater);
		
	}
	
	public static boolean objectsNotNull(Object...objects) {
		if (objects==null)
			return false;
		for (int i=0; i<objects.length; i++)
			if (objects[i]==null)
				return false;
		return true;
	}
	
	public static boolean objectsAreNull(Object...objects) {
		if (objects==null)
			return true;
		for (int i=0; i<objects.length; i++)
			if (objects[i]!=null)
				return false;
		return true;
	}
	
	public static boolean objectsAreEqual(Object object1, Object object2) {
		if ((object1==null) && (object2==null))
			return true;
		if ((object1==null) && (object2!=null))
			return false;
		if ((object2==null) && (object1!=null))
			return false;
		return object1.equals(object2);
	}

	public static boolean listsAreEqual(List<?> list1, List<?> list2) {
		if ((list1==null) && (list2!=null))
			return false;
		if ((list2==null) && (list1!=null))
			return false;
		if ((list1==null) && (list2==null))
			return true;
		if (list1.size()!=list2.size())
			return false;
		int count = list1.size();
		for (int i=0; i<count; i++)
			if (!objectsAreEqual(list1.get(i), list2.get(i)))
				return false;
		return true;
	}
	
	public static boolean isValueable(String value){
		return ((value != null) && (value.trim().length()>0));
	}
	
	public static String getValueable(String value) {
		return getValueable(value, "");
	}
	
	public static String getValueable(String value, String defaultValue) {
		String def = isValueable(defaultValue)?defaultValue:"";
		return isValueable(value)?value:def;
	}
	
	public static boolean isTrue(Boolean bool){
		return (bool!=null) && (bool.booleanValue());
	}
	
	public static Map<Class<?>, Object> toTypedMap(Object[] objects, boolean unique){
		Map<Class<?>, Object> result = new HashMap<>();
		Map<Class<?>, List<Object>> temp = new HashMap<>();
		
		if (objects!=null)
			for (int i = 0; i < objects.length; i++) {
				Object object = objects[i];
				if (object==null)
					continue;
				if (!temp.containsKey(object.getClass()))
					temp.put(object.getClass(), new ArrayList<>());
				temp.get(object.getClass()).add(object);
			}
		
		for (Class<?> key: temp.keySet()) {
			if (unique)
				result.put(key, temp.get(key).get(0));
			else
				result.put(key, temp.get(key));
		}
		
		return result;
	}
}
