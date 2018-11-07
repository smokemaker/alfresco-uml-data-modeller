package ru.neodoc.content.modeller.uml2xml.helper.classes;

import java.util.List;

import org.alfresco.model.dictionary._1.Aspect;
import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Aspects;

import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class AspectHelper extends AbstractClassHelper<
		Model.Aspects, 
		Aspect, 
		AlfrescoProfile.ForClass.Aspect> {

	static {
		HELPER_REGISTRY.register(AspectHelper.class, AspectsHelper.class).asContained();
	}

	@Override
	protected String getStereotypeName() {
		return AlfrescoProfile.ForClass.Aspect._NAME;
	}

	@Override
	protected Class<AlfrescoProfile.ForClass.Aspect> getStereotypeClass() {
		return AlfrescoProfile.ForClass.Aspect.class;
	}

	@Override
	protected List<Aspect> addObjectsToContainer(Aspects container, List<Aspect> objectsToAdd) {
		container.getAspect().addAll(objectsToAdd);
		return container.getAspect();
	}

	@Override
	public List<Aspect> getObjects(Aspects container) {
		return container.getAspect();
	}

	@Override
	protected Aspect doCreateObject(org.eclipse.uml2.uml.Class element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createAspect();
	}
	
	
}
