package ru.neodoc.content.modeller.xml2uml.helper;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;

public abstract class AbstractEntitySubHelper<ContainerType, ElementType, E extends Element> 
		extends AbstractSubHelper<ContainerType, ElementType, E> {

	protected Package getSearchRoot(ModelObject<ElementType> modelObject, ElementType object) {
		return (Package)complexRegistry().get(ComplexRegistry.PROP_ROOT_OBJECT);
	}
	
	protected String getSearchName(ModelObject<ElementType> modelObject, ElementType object) {
		return modelObject.name;
	}
	
	@Override
	protected E getElement(ModelObject<ElementType> modelObject, ElementType object) {
		return resolveElement(
				getSearchRoot(modelObject, object), 
				getSearchName(modelObject, object)
				);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelObject<ElementType> store(ModelObject<ElementType> modelObject) {
		complexRegistry().getObjectRegistry().add(modelObject, true);
		return modelObject.noStore
				?modelObject
				:(ModelObject<ElementType>)complexRegistry().getObjectRegistry().get(modelObject.getName());
	}
}
