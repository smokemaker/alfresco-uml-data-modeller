package ru.neodoc.content.modeller.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.alfresco.model.dictionary._1.Aspect;
import org.alfresco.model.dictionary._1.Class;
import org.alfresco.model.dictionary._1.MandatoryDef;
import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.NamedValue;
import org.alfresco.model.dictionary._1.ObjectFactory;
import org.alfresco.model.dictionary._1.Property;
import org.alfresco.model.dictionary._1.Property.Index;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.AlfrescoProfileLibrary.SimpleParameter;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.AssociationSolid;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.TargetMandatory;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Archive;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintLength;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintList;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMinmax;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintRegex;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintRegistered;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.Constrainted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.ConstraintedInline;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Encrypted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Mandatory;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.ConstraintType;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.AssociationComposer;
import ru.neodoc.content.utils.uml.profile.descriptor.PropertyDefaultValueDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.StereotypeDescriptor;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

@Deprecated
public class UML2XML {
	
	public static ObjectFactory of = new ObjectFactory(); 
	
	public static Model emptyModel(){
		return of.createModel();
	}
	
	public static Model emptyModel(Package model) {
		Model result = emptyModel();
		return fillModel(model, result);
	}
	
	public static Model fillModel(Package model, Model xmlModel) {
		Model result = xmlModel;
		@SuppressWarnings("static-access")
		AlfrescoProfile.ForPackage.Model umlModel = AlfrescoProfile.asUntyped(model).get(AlfrescoProfile.ForPackage.Model.class);
		
//		if (AlfrescoUMLUtils.isModel(model)) {
		if (umlModel!=null) {
/*			Stereotype st = AlfrescoUMLUtils.getStereotype(model, AlfrescoProfile.ForPackage.Model._NAME);
			
			result.setName(model.getName());
			result.setAuthor((String)model.getValue(st, AlfrescoProfile.ForPackage.Model.AUTHOR));
			result.setDescription((String)model.getValue(st, AlfrescoProfile.ForPackage.Model.DESCRIPTION));
*/
			result.setName(model.getName());
			result.setAuthor(umlModel.getAuthor());
			result.setDescription(umlModel.getDesription());
			
			String published = /*(String)model.getValue(st, AlfrescoProfile.ForPackage.Model.PUBLISHED);*/umlModel.getPublished();
			if (published != null) {
				try {
					Date dob=null;
					try {
						DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						dob=df.parse(published);
					} catch (ParseException pe) {
						DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
						dob=df.parse(published);
					}
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(dob);
					XMLGregorianCalendar xgc = DatatypeFactory.newInstance()
							.newXMLGregorianCalendarDate(cal.get(Calendar.YEAR), 
										cal.get(Calendar.MONTH)+1, 
										cal.get(Calendar.DAY_OF_MONTH), 
										0);
					result.setPublished(xgc);
				} catch (Exception e) {
					// set "published" to now
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(new Date());
					try {
						XMLGregorianCalendar xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
						result.setPublished(xgc);
					} catch (Exception e1) {
						// NOOP
					}
				}
			}
			
			result.setVersion(/*(String)model.getValue(st, AlfrescoProfile.ForPackage.Model.VERSION)*/umlModel.getVersion());
/*			result.setNamespaces(of.createModelNamespaces());
			Namespace ns = of.createModelNamespacesNamespace();
			ns.setPrefix("");
			ns.setUri("");
			result.getNamespaces().getNamespace().add(ns);
*/		}
		return result;
	}
	
	public static Model.Imports modelImports(EList<PackageImport> packageImports) {
		Model.Imports result = of.createModelImports();
		List<Model.Imports.Import> imports = result.getImport();
		Model.Imports.Import _import;
		Package importedPackage;
		for (PackageImport pi: packageImports) {
			importedPackage =pi.getImportedPackage();
			_import = of.createModelImportsImport();
			_import.setPrefix(importedPackage.getName());
			_import.setUri(importedPackage.getURI());
			imports.add(_import);
		}
		return result;
	}

	public static Model.Namespaces modelNamespaces(List<Package> umlNamespacesLlist) {
		Model.Namespaces result = of.createModelNamespaces();
		List<Model.Namespaces.Namespace> namespaces = result.getNamespace();
		Model.Namespaces.Namespace namespace;

		for (Package p: umlNamespacesLlist) {
			namespace = of.createModelNamespacesNamespace();
			namespace.setPrefix(p.getName());
			namespace.setUri(p.getURI());
			namespaces.add(namespace);
		}
		return result;
	}
	
	public static Model.DataTypes modelDataTypes(List<DataType> umlDataTypesList){
		Model.DataTypes result = of.createModelDataTypes();
		List<Model.DataTypes.DataType> dataTypes = result.getDataType();
		Model.DataTypes.DataType dataType;
		
		for (DataType dt: umlDataTypesList) {
			dataType = of.createModelDataTypesDataType();
			dataType.setName(AlfrescoUMLUtils.getFullName(dt));
			
			@SuppressWarnings("static-access")
			AlfrescoProfile.ForPrimitiveType.DataType umlDataType = 
					AlfrescoProfile.asType(dt, AlfrescoProfile.ForPrimitiveType.DataType.class);
			
/*			dataType.setTitle(AlfrescoUMLUtilsDeprecated.getTitle(dt));
			dataType.setDescription(AlfrescoUMLUtilsDeprecated.getDescription(dt));
*/			
			dataType.setTitle(umlDataType.getTitle());
			dataType.setDescription(umlDataType.getDescription());
			
/*			dataType.setAnalyserClass(AlfrescoUMLUtils.getStereotypeValue(
					AlfrescoProfile.ForPrimitiveType.DataType._NAME, 
					dt, 
					AlfrescoProfile.ForPrimitiveType.DataType.ANAYLYZER_CLASS));
*/
			dataType.setAnalyserClass(umlDataType.getAnalyzerClass());
			
/*			dataType.setAnalyserClass(AlfrescoUMLUtils.getStereotypeValue(
					AlfrescoProfile.ForPrimitiveType.DataType._NAME, 
					dt, 
					AlfrescoProfile.ForPrimitiveType.DataType.JAVA_CLASS));
*/
			dataType.setJavaClass(umlDataType.getJavaClass());
			
			dataTypes.add(dataType);
		}
		return result;
	};
	
	public static Model.Constraints modelConstraints(List<Constraint> umlConstraintsList){
		Model.Constraints result = of.createModelConstraints();
		List<org.alfresco.model.dictionary._1.Constraint> constraints = result.getConstraint();
		org.alfresco.model.dictionary._1.Constraint constraint;
		
		for (Constraint con: umlConstraintsList) {
			constraints.add(modelConstraint(con));
		}
		return result;
	};
	
	public static org.alfresco.model.dictionary._1.Constraint modelConstraint(Constraint umlConstraint){
		org.alfresco.model.dictionary._1.Constraint constraint = of.createConstraint();
		constraint.setName(AlfrescoUMLUtils.getFullName(umlConstraint));
		
		@SuppressWarnings("static-access")
		AlfrescoProfile.ForConstraint.ConstraintMain constraintMain = 
				AlfrescoProfile.asType(umlConstraint, AlfrescoProfile.ForConstraint.ConstraintMain.class);
		
		constraint.setName(AlfrescoUMLUtils.getFullName(umlConstraint));
		
		ConstraintType ct = constraintMain.getConstraintType();
		if (ConstraintType.CUSTOM.equals(ct)) {
			constraint.setType(constraintMain.get(AlfrescoProfile.ForConstraint.ConstraintCustom.class).getClassName());
		} else {
			constraint.setType(ct.name());
		}
		
		fillConstraintParameters(ct, constraintMain, constraint);
		return constraint;
	}
	
	public static NamedValue createNamedValue(String name, Object value) {
		if (value == null)
			return createNamedValue(name, "", null);
		if (List.class.isAssignableFrom(value.getClass()))
			return createNamedValue(name, null, (List)value);
		return createNamedValue(name, value, null);
	}
	
	public static NamedValue createNamedValue(String name, Object value, List<?> list) {
		if (!CommonUtils.isValueable(name))
			return null;
		NamedValue result = new NamedValue();
		result.setName(name);
		
		if ((list!=null) && (!list.isEmpty())) {
			List<String> l = new ArrayList<>();
			for (Object obj: list) {
				if (obj==null)
					l.add("");
				else
					l.add(obj.toString());
			}
			result.setList(new NamedValue.List());
			result.getList().getValue().addAll(l);
		} else {
			result.setValue(value==null?"":value.toString());
		}
		
		return result;
	}
	
	public static NamedValue createNamedValue(ProfileStereotype profileStereotype, String propertyName) {
		StereotypeDescriptor sd = StereotypeDescriptor.find(profileStereotype.getClass());
		if (sd==null)
			return null;
//		PropertyDescriptor pd = sd.findPropertyDescriptor(propertyName);
		PropertyDefaultValueDescriptor pdvd = sd.getPropertyDefaultValueDescriptor(propertyName);
		Object value = profileStereotype.getAttribute(propertyName);
		if (pdvd != null) {
			if ((pdvd.getValue()==null) && (value==null))
				return null;
			if (pdvd.getValue()!=null)
				if (pdvd.getValue().equals(value))
					return null;
		}
		return createNamedValue(propertyName, value);
	}
	
	public static void fillConstraintParameters(ConstraintType constraintType, 
			AlfrescoProfile.ForConstraint.ConstraintMain constraintMain,
			org.alfresco.model.dictionary._1.Constraint constraint) {
		
		List<NamedValue> valuesToSet = new ArrayList<>();
		switch (constraintType) {
		case REGISTERED:
			ConstraintRegistered crg = constraintMain.get(ConstraintRegistered.class);
			valuesToSet.add(createNamedValue(crg, ConstraintRegistered.PROPERTIES.REGISTERED_NAME));
			break;
		case LENGTH:
			ConstraintLength cl = constraintMain.get(ConstraintLength.class);
			valuesToSet.add(createNamedValue(cl, ConstraintLength.PROPERTIES.MIN_LENGTH));
			valuesToSet.add(createNamedValue(cl, ConstraintLength.PROPERTIES.MAX_LENGTH));
			break;
		case LIST:
			ConstraintList clist = constraintMain.get(ConstraintList.class);
			valuesToSet.add(createNamedValue(clist, ConstraintList.PROPERTIES.ALLOWED_VALUES));
			valuesToSet.add(createNamedValue(clist, ConstraintList.PROPERTIES.CASE_SENSITIVE));
			valuesToSet.add(createNamedValue(clist, ConstraintList.PROPERTIES.SORTED));
			break;
		case MINMAX:
			ConstraintMinmax cm = constraintMain.get(ConstraintMinmax.class);
			valuesToSet.add(createNamedValue(cm, ConstraintMinmax.PROPERTIES.MIN_VALUE));
			valuesToSet.add(createNamedValue(cm, ConstraintMinmax.PROPERTIES.MAX_VALUE));
			break;
		case REGEX:
			ConstraintRegex cr = constraintMain.get(ConstraintRegex.class);
			valuesToSet.add(createNamedValue(cr, ConstraintRegex.PROPERTIES.EXPRESSION));
			valuesToSet.add(createNamedValue(cr, ConstraintRegex.PROPERTIES.REQUIRES_MATCH));
			break;
		case CUSTOM:;
		}

		for (SimpleParameter sp: constraintMain.getParameters()) {
			valuesToSet.add(createNamedValue(sp.getName(), sp.getValue(), sp.getList()));
		}
		
		constraint.getParameter().clear();
		for (NamedValue nv: valuesToSet)
			if (nv!=null)
				constraint.getParameter().add(nv);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static <T extends Class> List<T> modelClasses(List<? extends Type> umlTypesList) {
		List<T> result = new ArrayList<>();
		
		// ContentModellerPlugin.getDefault().log("-- BEGIN: modelClasses");
		for (Type t: umlTypesList)
			if (AlfrescoUMLUtils.isType(t)) {
				result.add((T)modelType(t));
				// ContentModellerPlugin.getDefault().log("-- >> added type: " + t.getName());
			} else if (AlfrescoUMLUtils.isAspect(t)) {
				result.add((T)modelAspect(t));
				// ContentModellerPlugin.getDefault().log("-- >> added aspect: " + t.getName());
			}
		
		// ContentModellerPlugin.getDefault().log("-- END: modelClasses");
		return result;
	}
	
	public static void fillModelClass(Class clazz, Type umlType) {
		
		// ContentModellerPlugin.getDefault().log("-- BEGIN fillModelClass FOR: " + umlType.getName());
		
		clazz.setName(AlfrescoUMLUtils.getFullName(umlType));
		// ContentModellerPlugin.getDefault().log("-- >> name set: " + AlfrescoUMLUtils.getFullName(umlType));
		
		@SuppressWarnings("static-access")
		ClassMain cm = AlfrescoProfile.asUntyped(umlType).get(ClassMain.class);
/*		ContentModellerPlugin.getDefault().log("-- >> ClassMain set: " 
				+ AlfrescoProfile.asUntyped(umlType).get(ClassMain.class).getClass().getName());
*/
//		if (umlType instanceof org.eclipse.uml2.uml.Class) {
		if (cm!=null) {

			String str = /*AlfrescoUMLUtilsDeprecated.getTitle(umlType)*/cm.getTitle();
			if (str!=null)
				clazz.setTitle(str);
			str = /*AlfrescoUMLUtilsDeprecated.getDescription(umlType)*/cm.getDescription();
			if (str!=null)
				clazz.setDescription(str);

			// ContentModellerPlugin.getDefault().log("-- >> title & description set");
		
			org.eclipse.uml2.uml.Class umlClass = (org.eclipse.uml2.uml.Class)umlType;

			/*
			 * parent
			 */
			List<Generalization> gens = umlClass.getGeneralizations();
			if (gens.size()>0)
				clazz.setParent(AlfrescoUMLUtils.getFullName(gens.get(0).getGeneral()));
			// ContentModellerPlugin.getDefault().log("-- >> parent set");
			
			/*
			 * archive
			 */
/*			if (AlfrescoUMLUtils.hasStereotype(umlType, AlfrescoProfile.ForClass.Archive._NAME))
				clazz.setArchive(Boolean.TRUE);
*/
			clazz.setArchive(new Boolean(cm.has(Archive.class)));
			// ContentModellerPlugin.getDefault().log("-- >> archive set");
			
			/*
			 * properties
			 */
			Class.Properties properties = AlfrescoXMLUtils.getProperties(clazz);
			List<Property> propertyList = properties.getProperty();

/*			for (org.eclipse.uml2.uml.Property prop: umlClass.getOwnedAttributes()){
				
				if (!AlfrescoUMLUtils.hasStereotype(prop, AlfrescoProfile.ForProperty.Property._NAME))
					continue;
				
				Property p = classProperty(prop);
				propertyList.add(p);
			}
*/			
			for (AlfrescoProfile.ForProperty.Property prop: cm.getAllProperties()){
				propertyList.add(classProperty(prop));
			}
			
			if (propertyList.size()==0)
				clazz.setProperties(null);
			
			// ContentModellerPlugin.getDefault().log("-- >> properties set");
			
			/*
			 * associations
			 */
			// peer
			List<org.alfresco.model.dictionary._1.Association> finalPeerAssociations = new ArrayList<>();
			for (AlfrescoProfile.ForAssociation.Association a: cm.getPeerAssociations()) {
				org.alfresco.model.dictionary._1.Association assoc = peerAssociation(a);
				if (assoc!=null)
					finalPeerAssociations.add(assoc);
			}
			// ContentModellerPlugin.getDefault().log("-- >> peer collected: " + finalPeerAssociations.size());
			
			// child
			List<org.alfresco.model.dictionary._1.ChildAssociation> finalChildAssociations = new ArrayList<>();
			// ContentModellerPlugin.getDefault().log("-- >> child total: " + cm.getChildAssociations().size());
			for (AlfrescoProfile.ForAssociation.ChildAssociation a: cm.getChildAssociations()) {
				org.alfresco.model.dictionary._1.ChildAssociation assoc = childAssociation(a);
				if (assoc!=null)
					finalChildAssociations.add(assoc);
			}
			// ContentModellerPlugin.getDefault().log("-- >> >> child collected: " + finalChildAssociations.size());
			
			if (finalPeerAssociations.size()>0){
				AlfrescoXMLUtils.getAssociations(clazz).getAssociation().clear();
				AlfrescoXMLUtils.getAssociations(clazz).getAssociation().addAll(finalPeerAssociations);
			}
			// ContentModellerPlugin.getDefault().log("-- >> peer set");
			if (finalChildAssociations.size()>0){
				AlfrescoXMLUtils.getAssociations(clazz).getChildAssociation().clear();
				AlfrescoXMLUtils.getAssociations(clazz).getChildAssociation().addAll(finalChildAssociations);
			}
			// ContentModellerPlugin.getDefault().log("-- >> child set");
			
			if (finalChildAssociations.size()==0 && finalPeerAssociations.size()==0){
				clazz.setAssociations(null);
			}
			
			// mandatory aspects
			List<String> finalmandatoryAspects = new ArrayList<>();
			for (MandatoryAspect aspect: cm.getMandatoryAspects()){
				String mandatoryAspect = mandatoryAspect(aspect);
				if (mandatoryAspect!=null)
					finalmandatoryAspects.add(mandatoryAspect);
			}
			if (finalmandatoryAspects.size()>0){
				AlfrescoXMLUtils.getMandatoryAspects(clazz).getAspect().clear();
				clazz.getMandatoryAspects().getAspect().addAll(finalmandatoryAspects);
			} else {
				clazz.setMandatoryAspects(null);
			}
			// ContentModellerPlugin.getDefault().log("-- >> mandatory aspects set");
		}
		
		// ContentModellerPlugin.getDefault().log("-- END fillModelClass FOR: " + umlType.getName());
		
	}
	
	public static Property classProperty(AlfrescoProfile.ForProperty.Property attribute) {
		Property result = of.createProperty();
		
		/*
		 * name
		 */
		String attrName = attribute.getElementClassified().getName();
		String[] aName = attrName.split(":");
		if (aName.length<2) {
			attrName = AlfrescoUMLUtils.getFullName(attribute.getElementClassified()); 
		}
		result.setName(attrName);
		
		/*
		 * type
		 */
		AlfrescoProfile.ForPrimitiveType.DataType dataType = attribute.getDataType();
		if (dataType!=null)
			result.setType(AlfrescoUMLUtils.getFullName(dataType.getElementClassified()));
		
		/*
		 * title & description
		 */
		result.setTitle(attribute.getTitle());
		result.setDescription(attribute.getDescription());
		
		/*
		 * protected
		 */
		result.setProtected(new Boolean(attribute.isProtected()));
		
		/*
		 * index
		 */
		AlfrescoProfile.ForProperty.Index umlIndex = attribute.get(AlfrescoProfile.ForProperty.Index.class);
		if (umlIndex!=null) {
			Index index = of.createPropertyIndex();
			
			index.setEnabled(umlIndex.isEnabled());
			index.setAtomic(umlIndex.isAtomic());
			index.setStored(umlIndex.isStored());
			index.setTokenised(umlIndex.getTokenised());
			
			result.setIndex(index);
		}
		
		/*
		 * encrypted
		 */
		result.setEncrypted(new Boolean(attribute.has(Encrypted.class)));

		/*
		 * mandatory
		 */
		Mandatory umlMandatory = attribute.get(Mandatory.class);
		boolean mandatory = ((org.eclipse.uml2.uml.Property)attribute.getElement()).getLower()>0;
		Boolean enforcedValue = null;
		if (umlMandatory!=null){
			enforcedValue = umlMandatory.isEnforced();
		}
		if (mandatory || enforcedValue!=null) {
			MandatoryDef md = of.createMandatoryDef();
			md.setContent(mandatory?"true":"false");
			if (enforcedValue!=null)
				md.setEnforced(enforcedValue);
			result.setMandatory(md);
		}
		
		/*
		 * multiple
		 */
		result.setMultiple(attribute.isMultiple());
		
		
		/*
		 * constraints
		 */
		
		List<org.alfresco.model.dictionary._1.Constraint> constraints = new ArrayList<>();
		
		for (Constrainted constrainted: attribute.getConstraintRefs()) {
			ConstraintMain constraintMain = constrainted.getConstraint();
			org.alfresco.model.dictionary._1.Constraint c = of.createConstraint();
			c.setRef(AlfrescoUMLUtils.getFullName(constraintMain.getElementClassified()));
			constraints.add(c);
		}

		for (ConstraintedInline constrainted: attribute.getInlineConstraints()) {
			ConstraintMain constraintMain = constrainted.getConstraint();
			org.alfresco.model.dictionary._1.Constraint c = modelConstraint(constraintMain.getElementClassified());
			constraints.add(c);
		}

		if (result.getConstraints()!=null)
			result.getConstraints().getConstraint().clear();
		
		if (!constraints.isEmpty()) {
			if (result.getConstraints()==null)
				result.setConstraints(of.createPropertyConstraints());
			for (org.alfresco.model.dictionary._1.Constraint c: constraints)
				result.getConstraints().getConstraint().add(c);
		}
		
		return result;	
	}
	
	public static org.alfresco.model.dictionary._1.Type modelType(Type umlType){
		org.alfresco.model.dictionary._1.Type result = of.createType();
		fillModelClass(result, umlType);
		return result;
	}

	public static Aspect modelAspect(Type umlType){
		Aspect result = of.createAspect();
		fillModelClass(result, umlType);
		return result;
	}

	public static void fillAssociation(
			org.alfresco.model.dictionary._1.Association xmlAssociation, AssociationSolid umlAssociation) {
		Association association = umlAssociation.getElementClassified();
		AssociationComposer composer = AssociationComposer.create(association);
		
		xmlAssociation.setName(association.getName());
		xmlAssociation.setTitle(umlAssociation.getTitle());
		xmlAssociation.setDescription(umlAssociation.getDescription());
		
		/*
		 * source
		 */
		org.alfresco.model.dictionary._1.Association.Source source = of.createAssociationSource();
		org.eclipse.uml2.uml.Property property = composer.source().getElement();
		
		source.setMandatory(composer.source().isMandatory());
		source.setMany(composer.source().isMultiple());
		source.setRole(
				CommonUtils.isValueable(property.getName())
				?AlfrescoUMLUtils.getFullName(property, association)
				:null);
		xmlAssociation.setSource(source);
		
		/*
		 * target
		 */
		org.alfresco.model.dictionary._1.Association.Target target = of.createAssociationTarget();
		property = composer.target().getElement();
		
		target.setClazz(AlfrescoUMLUtils.getFullName(property.getType()));
		TargetMandatory tm = umlAssociation.get(TargetMandatory.class);
		Boolean enforced = false;
		if (tm!=null)
			enforced = tm.isEnforced();
		
		if (enforced!=null || composer.target().isMandatory()){
			MandatoryDef mandatory = of.createMandatoryDef();
			if (property.getUpper()>1)
				mandatory.setContent("true");
			else
				mandatory.setContent("false");
			if (enforced!=null)
				mandatory.setEnforced(enforced);
			target.setMandatory(mandatory);
		}
		target.setMany(composer.target().isMultiple());
		target.setRole(
				CommonUtils.isValueable(property.getName())
				?AlfrescoUMLUtils.getFullName(property, association)
				:null);
		xmlAssociation.setTarget(target);		
	} 
	
	
	public static org.alfresco.model.dictionary._1.Association peerAssociation(AlfrescoProfile.ForAssociation.Association umlAssociation){
		org.alfresco.model.dictionary._1.Association result = of.createAssociation();
		
		AssociationComposer composer = AssociationComposer.create(umlAssociation.getElementClassified());
		if (!composer.isValid() || composer.agregates() || !composer.isDefinedFromSource())
			return null;
		
		fillAssociation(result, umlAssociation);
		
		return result;
	}
	
	public static org.alfresco.model.dictionary._1.ChildAssociation childAssociation(AlfrescoProfile.ForAssociation.ChildAssociation umlAssociation){
		// ContentModellerPlugin.getDefault().log("-- >> >> >> BEGIN childAssociation FOR:" + umlAssociation.getName());
		org.alfresco.model.dictionary._1.ChildAssociation result = of.createChildAssociation();
		// ContentModellerPlugin.getDefault().log("-- >> >> >> xml object created");
		AssociationComposer composer = AssociationComposer.create(umlAssociation.getElementClassified());
		// ContentModellerPlugin.getDefault().log("-- >> >> >> composer created:" + composer);
		try {
			// ContentModellerPlugin.getDefault().log("-- >> >> >> composer isValid:" + composer.isValid());
			// ContentModellerPlugin.getDefault().log("-- >> >> >> composer agregates:" + composer.agregates());
		} catch (Exception e) {
			e.printStackTrace();
			// ContentModellerPlugin.getDefault().log(e);
		}
		if (!composer.isValid() || !composer.agregates())
			return null;
		// ContentModellerPlugin.getDefault().log("-- >> >> >> composer ok");
		
		fillAssociation(result, umlAssociation);
		// ContentModellerPlugin.getDefault().log("-- >> >> >> filled");
		
		result.setChildName(CommonUtils.getValueable(umlAssociation.getChildName()));
		// ContentModellerPlugin.getDefault().log("-- >> >> >> childName set");
		result.setDuplicate(umlAssociation.isDuplicate());
		// ContentModellerPlugin.getDefault().log("-- >> >> >> duplicate set");
		result.setPropagateTimestamps(umlAssociation.isPropagateTimestamps());
		// ContentModellerPlugin.getDefault().log("-- >> >> >> propagateTimestamps set");
		
		return result;
	}
	
	public static String mandatoryAspect(AlfrescoProfile.ForAssociation.MandatoryAspect umlAssociation){
		AssociationComposer composer = AssociationComposer.create(umlAssociation.getElementClassified());
		if (!composer.isFull() || !composer.agregates())
			return null;
		
		return AlfrescoUMLUtils.getFullName(umlAssociation.getAspect().getElementClassified());
	}
	
}
