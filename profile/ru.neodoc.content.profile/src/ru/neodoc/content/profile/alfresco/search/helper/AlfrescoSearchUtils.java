package ru.neodoc.content.profile.alfresco.search.helper;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.ChildAssociation;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Aspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Type;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.Constrainted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.ConstraintedInline;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.PropertyOverride;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForGeneralization.Inherit;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPrimitiveType.DataType;
import ru.neodoc.content.utils.uml.search.converter.CommonUMLToProfileConverter;
import ru.neodoc.content.utils.uml.search.converter.UMLSearchConverter;

public class AlfrescoSearchUtils {

	public static class ClassToAspectConverter extends CommonUMLToProfileConverter<Class, Aspect> {
		
		public static final ClassToAspectConverter INSTANCE = new ClassToAspectConverter();
		
		protected ClassToAspectConverter() {
			super(Aspect.class);
		}
	}
	
	public static class ClassToTypeConverter extends CommonUMLToProfileConverter<Class, Type> {
		
		public static final ClassToTypeConverter INSTANCE = new ClassToTypeConverter();
		
		protected ClassToTypeConverter() {
			super(Type.class);
		}
	}
	
	public static class ClassToClassMainConverter extends CommonUMLToProfileConverter<Class, ClassMain> {
		
		public static final UMLSearchConverter<Class, ClassMain> INSTANCE = new ClassToClassMainConverter();
		
		protected ClassToClassMainConverter() {
			super(ClassMain.class);
		}
	}
	
	public static class ConstraintToConstraintMainConverter extends CommonUMLToProfileConverter<Constraint, ConstraintMain> {

		public static final UMLSearchConverter<Constraint, ConstraintMain> INSTANCE = new ConstraintToConstraintMainConverter();
		
		protected ConstraintToConstraintMainConverter() {
			super(ConstraintMain.class);
		}
		
	}
				
	
	public static class PropertyToPropertyConverter extends CommonUMLToProfileConverter<Property, ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property> {

		public static final UMLSearchConverter<Property, ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property> INSTANCE 
				= new PropertyToPropertyConverter();
		
		protected PropertyToPropertyConverter() {
			super(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property.class);
		}
		
	}

	public static class GeneralizationToInheritConverter extends CommonUMLToProfileConverter<Generalization, Inherit> {

		public static final UMLSearchConverter<Generalization, Inherit> INSTANCE = new GeneralizationToInheritConverter();
		
		protected GeneralizationToInheritConverter() {
			super(Inherit.class);
		}
		
	}

	public static class AssociationToMandatoryAspectConverter extends CommonUMLToProfileConverter<Association, MandatoryAspect> {

		public static final UMLSearchConverter<Association, MandatoryAspect> INSTANCE = new AssociationToMandatoryAspectConverter();
		
		protected AssociationToMandatoryAspectConverter() {
			super(MandatoryAspect.class);
		}
		
	}

	public static class AssociationToPeerAssociationConverter extends CommonUMLToProfileConverter<Association, ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association> {

		public static final UMLSearchConverter<Association, ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association> INSTANCE 
				= new AssociationToPeerAssociationConverter();
		
		protected AssociationToPeerAssociationConverter() {
			super(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.Association.class);
		}
		
	}

	public static class AssociationToChildAssociationConverter extends CommonUMLToProfileConverter<Association, ChildAssociation> {

		public static final UMLSearchConverter<Association, ChildAssociation> INSTANCE 
				= new AssociationToChildAssociationConverter();
		
		protected AssociationToChildAssociationConverter() {
			super(ChildAssociation.class);
		}
		
	}

	public static class PackageToNamespaceConverter extends CommonUMLToProfileConverter<Package, Namespace> {
		
		public static final UMLSearchConverter<Package, Namespace> INSTANCE 
				= new PackageToNamespaceConverter();
		
		protected PackageToNamespaceConverter() {
			super(Namespace.class);
		}
	}
	
	public static class PackageToModelConverter extends CommonUMLToProfileConverter<Package, Model> {
		
		public static final UMLSearchConverter<Package, Model> INSTANCE 
				= new PackageToModelConverter();
		
		protected PackageToModelConverter() {
			super(Model.class);
		}
	}
	
	public static class TypeToDataTypeConverter extends CommonUMLToProfileConverter<org.eclipse.uml2.uml.Type, DataType> {

		public static final UMLSearchConverter<org.eclipse.uml2.uml.Type, DataType> INSTANCE 
				= new TypeToDataTypeConverter();  
		
		protected TypeToDataTypeConverter() {
			super(DataType.class);
			// TODO Auto-generated constructor stub
		}

	}
	
	public static class DependencyToConstraintedConverter extends CommonUMLToProfileConverter<Dependency, Constrainted> {

		public static final UMLSearchConverter<Dependency, Constrainted> INSTANCE = new DependencyToConstraintedConverter();
		
		public DependencyToConstraintedConverter() {
			super(Constrainted.class);
		}
		
	}
	
	public static class DependencyToConstraintedInlineConverter extends CommonUMLToProfileConverter<Dependency, ConstraintedInline> {

		public static final UMLSearchConverter<Dependency, ConstraintedInline> INSTANCE = new DependencyToConstraintedInlineConverter();
		
		public DependencyToConstraintedInlineConverter() {
			super(ConstraintedInline.class);
		}
		
	}
	
	public static class DependencyToPropertyOverrideConverter extends CommonUMLToProfileConverter<Dependency, PropertyOverride> {

		public static final UMLSearchConverter<Dependency, PropertyOverride> INSTANCE = new DependencyToPropertyOverrideConverter();
		
		public DependencyToPropertyOverrideConverter() {
			super(PropertyOverride.class);
		}

	}
	
}
