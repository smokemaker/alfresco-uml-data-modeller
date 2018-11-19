package ru.neodoc.content.modeller.uml2xml.helper.model;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Namespaces;
import org.eclipse.uml2.uml.Package;
import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class NamespacesHelper extends AbstractSubHelperForContainerSingleton<Package, Model, Model.Namespaces> {

	static {
		HELPER_REGISTRY.register(NamespacesHelper.class, ModelHelper.class);
	}
	
	@Override
	protected Namespaces getSingleObjectFromContainer(Model container) {
		return container.getNamespaces();
	}

	@Override
	protected void addSingleObjectToContainer(Namespaces object, Model container) {
		container.setNamespaces(object);
	}

	@Override
	protected Namespaces doCreateObject(Package element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createModelNamespaces();
	}


}
