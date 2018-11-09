package ru.neodoc.content.modeller.utils;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class ContainerPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object container, String property, Object[] args, Object expectedValue) {
		if (container instanceof Package) {
			Package pack = (Package) container;
			if ("isAlfresco".equals(property))
				return isRoot(pack);
			if ("isModel".equals(property))
				return isModel(pack);
			if ("isNamespace".equals(property))
				return isNamespace(pack);
			if ("inModel".equals(property))
				return isInsideModel(pack);
			if ("inNamespace".equals(property))
				return isInsideNamespace(pack);
			if ("hasXmlFile".equals(property))
				return hasXmlFile(pack);
		}
		return false;
	}

	protected boolean isRoot(Package pack) {
		return AlfrescoUMLUtils.hasStereotype(pack, AlfrescoProfile.ForModel.Alfresco._NAME);
	}

	protected boolean isModel(Package pack) {
		return AlfrescoUMLUtils.isModel(pack);
	}

	protected boolean isNamespace(Package pack) {
		return AlfrescoUMLUtils.isNamespace(pack);
	}

	protected boolean isInsideNamespace(Package pack){
		boolean result = AlfrescoUMLUtils.getNearestNamespace(pack)!=null; 
		return result;
	}

	protected boolean isInsideModel(Package pack){
		boolean result = AlfrescoUMLUtils.getNearestModel(pack)!=null; 
		return result;
	}
	
	protected boolean hasXmlFile(Package pack) {
		boolean result = false;
		
		if (AlfrescoUMLUtils.isModel(pack)) {
			Object location = pack.getValue(
					AlfrescoUMLUtils.getStereotype(pack, AlfrescoProfile.ForPackage.Model._NAME), 
					AlfrescoProfile.ForPackage.Model.LOCATION);
			result = (location!=null && (location.toString().trim().length()>0));
		}
		
		return result;
	}
}
