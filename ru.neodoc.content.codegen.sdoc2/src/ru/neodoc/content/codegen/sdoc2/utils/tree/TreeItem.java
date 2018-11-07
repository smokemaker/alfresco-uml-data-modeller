package ru.neodoc.content.codegen.sdoc2.utils.tree;

import java.util.List;

public interface TreeItem<T> {

	public static interface TreeItemList<T> extends List<TreeItem<T>> {
		List<T> getObjects();
	}
	
	public static interface AvailabilityProvider {
		boolean isAvailable(TreeItem<?> treeItem);
	}
	
	TreeItemList<T> getItems();
	TreeItemList<T> getAllItems();
	void clear();
	
	TreeItem<T> getNextItem(TreeItem<T> item);
	TreeItem<T> getTreeItem(T object);
	
	void add(T object);
	void add(TreeItem<T> item);
	
	TreeItem<T> getParent();
	void setParent(TreeItem<T> parent);
	TreeItem<T> getRoot();
	
	boolean isRoot();
	
	void setAvailbalityProvider(AvailabilityProvider availabilityProvider);
	boolean isAvailable();
	
	List<TreeItemTransitionValidator<T>> getValidators();
	List<TreeItemTransitionValidator<T>> getOwnValidators();
	void addValidator(TreeItemTransitionValidator<T> validator);
}
