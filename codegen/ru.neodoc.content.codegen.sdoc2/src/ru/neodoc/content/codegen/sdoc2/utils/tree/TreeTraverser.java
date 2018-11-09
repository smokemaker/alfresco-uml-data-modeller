package ru.neodoc.content.codegen.sdoc2.utils.tree;

public interface TreeTraverser<T> {
	
	Leaf<T> getNext();
	Leaf<T> getPrev();
	Leaf<T> goNext();
	Leaf<T> goPrev();
	
	Leaf<T> getCurrent();
	void setCurrent(T current);
	
	TreeTraverser<T> copy();
	TreeTraverser<T> copy(Tree<T> newRoot);
}
