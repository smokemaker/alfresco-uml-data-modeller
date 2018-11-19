package ru.neodoc.content.modeller.uml2xml.helper.classes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelper;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Archive;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ListComparator;
import ru.neodoc.content.utils.uml.search.helper.SearchHelperFactory;

public abstract class AbstractClassHelper<
	JAXBContainerType,
	JAXBObjectType extends org.alfresco.model.dictionary._1.Class,
	StereotypeClass extends ClassMain> 
		extends
		AbstractSubHelper<
			Package, 
			org.eclipse.uml2.uml.Class, 
			JAXBContainerType, 
			JAXBObjectType, 
			StereotypeClass>{

	@Override
	public List<Class> getSubElements(Package container) {
		List<Class> result = new ArrayList<>();
		AlfrescoProfile.ForPackage.Model umlModel = AlfrescoProfile.ForPackage.Model._HELPER.getFor(container);
		if (umlModel != null) {
			for (Namespace namespace: umlModel.getAllNamespaces()) {
				List<? extends Class> search = SearchHelperFactory.getClassSearcher()
					.target(getStereotypeName())
					.startWith(namespace.getElementClassified())
					.search();
				for (Class cl: search)
					if (!result.contains(cl))
						result.add(cl);
			}
		}
		return result;
	}

	protected abstract String getStereotypeName();
	
	@Override
	protected List<ObjectContainer<JAXBObjectType>> addObjectsToContainer(ObjectContainer<JAXBContainerType> container,
			List<ObjectContainer<JAXBObjectType>> objectsToAdd) {
		List<JAXBObjectType> list = addObjectsToContainer(
				container.getObject(), 
				ObjectContainer.FACTORY.extractTyped(objectsToAdd));
		return ObjectContainer.FACTORY.createListTyped(list);
	}

	protected abstract List<JAXBObjectType> addObjectsToContainer(JAXBContainerType container,
			List<JAXBObjectType> objectsToAdd);
	
	@Override
	public ListComparator<JAXBObjectType> getComparator() {
		return new CommonUtils.BaseListComparator<JAXBObjectType>() {

			@Override
			public boolean equals(JAXBObjectType item1, JAXBObjectType item2) {
				// always update
				return false;
			}
			
			@Override
			public String itemHash(JAXBObjectType item) {
				if (item == null)
					return "";
				return item.getName();
			}
		};
	}

	@Override
	protected List<JAXBObjectType> getOrCreateObjects(JAXBContainerType container) {
		return getObjects(container);
	}

	@Override
	protected JAXBObjectType doFillObjectProperties(JAXBObjectType object, Class element, StereotypeClass stereotyped) {
		object.setName(stereotyped.getPrfixedName());
		object.setTitle(stereotyped.getTitle());
		object.setDescription(stereotyped.getDescription());
		try {
			// parent can be null
			object.setParent(null);
			object.setParent(stereotyped.getInherits().get(0).getGeneral().getPrfixedName());
		} catch (Exception e) {
			
		}
		object.setArchive(stereotyped.has(Archive.class));
		object.setIncludedInSuperTypeQuery(stereotyped.getIncludedInSuperTypeQuery());
		return object;
	}

	@Override
	protected void updateProperties(JAXBObjectType objectToUpdate, ObjectContainer<JAXBObjectType> newObjectContainer) {
		JAXBObjectType newObject = newObjectContainer.getObject();
		objectToUpdate.setName(newObject.getName());
		objectToUpdate.setTitle(newObject.getTitle());
		objectToUpdate.setDescription(newObject.getDescription());
		objectToUpdate.setParent(newObject.getParent());
		objectToUpdate.setArchive(newObject.isArchive());
		objectToUpdate.setIncludedInSuperTypeQuery(newObject.isIncludedInSuperTypeQuery());
		
	}

	
	
}
