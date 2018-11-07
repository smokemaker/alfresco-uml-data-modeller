package ru.neodoc.content.modeller.extensions.enum2constraint.uml2xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.AlfrescoModelUtils;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.modeller.extensions.common.ExtensionManagedObject;
import ru.neodoc.content.modeller.extensions.jaxb.ExtensionsJAXBHelper;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.ConstraintParametersHelper;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.PropertyConstraintHelper;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.PropertyConstraintsHelper;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintList;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.modeller.extensions.ModelObject;

public class EnumPropertyConstraintHelper extends PropertyConstraintHelper 
		implements ExtensionManagedObject {

	static {
		HELPER_REGISTRY.register(EnumPropertyConstraintHelper.class, PropertyConstraintsHelper.class);
		HELPER_REGISTRY.register(ConstraintParametersHelper.class, EnumPropertyConstraintHelper.class);
	}
	
	@Override
	public List<Constraint> getSubElements(Property container) {
		Type type = container.getType();  
		if (type == null)
			return Collections.emptyList();
		if (type instanceof Enumeration) {
			Enumeration enumeration = (Enumeration)type;
			Constraint constraint = ((org.eclipse.uml2.uml.Class)container.getOwner()).createOwnedRule("enum_" + enumeration.getName() + "_constraint");
			ConstraintList constraintList = ConstraintList._HELPER.getFor(constraint);
			List<String> list = new ArrayList<>();
			for (EnumerationLiteral enumerationLiteral: enumeration.getOwnedLiterals())
				list.add(enumerationLiteral.getName());
			constraintList.setAllowedValues(list);
			List<Constraint> result = new ArrayList<>();
			result.add(constraint);
			return result;
		}
		return Collections.emptyList();
	}

	@Override
	protected ObjectContainer<org.alfresco.model.dictionary._1.Constraint> fillObjectProperties(
			ObjectContainer<org.alfresco.model.dictionary._1.Constraint> objectContainer, Constraint element,
			ConstraintMain stereotyped) {
		ObjectContainer<org.alfresco.model.dictionary._1.Property> propertyContainer 
			= objectContainer.findParent(org.alfresco.model.dictionary._1.Property.class);
		propertyContainer.getObject().setType("d:text");
		PrimitiveType dataType = AlfrescoUMLUtils.findDataType("d:text", AlfrescoUMLUtils.getUMLRoot(element), true);
		if (dataType!=null) {
			Namespace namespace = Namespace._HELPER.findNearestFor(dataType);
			Model model = Model._HELPER.findNearestFor(element);
			if ((model!=null) || (namespace!=null))
				model.importNamespace(namespace);
		}
		ObjectContainer<org.alfresco.model.dictionary._1.Constraint> result 
				= super.fillObjectProperties(objectContainer, element, stereotyped);
		
		List<ObjectContainer<?>> ocHierarchy = result.getHierarchy();
		ocHierarchy.add(result);
		
		@SuppressWarnings("unchecked")
		List<Object> objectHierarchy = (List<Object>)ObjectContainer.FACTORY.extract(ocHierarchy);
		List<PrefixedName> path = AlfrescoModelUtils.getHierarchyNames(objectHierarchy);
		
		ExtensionsJAXBHelper helper = getExecutionContext().get(ExtensionsJAXBHelper.class); 
		ModelObject modelObject = helper.locate(path, true);
		
		return result;
	}
	
	@Override
	protected void clear(ObjectContainer<org.alfresco.model.dictionary._1.Constraint> createdObject,
			Constraint element) {
		super.clear(createdObject, element);
		element.destroy();
	}
	
}
