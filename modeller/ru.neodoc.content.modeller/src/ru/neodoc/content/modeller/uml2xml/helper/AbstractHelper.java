package ru.neodoc.content.modeller.uml2xml.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.common.Enablable;
import ru.neodoc.content.modeller.tasks.ExecutionContext;
import ru.neodoc.content.modeller.tasks.ExecutionContextAware;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotypeClassified;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public abstract class AbstractHelper<
		ElementType extends Element, 
		JAXBObjectType, 
		StereotypeClass extends ProfileStereotypeClassified<? super ElementType>> 
			implements ExecutionContextAware, Enablable {

	public static final HelperRegistry HELPER_REGISTRY = new HelperRegistry(); 
	
	/*
	 * ExecutionContext
	 */
	
	protected ExecutionContext executionContext;
	
	@Override
	public void setExecutionContext(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}
	
	@Override
	public ExecutionContext getExecutionContext() {
		return this.executionContext;
	}
	
	protected void shareContext(Object object) {
		ExecutionContextAware.setContext(this.executionContext, object);
	}
	
	/*
	 * BEGIN :: CREATE & FILL
	 */
	
	public ObjectContainer<JAXBObjectType> createObject(ElementType element) {	
		ObjectContainer<JAXBObjectType> result = ObjectContainer.FACTORY.getOrCreate(doCreateObject(element));
		result.put(Element.class.getName(), element);
		result = initObject(result, element);
		if (result!=null)
			clear(result, element);
		return result; 
	};
	
	protected ObjectContainer<JAXBObjectType> initObject(ObjectContainer<JAXBObjectType> object, ElementType element){
		if (object.hasObject()) {
			StereotypeClass stereotyped = getStereotypedElement(element);
			fillObjectProperties(object, element, stereotyped);
			List<Object> subObjects = createSubObjects(object, element);
			if (subObjects.isEmpty() && !isEmptySubObjectsAllowed())
				return null;
		}
		return object;
	}
	
	@SuppressWarnings("unchecked")
	public List<AbstractSubHelper<ElementType, ?, JAXBObjectType, ?, ?>> getSubHelpers(){
		List<AbstractSubHelper<ElementType, ?, JAXBObjectType, ?, ?>> result = HELPER_REGISTRY.getSubHelpers((Class<? extends AbstractHelper<ElementType, JAXBObjectType, ?>>) this.getClass());
		for (AbstractSubHelper<ElementType, ?, JAXBObjectType, ?, ?> item: result)
			shareContext(item);
		return result;
	}
	
	protected abstract Class<StereotypeClass> getStereotypeClass();
	
	protected boolean isEmptySubObjectsAllowed() {
		return true; 
	}
	
	protected void clear(ObjectContainer<JAXBObjectType> createdObject, ElementType element) {
		
	}
	
	protected StereotypeClass getStereotypedElement(ElementType element) {
		Class<StereotypeClass> clazz = getStereotypeClass();
		StereotypedElement se = AbstractProfile.asUntyped(element);
		return se.get(clazz);
	}

	protected final List<Object> createSubObjects(ObjectContainer<JAXBObjectType> object, ElementType element) {
		List<Object> result = new ArrayList<>();
		for (AbstractSubHelper<ElementType, ?, JAXBObjectType, ?, ?> subHelper: getSubHelpers()) {
			try {
				if (Enablable.isEnabled(subHelper)) {
					List<?> subObjects = subHelper.createObjects(object, element);
/*					for (Object obj: subObjects)
						if (obj instanceof ObjectContainer<?>)
							object.addChild((ObjectContainer<?>)obj);
*/					result.addAll(subObjects);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	protected abstract JAXBObjectType doCreateObject(ElementType element);
	
	protected ObjectContainer<JAXBObjectType> fillObjectProperties(ObjectContainer<JAXBObjectType> objectContainer, ElementType element, StereotypeClass stereotyped) {
		JAXBObjectType result = doFillObjectProperties(objectContainer.getObject(), element, stereotyped);
		return ObjectContainer.FACTORY.getOrCreate(result);
	}
	
	protected abstract JAXBObjectType doFillObjectProperties(JAXBObjectType object, ElementType element, StereotypeClass stereotyped);
	
	/* END: CREATE & FILL */
	
	/*
	 * BEGIN :: UPDATE EXISTING OBJECTS
	 */
	
	public final void update(JAXBObjectType objectToUpdate, ObjectContainer<JAXBObjectType> newObjectContainer) {
		updateProperties(objectToUpdate, newObjectContainer);
		updateSubObjects(objectToUpdate, newObjectContainer);
	}
	
	protected abstract void updateProperties(JAXBObjectType objectToUpdate, ObjectContainer<JAXBObjectType> newObjectContainer);

	protected final void updateSubObjects(JAXBObjectType objectToUpdate, ObjectContainer<JAXBObjectType> newObjectContainer) {
		for (AbstractSubHelper<ElementType, ?, JAXBObjectType, ?, ?> subHelper: getSubHelpers()) {
			try {
				if (Enablable.isEnabled(subHelper))
					subHelper.updateFromContainer(objectToUpdate, newObjectContainer);
			} catch (Exception e) {
				
			}
		}
	}
	
}
