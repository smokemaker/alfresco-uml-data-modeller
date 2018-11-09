package ru.neodoc.content.modeller.uml2xml.helper.classes;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Aspects;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.uml2xml.helper.model.ModelHelper;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class AspectsHelper extends AbstractSubHelperForContainerSingleton<Package, Model, Aspects> {

	static {
		HELPER_REGISTRY.register(AspectsHelper.class, ModelHelper.class);
	}
	
	@Override
	protected Aspects getSingleObjectFromContainer(Model container) {
		return container.getAspects();
	}

	@Override
	protected void addSingleObjectToContainer(Aspects object, Model container) {
		container.setAspects(object);
	}

	@Override
	protected Aspects doCreateObject(Package element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createModelAspects();
	}

}
