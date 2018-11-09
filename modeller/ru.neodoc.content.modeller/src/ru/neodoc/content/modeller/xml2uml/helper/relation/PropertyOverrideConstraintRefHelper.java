package ru.neodoc.content.modeller.xml2uml.helper.relation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Constraint;
import org.alfresco.model.dictionary._1.PropertyOverride;
import org.eclipse.uml2.uml.Dependency;

import ru.neodoc.content.modeller.xml2uml.helper.AbstractConstraintRefHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.PropertyOverrideHelper;
import ru.neodoc.content.utils.CommonUtils;

public class PropertyOverrideConstraintRefHelper extends AbstractConstraintRefHelper<PropertyOverride> {
	
	static {
		HELPER_REGISTRY.register(PropertyOverrideConstraintRefHelper.class, PropertyOverrideHelper.class, Constraint.class, Dependency.class);
	}

	@Override
	protected List<Constraint> getElementsFromContainer(PropertyOverride container) {
		if (container.getConstraints()!=null) {
			List<Constraint> result = new ArrayList<>();
			for (Constraint c: container.getConstraints().getConstraint())
				if (CommonUtils.isValueable(c.getRef()))
					result.add(c);
			return result;
		}
		return Collections.emptyList();
	}
	
}
