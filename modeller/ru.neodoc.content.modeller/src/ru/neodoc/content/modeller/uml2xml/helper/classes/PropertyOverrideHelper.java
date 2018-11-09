package ru.neodoc.content.modeller.uml2xml.helper.classes;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.model.dictionary._1.Class.Overrides;
import org.alfresco.model.dictionary._1.PropertyOverride;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ListComparator;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelper;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class PropertyOverrideHelper extends 
		AbstractSubHelper<
			Class, 
			Dependency, 
			Overrides, 
			PropertyOverride, 
			AlfrescoProfile.ForDependency.PropertyOverride> {

	static {
		HELPER_REGISTRY.register(PropertyOverrideHelper.class, OverridesHelper.class).asContained();
	}
	
	@Override
	public List<Dependency> getSubElements(Class container) {
		List<Dependency> result = new ArrayList<>();
		AlfrescoProfile.ForClass.ClassMain classMain = AbstractProfile.asUntyped(container).get(AlfrescoProfile.ForClass.ClassMain.class);
		if (classMain==null)
			return result;
		for (AlfrescoProfile.ForDependency.PropertyOverride propertyOverride: classMain.getAllPropertyOverrides())
			result.add(propertyOverride.getElementClassified());
		return result;
	}

	@Override
	protected List<ObjectContainer<PropertyOverride>> addObjectsToContainer(ObjectContainer<Overrides> container,
			List<ObjectContainer<PropertyOverride>> objectsToAdd) {
		getObjects(container.getObject()).addAll(ObjectContainer.FACTORY.extractTyped(objectsToAdd));
		return ObjectContainer.FACTORY.createListTyped(getObjects(container.getObject()));
	}

	@Override
	public ListComparator<PropertyOverride> getComparator() {
		return new CommonUtils.BaseListComparator<PropertyOverride>() {

			@Override
			public boolean equals(PropertyOverride item1, PropertyOverride item2) {
				// always update
				return false;
			}
			
			@Override
			public String itemHash(PropertyOverride item) {
				if (item==null)
					return "";
				return item.getName();
			}
		};
	}

	@Override
	protected List<PropertyOverride> getOrCreateObjects(Overrides container) {
		return getObjects(container);
	}

	@Override
	public List<PropertyOverride> getObjects(Overrides container) {
		return container.getProperty();
	}

	@Override
	protected java.lang.Class<AlfrescoProfile.ForDependency.PropertyOverride> getStereotypeClass() {
		return AlfrescoProfile.ForDependency.PropertyOverride.class;
	}

	@Override
	protected PropertyOverride doCreateObject(Dependency element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createPropertyOverride();
	}

	@Override
	protected PropertyOverride doFillObjectProperties(PropertyOverride object, Dependency element,
			AlfrescoProfile.ForDependency.PropertyOverride stereotyped) {
		object.setName(stereotyped.getOverridenProperty().getPrfixedName());
		object.setDefault(stereotyped.getDefaultValue());
		object.setMandatory(stereotyped.getMandatory());
		return object;
	}

	@Override
	protected void updateProperties(PropertyOverride objectToUpdate,
			ObjectContainer<PropertyOverride> newObjectContainer) {
		objectToUpdate.setName(newObjectContainer.getObject().getName());
		objectToUpdate.setDefault(newObjectContainer.getObject().getDefault());
		objectToUpdate.setMandatory(newObjectContainer.getObject().isMandatory());
	}

}
