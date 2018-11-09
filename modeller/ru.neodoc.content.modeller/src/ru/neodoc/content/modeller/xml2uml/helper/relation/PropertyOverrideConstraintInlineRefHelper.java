package ru.neodoc.content.modeller.xml2uml.helper.relation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Constraint;
import org.alfresco.model.dictionary._1.Property;
import org.alfresco.model.dictionary._1.PropertyOverride;

import ru.neodoc.content.modeller.xml2uml.helper.AbstractConstraintHelper;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo.DependencyType;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class PropertyOverrideConstraintInlineRefHelper extends PropertyOverrideConstraintRefHelper {

	static {
//		HELPER_REGISTRY.register(PropertyConstraintInlineRefHelper.class, PropertyHelper.class, Constraint.class, Dependency.class);
	}

	@Override
	protected DependencyType getRelationType() {
		return DependencyType.CONSTRAINT_INLINE;
	}
	
	@Override
	protected ModelObject<?> getTargetModelObject(ModelObject<Constraint> modelObject) {
		String name = modelObject.name;
		if (!CommonUtils.isValueable(name))
			name = AbstractConstraintHelper.generateConstraintName(modelObject, containerModelObject);
		ModelObject<Constraint> mo =
				complexRegistry().getObjectSmartFactory().getObject(name);		
		return mo;
	}
	
	@Override
	protected List<Constraint> getElementsFromContainer(PropertyOverride container) {
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
	protected boolean processStereotypedElement(StereotypedElement stereotypedElement,
			ModelObject<Constraint> object) {
		
		return super.processStereotypedElement(stereotypedElement, object);
	}
}
