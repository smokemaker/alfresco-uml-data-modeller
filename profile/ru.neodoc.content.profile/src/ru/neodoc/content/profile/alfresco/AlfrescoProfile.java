package ru.neodoc.content.profile.alfresco;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.LiteralSpecification;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Profile;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.AlfrescoProfileLibrary.SimpleParameter;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.ChildAssociation;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Aspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Type;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.Inline;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.Constrainted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.ConstraintedInline;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.PropertyOverride;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForGeneralization.Inherit;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForNamedElement.ConstraintedObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackageImport.ImportNamespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPrimitiveType.DataType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.ConstraintType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Enforced;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Named;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.NamespaceClient;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.NamespaceDefinition;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.NamespaceDefinitionUpdatable;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Namespaced;
import ru.neodoc.content.profile.meta.alfresco.forassociation.AssociationMainAbstract;
import ru.neodoc.content.profile.meta.alfresco.forclass.AbstractClass;
import ru.neodoc.content.profile.meta.alfresco.forconstraint.ConstraintMainAbstract;
import ru.neodoc.content.profile.meta.alfresco.forconstraint.ConstraintOptionalAbstract;
import ru.neodoc.content.profile.meta.alfresco.forpackage.PackageMainAbstract;
import ru.neodoc.content.profile.meta.alfresco.forproperty.PropertyOptinal;
import ru.neodoc.content.profile.meta.alfresco.forproperty.PropertyOptionalAbstract;
import ru.neodoc.content.profile.meta.alfresco.internal.TextualDescription;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.annotation.ACustomDataTypeClass;
import ru.neodoc.content.utils.uml.profile.annotation.AImplemented;
import ru.neodoc.content.utils.uml.profile.annotation.ALibrary;
import ru.neodoc.content.utils.uml.profile.annotation.AProfile;
import ru.neodoc.content.utils.uml.profile.annotation.ARelationStereotype;
import ru.neodoc.content.utils.uml.profile.annotation.ARelationStereotype.ARelation;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotype;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotype.AApplication;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotype.AApplication.Multiplicity;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypeProperties;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypeProperty;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypePropertyDefaultValue;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypePropertyDefaults;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypePropertyRedefined;
import ru.neodoc.content.utils.uml.profile.annotation.AStereotypePropertyStoreIn;
import ru.neodoc.content.utils.uml.profile.annotation.AWrapped;
import ru.neodoc.content.utils.uml.profile.library.ProfileLibraryImpl;
import ru.neodoc.content.utils.uml.profile.registry.ProfileRegistry;
import ru.neodoc.content.utils.uml.profile.requirement.ARequirement;
import ru.neodoc.content.utils.uml.profile.requirement.ARequirementAggregation;
import ru.neodoc.content.utils.uml.profile.requirement.ARequirementLink;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotypeClassified;

@AProfile(name="alfresco")
@ALibrary
public class AlfrescoProfile extends AbstractProfile {

	
	public static final AbstractProfile _INSTANCE = new AlfrescoProfile();
	

	protected AlfrescoProfile() {
		super();
	}
	
	public static synchronized URI getProfileUri() {
/*		if (profileUri == null) {
			IRegisteredProfile registeredProfile = RegisteredProfile.getRegisteredProfile(_INSTANCE.getName());
			if (registeredProfile == null)
				return null;
			
			String path = registeredProfile.getPath();
			if (path == null)
				return null;
			if (path.trim().length()==0)
				return null;
			
			profileUri = URI.createURI(path);
		}
		return profileUri;*/
		return _INSTANCE.getUMLProfileUri();
	}
	
	public static Profile getProfile(ResourceSet resourceSet) {
/*		Profile result = (Profile)PackageUtil.loadPackage(getProfileUri(), resourceSet);
		return result;
*/	
		return ProfileRegistry.INSTANCE.getProfileDescriptor(AlfrescoProfile.class).getProfile();
	}

	public static Profile getProfile(Element element) {
		if (element==null)
			return null;
		if (element.eResource().getResourceSet()!=null)
			return getProfile(element.eResource().getResourceSet());
		return null;
	}
	

	/*
	 * Profile contents
	 */
	
	@ALibrary
	public static class AlfrescoProfileLibrary extends ProfileLibraryImpl {
		
		@ACustomDataTypeClass(wrapper=AlfrescoProfile.ForLiteralSpecification.SimpleParameterWrapper.class)
		public static class SimpleParameter {
			
			public String name;
			public String value;
			public List<String> list = null;
			
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public List<String> getList() {
				return list;
			}
			public void setList(List<String> list) {
				this.list = list;
			}
			
		}
		
	}
	
	
	public static interface Internal {
		
		public static enum ConstraintType {
			LENGTH,
			MINMAX,
			LIST,
			REGEX,
			CUSTOM,
			REGISTERED
		}
		
		@AStereotype(name="stored_element", isAbstract=true)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.internal.StoredElement.class)
		public static interface StoredElement extends ProfileStereotype {
			public static final String PREDEFINED = "predefined";
			public static final String DETACHED = "detached";
			
			public boolean getPredefined();
			public boolean getDetached();

			public void setPredefined(boolean value);
			public void setDetached(boolean value);
		}
		
		@AStereotype(name="ddTextualDescription", isAbstract=true)
		@AImplemented(TextualDescription.class)
		public static interface DdTextualDescription extends ProfileStereotype {
			public static final String DD_TITLE = "ddTitle";
			public static final String DD_DESCRIPTION = "ddDescription";
			
			public String getTitle();
			public void setTitle(String title);
			
			public String getDescription();
			public void setDescription(String description);
		} 
		
		@AStereotype(name="enforced")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.internal.Enforced.class)
		public static interface Enforced extends ProfileStereotype {
			public static final String ENFORCED = "enforced";
			
			public boolean isEnforced();
			public void setEnforced(boolean value);
		}
		
		@AStereotype(name="named", isAbstract=true)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.internal.Named.class)
		public static interface Named extends ProfileStereotype {
			public String getName();
			public void setName(String value);
		}
		
		@AStereotype(name="namespaced", isAbstract=true)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.internal.Namespaced.class)
		public static interface Namespaced extends Named {
			public Namespace getNamespace();
			public String getPrfixedName();
		}
		
		public static interface NamespaceClient extends ProfileStereotype {
			public List<Namespace> getRequiredNamespaces();
		}
		
		@AStereotype(name="namespaceDefinition", isAbstract=true)
		public static interface NamespaceDefinition extends ProfileStereotype {
			public String getUri();
			public String getPrefix();
		}

		@AStereotype(name="namespaceDefinitionUpdatable", isAbstract=true)
		public static interface NamespaceDefinitionUpdatable extends NamespaceDefinition {
			public void setUri(String value);
			public void setPrefix(String value);
		}
		
	}
	
	public static interface ForLiteralSpecification {
		
		@AStereotype(name=SimpleParameterWrapper._NAME)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forliteralspecification.SimpleParameterWrapper.class)
		public static interface SimpleParameterWrapper extends ProfileStereotypeClassified<LiteralSpecification> {
			
			public static final String _NAME = "SimpleParameter-wrapper";
			
			@AStereotypeProperties
			public static interface PROPERTIES {
				@AStereotypeProperty(name=PROPERTIES.WRAPPED_VALUE, type=SimpleParameter.class)
				public static final String WRAPPED_VALUE = "wrappedValue";
			}
			
			public SimpleParameter getWrappedValue();
			public void setWrappedValue(SimpleParameter value);
			
		}
		
	}
	
	public static interface ForClass {
		@AStereotype(name="class_main", 
				applications = {@AApplication(classes={Class.class})}, 
				isAbstract=true, 
				profile=AlfrescoProfile.class
		)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forclass.ClassMain.class)
		@AWrapped(AbstractClass.class)
		public static interface ClassMain extends Internal.StoredElement, Internal.DdTextualDescription, Namespaced, 
				NamespaceClient, ProfileStereotypeClassified<Class> {
			public static final String DD_INCLUDED_IN_SUPERTYPE_QUERY = "ddIncludedInSuperTypeQuery";
			
			public boolean getIncludedInSuperTypeQuery();
			public void setIncludedInSuperTypeQuery(boolean value);
			
			public Property getProperty(String propertyName, PrimitiveType propertyType, boolean create);
			@ARequirementLink
			public List<Property> getAllProperties();
			
			public PropertyOverride overrideProperty(Property property);
			public List<PropertyOverride> getAllPropertyOverrides();
			
			@ARequirementLink
			public List<Inherit> getInherits();
			public Inherit inherit(ClassMain parent);
			
			/* mandatory aspects */
			public MandatoryAspect addMandatoryAspect(Aspect aspect);
			public void removeMandatoryAspect(Aspect aspect);
			@ARequirementLink
			public List<MandatoryAspect> getMandatoryAspects();
			
			/* peer associations */
			public Association addPeerAssociation(ClassMain peer, String name);
			public Association getPeerAssociation(String name);
			@ARequirementLink
			public List<Association> getPeerAssociations();
			public List<Association> getPeerAssociations(ClassMain peer);
			public void removePeerAssociation(String name);
			public void removePeerAssociations();
			public void removePeerAssociations(ClassMain peer);
			
			/* child associations */
			public ChildAssociation addChildAssociation(ClassMain child, String name);
			public ChildAssociation getChildAssociation(String name);
			@ARequirementLink
			public List<ChildAssociation> getChildAssociations();
			public List<ChildAssociation> getChildAssociations(ClassMain child);
			public void removeChildAssociation(String name);
			public void removeChildAssociations();
			public void removeChildAssociations(ClassMain child);
		}

		@AStereotype(name="class_optional", 
				applications = {
					@AApplication(classes={Class.class}, requires={ClassMain.class}, multiple=Multiplicity.UNIQUE)
				}, 
				isAbstract=true, 
				profile=AlfrescoProfile.class
		)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forclass.ClassOptional.class)
		public static interface ClassOptional extends ProfileStereotypeClassified<Class> {
		}
		
		@AStereotype(name="type")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forclass.Type.class)
		public static interface Type extends ClassMain {
			public static final String _NAME = "type";

			public static class Helper extends StereotypeHelper<Type, org.eclipse.uml2.uml.Class>{
			}
			public static StereotypeHelper<Type, org.eclipse.uml2.uml.Class> _HELPER = new Helper();
		}

		@AStereotype(name="aspect")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forclass.Aspect.class)
		public static interface Aspect extends ClassMain {
			public static final String _NAME = "aspect";

			public static class Helper extends StereotypeHelper<Aspect, org.eclipse.uml2.uml.Class>{
			}
			public static StereotypeHelper<Aspect, org.eclipse.uml2.uml.Class> _HELPER = new Helper();
		}
		
		@AStereotype(name="archive")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forclass.Archive.class)
		public static interface Archive extends ClassOptional {
			public static final String _NAME = "archive";

			public static class Helper extends StereotypeHelper<Archive, org.eclipse.uml2.uml.Class>{
			}
			public static StereotypeHelper<Archive, org.eclipse.uml2.uml.Class> _HELPER = new Helper();
		}		
	}
	
	public static interface ForNamedElement {
		
		@AStereotype(name="constrainted_object", isAbstract=true)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.fornamedelement.ConstraintedObject.class)
		public static interface ConstraintedObject<T extends NamedElement> 
				extends ProfileStereotypeClassified<T> {
			
			@ARequirementLink
			public List<Constrainted> getAllConstraints();
			@ARequirementLink
			public List<Constrainted> getConstraintRefs();
			public List<ConstraintedInline> getInlineConstraints();
			
			public Constrainted addConstraintRef(ConstraintMain constraint);
			public ConstraintedInline addInlineConstraint(ConstraintMain constraint);
			
			public org.eclipse.uml2.uml.Namespace getConstraintContext();
		}
		
	}
	
	public static interface ForConstraint {
		
		@AStereotype(name="constraint_main",
				applications = {@AApplication(classes= {Constraint.class})},
				isAbstract=true,
				profile=AlfrescoProfile.class)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forconstraint.ConstraintMain.class)
		@AWrapped(ConstraintMainAbstract.class)
		public static interface ConstraintMain extends ProfileStereotypeClassified<Constraint>, Namespaced {
			
			public static final StereotypeFactory<ConstraintType, ConstraintMain> _FACTORY = 
					new StereotypeFactory<>(
							Arrays.asList(
								ConstraintType.LENGTH,
								ConstraintType.MINMAX,
								ConstraintType.LIST,
								ConstraintType.REGEX,
								ConstraintType.CUSTOM,
								ConstraintType.REGISTERED
							), 
							Arrays.asList(
								ConstraintLength.class,
								ConstraintMinmax.class,
								ConstraintList.class,
								ConstraintRegex.class,
								ConstraintCustom.class,
								ConstraintRegistered.class
							)
						);
			
			@AStereotypeProperties
			public static interface PROPERTIES {
				@AStereotypeProperty(name=PROPERTIES.TYPE,externalName=PROPERTIES.TYPE_EXT,type=String.class,exposedType=ConstraintType.class,isReadOnly=true)
				public static final String TYPE = "_type";
				public static final String TYPE_EXT = "type";

				@AStereotypeProperty(type=SimpleParameter[].class, exposedType=List.class)
				public static final String PARAMETERS = "parameters";
			}
			
			public ConstraintType getConstraintType();
			public void setConstraintType(ConstraintType constraintType);
			
			public List<SimpleParameter> getParameters();
			public void setParameters(List<SimpleParameter> parameters);
		}
		
		@AStereotype(name="constraint-length")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forconstraint.ConstraintLength.class)
		public static interface ConstraintLength extends ConstraintMain {
			
			public static final String _NAME = "constraint-length";
			
			public static class Helper extends StereotypeHelper<ConstraintLength, Constraint>{
				
			}
			public static StereotypeHelper<ConstraintLength, Constraint> _HELPER = new Helper();

			@AStereotypeProperties
			public static interface PROPERTIES extends ConstraintMain.PROPERTIES {
				@AStereotypeProperty(name=PROPERTIES.MIN_LENGTH,type=Integer.class)
				@AStereotypePropertyStoreIn(storageOwner=ConstraintMain.class, storagePropertyName=ConstraintMain.PROPERTIES.PARAMETERS)
				public static final String MIN_LENGTH = "minLength";
				@AStereotypeProperty(name=PROPERTIES.MAX_LENGTH,type=Integer.class)
				@AStereotypePropertyStoreIn(storageOwner=ConstraintMain.class, storagePropertyName=ConstraintMain.PROPERTIES.PARAMETERS)
				public static final String MAX_LENGTH = "maxLength";
				
				@AStereotypePropertyDefaults
				public static interface DEFAULTS {
					@SuppressWarnings("hiding")
					@AStereotypePropertyDefaultValue(propertyName=PROPERTIES.MIN_LENGTH,owner=ConstraintLength.class)
					public static final Integer MIN_LENGTH = 0;
					@SuppressWarnings("hiding")
					@AStereotypePropertyDefaultValue(propertyName=PROPERTIES.MAX_LENGTH,owner=ConstraintLength.class)
					public static final Integer MAX_LENGTH = Integer.MAX_VALUE;
					@SuppressWarnings({ "hiding", "static-access" })
					@AStereotypePropertyDefaultValue(propertyName=PROPERTIES.TYPE,owner=ConstraintMain.class)
					public static final ConstraintType TYPE = ConstraintType.LENGTH;
				}
			}
			
			public int getMinLength();
			public void setMinLength(int minLength);
			public int getMaxLength();
			public void setMaxLength(int maxLength);
			
		}
		
		@AStereotype(name="constraint-minmax")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forconstraint.ConstraintMinmax.class)
		public static interface ConstraintMinmax extends ConstraintMain {
			
			public static final String _NAME = "constraint-minmax";
			
			public static class Helper extends StereotypeHelper<ConstraintMinmax, Constraint>{
				
			}
			public static StereotypeHelper<ConstraintMinmax, Constraint> _HELPER = new Helper();

			@AStereotypeProperties
			public static interface PROPERTIES extends ConstraintMain.PROPERTIES {
				@AStereotypeProperty(name=PROPERTIES.MIN_VALUE,type=Double.class)
				@AStereotypePropertyStoreIn(storageOwner=ConstraintMain.class, storagePropertyName=ConstraintMain.PROPERTIES.PARAMETERS)
				public static final String MIN_VALUE = "minValue";
				@AStereotypeProperty(name=PROPERTIES.MAX_VALUE,type=Double.class)
				@AStereotypePropertyStoreIn(storageOwner=ConstraintMain.class, storagePropertyName=ConstraintMain.PROPERTIES.PARAMETERS)
				public static final String MAX_VALUE = "maxValue";
				
				@AStereotypePropertyDefaults
				public static interface DEFAULTS {
					@SuppressWarnings("hiding")
					@AStereotypePropertyDefaultValue(propertyName=PROPERTIES.MIN_VALUE,owner=ConstraintMinmax.class)
					public static final Double MIN_VALUE = Double.MIN_VALUE;
					@SuppressWarnings("hiding")
					@AStereotypePropertyDefaultValue(propertyName=PROPERTIES.MAX_VALUE,owner=ConstraintMinmax.class)
					public static final Double MAX_VALUE = Double.MAX_VALUE;
					@SuppressWarnings({ "hiding", "static-access" })
					@AStereotypePropertyDefaultValue(propertyName=PROPERTIES.TYPE,owner=ConstraintMain.class)
					public static final ConstraintType TYPE = ConstraintType.MINMAX;
				}
			}
			
			
			public double getMinValue();
			public void setMinValue(double minValue);
			public double getMaxValue();
			public void setMaxValue(double maxValue);
			
		}
		
		@AStereotype(name="constraint-regex")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forconstraint.ConstraintRegex.class)
		public static interface ConstraintRegex extends ConstraintMain {
			
			public static final String _NAME = "constraint-regex";
			
			public static class Helper extends StereotypeHelper<ConstraintRegex, Constraint>{
				
			}
			public static StereotypeHelper<ConstraintRegex, Constraint> _HELPER = new Helper();

			@AStereotypeProperties
			public static interface PROPERTIES extends ConstraintMain.PROPERTIES {
				@AStereotypeProperty(name=PROPERTIES.REQUIRES_MATCH, type=Boolean.class)
				@AStereotypePropertyStoreIn(storageOwner=ConstraintMain.class, storagePropertyName=ConstraintMain.PROPERTIES.PARAMETERS)
				public static final String REQUIRES_MATCH = "requiresMatch";
				@AStereotypeProperty(name=PROPERTIES.EXPRESSION, type=String.class)
				@AStereotypePropertyStoreIn(storageOwner=ConstraintMain.class, storagePropertyName=ConstraintMain.PROPERTIES.PARAMETERS)
				public static final String EXPRESSION = "expression";
				
				@AStereotypePropertyDefaults
				public static interface DEFAULTS {
					@SuppressWarnings({ "hiding", "static-access" })
					@AStereotypePropertyDefaultValue(propertyName=PROPERTIES.TYPE,owner=ConstraintMain.class)
					public static final ConstraintType TYPE = ConstraintType.REGEX;
				}
			}			
			
			@Override
			@AStereotypePropertyRedefined(ownerClass=ConstraintMain.class, propertyName="type", defaultValue="REGEX")
			public ConstraintType getConstraintType();
			
			public boolean getRequiresMatch();
			public void setRequiresMatch(boolean requiresMatch);
			public String getExpression();
			public void setExpression(String expression);
		}
		
		@AStereotype(name="constraint-list")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forconstraint.ConstraintList.class)
		public static interface ConstraintList extends ConstraintMain {
			
			public static final String _NAME = "constraint-list";
			
			public static class Helper extends StereotypeHelper<ConstraintList, Constraint>{
				
			}
			public static StereotypeHelper<ConstraintList, Constraint> _HELPER = new Helper();
			
			@Override
			@AStereotypePropertyRedefined(ownerClass=ConstraintMain.class, propertyName="type", defaultValue="LIST")
			public ConstraintType getConstraintType();

			@AStereotypeProperties
			public static interface PROPERTIES extends ConstraintMain.PROPERTIES {
				@AStereotypeProperty(name=PROPERTIES.CASE_SENSITIVE, type=Boolean.class)
				@AStereotypePropertyStoreIn(storageOwner=ConstraintMain.class, storagePropertyName=ConstraintMain.PROPERTIES.PARAMETERS)
				public static final String CASE_SENSITIVE = "caseSensitive";
				@AStereotypeProperty(name=PROPERTIES.ALLOWED_VALUES, type=String[].class, exposedType=List.class)
				@AStereotypePropertyStoreIn(storageOwner=ConstraintMain.class, storagePropertyName=ConstraintMain.PROPERTIES.PARAMETERS)
				public static final String ALLOWED_VALUES = "allowedValues";
				@AStereotypeProperty(name=PROPERTIES.SORTED, type=Boolean.class)
				@AStereotypePropertyStoreIn(storageOwner=ConstraintMain.class, storagePropertyName=ConstraintMain.PROPERTIES.PARAMETERS)
				public static final String SORTED = "sorted";
				
				@AStereotypePropertyDefaults
				public static interface Defaults {
					@SuppressWarnings("hiding")
					@AStereotypePropertyDefaultValue(propertyName=PROPERTIES.SORTED, owner=ConstraintList.class)
					public static final Boolean SORTED = false;
					@SuppressWarnings("hiding")
					@AStereotypePropertyDefaultValue(propertyName=PROPERTIES.CASE_SENSITIVE, owner=ConstraintList.class)
					public static final Boolean CASE_SENSITIVE = true;
				}
			}			
			
			
			public boolean getCaseSensitive();
			public void setCaseSensitive(boolean caseSensitive);
			public List<String> getAllowedValues();
			public void setAllowedValues(List<String> allowedValues);
			public boolean getSorted();
			public void setSorted(boolean sorted);
		}
		
		@AStereotype(name="constraint-custom")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forconstraint.ConstraintCustom.class)
		public static interface ConstraintCustom extends ConstraintMain {
			
			public static final String _NAME = "constraint-custom";
			
			public static class Helper extends StereotypeHelper<ConstraintCustom, Constraint>{
				
			}
			public static StereotypeHelper<ConstraintCustom, Constraint> _HELPER = new Helper();
			
			@AStereotypeProperties
			public static interface PROPERTIES extends ConstraintMain.PROPERTIES {
				@AStereotypeProperty(type=String.class)
				public static final String CLASS_NAME = "className";

				@AStereotypePropertyDefaults
				public static interface DEFAULTS {
					@SuppressWarnings({ "hiding", "static-access" })
					@AStereotypePropertyDefaultValue(propertyName=PROPERTIES.TYPE,owner=ConstraintMain.class)
					public static final ConstraintType TYPE = ConstraintType.CUSTOM;
				}
				
			}			
				
			
			@Override
			@AStereotypePropertyRedefined(ownerClass=ConstraintMain.class, propertyName="type", defaultValue="CUSTOM")
			public ConstraintType getConstraintType();
			
			public String getClassName();
			public void setClassName(String className);
		}
		
		@AStereotype(name="constraint-registered")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forconstraint.ConstraintRegistered.class)
		public static interface ConstraintRegistered extends ConstraintMain {
			
			public static final String _NAME = "constraint-registered";
			
			public static class Helper extends StereotypeHelper<ConstraintRegistered, Constraint>{
				
			}
			public static StereotypeHelper<ConstraintRegistered, Constraint> _HELPER = new Helper();
			
			@AStereotypeProperties
			public static interface PROPERTIES extends ConstraintMain.PROPERTIES {
				@AStereotypeProperty(type=String.class)
				@AStereotypePropertyStoreIn(storageOwner=ConstraintMain.class, storagePropertyName=ConstraintMain.PROPERTIES.PARAMETERS)
				public static final String REGISTERED_NAME = "registeredName";

				@AStereotypePropertyDefaults
				public static interface DEFAULTS {
					@SuppressWarnings({ "hiding", "static-access" })
					@AStereotypePropertyDefaultValue(propertyName=PROPERTIES.TYPE,owner=ConstraintMain.class)
					public static final ConstraintType TYPE = ConstraintType.REGISTERED;
				}
				
			}			
				
			
			
			public String getRegisteredName();
			public void setRegisteredName(String registeredName);
		}
		
		@AStereotype(name="constraint_optional",
				applications= {@AApplication(classes= {Constraint.class},
						requires= {ConstraintMain.class},
						multiple=Multiplicity.UNIQUE)},
				isAbstract=true,
				profile=AlfrescoProfile.class
				)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forconstraint.ConstraintOptional.class)
		@AWrapped(ConstraintOptionalAbstract.class)
		public static interface ConstraintOptional extends ProfileStereotypeClassified<Constraint> {
			
		}
		
		@AStereotype(name="inline",profile=AlfrescoProfile.class)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forconstraint.Inline.class)
		public static interface Inline extends ConstraintOptional {
			
			public static final String _NAME = "inline";
			
			public static class Helper extends StereotypeHelper<Inline, Constraint>{
				
			}
			public static StereotypeHelper<Inline, Constraint> _HELPER = new Helper();
			
		}
	}
	
	public static interface ForDependency {
		
		@AStereotype(name="constrainted",profile=AlfrescoProfile.class)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.fordependency.Constrainted.class)
		@ARelationStereotype(relations= {
				@ARelation(source= {Property.class, PropertyOverride.class}, target= {ConstraintMain.class}, notTarget= {Inline.class})
			})
		public static interface Constrainted extends ProfileStereotypeClassified<Dependency>{
			public static final String _NAME = "constrainted";
			
			public static class Helper extends StereotypeHelper<Constrainted, Dependency>{
				
			}
			public static StereotypeHelper<Constrainted, Dependency> _HELPER = new Helper();
			
			@ARequirement
			public ConstraintMain getConstraint();
			
			@ARequirement
			public ConstraintedObject<? extends NamedElement> getConstraintedObject();
		}
		
		@AStereotype(name="constrainted-inline",profile=AlfrescoProfile.class)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.fordependency.ConstraintedInline.class)
		@ARelationStereotype(relations= {
			@ARelation(
					source= {Property.class, PropertyOverride.class}, 
					target= {Inline.class},
					sourceIsMultiple = false)
		})
		public static interface ConstraintedInline extends Constrainted{
			public static final String _NAME = "constrainted-inline";
			
			public static class Helper extends StereotypeHelper<ConstraintedInline, Dependency>{
				
			}
			public static StereotypeHelper<ConstraintedInline, Dependency> _HELPER = new Helper();
			
		}
		
		@AStereotype(name=PropertyOverride._NAME)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.fordependency.PropertyOverride.class)
		@ARelationStereotype(relations= {
				@ARelation(source= {Aspect.class, Type.class}, target= {Property.class})
		})
		public static interface PropertyOverride extends ConstraintedObject<Dependency> {
			
			public static final String _NAME = "property-override";
			
			public static class Helper extends StereotypeHelper<PropertyOverride, NamedElement>{
			}
			public static StereotypeHelper<PropertyOverride, NamedElement> _HELPER = new Helper();
			
			@AStereotypeProperties
			public static interface PROPERTIES {
				@AStereotypeProperty(name=PROPERTIES.MANDATORY, type=Boolean.class)
				public static final String MANDATORY = "mandatory";
				@AStereotypeProperty(name=PROPERTIES.DEFAULT_VALUE, type=String.class)
				public static final String DEFAULT_VALUE = "defaultValue";
			}
			
			public Boolean getMandatory();
			public void setMandatory(Boolean value);
			
			public String getDefaultValue();
			public void setDefaultValue(String value);
			
			@ARequirement
			public Property getOverridenProperty();
			public ClassMain getOverridingClass();
		}
	}
	
	public static interface ForPackage {
		
		@AStereotype(name="package_main", isAbstract=true)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forpackage.PackageMain.class)
		@AWrapped(PackageMainAbstract.class)
		public static interface PackageMain extends ProfileStereotypeClassified<Package>, NamespaceClient {
			
		}
		
		@AStereotype(name="model")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forpackage.Model.class)
		public static interface Model extends PackageMain, Internal.StoredElement, Named {
			
			public static final String _NAME = "model";
			
			public static final String LOCATION = "location";
			public static final String DESCRIPTION = "description";
			public static final String AUTHOR = "author";
			public static final String PUBLISHED = "published";
			public static final String VERSION = "version";
			
			public String getLocation();
			public String getDesription();
			public String getAuthor();
			public String getPublished();
			public String getVersion();

			public void setLocation(String value);
			public void setDescription(String value);
			public void setAuthor(String value);
			public void setPublished(String value);
			public void setVersion(String value);
		
			public Namespace getNamespace(String prefix, boolean create);
			
			public ImportNamespace importNamespace(Namespace namespace);
			public void dropImportNamespace(Namespace namespace);
			public List<Namespace> getImportedNamespaces();
			
			@ARequirementLink
			public List<Namespace> getAllNamespaces();
			
			public static class Helper extends StereotypeHelper<Model, Package>{
			}
			public static StereotypeHelper<Model, Package> _HELPER = new Helper();
			
		}
		
		@AStereotype(name="namespace")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forpackage.Namespace.class)
		public static interface Namespace extends PackageMain, NamespaceDefinitionUpdatable {
			public static final String _NAME = "namespace";
			
			public static final String DEFINED_IN_FILES = "definedInFiles";
			
			public List<String> getDefinedInFiles();
			public void setDefinedInFiles(List<String> value);
			
			public ConstraintMain getConstraint(String name, boolean create);
			@ARequirementAggregation
			public List<ConstraintMain> getAllConstraints();
			
			public Aspect getAspect(String name, boolean create);
			public Type getType(String name, boolean create);
			
			@ARequirementAggregation
			public List<Aspect> getAllAspects();
			@ARequirementAggregation
			public List<Type> getAllTypes();
			public List<ClassMain> getAllClasses();
			
			public DataType getDataType(String name, boolean create);
			@ARequirementAggregation
			public List<DataType> getAllDataTypes();
			public Dependency dependent(Namespace namespace);
			public boolean isDpendent(Namespace namespace);
			public void idependent(Namespace namespace);
			
			public static class Helper extends StereotypeHelper<Namespace, Package>{
			}
			public static StereotypeHelper<Namespace, Package> _HELPER = new Helper();
		
		}
	}
	
	public static interface ForModel {
		
		@AStereotype(name="alfresco", applications= {
				@AApplication(classes= {org.eclipse.uml2.uml.Model.class})
		})
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.formodel.Alfresco.class)
		public static interface Alfresco extends ProfileStereotypeClassified<org.eclipse.uml2.uml.Model>,
				NamespaceClient {
			
			public static final String _NAME = "alfresco";
			
		}
		
	}
	
	public static interface ForAssociation {
		
		@AStereotype(name="association_main", isAbstract=true, applications= {
				@AApplication(classes= {org.eclipse.uml2.uml.Association.class})
		})
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forassociation.AssociationMain.class)
		@AWrapped(AssociationMainAbstract.class)
		public static interface AssociationMain extends ProfileStereotypeClassified<org.eclipse.uml2.uml.Association> {
			
			@ARequirement
			public ClassMain getSource();
			@ARequirement
			public ClassMain getTarget();
			
		}

		@AStereotype(name="association_optional", isAbstract=true, applications= {
				@AApplication(classes= {org.eclipse.uml2.uml.Association.class},
						requires= {AssociationMain.class})
		})
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forassociation.AssociationOptional.class)
		public static interface AssociationOptional extends ProfileStereotypeClassified<org.eclipse.uml2.uml.Association> {
		}
		
		@AStereotype(name="mandatory-aspect")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forassociation.MandatoryAspect.class)
		@ARelationStereotype(relations= {
				@ARelation(
					source = {Type.class, Aspect.class},
					target = (Aspect.class)
				)
		})
		public static interface MandatoryAspect extends AssociationMain {
			public static final String _NAME = "mandatory-aspect";
			
			@ARequirement
			public ClassMain getAspected();
			@ARequirement
			public Aspect getAspect();
			
			public static class Helper extends StereotypeHelper<MandatoryAspect, org.eclipse.uml2.uml.Association>{
			}
			public static StereotypeHelper<MandatoryAspect, org.eclipse.uml2.uml.Association> _HELPER = new Helper();
			
		}
		
		@AStereotype(name="association_solid", isAbstract=true)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forassociation.AssociationSolid.class)
		public static interface AssociationSolid extends AssociationMain, Internal.DdTextualDescription {
			
		}
		
		@AStereotype(name="association")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forassociation.Association.class)
		public static interface Association extends AssociationSolid, Namespaced {
			public static final String _NAME = "association";

			public static class Helper extends StereotypeHelper<Association, org.eclipse.uml2.uml.Association>{
			}
			public static StereotypeHelper<Association, org.eclipse.uml2.uml.Association> _HELPER = new Helper();
		}
		
		@AStereotype(name="child-association")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forassociation.ChildAssociation.class)
		public static interface ChildAssociation extends Association {
			public static final String _NAME = "child-association";
			
			// properties
			public static final String DUPLICATE = "duplicate";
			public static final String CHILD_NAME = "childName";
			public static final String PROPAGATE_TIMESTAMPS = "propagateTimestamps";

			public boolean isDuplicate();
			public boolean isPropagateTimestamps();
			public String getChildName();

			public void setDuplicate(Boolean value);
			public void setPropagateTimestamps(Boolean value);
			public void setChildName(String value);
			
			// operations
			@ARequirement
			public ClassMain getComposite();
			@ARequirement
			public ClassMain getChild();
			
			public static class Helper extends StereotypeHelper<ChildAssociation, org.eclipse.uml2.uml.Association>{
			}
			public static StereotypeHelper<ChildAssociation, org.eclipse.uml2.uml.Association> _HELPER = new Helper();
		}
		
		@AStereotype(name="optional")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forassociation.Optional.class)
		public static interface Optional extends AssociationOptional {
			public static final String _NAME = "optional";

			public static class Helper extends StereotypeHelper<Optional, org.eclipse.uml2.uml.Association>{
			}
			public static StereotypeHelper<Optional, org.eclipse.uml2.uml.Association> _HELPER = new Helper();
		}

		@AStereotype(name=AssociationSolidOptional._NAME, isAbstract=true, applications= {
				@AApplication(requires= {Association.class}),
				@AApplication(requires= {ChildAssociation.class})
		})
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forassociation.AssociationSolidOptional.class)
		public static interface AssociationSolidOptional extends ProfileStereotypeClassified<org.eclipse.uml2.uml.Association> {
			public static final String _NAME = "association_solid_optional";
		} 
		
		@AStereotype(name="target-mandatory")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forassociation.TargetMandatory.class)
		public static interface TargetMandatory extends AssociationSolidOptional, Enforced {
			public static final String _NAME = "target-mandatory";

			public static class Helper extends StereotypeHelper<TargetMandatory, org.eclipse.uml2.uml.Association>{
			}
			
			public static StereotypeHelper<TargetMandatory, org.eclipse.uml2.uml.Association> _HELPER = new Helper();
		}
	}
	
	public static interface ForPackageImport {
		
		@AStereotype(name="importNamespace",
			applications= {
				@AApplication(classes= {PackageImport.class})
			}
		)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forpackageimport.ImportNamespace.class)
		public static interface ImportNamespace extends ProfileStereotypeClassified<PackageImport>, NamespaceDefinition {
			public static final String _NAME = "importNamespace";

			public static class Helper extends StereotypeHelper<ImportNamespace, PackageImport>{
			}
			public static StereotypeHelper<ImportNamespace, PackageImport> _HELPER = new Helper();
		}
		
	}
	
	public static interface ForPrimitiveType {

		@AStereotype(name="data-type", applications= {
				@AApplication(classes= {PrimitiveType.class})
			}
		)
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forprimitivetype.DataType.class)
		public static interface DataType extends ProfileStereotypeClassified<PrimitiveType>, Internal.DdTextualDescription, Namespaced {
			public static final String _NAME = "data-type";
			
			public static final String ANAYLYZER_CLASS = "analyzerClass";
			public static final String JAVA_CLASS = "javaClass";
			
			public String getAnalyzerClass();
			public String getJavaClass();

			public void setAnalyzerClass(String value);
			public void setJavaClass(String value);
			
			public static class Helper extends StereotypeHelper<DataType, PrimitiveType>{
			}
			public static StereotypeHelper<DataType, PrimitiveType> _HELPER = new Helper();
			
		}
		
	}
	
	public static interface ForGeneralization {

		@AStereotype(name="inherit", applications= {
				@AApplication(classes= {Generalization.class})
		})
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forgeneralization.Inherit.class)
		@ARelationStereotype(relations= {
				@ARelation(source= {Type.class}, target= {Type.class}),
				@ARelation(source= {Aspect.class}, target= {Aspect.class})
		})
		public static interface Inherit extends ProfileStereotypeClassified<Generalization> {
			public static final String _NAME = "inherit";
			
			@ARequirement
			public ClassMain getGeneral();
			@ARequirement
			public ClassMain getSpecific();
			
			public static class Helper extends StereotypeHelper<Inherit, Generalization>{
			}
			public static StereotypeHelper<Inherit, Generalization> _HELPER = new Helper();
		}
		
	}
	
	public static interface ForProperty {
		
		@AStereotype(name="property_main", isAbstract=true, applications= {
				@AApplication (classes= {org.eclipse.uml2.uml.Property.class})
		})
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forproperty.PropertyMain.class)
		public static interface PropertyMain extends ProfileStereotypeClassified<org.eclipse.uml2.uml.Property>, 
				Internal.DdTextualDescription, NamespaceClient, ForNamedElement.ConstraintedObject<org.eclipse.uml2.uml.Property> {
			
			@ARequirement
			public DataType getDataType();
			public void setDataType(DataType dataType);
			
		}
		
		@AStereotype(name="property")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forproperty.Property.class)
		public static interface Property extends PropertyMain, Namespaced {
			public static final String _NAME = "property";
			
			public boolean isProtected(); 
			public void setProtected(boolean value);
			
			public boolean isMultiple();
			public void setMultiple(boolean value);
			
			public static class Helper extends StereotypeHelper<Property, org.eclipse.uml2.uml.Property>{
			}
			public static StereotypeHelper<Property, org.eclipse.uml2.uml.Property> _HELPER = new Helper();
		} 
		
		@AStereotype(name="property_optional", isAbstract=true, applications= {
				@AApplication (classes= {org.eclipse.uml2.uml.Property.class}, requires= {Property.class})
		})
		@AImplemented(PropertyOptinal.class)
		@AWrapped(PropertyOptionalAbstract.class)
		public static interface PropertyOptional extends ProfileStereotypeClassified<org.eclipse.uml2.uml.Property> {
			
		}
		
		@AStereotype(name="encrypted")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forproperty.Encrypted.class)
		public static interface Encrypted extends PropertyOptional {
			public static final String _NAME = "optional";

			public static class Helper extends StereotypeHelper<Encrypted, org.eclipse.uml2.uml.Property>{
			}
			public static StereotypeHelper<Encrypted, org.eclipse.uml2.uml.Property> _HELPER = new Helper();
		}

		@AStereotype(name="index")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forproperty.Index.class)
		public static interface Index extends PropertyOptional {
			public static final String _NAME = "index";
			
			public static final String ENABLED = "enabled";
			public static final String ATOMIC = "atomic";
			public static final String STORED = "stored";
			public static final String TOKENISED = "tokenised";
			
			public Boolean isEnabled();
			public void setEnabled(Boolean value);

			public Boolean isAtomic();
			public void setAtomic(Boolean atomic);

			public Boolean isStored();
			public void setStored(Boolean stored);
			
			public String getTokenised();
			public void setTokenised(String value);

			public static class Helper extends StereotypeHelper<Index, org.eclipse.uml2.uml.Property>{
			}
			public static StereotypeHelper<Index, org.eclipse.uml2.uml.Property> _HELPER = new Helper();
		}
		
		@AStereotype(name="mandatory")
		@AImplemented(ru.neodoc.content.profile.meta.alfresco.forproperty.Mandatory.class)
		public static interface Mandatory extends Internal.Enforced, PropertyOptional {
			public static final String _NAME = "mandatory";
			
			public boolean getMandatory();
			public void setMandatory(boolean value);
			
			public static class Helper extends StereotypeHelper<Mandatory, org.eclipse.uml2.uml.Property>{
			}
			public static StereotypeHelper<Mandatory, org.eclipse.uml2.uml.Property> _HELPER = new Helper();
		}
	}
}
