package ru.neodoc.content.modeller.xml2uml.helper;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.model.dictionary._1.Constraint;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;
import ru.neodoc.content.utils.uml.search.UMLSearchUtils;

public abstract class AbstractConstraintInConstraintedObjectHelper<ContainerType> 
		extends AbstractConstraintHelper<ContainerType>{

	@Override
	protected ModelObject<Constraint> createNewModelObject(Constraint object) {
		return  createNewTempModelObject(object);
	}

	@Override
	protected List<ModelObject<? extends Constraint>> getModelObjectsFromContainer(ModelObject<? extends ContainerType> container) {
		List<ModelObject<? extends Constraint>> result = new ArrayList<>();
		for (ModelObject<?> mo: container.inners)
			if (mo.source instanceof Constraint)
				result.add((ModelObject<? extends Constraint>)mo);
		return result;
	}

	@Override
	protected void doCustomFillModelObject(ModelObject<Constraint> modelObject, Constraint object) {
		super.doCustomFillModelObject(modelObject, object);
		if (!CommonUtils.isValueable(modelObject.name)) {
			modelObject.name = generateConstraintName(modelObject, containerModelObject);
		}
		
		if (!CommonUtils.isValueable(modelObject.pack)) {
			modelObject.pack = this.containerModelObject.pack;
		}
		containerModelObject.inners.add(modelObject);
	}

	@Override
	public org.eclipse.uml2.uml.Constraint resolveElement(Package root, String name) {
		Class rootClass = null;
		try {
			// (Class)((org.eclipse.uml2.uml.Property)this.containerModelObject.getElement()).getClass_();
			rootClass = getClassForElement(this.containerModelObject.getElement());
		} catch (Exception e) {
			
		}
		org.eclipse.uml2.uml.Constraint result = null;
		// first search in class
		if (rootClass!=null)
			result = UMLSearchUtils.getByName(rootClass, name, org.eclipse.uml2.uml.Constraint.class);
		// then in package
		if (result == null)
			result = UMLSearchUtils.getByName(root, name, org.eclipse.uml2.uml.Constraint.class);
		return result;
	}
	
	protected abstract Class getClassForElement(Element element);

	@Override
	protected boolean processStereotypedElement(StereotypedElement stereotypedElement, ModelObject<Constraint> object) {
		if (super.processStereotypedElement(stereotypedElement, object)) {
			stereotypedElement.getOrCreate(AlfrescoProfile.ForConstraint.Inline.class);
			Element prop = this.containerModelObject.getElement();
			AlfrescoProfile.ForNamedElement.ConstraintedObject<NamedElement> constraintedObject 
				= AbstractProfile.asUntyped(prop).get(AlfrescoProfile.ForNamedElement.ConstraintedObject.class);
			AlfrescoProfile.ForConstraint.ConstraintMain cm = stereotypedElement.get(AlfrescoProfile.ForConstraint.ConstraintMain.class);
			if ((constraintedObject!=null) && (cm!=null))
				constraintedObject.addInlineConstraint(cm);
			return true;
		}
		return false;
	}

}
