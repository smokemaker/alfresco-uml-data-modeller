package ru.neodoc.content.modeller.uml2xml.helper.classes;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.model.dictionary._1.MandatoryDef;
import org.alfresco.model.dictionary._1.Class.Properties;
import org.alfresco.model.dictionary._1.Property.Index;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Encrypted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Mandatory;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ListComparator;
import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelper;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.modeller.utils.JaxbUtils;

public class PropertyHelper extends AbstractSubHelper<
			org.eclipse.uml2.uml.Class,
			Property,
			org.alfresco.model.dictionary._1.Class.Properties,
			org.alfresco.model.dictionary._1.Property,
			AlfrescoProfile.ForProperty.Property
		> {

	static {
		HELPER_REGISTRY.register(PropertyHelper.class, PropertiesHelper.class);
	}

	@Override
	public List<Property> getSubElements(Class container) {
		List<Property> result = new ArrayList<>();
		for (Property property: container.getOwnedAttributes())
			if (AlfrescoProfile.ForProperty.Property._HELPER.is(property))
				result.add(property);
		return result;
	}

	@Override
	protected List<ObjectContainer<org.alfresco.model.dictionary._1.Property>> addObjectsToContainer(
			ObjectContainer<Properties> container,
			List<ObjectContainer<org.alfresco.model.dictionary._1.Property>> objectsToAdd) {
		container.getObject().getProperty().addAll(ObjectContainer.FACTORY.extractTyped(objectsToAdd));
		return ObjectContainer.FACTORY.createListTyped(container.getObject().getProperty());
	}

	@Override
	public ListComparator<org.alfresco.model.dictionary._1.Property> getComparator() {
		return new CommonUtils.BaseListComparator<org.alfresco.model.dictionary._1.Property>(){

			@Override
			public boolean equals(org.alfresco.model.dictionary._1.Property item1,
					org.alfresco.model.dictionary._1.Property item2) {
				// always update
				return false;
			}
			
			@Override
			public String itemHash(org.alfresco.model.dictionary._1.Property item) {
				if (item==null)
					return "";
				return item.getName();
			}
			
		};
	}

	@Override
	protected List<org.alfresco.model.dictionary._1.Property> getOrCreateObjects(Properties container) {
		return getObjects(container);
	}

	@Override
	public List<org.alfresco.model.dictionary._1.Property> getObjects(Properties container) {
		return container.getProperty();
	}

	@Override
	protected java.lang.Class<ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property> getStereotypeClass() {
		return AlfrescoProfile.ForProperty.Property.class;
	}

	@Override
	protected org.alfresco.model.dictionary._1.Property doCreateObject(Property element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createProperty();
	}

	@Override
	protected org.alfresco.model.dictionary._1.Property doFillObjectProperties(
			org.alfresco.model.dictionary._1.Property object, Property element,
			ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property stereotyped) {
		
		object.setName(stereotyped.getPrfixedName());
		
		if (stereotyped.getDataType()!=null)
			object.setType(stereotyped.getDataType().getPrfixedName());
		else
			object.setType(null);
		
		object.setTitle(stereotyped.getTitle());
		object.setDescription(stereotyped.getDescription());
		
		object.setProtected(new Boolean(stereotyped.isProtected()));

		/*
		 * index
		 */
		AlfrescoProfile.ForProperty.Index umlIndex = stereotyped.get(AlfrescoProfile.ForProperty.Index.class);
		if (umlIndex!=null) {
			Index index = JaxbUtils.ALFRESCO_OBJECT_FACTORY.createPropertyIndex();
			
			index.setEnabled(umlIndex.isEnabled());
			index.setAtomic(umlIndex.isAtomic());
			index.setStored(umlIndex.isStored());
			index.setTokenised(umlIndex.getTokenised());
			
			object.setIndex(index);
		} else {
			object.setIndex(null);
		}
		
		object.setEncrypted(new Boolean(stereotyped.has(Encrypted.class)));

		/*
		 * mandatory
		 */
		Mandatory umlMandatory = stereotyped.get(Mandatory.class);
		boolean mandatory = (stereotyped.getElementClassified()).getLower()>0;
		Boolean enforcedValue = null;
		if (umlMandatory!=null){
			enforcedValue = umlMandatory.isEnforced();
		}
		if (mandatory || enforcedValue!=null) {
			MandatoryDef md = JaxbUtils.ALFRESCO_OBJECT_FACTORY.createMandatoryDef();
			md.setContent(mandatory?"true":"false");
			if (enforcedValue!=null)
				md.setEnforced(enforcedValue);
			object.setMandatory(md);
		}
		
		object.setMultiple(stereotyped.isMultiple());
		
		object.setDefault(stereotyped.getElementClassified().getDefault());
		
		return object;
	}

	@Override
	protected void updateProperties(org.alfresco.model.dictionary._1.Property objectToUpdate,
			ObjectContainer<org.alfresco.model.dictionary._1.Property> newObjectContainer) {
		
		org.alfresco.model.dictionary._1.Property newProperty = newObjectContainer.getObject();
		
		objectToUpdate.setName(newProperty.getName());
		objectToUpdate.setTitle(newProperty.getTitle());
		objectToUpdate.setDescription(newProperty.getDescription());
		
		objectToUpdate.setType(newProperty.getType());
		
		objectToUpdate.setDefault(newProperty.getDefault());
		objectToUpdate.setEncrypted(newProperty.isEncrypted());
		objectToUpdate.setIndex(newProperty.getIndex());
		objectToUpdate.setMandatory(newProperty.getMandatory());
		objectToUpdate.setMultiple(newProperty.isMultiple());
		objectToUpdate.setProtected(newProperty.isProtected());
		
	}
	
	
	
}
