package ru.neodoc.content.modeller.xml2uml.helper.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Constraint;
import org.alfresco.model.dictionary._1.Property;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Namespace;

import ru.neodoc.content.modeller.xml2uml.helper.AbstractConstraintInConstraintedObjectHelper;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.utils.CommonUtils;

public class PropertyConstraintHelper extends AbstractConstraintInConstraintedObjectHelper<Property> {

	static {
		HELPER_REGISTRY.register(
				PropertyConstraintHelper.class, 
				PropertyHelper.class,
				org.eclipse.uml2.uml.Constraint.class, 
				Constraint.class);
	}
	
	@Override
	protected List<Constraint> getElementsFromContainer(Property container) {
		if (container.getConstraints()!=null) {
			List<Constraint> result = new ArrayList<>();
			for (Constraint c: container.getConstraints().getConstraint())
				if (!CommonUtils.isValueable(c.getRef()))
					result.add(c);
			return result;
		}
		return Collections.emptyList();
	}

	@Override
	protected Class getClassForElement(Element element) {
		return (Class)((org.eclipse.uml2.uml.Property)this.containerModelObject.getElement()).getClass_();
	}
	
	@Override
	protected Namespace getParentNamespaceObject(ModelObject<Constraint> object) {
		try {
			return ((org.eclipse.uml2.uml.Property)this.containerModelObject.getElement()).getClass_();
		} catch (Exception e) {
			
		}
		return null;
	}

}
