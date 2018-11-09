package ru.neodoc.content.modeller.xml2uml.helper.entity;

import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Type;
import org.eclipse.uml2.uml.Class;

import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class TypeHelper extends ClassHelper<Type> {

	static {
		HELPER_REGISTRY.register(TypeHelper.class, ModelHelper.class, Class.class, Type.class);
	}
	
	@Override
	protected List<Type> getElementsFromContainer(Model container) {
		return container.getTypes()==null
				?Collections.emptyList()
				:container.getTypes().getType()==null
					?Collections.emptyList()
					:container.getTypes().getType();
	}

	@Override
	protected String initStereotypeToSearch() {
		return AlfrescoProfile.ForClass.Type._NAME;
	}
	
	@Override
	protected ClassMain getClassMain(StereotypedElement stereotypedElement, ModelObject<Type> object) {
		return stereotypedElement.getOrCreate(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Type.class);
	}

}
