package ru.neodoc.content.modeller.xml2uml.helper;

import org.alfresco.model.dictionary._1.Model;
import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;

public abstract class AbstractModelAwareSubHelper<ElementType, E extends Element> 
		extends AbstractEntitySubHelper<Model, ElementType, E> {

	@Override
	protected void doCustomFillModelObject(ModelObject<ElementType> modelObject,
			ElementType object) {
		super.doCustomFillModelObject(modelObject, object);
		if (container!=null)
			modelObject.model = container.getName();
	}

}
