package ru.neodoc.content.modeller.uml2xml.helper.classes;

import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Types;
import org.alfresco.model.dictionary._1.Type;

import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class TypeHelper extends AbstractClassHelper<
		Model.Types, 
		Type, 
		AlfrescoProfile.ForClass.Type> {

	static {
		HELPER_REGISTRY.register(TypeHelper.class, TypesHelper.class).asContained();
	}

	@Override
	protected String getStereotypeName() {
		return AlfrescoProfile.ForClass.Type._NAME;
	}

	@Override
	protected Class<ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Type> getStereotypeClass() {
		return AlfrescoProfile.ForClass.Type.class;
	}

	@Override
	protected List<Type> addObjectsToContainer(Types container, List<Type> objectsToAdd) {
		container.getType().addAll(objectsToAdd);
		return container.getType();
	}

	@Override
	public List<Type> getObjects(Types container) {
		return container.getType();
	}

	@Override
	protected Type doCreateObject(org.eclipse.uml2.uml.Class element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createType();
	}
	
	
}
