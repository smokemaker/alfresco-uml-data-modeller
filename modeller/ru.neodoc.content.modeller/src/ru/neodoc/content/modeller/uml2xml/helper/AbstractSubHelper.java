package ru.neodoc.content.modeller.uml2xml.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotypeClassified;

public abstract class AbstractSubHelper<
				ContainerElementType extends Element, 
				ElementType extends Element,
				JAXBContainerType,
				JAXBObjectType, 
				StereotypeClass extends ProfileStereotypeClassified<? super ElementType>> 
		extends AbstractHelper<ElementType, JAXBObjectType, StereotypeClass> {

/*	@Override
	public JAXBObjectType createObject(ElementType element) {
		JAXBObjectType result = super.createObject(element);
		if (result != null)
			createSubObjects(result, element);
		return result;
	}
*/	
	public abstract List<ElementType> getSubElements(ContainerElementType container); 
	
	protected abstract List<ObjectContainer<JAXBObjectType>> addObjectsToContainer(ObjectContainer<JAXBContainerType> container, List<ObjectContainer<JAXBObjectType>> objectsToAdd);
	
	protected void initObjectWithContainer(ObjectContainer<JAXBContainerType> parentObject, ObjectContainer<JAXBObjectType> object) {
		
	};
	
	protected ObjectContainer<JAXBObjectType> createObject(ObjectContainer<JAXBContainerType> parentObject, ElementType element){
		ObjectContainer<JAXBObjectType> result = ObjectContainer.FACTORY.getOrCreate(doCreateObject(element));
		parentObject.addChild(result);
		initObjectWithContainer(parentObject, result);
		result = initObject(result, element);
		clear(result, element);
		return result;
	}
	
	public final List<ObjectContainer<JAXBObjectType>> createObjects(ObjectContainer<JAXBContainerType> parentObject, ContainerElementType container){
		List<ObjectContainer<JAXBObjectType>> result = new ArrayList<>();
		for (ElementType element: getSubElements(container)) {
			ObjectContainer<JAXBObjectType> object = createObject(parentObject, element);
			if (object!=null)
				result.add(object);
		}
		return addObjectsToContainer(parentObject, result);
	};
	
	public void updateFromContainer(JAXBContainerType containerToUpdate, ObjectContainer<JAXBContainerType> newObjectContainer) {
		List<JAXBObjectType> objectsToUpdate = getOrCreateObjects(containerToUpdate);
		List<JAXBObjectType> newObjects = getObjects(newObjectContainer.getObject());
		doUpdateFromContainer(containerToUpdate, objectsToUpdate, newObjects);
	};

	protected void doUpdateFromContainer(JAXBContainerType containerToUpdate, List<JAXBObjectType> objectsToUpdate, List<JAXBObjectType> newObjects) {
		CommonUtils.<JAXBObjectType>updateAndApply(
				objectsToUpdate, 
				newObjects, 
				getComparator(), 
				new CommonUtils.ItemUpdater<JAXBObjectType>() {
					@Override
					public void updateItem(JAXBObjectType origin, JAXBObjectType updated) {
						AbstractSubHelper.this.update(origin, ObjectContainer.FACTORY.getOrCreate(updated));
					}
				});
	};
	
	public abstract CommonUtils.ListComparator<JAXBObjectType> getComparator();
	
	protected abstract List<JAXBObjectType> getOrCreateObjects(JAXBContainerType container);
	
	public abstract List<JAXBObjectType> getObjects(JAXBContainerType container);
	 
}
