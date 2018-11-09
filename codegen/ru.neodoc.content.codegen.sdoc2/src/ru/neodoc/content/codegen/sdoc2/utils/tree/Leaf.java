package ru.neodoc.content.codegen.sdoc2.utils.tree;

public interface Leaf<T> extends TreeItem<T> {
	
	T getObject();
	void setObject(T object);
	
}
