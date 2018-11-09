package ru.neodoc.content.modeller.uml2xml.helper.model;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.ObjectFactory;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractHelper;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.modeller.utils.UML2XML;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class ModelHelper extends AbstractHelper<Package, Model, AlfrescoProfile.ForPackage.Model> {

	static {
		HELPER_REGISTRY.register(ModelHelper.class);
	}
	
	@Override
	protected Class<AlfrescoProfile.ForPackage.Model> getStereotypeClass() {
		return AlfrescoProfile.ForPackage.Model.class;
	}

	@Override
	protected Model doCreateObject(Package element) {
		return (new ObjectFactory()).createModel();
	}

	@Override
	protected Model doFillObjectProperties(Model object, Package element,
			AlfrescoProfile.ForPackage.Model stereotyped) {
		UML2XML.fillModel(element, object);
		return object;
	}

	@Override
	protected void updateProperties(Model objectToUpdate, ObjectContainer<Model> newObjectContainer) {
		objectToUpdate.setAuthor(newObjectContainer.getObject().getAuthor());
		objectToUpdate.setDescription(newObjectContainer.getObject().getDescription());
		objectToUpdate.setName(newObjectContainer.getObject().getName());
		objectToUpdate.setPublished(newObjectContainer.getObject().getPublished());
		objectToUpdate.setVersion(newObjectContainer.getObject().getVersion());
	}
	
}
