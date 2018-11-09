package ru.neodoc.content.utils.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapOfList<K, V> implements Map<K, List<V>> {

	protected final Map<K, List<V>> map = new LinkedHashMap<>();
	
	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public List<V> get(Object key) {
		return map.get(key);
	}

	public List<V> getOrEmpty(Object key) {
		return map.getOrDefault(key, Collections.emptyList());
	}

	@Override
	public List<V> put(K key, List<V> value) {
		return map.put(key, value);
	}

	@Override
	public List<V> remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends List<V>> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<List<V>> values() {
		return map.values();
	}

	@Override
	public Set<Entry<K, List<V>>> entrySet() {
		return map.entrySet();
	}

	public void add(K key, V value) {
		if (!containsKey(key))
			put(key, new ArrayList<V>());
		get(key).add(value);
	}
	
	public void addUnique(K key, V value) {
		if (!containsKey(key))
			put(key, new ArrayList<V>());
		if (!get(key).contains(value))
			get(key).add(value);
	}
	
	public List<V> allValues(){
		List<V> result = new ArrayList<>();
		for (List<V> list: values())
			result.addAll(list);
		return result;
	}
	
	public List<V> allValuesDistinct(){
		List<V> result = new ArrayList<>();
		for (List<V> list: values()) {
			for (V value: list) {
				if (!result.contains(value))
					result.add(value);
			}
		}
		return result;
	}
	
	public void removeValue(K key, V value) {
		getOrEmpty(key).remove(value);
	}

	public void removeValue(V value) {
		for (K key: map.keySet())
			removeValue(key, value);
	}
}
