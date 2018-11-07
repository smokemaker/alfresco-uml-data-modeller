package ru.neodoc.content.codegen.sdoc2.utils.tree.impl;

import ru.neodoc.content.codegen.sdoc2.utils.tree.Tree;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItem;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeTraverser;

public class TreeImpl<T> extends SubTreeImpl<T> implements Tree<T> {

	protected TreeTraverser<T> mainTraverser = null;
	
	public TreeImpl() {
		super();
	}
	
	public TreeImpl(TreeItem<T> parent) {
		super(parent);
	}
	
	@Override
	public TreeTraverser<T> startTraverse() {
		TreeItem<T> up = parent;
		while (up!=null && !(up instanceof Tree))
			up = up.getParent();
		if (up!=null)
			return ((Tree<T>)up).startTraverse();
		if (mainTraverser==null) {
			mainTraverser = startNewTraverse();
		}
		return mainTraverser;
	}

	@Override
	public TreeTraverser<T> startNewTraverse() {
		return new TreeTraverserImpl<>(this);
	}

	@Override
	public TreeTraverser<T> startNewTraverse(TreeTraverser<T> origin) {
		return origin.copy(this);
	}

}
