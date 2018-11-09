package ru.neodoc.content.modeller.xml2uml.helper;

import org.alfresco.model.dictionary._1.Constraint;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo.DependencyType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.Constrainted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForNamedElement.ConstraintedObject;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.search.UMLSearchUtils;

public abstract class AbstractConstraintRefHelper<ContainerType>
		extends AbstractRelationSubHelper<ContainerType, Constraint, Dependency> {

	@Override
	protected ModelObject<?> getTargetModelObject(ModelObject<Constraint> modelObject) {
		ModelObject<Constraint> mo =
				complexRegistry().getObjectSmartFactory().getObject(modelObject.source.getRef(), org.eclipse.uml2.uml.Constraint.class);		
		return mo;
	}

	@Override
	protected DependencyType getRelationType() {
		return DependencyType.CONSTRAINT;
	}

	@Override
	protected Dependency createElement(ModelObject<Constraint> modelObject, Constraint object, RelationInfo relationInfo, ModelObject<?> sourceModelObject, ModelObject<?> targetModelObject) {
		if (!ModelObject.hasElement(sourceModelObject, targetModelObject))
			return null;
		
		AlfrescoProfile.ForNamedElement.ConstraintedObject<NamedElement> constraintedObject 
				= AbstractProfile.asUntyped(sourceModelObject.getElement()).get(AlfrescoProfile.ForNamedElement.ConstraintedObject.class);
		AlfrescoProfile.ForConstraint.ConstraintMain constraint = 
				AbstractProfile.asUntyped(targetModelObject.getElement()).get(AlfrescoProfile.ForConstraint.ConstraintMain.class);
		if ((constraintedObject==null) || (constraint==null))
			return null;
		
		AlfrescoProfile.ForDependency.Constrainted result = constraintedObject.addConstraintRef(constraint); 
		return result==null?null:result.getElementClassified();
		
	}

	@Override
	protected Dependency getElement(ModelObject<Constraint> modelObject, Constraint object, RelationInfo relationInfo, ModelObject<?> sourceModelObject, ModelObject<?> targetModelObject) {
		if (!ModelObject.hasElement(sourceModelObject))
			return null;
		
		org.eclipse.uml2.uml.Constraint targetConstraint = (org.eclipse.uml2.uml.Constraint)targetModelObject.getElement();
		if (targetConstraint==null)
			targetConstraint = UMLSearchUtils.constraintByName(
					(Package)complexRegistry().get(ComplexRegistry.PROP_ROOT_OBJECT), 
					targetModelObject.getName());
		if (targetConstraint==null)
			targetConstraint = UMLSearchUtils.constraintByName(
					complexRegistry().getUmlRoot(), 
					targetModelObject.getName());
		if (targetConstraint==null)
			return null;
		
		return getElementFromSourceModelObject(sourceModelObject, targetConstraint);
	}

	protected Dependency getElementFromSourceModelObject(ModelObject<?> sourceModelObject, org.eclipse.uml2.uml.Constraint targetConstraint) {
			
			ConstraintedObject<NamedElement> constraintedObject = AbstractProfile.asUntyped(sourceModelObject.getElement())
					.get(ConstraintedObject.class);
			if (constraintedObject==null)
				return null;
			
			for (Constrainted constrainted: constraintedObject.getConstraintRefs()) {
				if (targetConstraint.equals(constrainted.getConstraint().getElementClassified()))
					return constrainted.getElementClassified();
			}
			
	/*		if (sourceModelObject.getElement() instanceof org.eclipse.uml2.uml.Property) {
				org.eclipse.uml2.uml.Property property = (org.eclipse.uml2.uml.Property)sourceModelObject.getElement();
				for (Dependency dep: property.getClientDependencies()) {
					for (NamedElement supplier: dep.getSuppliers())
						if (targetConstraint.equals(supplier) && AbstractProfile.isType(dep, AlfrescoProfile.ForDependency.Constrainted.class))
							return dep;
				}
			}
	*/		return null;
		}
}
