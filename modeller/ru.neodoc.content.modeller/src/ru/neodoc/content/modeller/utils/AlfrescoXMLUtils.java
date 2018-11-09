package ru.neodoc.content.modeller.utils;

import org.alfresco.model.dictionary._1.Class;
import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.ObjectFactory;

public class AlfrescoXMLUtils {

	public static ObjectFactory of = new ObjectFactory();
	
	public static Model.Imports getImports(Model xmlModel) {
		Model.Imports imports = xmlModel.getImports();
		if (imports == null) {
			imports = of.createModelImports();
			xmlModel.setImports(imports);
		}
		return imports;
	}
	
	public static Model.Namespaces getNamespaces(Model xmlModel) {
		Model.Namespaces namespaces = xmlModel.getNamespaces();
		if (namespaces == null) {
			namespaces = of.createModelNamespaces();
			xmlModel.setNamespaces(namespaces);
		}
		
		return namespaces;
	}

	public static Model.DataTypes getDataTypes(Model xmlModel) {
		Model.DataTypes dataTypes = xmlModel.getDataTypes();
		if (dataTypes == null) {
			dataTypes = of.createModelDataTypes();
			xmlModel.setDataTypes(dataTypes);
		}
		
		return dataTypes;
	}

	public static Model.Constraints getConstraint(Model xmlModel) {
		Model.Constraints constraints = xmlModel.getConstraints();
		if (constraints == null) {
			constraints = of.createModelConstraints();
			xmlModel.setConstraints(constraints);
		}
		return constraints;
	}
	
	public static Model.Types getTypes(Model xmlModel) {
		Model.Types types = xmlModel.getTypes();
		if (types == null) {
			types = of.createModelTypes();
			xmlModel.setTypes(types);
		}
		
		return types;
	}

	public static Model.Aspects getAspects(Model xmlModel) {
		Model.Aspects aspects = xmlModel.getAspects();
		if (aspects == null) {
			aspects = of.createModelAspects();
			xmlModel.setAspects(aspects);
		}
		
		return aspects;
	}

	public static Class.Properties getProperties(Class xmlClass) {
		Class.Properties properties = xmlClass.getProperties();
		if (properties == null) {
			properties = of.createClassProperties();
			xmlClass.setProperties(properties);
		}
		
		return properties;
	}

	public static Class.Associations getAssociations(Class xmlClass) {
		Class.Associations associations = xmlClass.getAssociations();
		if (associations == null){
			associations = of.createClassAssociations();
			xmlClass.setAssociations(associations);
		}
		return associations;
	}

	public static Class.MandatoryAspects getMandatoryAspects(Class xmlClass) {
		Class.MandatoryAspects mandatoryAspects = xmlClass.getMandatoryAspects();
		if (mandatoryAspects == null){
			mandatoryAspects = of.createClassMandatoryAspects();
			xmlClass.setMandatoryAspects(mandatoryAspects);
		}
		return mandatoryAspects;
	}
	
}
