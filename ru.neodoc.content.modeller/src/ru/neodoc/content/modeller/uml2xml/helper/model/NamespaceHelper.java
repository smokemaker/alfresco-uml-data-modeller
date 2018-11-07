package ru.neodoc.content.modeller.uml2xml.helper.model;

import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Model.Namespaces;
import org.alfresco.model.dictionary._1.Model.Namespaces.Namespace;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelper;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer.FACTORY;
import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ListComparator;

public class NamespaceHelper extends
		AbstractSubHelper<Package, Package, Namespaces, Namespace, AlfrescoProfile.ForPackage.Namespace> {

	static {
		HELPER_REGISTRY.register(NamespaceHelper.class, NamespacesHelper.class);
	}
	
	@Override
	public List<Package> getSubElements(Package container) {
		AlfrescoProfile.ForPackage.Model model = AlfrescoProfile.ForPackage.Model._HELPER.findNearestFor(container);
		if (model==null)
			return Collections.emptyList();
		return AlfrescoProfile.ForPackage.Namespace._HELPER.getElementList(model.getAllNamespaces());
	}

	@Override
	protected List<ObjectContainer<Namespace>> addObjectsToContainer(ObjectContainer<Namespaces> container,
			List<ObjectContainer<Namespace>> objectsToAdd) {
		getObjects(container.getObject()).addAll(ObjectContainer.FACTORY.extractTyped(objectsToAdd));
		return ObjectContainer.FACTORY.createListTyped(getObjects(container.getObject()));
	}

	@Override
	public ListComparator<Namespace> getComparator() {
		return new CommonUtils.BaseListComparator<Namespace>() {

			@Override
			public String itemHash(Namespace item) {
				if (item==null)
					return "";
				return item.getPrefix() + "::" + item.getUri();
			}
			
		};
	}

	@Override
	protected List<Namespace> getOrCreateObjects(Namespaces container) {
		return getObjects(container);
	}

	@Override
	public List<Namespace> getObjects(Namespaces container) {
		return container.getNamespace();
	}

	@Override
	protected Class<AlfrescoProfile.ForPackage.Namespace> getStereotypeClass() {
		return AlfrescoProfile.ForPackage.Namespace.class;
	}

	@Override
	protected Namespace doCreateObject(Package element) {
		AlfrescoProfile.ForPackage.Namespace ns = AlfrescoProfile.ForPackage.Namespace._HELPER.getFor(element);
		if (ns!=null)
			return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createModelNamespacesNamespace();
		return null;
	}

	@Override
	protected Namespace doFillObjectProperties(Namespace object, Package element,
			AlfrescoProfile.ForPackage.Namespace stereotyped) {
		object.setPrefix(stereotyped.getPrefix());
		object.setUri(stereotyped.getUri());
		return object;
	}

	@Override
	protected void updateProperties(Namespace objectToUpdate, ObjectContainer<Namespace> newObjectContainer) {
		objectToUpdate.setPrefix(newObjectContainer.getObject().getPrefix());
		objectToUpdate.setUri(newObjectContainer.getObject().getUri());

	}

}
