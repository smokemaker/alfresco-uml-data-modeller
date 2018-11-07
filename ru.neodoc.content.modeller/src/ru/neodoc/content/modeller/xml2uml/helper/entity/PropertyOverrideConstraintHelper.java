package ru.neodoc.content.modeller.xml2uml.helper.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Constraint;
import org.alfresco.model.dictionary._1.PropertyOverride;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.xml2uml.helper.AbstractConstraintInConstraintedObjectHelper;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.search.UMLSearchUtils;

public class PropertyOverrideConstraintHelper extends AbstractConstraintInConstraintedObjectHelper<PropertyOverride> {

	static {
		HELPER_REGISTRY.register(
				PropertyOverrideConstraintHelper.class, 
				PropertyOverrideHelper.class,
				org.eclipse.uml2.uml.Constraint.class, 
				Constraint.class);
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
	protected Class getClassForElement(Element element) {
		AlfrescoProfile.ForDependency.PropertyOverride propertyOverride = AbstractProfile.asType(element, AlfrescoProfile.ForDependency.PropertyOverride.class);
		return propertyOverride.getOverridingClass().getElementClassified();
	}
	
	@Override
	protected Namespace getParentNamespaceObject(ModelObject<Constraint> object) {
		try {
			return getClassForElement(this.containerModelObject.getElement());
		} catch (Exception e) {
			
		}
		return null;
	}
}
