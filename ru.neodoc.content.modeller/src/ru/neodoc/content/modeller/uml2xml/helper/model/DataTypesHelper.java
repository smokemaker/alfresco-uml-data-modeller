package ru.neodoc.content.modeller.uml2xml.helper.model;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.DataTypes;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class DataTypesHelper extends AbstractSubHelperForContainerSingleton<Package, Model, DataTypes> {

	static {
		HELPER_REGISTRY.register(DataTypesHelper.class, ModelHelper.class);
	}
	
	@Override
	protected DataTypes getSingleObjectFromContainer(Model container) {
		return container.getDataTypes();
	}

	@Override
	protected void addSingleObjectToContainer(DataTypes object, Model container) {
		container.setDataTypes(object);
	}

	@Override
	protected DataTypes doCreateObject(Package element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createModelDataTypes();
	}

}
