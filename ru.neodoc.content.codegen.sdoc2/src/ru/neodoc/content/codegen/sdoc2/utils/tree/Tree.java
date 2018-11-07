package ru.neodoc.content.codegen.sdoc2.utils.tree;

public interface Tree<T> extends SubTree<T> {

	TreeTraverser<T> startTraverse();
	TreeTraverser<T> startNewTraverse();
	TreeTraverser<T> startNewTraverse(TreeTraverser<T> origin);

}
