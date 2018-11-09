package ru.neodoc.content.modeller.xml2uml.helper.relation.proxy;

import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo;

public abstract class AbstractRelationProxy extends AbstractProxy {

	protected RelationInfo relationInfo = null; 
	
	public AbstractRelationProxy(Object value) {
		super(value);
	}

	public AbstractRelationProxy(Object value, RelationInfo relationInfo) {
		this(value);
		setRelationInfo(relationInfo);
	}

	public RelationInfo getRelationInfo() {
		return relationInfo;
	}

	public void setRelationInfo(RelationInfo relationInfo) {
		this.relationInfo = relationInfo;
	}
	
}
