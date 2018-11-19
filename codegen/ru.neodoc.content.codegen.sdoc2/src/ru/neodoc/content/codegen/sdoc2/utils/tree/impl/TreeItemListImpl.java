package ru.neodoc.content.codegen.sdoc2.utils.tree.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ru.neodoc.content.codegen.sdoc2.utils.tree.Leaf;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItem;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItem.TreeItemList;

public class TreeItemListImpl<T> implements TreeItemList<T> {

	protected List<TreeItem<T>> list = new ArrayList<>();
	
	public TreeItemListImpl() {
		super();
	}
	
	public TreeItemListImpl(List<TreeItem<T>> list) {
		super();
		this.list.addAll(list);
	}
	
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
	public Iterator<TreeItem<T>> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <AT> AT[] toArray(AT[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean add(TreeItem<T> e) {
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
	public boolean addAll(Collection<? extends TreeItem<T>> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends TreeItem<T>> c) {
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
	public TreeItem<T> get(int index) {
		return list.get(index);
	}

	@Override
	public TreeItem<T> set(int index, TreeItem<T> element) {
		return list.set(index, element);
	}

	@Override
	public void add(int index, TreeItem<T> element) {
		list.add(index, element);
	}

	@Override
	public TreeItem<T> remove(int index) {
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
	public ListIterator<TreeItem<T>> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<TreeItem<T>> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<TreeItem<T>> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public List<T> getObjects() {
		List<T> result = new ArrayList<>();
		for (TreeItem<T> item: list)
			if (item instanceof Leaf)
				result.add(((Leaf<T>)item).getObject());
		return result;
	}

}
