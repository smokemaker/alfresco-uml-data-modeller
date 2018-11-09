package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import org.alfresco.model.dictionary._1.Constraint;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelperForContainerSingleton;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.utils.uml.profile.stereotype.DefaultElementStereotype;

public class ConstraintParametersHelper extends AbstractSubHelperForContainerSingleton<
		org.eclipse.uml2.uml.Constraint,
		Constraint, 
		ConstraintParametersProxyObject> {

	static {
		HELPER_REGISTRY.register(ConstraintParametersHelper.class, ModelConstraintHelper.class);
		HELPER_REGISTRY.register(ConstraintParametersHelper.class, PropertyConstraintHelper.class);
		HELPER_REGISTRY.register(ConstraintParametersHelper.class, PropertyOverrideConstraintHelper.class);
	}
	
	@Override
	protected ru.neodoc.content.modeller.uml2xml.helper.constraints.ConstraintParametersProxyObject getSingleObjectFromContainer(
			Constraint container) {
		return new ConstraintParametersProxyObject(container);
	}

	@Override
	protected void initObjectWithContainer(ObjectContainer<Constraint> parentObject,
			ObjectContainer<ConstraintParametersProxyObject> object) {
		super.initObjectWithContainer(parentObject, object);
		object.getObject().setOwnerConstraint(parentObject.getObject());
	}
	
	@Override
	protected void addSingleObjectToContainer(
			ru.neodoc.content.modeller.uml2xml.helper.constraints.ConstraintParametersProxyObject object, Constraint container) {
		//object.setOwnerConstraint(container);
	}

	@Override
	protected ru.neodoc.content.modeller.uml2xml.helper.constraints.ConstraintParametersProxyObject doCreateObject(
			org.eclipse.uml2.uml.Constraint element) {
		return new ConstraintParametersProxyObject(null);
	}
	
	@Override
	protected ConstraintParametersProxyObject doFillObjectProperties(ConstraintParametersProxyObject object,
			org.eclipse.uml2.uml.Constraint element, DefaultElementStereotype stereotyped) {
		return super.doFillObjectProperties(object, element, stereotyped);
	}
}
