package ru.neodoc.content.modeller.uml2xml.helper.model;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Imports;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class ImportsHelper extends AbstractSubHelperForContainerSingleton<
				Package,
				Model,
				Model.Imports> {

	static {
		HELPER_REGISTRY.register(ImportsHelper.class, ModelHelper.class);
	}
	
/*	@Override
	protected List<Imports> getOrCreateObjects(Model container) {
		if (container.getImports()==null)
			container.setImports(JaxbUtils.ALFRESCO_OBJECT_FACTORY.createModelImports());
		return getObjects(container);
	}
*/
	@Override
	protected Imports getSingleObjectFromContainer(Model container) {
		return container.getImports();
	}
	
	@Override
	protected Imports doCreateObject(Package element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createModelImports();
	}

	@Override
	protected void addSingleObjectToContainer(Imports object, Model container) {
		container.setImports(object);
	}
	
}
