package ru.neodoc.content.codegen.sdoc2.utils.tree.impl;

import ru.neodoc.content.codegen.sdoc2.utils.tree.Leaf;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItem;

public class LeafImpl<T> extends TreeItemImpl<T> implements Leaf<T> {

	protected T object;
	
	public LeafImpl(T object) {
		super();
		setObject(object);
	}

	public LeafImpl(T object, TreeItem<T> parent) {
		super(parent);
		setObject(object);
	}
	
	@Override
	public T getObject() {
		return object;
	}

	@Override
	public void setObject(T object) {
		this.object = object;
	}

	@Override
	public TreeItemList<T> getItems() {
		return new TreeItemListImpl<T>();
	}

	@Override
	public TreeItemList<T> getAllItems() {
		return new TreeItemListImpl<T>();
	}

	@Override
	public void clear() {
		
	}

	@Override
	public void add(T object) {
		
	}

	@Override
	public void add(TreeItem<T> item) {
		
	}

	@Override
	public TreeItem<T> getNextItem(TreeItem<T> item) {
		return null;
	}
	
	@Override
	public TreeItem<T> getTreeItem(T object) {
		if (object!=null && object.equals(this.object))
			return this;
		return null;
	}
}