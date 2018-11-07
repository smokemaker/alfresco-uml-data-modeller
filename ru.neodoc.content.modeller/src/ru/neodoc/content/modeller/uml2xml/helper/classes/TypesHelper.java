package ru.neodoc.content.modeller.uml2xml.helper.classes;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Types;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.uml2xml.helper.model.ModelHelper;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class TypesHelper extends AbstractSubHelperForContainerSingleton<Package, Model, Types> {

	static {
		HELPER_REGISTRY.register(TypesHelper.class, ModelHelper.class);
	}
	
	@Override
	protected Types getSingleObjectFromContainer(Model container) {
		return container.getTypes();
	}

	@Override
	protected void addSingleObjectToContainer(Types object, Model container) {
		container.setTypes(object);
	}

	@Override
	protected Types doCreateObject(Package element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createModelTypes();
	}

}
