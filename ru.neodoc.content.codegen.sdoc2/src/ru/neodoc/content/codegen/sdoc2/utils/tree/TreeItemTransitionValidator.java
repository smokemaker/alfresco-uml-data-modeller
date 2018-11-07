package ru.neodoc.content.codegen.sdoc2.utils.tree;

public interface TreeItemTransitionValidator<T> {

	boolean isValid(T objectFrom, T objectTo);
	
}
