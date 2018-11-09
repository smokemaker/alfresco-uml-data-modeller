package ru.neodoc.content.utils.uml.search.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class UMLSearchConvertibleListImpl<T> implements UMLSearchConvertibleList<T> {

	protected List<T> list = new ArrayList<>();

	public UMLSearchConvertibleListImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public UMLSearchConvertibleListImpl(List<T> originList) {
		this();
		this.list.addAll(originList);
	}
	
	@Override
	public boolean add(T e) {
		return list.add(e);
	}

	@Override
	public void add(int index, T element) {
		list.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return list.addAll(index, c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public T get(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public T remove(int index) {
		return list.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public T set(int index, T element) {
		return list.set(index, element);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <U> U[] toArray(U[] a) {
		return list.toArray(a);
	}

	@Override
	public <S> UMLSearchConvertibleList<S> convert(UMLSearchConverter<? super T, S> converter) {
		UMLSearchConvertibleList<S> result = new UMLSearchConvertibleListImpl<>();
		for(T entry: list)
			if (converter.isConvertible(entry)) {
				S converted = converter.convert(entry);
				if (converted!=null)
					result.add(converted);
			}
		return result;
	}

	@Override
	public T first() {
		return list==null || list.isEmpty()?null:list.get(0);
	}
}
