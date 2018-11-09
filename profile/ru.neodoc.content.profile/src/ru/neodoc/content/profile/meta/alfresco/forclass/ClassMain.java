package ru.neodoc.content.profile.meta.alfresco.forclass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.PrimitiveType;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.AssociationSolid;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.ChildAssociation;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Aspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.PropertyOverride;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForGeneralization.Inherit;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.DdTextualDescription;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Named;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Namespaced;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.StoredElement;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchUtils;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.AssociationComposer;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.descriptor.StereotypeDescriptor;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;
import ru.neodoc.content.utils.uml.profile.registry.ProfileRegistry;
import ru.neodoc.content.utils.uml.search.converter.UMLSearchConverter;
import ru.neodoc.content.utils.uml.search.converter.UMLSearchConvertibleList;
import ru.neodoc.content.utils.uml.search.converter.UMLSearchConvertibleListImpl;
import ru.neodoc.content.utils.uml.search.filter.SearchFilterFactory;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterAssociationByTarget;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterByName;
import ru.neodoc.content.utils.uml.search.helper.SearchHelperFactory;

@AImplements(AlfrescoProfile.ForClass.ClassMain.class)
public class ClassMain extends ImplementationMetaObjectClassified<Class> implements AlfrescoProfile.ForClass.ClassMain {

	protected DdTextualDescription td;
	protected StoredElement se;
	protected Namespaced nd;
	
	public ClassMain(CompositeMetaObject composite) {
		super(composite);
		this.td = createAndRegisterSubimplementor(DdTextualDescription.class);
		this.se = createAndRegisterSubimplementor(StoredElement.class);
		this.nd = createAndRegisterSubimplementor(Namespaced.class);
	}

	@Override
	public String getTitle() {
		return this.td.getTitle();
	}

	@Override
	public void setTitle(String title) {
		this.td.setTitle(title);
	}

	@Override
	public String getDescription() {
		return this.td.getDescription();
	}

	@Override
	public void setDescription(String description) {
		this.td.setDescription(description);
	}

	@Override
	public boolean getIncludedInSuperTypeQuery() {
		return getBoolean(DD_INCLUDED_IN_SUPERTYPE_QUERY);
	}
	
	@Override
	public void setIncludedInSuperTypeQuery(boolean value) {
		setAttribute(DD_INCLUDED_IN_SUPERTYPE_QUERY, new Boolean(value));
	}
	
	@Override
	public Property getProperty(String propertyName, PrimitiveType propertyType, boolean create) {
		Property result = null;
		Class umlClass = (Class) this.element;
		if (umlClass == null)
			return null;
		
		org.eclipse.uml2.uml.Property property = umlClass.getOwnedAttribute(propertyName, propertyType);
		if (property==null && !create)
			return null;
		if (property != null)
			result = Property._HELPER.getFor(property);
		else {
			property = umlClass.createOwnedAttribute(propertyName, propertyType);
			result = Property._HELPER.getFor(property);
		}
		return result;
	}
	
	@Override
	public List<Property> getAllProperties() {
		List<Property> result = AlfrescoSearchHelperFactory.getPropertySearcher()
				.startWith(getElementClassified())
				.search()
				.convert(AlfrescoSearchUtils.PropertyToPropertyConverter.INSTANCE);
		return result;
	}

	@Override
	public boolean getPredefined() {
		return this.se.getPredefined();
	}

	@Override
	public boolean getDetached() {
		return this.se.getDetached();
	}

	@Override
	public void setPredefined(boolean value) {
		this.se.setPredefined(value);
	}

	@Override
	public void setDetached(boolean value) {
		this.se.setDetached(value);
	}
	
	@Override
	public List<Inherit> getInherits() {
		Class umlClass = getElementClassified();
		UMLSearchConvertibleList<Generalization> list = new UMLSearchConvertibleListImpl<>(umlClass.getGeneralizations()); 
		return list.convert(AlfrescoSearchUtils.GeneralizationToInheritConverter.INSTANCE);
	}
	
	@Override
	public Inherit inherit(AlfrescoProfile.ForClass.ClassMain parent) {
		Class umlClass = getElementClassified();
		for (Generalization g: umlClass.getGeneralizations())
			if (parent==null || g.getGeneral()!=parent.getElementClassified())
				g.destroy();
		if (parent==null)
			return null;
		if (umlClass.getGeneralization(parent.getElementClassified()) == null)
			umlClass.createGeneralization(parent.getElementClassified());
		return Inherit._HELPER.getFor(umlClass.getGeneralization(parent.getElementClassified()));
		
	}

	/* mandatory aspects */
	
	public MandatoryAspect getMandatoryAspect(Aspect aspect) {
		if (aspect==null)
			return null;
		Association a = AlfrescoSearchHelperFactory.getMandatoryAspectSearcher(this)
			.filter((new UMLSearchFilterAssociationByTarget()).value(aspect.getElementClassified()))
			.first();
		if (a==null)
			return null;
		return MandatoryAspect._HELPER.getFor(a);
	}
	
	@Override
	public MandatoryAspect addMandatoryAspect(Aspect aspect) {
		MandatoryAspect result = getMandatoryAspect(aspect);
		if (result!=null)
			return result;
		Association a =  AssociationComposer.create(getElementClassified())
			.to(aspect.getElementClassified())
			.target()
				.aggregation(AggregationKind.SHARED_LITERAL)
				.builder()
			.prefix("ma_")
			.getAssociation();
		return MandatoryAspect._HELPER.getFor(a);
	}

	@Override
	public void removeMandatoryAspect(Aspect aspect) {
		MandatoryAspect ma = getMandatoryAspect(aspect);
		if (ma!=null)
			ma.getElement().destroy();
		
	}

	@Override
	public List<MandatoryAspect> getMandatoryAspects() {
		return AlfrescoSearchHelperFactory.getMandatoryAspectSearcher(this)
				.search()
				.convert(AlfrescoSearchUtils.AssociationToMandatoryAspectConverter.INSTANCE);
	}
	
	/* common association */
	
	protected <T extends AssociationSolid> T getAssociationByName(java.lang.Class<T> clazz, String name, UMLSearchConverter<Association, ? super T> converter) {
		StereotypeDescriptor sd = ProfileRegistry.INSTANCE.getDescriptor(StereotypeDescriptor.class, clazz);
		@SuppressWarnings("unchecked")
		T result = (T)SearchHelperFactory.getAssociationSearcher()
				.target(sd.getStereotypeName())
				.startWith(getElementClassified())
				.filter(SearchFilterFactory.<Association, Class>not()
						.filter(SearchFilterFactory.associationByTarget(getElementClassified()))
						)
				.filter(
						(new UMLSearchFilterByName()).value(name)
						)
				.search()
				.convert(converter)
				.first();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends AssociationSolid> List<T> getAssociations(java.lang.Class<T> clazz, 
			UMLSearchConverter<Association, ? super T> converter){
		StereotypeDescriptor sd = ProfileRegistry.INSTANCE.getDescriptor(StereotypeDescriptor.class, clazz);;
		return (List<T>)SearchHelperFactory.getAssociationSearcher()
				.target(sd.getStereotypeName())
				.startWith(getElementClassified())
				.filter(SearchFilterFactory.<Association, Class>not()
						.filter(SearchFilterFactory.associationByTarget(getElementClassified()))
						)
				.search()
				.convert(converter);		
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends AssociationSolid> List<T> getAssociationsByTarget(java.lang.Class<T> clazz, 
			UMLSearchConverter<Association, ? super T> converter, AlfrescoProfile.ForClass.ClassMain target){
		StereotypeDescriptor sd = ProfileRegistry.INSTANCE.getDescriptor(StereotypeDescriptor.class, clazz);;
		return (List<T>)SearchHelperFactory.getAssociationSearcher()
				.target(sd.getStereotypeName())
				.startWith(getElementClassified())
				.filter(SearchFilterFactory.<Association, Class>not()
						.filter(SearchFilterFactory.associationByTarget(getElementClassified()))
						)
				.filter((new UMLSearchFilterAssociationByTarget()).value(target.getElementClassified()))
				.search()
				.convert(converter);		
	}
	
	protected void removeAssociation(AssociationSolid as) {
		if (as!=null && as.getElement()!=null)
			as.getElement().destroy();
	}
	
	/* peer associations */
	
	@Override
	public AlfrescoProfile.ForAssociation.Association addPeerAssociation(
			AlfrescoProfile.ForClass.ClassMain peer, String name) {
		
		String nameToSet = name;
		if (!CommonUtils.isValueable(nameToSet))
			nameToSet = getElementClassified().getName() + "_" + getElementClassified().getName();
		
		AlfrescoProfile.ForAssociation.Association result = 
				getPeerAssociation(nameToSet);
		
		if (result!=null) {
			AssociationComposer ab = AssociationComposer.create(result.getElementClassified());
			if (!ab.target().getType().equals(peer.getElement())) {
				result = null;
				ab.getAssociation().destroy();
			}
		}
		
		if (result==null) {
			Association a = AssociationComposer.create(getElementClassified())
				.to(peer.getElementClassified())
				.target()
					.navigable(false)
					.builder()
				.source()
					.navigable(false)
					.builder()
				.getAssociation();
			a.setName(nameToSet);
			
			result = AlfrescoProfile.ForAssociation.Association._HELPER.getFor(a);
			result.setTitle(nameToSet);
				
		}
		
		return result;
	}

	@Override
	public AlfrescoProfile.ForAssociation.Association getPeerAssociation(
			String name) {
		AlfrescoProfile.ForAssociation.Association a = 
			AlfrescoSearchHelperFactory.getPeerAssociationSearcher(this)
				.filter(
						(new UMLSearchFilterByName()).value(name)
						)
				.search()
				.convert(
						AlfrescoSearchUtils.AssociationToPeerAssociationConverter.INSTANCE
						)
				.first();
		return a;
	}

	@Override
	public List<AlfrescoProfile.ForAssociation.Association> getPeerAssociations() {
		return AlfrescoSearchHelperFactory.getPeerAssociationSearcher(this)
				.search()
				.convert(
						AlfrescoSearchUtils.AssociationToPeerAssociationConverter.INSTANCE
						);
	}

	@Override
	public List<AlfrescoProfile.ForAssociation.Association> getPeerAssociations(
			AlfrescoProfile.ForClass.ClassMain peer) {
		return AlfrescoSearchHelperFactory.getPeerAssociationSearcher(this)
				.filter((new UMLSearchFilterAssociationByTarget()).value(peer.getElementClassified()))
				.search()
				.convert(
						AlfrescoSearchUtils.AssociationToPeerAssociationConverter.INSTANCE
						);
	}

	@Override
	public void removePeerAssociation(String name) {
		AlfrescoProfile.ForAssociation.Association a = getPeerAssociation(name);
		if (a!=null)
			a.getElement().destroy();
	}

	@Override
	public void removePeerAssociations() {
		List<AlfrescoProfile.ForAssociation.Association> list = getPeerAssociations();
		for (AlfrescoProfile.ForAssociation.Association a: list)
			a.getElement().destroy();
		
	}

	@Override
	public void removePeerAssociations(AlfrescoProfile.ForClass.ClassMain peer) {
		List<AlfrescoProfile.ForAssociation.Association> list = getPeerAssociations(peer);
		for (AlfrescoProfile.ForAssociation.Association a: list)
			a.getElement().destroy();
	}

	/* child associations */
	
	@Override
	public ChildAssociation addChildAssociation(
			AlfrescoProfile.ForClass.ClassMain child, String name) {
		
		String nameToSet = name;
		if (!CommonUtils.isValueable(nameToSet))
			nameToSet = getElementClassified().getName() + "_" + getElementClassified().getName();
		
		AlfrescoProfile.ForAssociation.ChildAssociation result = 
				getChildAssociation(nameToSet);
		
		if (result!=null) {
			AssociationComposer ab = AssociationComposer.create(result.getElementClassified());
			if (!ab.target().getType().equals(child.getElement())) {
				result = null;
				ab.getAssociation().destroy();
			}
		}
		
		if (result==null) {
			Association a = AssociationComposer.create(getElementClassified())
				.to(child.getElementClassified())
				.target()
					.navigable(false)
					.aggregation(AggregationKind.SHARED_LITERAL)
					.builder()
				.source()
					.navigable(false)
					.builder()
				.getAssociation();
			a.setName(nameToSet);
			
			result = AlfrescoProfile.ForAssociation.ChildAssociation._HELPER.getFor(a);
			result.setTitle(nameToSet);
				
		}
		
		return result;	}

	@Override
	public ChildAssociation getChildAssociation(String name) {
//			return getAssociationByName(ChildAssociation.class, name, AlfrescoSearchUtils.AssociationToChildAssociationConverter.INSTANCE);
		AlfrescoProfile.ForAssociation.ChildAssociation a = 
				AlfrescoSearchHelperFactory.getChildAssociationSearcher(this)
					.filter(
							(new UMLSearchFilterByName()).value(name)
							)
					.search()
					.convert(
							AlfrescoSearchUtils.AssociationToChildAssociationConverter.INSTANCE
							)
					.first();
			return a;		
	}

	@Override
	public List<ChildAssociation> getChildAssociations() {
		// return getAssociations(ChildAssociation.class, AlfrescoSearchUtils.AssociationToChildAssociationConverter.INSTANCE);
		return AlfrescoSearchHelperFactory.getChildAssociationSearcher(this)
				.search()
				.convert(
						AlfrescoSearchUtils.AssociationToChildAssociationConverter.INSTANCE
						);		
	}

	@Override
	public List<ChildAssociation> getChildAssociations(
			AlfrescoProfile.ForClass.ClassMain child) {
/*		return getAssociationsByTarget(ChildAssociation.class, 
				AlfrescoSearchUtils.AssociationToChildAssociationConverter.INSTANCE, child);
*/	
		return AlfrescoSearchHelperFactory.getChildAssociationSearcher(this)
				.filter((new UMLSearchFilterAssociationByTarget()).value(child.getElementClassified()))
				.search()
				.convert(
						AlfrescoSearchUtils.AssociationToChildAssociationConverter.INSTANCE
						);
		
	}

	@Override
	public void removeChildAssociation(String name) {
		ChildAssociation ca = getChildAssociation(name);
		removeAssociation(ca);
		
	}

	@Override
	public void removeChildAssociations() {
		List<ChildAssociation> list = getChildAssociations();
		for (ChildAssociation ca: list)
			removeAssociation(ca);
	}

	@Override
	public void removeChildAssociations(AlfrescoProfile.ForClass.ClassMain child) {
		List<ChildAssociation> list = getChildAssociations(child);
		for (ChildAssociation ca: list)
			removeAssociation(ca);
	}

	@Override
	public String getName() {
		return this.nd.getName();
	}

	@Override
	public void setName(String value) {
		this.nd.setName(value);
	}
	
	@Override
	public List<Namespace> getRequiredNamespaces() {
		Set<Namespace> result = new HashSet<>();
		
		for (Property p: getAllProperties()) 
			result.addAll(p.getRequiredNamespaces());
		
		for (Inherit i: getInherits())
			result.add(Namespace._HELPER.findNearestFor(i.getGeneral()));
		
		for (ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association peer: getPeerAssociations()) {
			result.add(Namespace._HELPER.findNearestFor(peer.getSource()));
			result.add(Namespace._HELPER.findNearestFor(peer.getTarget()));
		}
		
		for (ChildAssociation ca: getChildAssociations()) {
			result.add(Namespace._HELPER.findNearestFor(ca.getChild()));
		}
		
		for (MandatoryAspect ma: getMandatoryAspects())
			result.add(Namespace._HELPER.findNearestFor(ma.getAspect()));
		
		return new ArrayList<>(result);
	}

	@Override
	public PropertyOverride overrideProperty(Property property) {
		if (property==null)
			return null;
		List<PropertyOverride> overrides = getAllPropertyOverrides();
		for (PropertyOverride po: overrides)
			try {
				if (property.getElementClassified().equals(po.getOverridenProperty().getElementClassified()))
					return po;
			} catch (Exception e) {
				
			}
		Dependency d = getElementClassified().createDependency(property.getElementClassified());
		return PropertyOverride._HELPER.getFor(d);
	}

	@Override
	public List<PropertyOverride> getAllPropertyOverrides() {
		return AlfrescoSearchHelperFactory.getPropertyOverrideSearcher(this)
				.search()
				.convert(AlfrescoSearchUtils.DependencyToPropertyOverrideConverter.INSTANCE);
	}

	@Override
	public Namespace getNamespace() {
		return nd.getNamespace();
	}

	@Override
	public String getPrfixedName() {
		return nd.getPrfixedName();
	}
	
}
