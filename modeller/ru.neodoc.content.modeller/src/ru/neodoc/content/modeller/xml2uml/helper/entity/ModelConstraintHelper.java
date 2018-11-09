package ru.neodoc.content.modeller.xml2uml.helper.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Constraint;
import org.alfresco.model.dictionary._1.Model;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;

import ru.neodoc.content.modeller.xml2uml.helper.AbstractConstraintHelper;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.search.UMLSearchUtils;

public class ModelConstraintHelper extends AbstractConstraintHelper<Model> {

	static {
		HELPER_REGISTRY.register(
				ModelConstraintHelper.class, 
				ModelHelper.class,
				org.eclipse.uml2.uml.Constraint.class, 
				Constraint.class);
	}
	
	@Override
	public org.eclipse.uml2.uml.Constraint resolveElement(Package root, String name) {
		return UMLSearchUtils.getByName(root, name, org.eclipse.uml2.uml.Constraint.class);
	}
	
	@Override
	protected void doCustomFillModelObject(ModelObject<Constraint> modelObject,
			Constraint object) {
		super.doCustomFillModelObject(modelObject, object);
		modelObject.model = container.getName();
	}
	
	@Override
	protected List<Constraint> getElementsFromContainer(Model container) {
		if ((container.getConstraints()!=null) && (container.getConstraints().getConstraint()!=null)) {
			List<Constraint> constraints = new ArrayList<>();
			for (Constraint c: container.getConstraints().getConstraint()) {
				if (!CommonUtils.isValueable(c.getName()) 
						|| !CommonUtils.isValueable(c.getType())
						/*|| (ConstraintType.valueOf(c.getType().toUpperCase())==null)*/)
					continue;
				constraints.add(c);
			}
			return constraints;
		}
		return Collections.emptyList();
	}

	@Override
	protected Namespace getParentNamespaceObject(ModelObject<Constraint> object) {
		ModelObject<?> moPackage = complexRegistry().getObjectRegistry().get(object.pack);
		if (moPackage==null)
			return null;
		if (moPackage.getElement()==null)
			return null;
		if (!moPackage.model.equals(object.model))
			return null;
		
		return (Package)moPackage.getElement();
	}

}
