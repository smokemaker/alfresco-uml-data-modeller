package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.NamedValue;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.UMLFactory;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelper;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.AlfrescoProfileLibrary.SimpleParameter;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForLiteralSpecification.SimpleParameterWrapper;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ListComparator;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.descriptor.PropertyDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.StereotypeDescriptor;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotypeClassified;

public class ConstraintParameterHelper 
		extends AbstractSubHelper<Constraint, LiteralString, ConstraintParametersProxyObject, NamedValue, AlfrescoProfile.ForLiteralSpecification.SimpleParameterWrapper>{

	static {
		HELPER_REGISTRY.register(ConstraintParameterHelper.class, ConstraintParametersHelper.class).asContained();
	}	
	
	@Override
	public List<LiteralString> getSubElements(Constraint container) {
		ConstraintMain constraintMain = AbstractProfile.asUntyped(container).get(ConstraintMain.class);
		if (constraintMain==null)
			return Collections.emptyList();
		
		List<LiteralString> result = new ArrayList<>();
		
		Class<? extends ConstraintMain> stereotypeClass = ConstraintMain._FACTORY.get(constraintMain.getConstraintType());
		StereotypeDescriptor stereotypeDescriptor = StereotypeDescriptor.find(stereotypeClass);
		PropertyDescriptor parametersDescriptor = stereotypeDescriptor.findPropertyDescriptor(ConstraintMain.PROPERTIES.PARAMETERS); 
		for (PropertyDescriptor propertyDescriptor: stereotypeDescriptor.getPropertyDescriptors()) {
			if (!propertyDescriptor.isStoreInOther())
				continue;
			try {
				// in case of e.g. NPE we just continue
				if (propertyDescriptor.getStorageOwner().equals(parametersDescriptor.getStereotypeDescriptor().getStereotypeClass()))
					if (propertyDescriptor.getStoragePropertyName().equals(parametersDescriptor.getName())) {
						LiteralString literalString = UMLFactory.eINSTANCE.createLiteralString();
						container.getNearestPackage().getPackagedElements().add(literalString);
						SimpleParameterWrapper wrapper = AbstractProfile.asType(literalString, SimpleParameterWrapper.class);
						SimpleParameter parameter = new SimpleParameter();
						parameter.setName(propertyDescriptor.getName());
						Object value = constraintMain.getAttribute(propertyDescriptor.getName());
						if (value==null)
							continue;
						if (value instanceof List) {
							List<String> list = new ArrayList<>();
							for (Object object: (List)value) {
								if (object!=null)
									list.add(object.toString());
								else
									list.add("");
							}
							parameter.setList(list);
						} else {
							if (value != null)
								parameter.setValue(value.toString());
						}
						wrapper.setWrappedValue(parameter);
						result.add(literalString);
					}
			} catch (Exception e) {
				continue;
			}
		}
		
		List<SimpleParameter> parameters = constraintMain.getParameters();
		for (SimpleParameter parameter: parameters) {
			LiteralString literalString = UMLFactory.eINSTANCE.createLiteralString();
			container.getNearestPackage().getPackagedElements().add(literalString);
			SimpleParameterWrapper wrapper = AbstractProfile.asType(literalString, SimpleParameterWrapper.class);
			wrapper.setWrappedValue(parameter);
			result.add(literalString);
		}
		return result;
	}

	@Override
	protected List<ObjectContainer<NamedValue>> addObjectsToContainer(
			ObjectContainer<ConstraintParametersProxyObject> container,
			List<ObjectContainer<NamedValue>> objectsToAdd) {
		container.getObject().getOwnerConstraint().getParameter().addAll(ObjectContainer.FACTORY.extractTyped(objectsToAdd));
		return ObjectContainer.FACTORY.createListTyped(container.getObject().getOwnerConstraint().getParameter());
	}

	@Override
	public ListComparator<NamedValue> getComparator() {
		return new CommonUtils.BaseListComparator<NamedValue>() {

			@Override
			public boolean equals(NamedValue item1, NamedValue item2) {
				if (CommonUtils.objectsAreNull(item1, item2))
					return true;
				return itemHash(item1).equalsIgnoreCase(itemHash(item2))
						&& CommonUtils.objectsAreEqual(item1.getValue(), item2.getValue()) 
						&& (CommonUtils.objectsNotNull(item1.getList(), item2.getList())
								?CommonUtils.listsAreEqual(item1.getList().getValue(), item2.getList().getValue())
								:CommonUtils.objectsAreNull(item1.getList(), item2.getList())
						);
			}
			
			@Override
			public String itemHash(NamedValue item) {
				if (item==null)
					return "";
				return item.getName();
			}
			
		};
	}

	@Override
	protected List<NamedValue> getOrCreateObjects(ConstraintParametersProxyObject container) {
		return getObjects(container);
	}

	@Override
	public List<NamedValue> getObjects(ConstraintParametersProxyObject container) {
		return container.ownerConstraint.getParameter();
	}

	@Override
	protected Class<SimpleParameterWrapper> getStereotypeClass() {
		return SimpleParameterWrapper.class;
	}

	@Override
	protected NamedValue doCreateObject(LiteralString element) {
		return new NamedValue();
	}

	@Override
	protected NamedValue doFillObjectProperties(NamedValue object, LiteralString element,
			SimpleParameterWrapper stereotyped) {
		SimpleParameter simpleParameter = stereotyped.getWrappedValue();
		object.setName(simpleParameter.getName());
		if ((simpleParameter.getList()!=null) && (!simpleParameter.getList().isEmpty())) {
			NamedValue.List list = new NamedValue.List();
			list.getValue().addAll(simpleParameter.getList());
			object.setList(list);
			object.setValue(null);
		} else {
			object.setValue(simpleParameter.getValue());
			object.setList(null);
		};
		return object;
	}

	@Override
	protected void clear(ObjectContainer<NamedValue> createdObject, LiteralString element) {
		super.clear(createdObject, element);
		element.destroy();
	}
	
	@Override
	protected void updateProperties(NamedValue objectToUpdate, ObjectContainer<NamedValue> newObjectContainer) {
		objectToUpdate.setName(newObjectContainer.getObject().getName());
		objectToUpdate.setValue(newObjectContainer.getObject().getValue());
		objectToUpdate.setList(newObjectContainer.getObject().getList());
	}

}
