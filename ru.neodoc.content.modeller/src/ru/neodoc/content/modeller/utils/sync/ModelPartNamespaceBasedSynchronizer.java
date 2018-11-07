package ru.neodoc.content.modeller.utils.sync;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.profile.alfresco.AlfrescoProfileUtils;

public abstract class ModelPartNamespaceBasedSynchronizer<ContainerType, ContainerElementType> extends
		ModelPartSynchronizer<ContainerType, ContainerElementType> {

	public ModelPartNamespaceBasedSynchronizer(Package umlModel, Model xmlModel) {
		super(umlModel, xmlModel);
	}	
	
	@Override
	protected List<ContainerElementType> getUmlList() {
		List<Package> namespaces = AlfrescoProfileUtils.getNamespaces(this.umlModel);
		List<ContainerElementType> result = new ArrayList<>();
		
		for (Package namespace: namespaces) {
			List<ContainerElementType> tmpList = getUmlListForNamespace(namespace);
			if (tmpList!=null && tmpList.size()>0)
				result.addAll(tmpList);
		}
		
		return result;
	}

	protected abstract List<ContainerElementType> getUmlListForNamespace(Package namespace); 

}
