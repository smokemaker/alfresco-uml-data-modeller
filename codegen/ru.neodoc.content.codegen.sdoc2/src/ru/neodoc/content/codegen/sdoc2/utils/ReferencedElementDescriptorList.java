package ru.neodoc.content.codegen.sdoc2.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReferencedElementDescriptorList implements List<ReferencedElementDescriptor> {

	protected List<ReferencedElementDescriptor> list = new ArrayList<>();
	
	public int ignoredCount() {
		int result = 0;
		for (ReferencedElementDescriptor red: list)
			if (red.isIgnored)
				result++;
		return result;
	}
	
	public boolean hasIgnored() {
		return this.ignoredCount()>0;
	}
	
	public ReferencedElementDescriptorList sort() {
		Collections.sort(this.list, new Comparator<ReferencedElementDescriptor>() {

			@Override
			public int compare(ReferencedElementDescriptor o1, ReferencedElementDescriptor o2) {
				if (o1==null)
					return -1;
				if (o2==null)
					return 1;
				return o1.getElementName().compareTo(o2.getElementName());
			}
			
		});
		return this;
	}
	
	public String concat() {
		boolean isFirst = true;
		StringBuffer result = new StringBuffer("");
		for (ReferencedElementDescriptor red: this.list) {
			if (!isFirst)
				result.append(",");
			if(red.isIgnored)
				result.append("#");
			result.append(red.getElementName());
			if(red.isIgnored)
				result.append("#");
			isFirst = false;
		}
		return result.toString();
	}
	
	//
	// java.util.List implementation
	//
	
	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Iterator<ReferencedElementDescriptor> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean add(ReferencedElementDescriptor e) {
		return list.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends ReferencedElementDescriptor> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends ReferencedElementDescriptor> c) {
		return list.addAll(index, c);
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
	public void clear() {
		list.clear();
	}

	@Override
	public ReferencedElementDescriptor get(int index) {
		return list.get(index);
	}

	@Override
	public ReferencedElementDescriptor set(int index, ReferencedElementDescriptor element) {
		return list.set(index, element);
	}

	@Override
	public void add(int index, ReferencedElementDescriptor element) {
		list.add(index, element);
	}

	@Override
	public ReferencedElementDescriptor remove(int index) {
		return list.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<ReferencedElementDescriptor> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<ReferencedElementDescriptor> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<ReferencedElementDescriptor> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

}
