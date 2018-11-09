package ru.neodoc.content.codegen.sdoc2.utils.tree.impl;

import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItem;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItemTransitionValidator;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItem.AvailabilityProvider;

public abstract class TreeItemImpl<T> implements TreeItem<T> {

	protected TreeItem<T> parent = null;
	protected List<TreeItemTransitionValidator<T>> validators = new ArrayList<>();

	protected AvailabilityProvider availabilityProvider;
	
	public TreeItemImpl() {
		super();
	}
	
	public TreeItemImpl(TreeItem<T> parent) {
		this();
		this.parent = parent;
	}
	
	@Override
	public TreeItem<T> getParent() {
		return this.parent;
	}
	
	@Override
	public void setParent(TreeItem<T> parent) {
		this.parent = parent;
	}
	
	@Override
	public TreeItem<T> getRoot() {
		if (isRoot())
			return this;
		return getParent().getRoot();
	}

	@Override
	public boolean isRoot() {
		return this.getParent()==null;
	}

	@Override
	public void setAvailbalityProvider(AvailabilityProvider availabilityProvider) {
		this.availabilityProvider = availabilityProvider;
	}
	
	@Override
	public boolean isAvailable() {
		boolean result = true;
		if (availabilityProvider!=null)
			result = availabilityProvider.isAvailable(this);
		return result && ((parent!=null)?parent.isAvailable():true);
	}
	
	@Override
	public List<TreeItemTransitionValidator<T>> getValidators() {
		List<TreeItemTransitionValidator<T>> result = new ArrayList<>();
		result.addAll(getOwnValidators());
		if (parent!=null)
			result.addAll(parent.getValidators());
		return result;
	}
	
	@Override
	public List<TreeItemTransitionValidator<T>> getOwnValidators() {
		return this.validators;
	}
	
	@Override
	public void addValidator(TreeItemTransitionValidator<T> validator) {
		validators.add(validator);
	}
	
}
