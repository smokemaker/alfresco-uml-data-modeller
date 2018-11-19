package ru.neodoc.content.modeller.xml2uml.helper.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Class;
import org.alfresco.model.dictionary._1.Property;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;

import ru.neodoc.content.modeller.tasks.ExecutionCallback;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractEntitySubHelper;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractHelper;
import ru.neodoc.content.modeller.xml2uml.helper.relation.proxy.DependencyProxy;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo.DependencyType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Encrypted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Index;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Mandatory;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

@SuppressWarnings("unchecked")
public class PropertyHelper extends AbstractEntitySubHelper<Class, Property, org.eclipse.uml2.uml.Property> {

	static {
		HELPER_REGISTRY.register(PropertyHelper.class, 
				(java.lang.Class<? extends AbstractHelper<?, ?>>) ClassHelper.class,
				org.eclipse.uml2.uml.Property.class, Property.class);
	}
	
	protected ClassMain containerStereotyped;
	
	@Override
	protected ModelObject<Property> createNewModelObject(Property object) {
		return createNewTempModelObject(object);
	}
	
	@Override
	protected void doCustomFillModelObject(ModelObject<Property> modelObject,
			Property object) {
		super.doCustomFillModelObject(modelObject, object);
		PrefixedName pn = new PrefixedName(object.getName());
		modelObject.name = pn.getName();
		modelObject.pack = pn.getPrefix();
	}
	
	@Override
	protected List<Property> getElementsFromContainer(Class container) {
		return container.getProperties()==null
			?Collections.emptyList()
			:container.getProperties().getProperty();
	}

	@Override
	protected List<ModelObject<? extends Property>> getModelObjectsFromContainer(
			ModelObject<? extends Class> container) {
		List<ModelObject<? extends Property>> result = new ArrayList<>();
		for (ModelObject<?> mo: containerModelObject.inners)
			if ((mo.source!=null) && (mo.source instanceof Property))
				result.add((ModelObject<? extends Property>)mo);
		return result;
	}
	
	protected boolean checkContainer() {
		return ((containerModelObject.getElement()!=null) && (containerModelObject.getElement() instanceof org.eclipse.uml2.uml.Class));
	}
	
	@Override
	protected void preSubHelpersPopulate(ModelObject<Property> modelObject,
			ExecutionCallback callback) {
		containerModelObject.inners.add(modelObject);
		String pType = modelObject.source.getType();
		if (pType!=null) {
			String pTypeNs = pType.split(":")[0];
			ModelObject<org.alfresco.model.dictionary._1.Model.Namespaces.Namespace> nsObj = 
					complexRegistry().getObjectSmartFactory().getObject(pTypeNs, Package.class);
			nsObj.setElement(AlfrescoUMLUtils.findNamespace(pTypeNs, complexRegistry().getUmlRoot(), true));
			
			if (nsObj.getElement() != null)
				nsObj.isTransient = true;
			
			complexRegistry().getObjectRegistry().add(nsObj);
			if (!containerModelObject.pack.equals(pTypeNs)) {
				RelationInfo ri = complexRegistry().getRelationRegistry().
						relationInfo(complexRegistry().getObjectRegistry().get(containerModelObject.pack), 
								nsObj, 
								DependencyType.DEPENDENCY, 
								new ModelObject<DependencyProxy>(new DependencyProxy(null)));
				((DependencyProxy)ri.relationObject.source).setRelationInfo(ri);
			}
		}
	}
	
	@Override
	protected org.eclipse.uml2.uml.Property getElement(ModelObject<Property> modelObject, Property object) {
		if (!checkContainer())
			return null;
		org.eclipse.uml2.uml.Class cl = (org.eclipse.uml2.uml.Class)containerModelObject.getElement();
		for (org.eclipse.uml2.uml.Property p: cl.getOwnedAttributes())
			if (p.getName().equals(modelObject.name))
				return p;
		return null;
	}
	
	@Override
	protected boolean doBeforeProcessing(ModelObject<Property> modelObject) {
		if (!checkContainer())
			return false;
		containerStereotyped = AlfrescoProfile.asUntyped(containerModelObject.getElement()).get(ClassMain.class);
		return true;
	}
	
	@SuppressWarnings("static-access")
	@Override
	protected org.eclipse.uml2.uml.Property createElement(ModelObject<Property> object) {
		if (!checkContainer() || (containerStereotyped==null) || !CommonUtils.isValueable(object.source.getType()))
			return null;
		
		PrefixedName pName = new PrefixedName(object.source.getName());
		
		PrefixedName type = new PrefixedName(object.source.getType());
		Package typePackage = type.isPrefixed() 
				?AlfrescoUMLUtils.findNamespace(type.getPrefix(), complexRegistry().getUmlRoot())  
				:complexRegistry().getUmlRoot();
		PrimitiveType pType = AlfrescoUMLUtils.findDataType(type.getName(), typePackage);
		
		ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property p = 
				containerStereotyped.getProperty(pName.getName(), pType, true);
		
		return p==null?null:p.getElementClassified();
	}
	
	@Override
	protected boolean processStereotypedElement(StereotypedElement stereotypedElement, ModelObject<Property> object) {
		
		AlfrescoProfile.ForProperty.Property theProperty = stereotypedElement.getOrCreate(AlfrescoProfile.ForProperty.Property.class);
		Property prop = object.source;

		PrefixedName type = new PrefixedName(object.source.getType());
		Package typePackage = type.isPrefixed() 
				?AlfrescoUMLUtils.findNamespace(type.getPrefix(), complexRegistry().getUmlRoot(), true)  
				:complexRegistry().getUmlRoot();
		PrimitiveType pType = AlfrescoUMLUtils.findDataType(type.getName(), typePackage);
		if ((pType!=null) && !pType.equals(theProperty.getElementClassified().getType()))
			theProperty.getElementClassified().setType(pType);
		
		theProperty.setTitle(prop.getTitle());
		theProperty.setDescription(prop.getDescription());
		
		theProperty.setProtected(CommonUtils.isTrue(prop.isProtected()));
		
		if (prop.getIndex()!=null) {
			Index index = theProperty.getOrCreate(Index.class);
			
			index.setEnabled(prop.getIndex().isEnabled());
			index.setAtomic(prop.getIndex().isAtomic());
			index.setStored(prop.getIndex().isStored());
			index.setTokenised(prop.getIndex().getTokenised());
		} else {
			theProperty.remove(Index.class);
		}
		
		if (CommonUtils.isTrue(prop.isEncrypted()))
			theProperty.getOrCreate(Encrypted.class);
		else
			theProperty.remove(Encrypted.class);

		if (prop.getMandatory()!=null){
			Mandatory mandatory = theProperty.getOrCreate(Mandatory.class);

			String s = prop.getMandatory().getContent(); 
			mandatory.setMandatory(null!=s && "true".equals(s.toLowerCase().trim()));
			mandatory.setEnforced(CommonUtils.isTrue(prop.getMandatory().isEnforced()));
		} else {
			theProperty.remove(Mandatory.class);
		}
		
		theProperty.setMultiple(CommonUtils.isTrue(prop.isMultiple()));			
		
		return true;
	}
	
}
