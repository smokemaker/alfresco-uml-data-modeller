package ru.neodoc.content.profile.meta.alfresco.forclass;

import java.util.List;

import org.eclipse.uml2.uml.PrimitiveType;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.ChildAssociation;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Aspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.PropertyOverride;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForGeneralization.Inherit;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property;
import ru.neodoc.content.utils.uml.profile.annotation.AWraps;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AWraps(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain.class)
public class AbstractClass extends ImplementationMetaObjectClassified<org.eclipse.uml2.uml.Class> 
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain {

	AlfrescoProfile.ForClass.ClassMain cm;
	
	public AbstractClass(CompositeMetaObject composite) {
		super(composite);
		ClassMain impl = new ClassMain(composite);
		addSubimplemetor(impl);
		this.cm = impl;
	}

	@Override
	public String getTitle() {
		return this.cm.getTitle();
	}

	@Override
	public void setTitle(String title) {
		this.cm.setTitle(title);
	}

	@Override
	public String getDescription() {
		return this.cm.getDescription();
	}

	@Override
	public void setDescription(String description) {
		this.cm.setDescription(description);
	}

	@Override
	public boolean getIncludedInSuperTypeQuery() {
		return this.cm.getIncludedInSuperTypeQuery();
	}

	@Override
	public void setIncludedInSuperTypeQuery(boolean value) {
		this.cm.setIncludedInSuperTypeQuery(value);
	}

	@Override
	public Property getProperty(String propertyName, PrimitiveType propertyType, boolean create) {
		return this.cm.getProperty(propertyName, propertyType, create);
	}

	@Override
	public boolean getPredefined() {
		return this.cm.getPredefined();
	}

	@Override
	public boolean getDetached() {
		return this.cm.getDetached();
	}

	@Override
	public void setPredefined(boolean value) {
		this.cm.setPredefined(value);
	}

	@Override
	public void setDetached(boolean value) {
		this.cm.setDetached(value);
	}

	@Override
	public List<Property> getAllProperties() {
		return this.cm.getAllProperties();
	}

	@Override
	public List<Inherit> getInherits() {
		return this.cm.getInherits();
	}

	@Override
	public Inherit inherit(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain parent) {
		return this.cm.inherit(parent);
	}

	@Override
	public MandatoryAspect addMandatoryAspect(Aspect aspect) {
		return this.cm.addMandatoryAspect(aspect);
	}

	@Override
	public void removeMandatoryAspect(Aspect aspect) {
		this.cm.removeMandatoryAspect(aspect);
	}

	@Override
	public List<MandatoryAspect> getMandatoryAspects() {
		return this.cm.getMandatoryAspects();
	}

	@Override
	public Association addPeerAssociation(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain peer,
			String name) {
		return this.cm.addPeerAssociation(peer, name);
	}

	@Override
	public Association getPeerAssociation(String name) {
		return this.cm.getPeerAssociation(name);
	}

	@Override
	public List<Association> getPeerAssociations() {
		return this.cm.getPeerAssociations();
	}

	@Override
	public List<Association> getPeerAssociations(AlfrescoProfile.ForClass.ClassMain peer) {
		return this.cm.getPeerAssociations(peer);
	}

	@Override
	public void removePeerAssociation(String name) {
		this.cm.removePeerAssociation(name);
	}

	@Override
	public void removePeerAssociations() {
		this.cm.removePeerAssociations();
	}

	@Override
	public void removePeerAssociations(AlfrescoProfile.ForClass.ClassMain peer) {
		this.cm.removePeerAssociations(peer);
	}

	@Override
	public ChildAssociation addChildAssociation(AlfrescoProfile.ForClass.ClassMain child, String name) {
		return this.cm.addChildAssociation(child, name);
	}

	@Override
	public ChildAssociation getChildAssociation(String name) {
		return this.cm.getChildAssociation(name);
	}

	@Override
	public List<ChildAssociation> getChildAssociations() {
		return this.cm.getChildAssociations();
	}

	@Override
	public List<ChildAssociation> getChildAssociations(AlfrescoProfile.ForClass.ClassMain child) {
		return this.cm.getChildAssociations(child);
	}

	@Override
	public void removeChildAssociation(String name) {
		this.cm.removeChildAssociation(name);
	}

	@Override
	public void removeChildAssociations() {
		this.cm.removeChildAssociations();
	}

	@Override
	public void removeChildAssociations(AlfrescoProfile.ForClass.ClassMain child) {
		this.cm.removeChildAssociations(child);
	}

	@Override
	public String getName() {
		return this.cm.getName();
	}

	@Override
	public void setName(String value) {
		this.cm.setName(value);
	}

	@Override
	public List<Namespace> getRequiredNamespaces() {
		return this.cm.getRequiredNamespaces();
	}

	@Override
	public PropertyOverride overrideProperty(Property property) {
		return this.cm.overrideProperty(property);
	}

	@Override
	public List<PropertyOverride> getAllPropertyOverrides() {
		return this.cm.getAllPropertyOverrides();
	}

	@Override
	public Namespace getNamespace() {
		return cm.getNamespace();
	}

	@Override
	public String getPrfixedName() {
		return cm.getPrfixedName();
	}

}
