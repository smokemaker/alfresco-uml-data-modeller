package ru.neodoc.content.modeller.xml2uml.helper.relation.proxy;

import java.util.UUID;

import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo;

public class DependencyProxy extends AbstractRelationProxy {

	public DependencyProxy(RelationInfo relationInfo) {
		super(UUID.randomUUID().toString(), relationInfo);
	}

	
}
