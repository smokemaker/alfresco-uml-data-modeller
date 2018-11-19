package ru.neodoc.content.codegen.sdoc2.utils.tree.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import ru.neodoc.content.codegen.sdoc2.utils.Skippable;
import ru.neodoc.content.codegen.sdoc2.utils.tree.Leaf;
import ru.neodoc.content.codegen.sdoc2.utils.tree.SubTree;
import ru.neodoc.content.codegen.sdoc2.utils.tree.Tree;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItem;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeTraverser;

public class TreeTraverserImpl<T> implements TreeTraverser<T> {

	protected static class Step<T> {
		protected TreeItem<T> item;
		protected boolean skipped;
		
		public Step(TreeItem<T> item) {
			super();
			this.item = item;
		}
		
	}
	
//	protected TreeItem<T> current = null;
	
	protected List<TreeItem<T>> traversed = new ArrayList<>();
//	protected int currentIndex = -1;
	
	protected Tree<T> root;
	
	public TreeTraverserImpl(Tree<T> root) {
		super();
		this.root = root;
	}
	
	protected int getCurrentIndex() {
		return traversed.size()-1;
	}

	protected TreeItem<T> getCurrentItem(){
		return getCurrentIndex()<0?null:traversed.get(getCurrentIndex());
	}
	
	@SuppressWarnings("unchecked")
	public TreeTraverserImpl(Tree<T> root, List<?> traversedOrigin) {
		this(root);
		for (Object obj: traversedOrigin)
			if (obj instanceof TreeItem)
				this.traversed.add((TreeItem<T>)obj);
			else this.traversed.add((new LeafImpl<T>((T)obj)));
	}
	
	protected TreeItem<T> getNextTreeItem(TreeItem<T> startItem){
		TreeItem<T> result = null;
		TreeItem<T> tested = startItem;
		TreeItem<T> parent = startItem.getParent();
		
		if (startItem instanceof SubTree) {
			SubTree<T> subTree = (SubTree<T>)startItem;
			if (subTree.getItems().size()>0) {
				return subTree.getNextItem(null);
			}
		}
		
		if (parent==null)
			return null;
		tested = startItem;
		result = parent.getNextItem(tested);
		while (parent!=null && (result==null)) {
			tested = parent;
			parent = parent.getParent();
			if (parent!=null)
				result = parent.getNextItem(tested);
		}
		return result;
	}
	
	protected Leaf<T> findFirstLeave(TreeItem<T> parent){
		if (parent instanceof Leaf)
			return (Leaf<T>)parent;
		for (TreeItem<T> item: parent.getItems()) {
			Leaf<T> found = findFirstLeave(item);
			if (found!=null)
				return found;
		}
		return null;	
	}
	
	protected boolean isAllowedTransition(Leaf<T> leaveFrom, Leaf<T> leaveTo) {
		return true;
	}
	
	protected Leaf<T> findLastTraversedLeave(TreeItem<T> from){
		int index = traversed.lastIndexOf(from);
		if (from==null)
			index = traversed.size()-1;
		for(int i=index; i>=0; i--)
			if (traversed.get(i) instanceof Leaf)
				return (Leaf<T>)traversed.get(i);
		return null;
	}
	
	@Override
	public Leaf<T> getNext() {
		return getOrMoveNext(false);
	}
		
	protected Leaf<T> getOrMoveNext(boolean move) {
		Leaf<T> leafFrom = null;
		Leaf<T> leafTo = null;
		
		if (getCurrentItem()!=null) {
			leafFrom = findLastTraversedLeave(getCurrentItem());
		}

		Leaf<T> result = null;
		boolean hasMoreItems = true;
		TreeItem<T> temp = null;
		
		while (result==null && hasMoreItems) {
			if (leafFrom == null)
				temp = findFirstLeave(root);
			else {
				temp = getNextTreeItem(leafFrom);
				while (temp!=null && (!(temp instanceof Leaf) || !temp.isAvailable()))
					temp = getNextTreeItem(temp);
			}
			if (temp==null) {
				hasMoreItems = false;
				continue;
			}
			leafTo = (Leaf<T>)temp;
			
			if (isAllowedTransition(leafFrom, leafTo)) {
				if (move)
					traversed.add(leafTo);
				if (isToSkip(leafTo, move)) {
					leafFrom = leafTo;
				} else {
					result = leafTo;
				}
			}
		}
		return result;
	}

	protected boolean isToSkip(Leaf<T> item, boolean doSkip) {
		Object obj = item.getObject();
		if (obj==null)
			return false;
		Skippable skippable = null;
		if (obj instanceof Skippable)
			skippable = (Skippable)obj;
		if (skippable==null)
			return false;
		if (!skippable.canSkip())
			return false;
		if (doSkip)
			skippable.skipped();
		return true;
	}
	
	protected void moveTo(TreeItem<T> target) {
		
	}
	
	@Override
	public Leaf<T> getPrev() {
		ListIterator<TreeItem<T>> li = traversed.listIterator(traversed.size()-1);
		TreeItem<T> temp = null;
		Leaf<T> result = null;
		while (result==null && li.hasPrevious()) {
			temp = li.previous();
			if (temp instanceof Leaf)
				result = (Leaf<T>)temp;
		}
		return result;
	}

	@Override
	public Leaf<T> goNext() {
		return getOrMoveNext(true);
	}

	@Override
	public Leaf<T> goPrev() {
		Leaf<T> target = getPrev();
		int index = traversed.lastIndexOf(target);
		traversed = traversed.subList(0, index+1);
		return target;
	}

	@Override
	public Leaf<T> getCurrent() {
		return findLastTraversedLeave(getCurrentItem());
	}

	@Override
	public void setCurrent(T current) {
		TreeItem<T> item = root.getRoot().getTreeItem(current);
		if (item==null || current==null) {
			 traversed.clear();
			 return;
		}
		int index = traversed.indexOf(item);
		if (index<0) {
			traversed.clear();
			traversed.add(item);
			return;
		} else {
			traversed = traversed.subList(0, index);
		}
		
	}

	@Override
	public TreeTraverser<T> copy() {
		return new TreeTraverserImpl<>(root, traversed);
	}

	@Override
	public TreeTraverser<T> copy(Tree<T> newRoot) {
		return new TreeTraverserImpl<>(newRoot, traversed);
	}
}
