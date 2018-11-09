package ru.neodoc.content.codegen.sdoc2.utils.tree.impl;

import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.codegen.sdoc2.utils.tree.Leaf;
import ru.neodoc.content.codegen.sdoc2.utils.tree.SubTree;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItem;

public class SubTreeImpl<T> extends TreeItemImpl<T> implements SubTree<T> {
	
	protected List<TreeItem<T>> items = new ArrayList<>();
	
	public SubTreeImpl() {
		super();
	}
	
	public SubTreeImpl(TreeItem<T> parent) {
		super(parent);
	}
	
	@Override
	public TreeItemList<T> getItems() {
		return new TreeItemListImpl<T>(items);
	}

	@Override
	public TreeItemList<T> getAllItems() {
		TreeItemList<T> result = getItems();
		for (TreeItem<T> item: items)
			result.addAll(item.getAllItems());
		return result;
	}

	@Override
	public void clear() {
		items.clear();
	}

	@Override
	public void add(T object) {
		add(new LeafImpl<T>(object));
	}

	@Override
	public void add(TreeItem<T> item) {
		items.add(item);
		item.setParent(this);
	}

	@Override
	public TreeItem<T> getNextItem(TreeItem<T> item) {
		int index = 0;
		if (item!=null)
			index = items.indexOf(item) + 1;
		if (index >= items.size())
			return null;
		else
			return items.get(index);
	}
	
	@Override
	public TreeItem<T> getTreeItem(T object) {
		List<TreeItem<T>> allItems = getAllItems();
		for (TreeItem<T> item: allItems)
			if (item instanceof Leaf) {
				@SuppressWarnings("unchecked")
				Leaf<T> leaf = (Leaf<T>)item;
				if (object==null && leaf.getObject()==null)
					return leaf;
				else if (object.equals(leaf.getObject()))
					return leaf;
			}
		return null;
	}
}
