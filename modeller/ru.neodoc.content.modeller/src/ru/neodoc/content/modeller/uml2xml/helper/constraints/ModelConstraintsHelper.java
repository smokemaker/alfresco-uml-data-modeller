package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Constraints;
import org.eclipse.uml2.uml.Package;
import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.uml2xml.helper.model.ModelHelper;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class ModelConstraintsHelper extends AbstractSubHelperForContainerSingleton<Package, Model, Constraints> {

	static {
		HELPER_REGISTRY.register(ModelConstraintsHelper.class, ModelHelper.class);
	}
	
	@Override
	protected Constraints getSingleObjectFromContainer(Model container) {
		return container.getConstraints();
	}

	@Override
	protected void addSingleObjectToContainer(Constraints object, Model container) {
		container.setConstraints(object);
	}

	@Override
	protected Constraints doCreateObject(Package element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createModelConstraints();
	}

}
