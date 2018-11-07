package ru.neodoc.content.modeller.xml2uml.helper.entity;

import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Aspect;
import org.alfresco.model.dictionary._1.Model;
import org.eclipse.uml2.uml.Class;

import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class AspectHelper extends ClassHelper<Aspect> {

	static {
		HELPER_REGISTRY.register(AspectHelper.class, ModelHelper.class, Class.class, Aspect.class);
	}
	
	@Override
	protected String initStereotypeToSearch() {
		return AlfrescoProfile.ForClass.Aspect._NAME;
	}

	@Override
	protected List<Aspect> getElementsFromContainer(Model container) {
		return container.getAspects()==null
				?Collections.emptyList()
				:container.getAspects().getAspect()==null
					?Collections.emptyList()
					:container.getAspects().getAspect();
	}

	@Override
	protected ClassMain getClassMain(StereotypedElement stereotypedElement, ModelObject<Aspect> object) {
		return stereotypedElement.getOrCreate(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Aspect.class);
	}
	
}
