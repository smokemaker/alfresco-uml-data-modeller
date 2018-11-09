package ru.neodoc.content.modeller.uml2xml.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.uml2.uml.Element;

public abstract class AbstractSubHelperForContainerSingleton<
			ContainerElementType extends Element,
			JAXBContainerType,
			JAXBObjectType>  
	extends AbstractSubHelperForContainer<ContainerElementType, JAXBContainerType, JAXBObjectType> {

	@Override
	protected boolean isEmptySubObjectsAllowed() {
		return false;
	}

	@Override
	protected List<JAXBObjectType> getOrCreateObjects(JAXBContainerType container) {
		if (getSingleObjectFromContainer(container)==null)
			createSingleObjectInContainer(container);
		return getObjects(container);
	}
	
	protected abstract JAXBObjectType getSingleObjectFromContainer(JAXBContainerType container);
	
	protected void createSingleObjectInContainer(JAXBContainerType container) {
		addSingleObjectToContainer(doCreateObject(null), container);
	};

	protected abstract void addSingleObjectToContainer(JAXBObjectType object, JAXBContainerType container);
	
	@Override
	public List<JAXBObjectType> getObjects(JAXBContainerType container) {
		List<JAXBObjectType> result = new ArrayList<>();
		JAXBObjectType singleObject = getSingleObjectFromContainer(container);
		if (singleObject==null)
			return Collections.emptyList();
		result.add(getSingleObjectFromContainer(container));
		return result;
	}
	
	/*
	 	@Override
	protected List<ObjectContainer<Imports>> addObjectsToContainer(ObjectContainer<Model> container,
			List<ObjectContainer<Imports>> objectsToAdd) {
		if ((objectsToAdd==null) || objectsToAdd.isEmpty())
			return Collections.emptyList();
		if (container.getObject().getImports()==null)
			container.getObject().setImports(objectsToAdd.get(0).getObject());
		
		return container.getObject().getImports()==null
				?Collections.emptyList()
				:ObjectContainer.FACTORY.createListTyped(Arrays.asList(new Imports[] {container.getObject().getImports()}));
	}
	 */
	
	@Override
	protected List<ObjectContainer<JAXBObjectType>> addObjectsToContainer(ObjectContainer<JAXBContainerType> container,
			List<ObjectContainer<JAXBObjectType>> objectsToAdd) {
		if ((objectsToAdd==null) || objectsToAdd.isEmpty())
			return Collections.emptyList();
		
		if (getSingleObjectFromContainer(container.getObject())==null) 
			addSingleObjectToContainer(objectsToAdd.get(0).getObject(), container.getObject());
		
		if (getSingleObjectFromContainer(container.getObject())==null)
			return Collections.emptyList();
		
		List<JAXBObjectType> result = new ArrayList<>();
		result.add(getSingleObjectFromContainer(container.getObject()));
		
		return ObjectContainer.FACTORY.createListTyped(result);
	}

	@Override
	protected void doUpdateFromContainer(JAXBContainerType containerToUpdate, List<JAXBObjectType> objectsToUpdate,
			List<JAXBObjectType> newObjects) {
		if (newObjects.isEmpty())
			removeSingleObjectFromContainer(containerToUpdate);
		else
			super.doUpdateFromContainer(containerToUpdate, objectsToUpdate, newObjects);
	}
	
	protected void removeSingleObjectFromContainer(JAXBContainerType containerToUpdate) {
		addSingleObjectToContainer(null, containerToUpdate);
	};
}
