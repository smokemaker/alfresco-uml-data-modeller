package ru.neodoc.content.modeller.xml2uml.helper.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Class;
import org.alfresco.model.dictionary._1.PropertyOverride;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Property;

import ru.neodoc.content.modeller.tasks.ExecutionCallback;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractEntitySubHelper;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractHelper;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForGeneralization.Inherit;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class PropertyOverrideHelper extends AbstractEntitySubHelper<Class, PropertyOverride, Dependency> {

	static {
		HELPER_REGISTRY.register(PropertyOverrideHelper.class, 
				(java.lang.Class<? extends AbstractHelper<?, ?>>) ClassHelper.class,
				org.eclipse.uml2.uml.Dependency.class, PropertyOverride.class);
	}

	@Override
	protected List<PropertyOverride> getElementsFromContainer(Class container) {
		if (container.getOverrides()==null)
			return Collections.emptyList();
		return container.getOverrides().getProperty();
	}
	
	@Override
	protected List<ModelObject<? extends PropertyOverride>> getModelObjectsFromContainer(
			ModelObject<? extends Class> container) {
		List<ModelObject<? extends PropertyOverride>> result = new ArrayList<>();
		for (ModelObject<?> mo: this.containerModelObject.inners)
			if ((mo.source!=null) && (mo.source instanceof PropertyOverride))
				result.add((ModelObject<? extends PropertyOverride>)mo);
		return result;
	}
	
	@Override
	protected ModelObject<PropertyOverride> createNewModelObject(PropertyOverride object) {
		return createNewTempModelObject(object);
	}
	
	@Override
	protected void doCustomFillModelObject(ModelObject<PropertyOverride> modelObject,
			PropertyOverride object) {
		super.doCustomFillModelObject(modelObject, object);
		PrefixedName pn = new PrefixedName(object.getName());
		modelObject.pack = pn.getPrefix();
		modelObject.name = pn.getName();
	}
	
	@Override
	protected void preSubHelpersPopulate(ModelObject<PropertyOverride> modelObject,
			ExecutionCallback callback) {
		this.containerModelObject.inners.add(modelObject);
	}
	
	protected boolean checkContainer() {
		return ((this.containerModelObject.getElement()!=null) && (this.containerModelObject.getElement() instanceof org.eclipse.uml2.uml.Class));
	}
	
	@Override
	protected Dependency getElement(ModelObject<PropertyOverride> modelObject, PropertyOverride object) {
		Property p = findOverridenProperty(modelObject);
		if (p==null)
			return null;
		ClassMain classMain = AbstractProfile.asUntyped(this.containerModelObject.getElement()).get(ClassMain.class);
		if (classMain==null)
			return null;
		AlfrescoProfile.ForProperty.Property property = AbstractProfile.asUntyped(p).get(AlfrescoProfile.ForProperty.Property.class);
		if (property==null)
			return null;
		for (ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.PropertyOverride po: classMain.getAllPropertyOverrides())
			if (po.getOverridenProperty().getElementClassified().equals(property.getElementClassified()))
				return po.getElementClassified();
		return null;
	}
	
	@Override
	protected Dependency createElement(ModelObject<PropertyOverride> object) {
		Property p = findOverridenProperty(object);
		if (p==null)
			return null;
		ClassMain classMain = AbstractProfile.asUntyped(this.containerModelObject.getElement()).get(ClassMain.class);
		if (classMain==null)
			return null;
		AlfrescoProfile.ForProperty.Property property = AbstractProfile.asUntyped(p).get(AlfrescoProfile.ForProperty.Property.class);
		if (property==null)
			return null;
		return classMain.overrideProperty(property).getElementClassified();
	}

	protected Property findOverridenProperty(ModelObject<PropertyOverride> modelObject) {
		org.eclipse.uml2.uml.Class clazz = (org.eclipse.uml2.uml.Class)this.containerModelObject.getElement();
		if (clazz==null)
			return null;
		PrefixedName pn = new PrefixedName("");
		pn.setPrefix(modelObject.pack);
		pn.setName(modelObject.name);
		return findPropertyInClass(clazz, pn);
	}
	
	protected Property findPropertyInClass(org.eclipse.uml2.uml.Class clazz, PrefixedName propertyName) {
		AlfrescoProfile.ForPackage.Namespace ns = AlfrescoProfile.ForPackage.Namespace._HELPER.findNearestFor(clazz);
		ClassMain classMain = AbstractProfile.asUntyped(clazz).get(ClassMain.class);
		Property result = null;
		if (ns.getPrefix().equals(propertyName.getPrefix())) {
			if (classMain!=null) {
				AlfrescoProfile.ForProperty.Property property = classMain.getProperty(propertyName.getName(), null, false);
				if (property!=null)
					return property.getElementClassified();
			}
		}
		
		List<Inherit> inherits = classMain.getInherits();
		for (Inherit inherit: inherits) {
			ClassMain parent = inherit.getGeneral();
			result = findPropertyInClass(parent.getElementClassified(), propertyName);
			if (result != null)
				return result;
		}
		return null;
	}
	
	@Override
	protected boolean doBeforeProcessing(ModelObject<PropertyOverride> modelObject) {
		if (!checkContainer())
			return false;
		if (findOverridenProperty(modelObject)==null)
			return false;
		return super.doBeforeProcessing(modelObject);
	}
	
	@Override
	protected boolean processStereotypedElement(StereotypedElement stereotypedElement,
			ModelObject<PropertyOverride> object) {
		super.processStereotypedElement(stereotypedElement, object);
		AlfrescoProfile.ForDependency.PropertyOverride propertyOverride 
				= stereotypedElement.getOrCreate(AlfrescoProfile.ForDependency.PropertyOverride.class);
		propertyOverride.setMandatory(object.source.isMandatory());
		propertyOverride.setDefaultValue(object.source.getDefault());
		return true;
	}
	
}
