package ru.neodoc.content.modeller.xml2uml.helper.entity;

import org.alfresco.model.dictionary._1.Class;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.tasks.ExecutionCallback;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractHelper;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractModelAwareSubHelper;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo.DependencyType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Archive;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;
import ru.neodoc.content.utils.uml.search.UMLSearchUtils;

public abstract class ClassHelper<ClassType extends Class> 
		extends AbstractModelAwareSubHelper<ClassType, org.eclipse.uml2.uml.Class> {

	protected String stereotypeToSearch;
	
	public ClassHelper() {
		super();
		stereotypeToSearch = initStereotypeToSearch();
	}
	
	protected abstract String initStereotypeToSearch();
	
	@SuppressWarnings("unchecked")
	@Override
	protected java.lang.Class<? extends AbstractHelper<?, ?>> getParentHelperClass() {
		return (java.lang.Class<? extends AbstractHelper<?, ?>>) ClassHelper.class;
	}
	
	@Override
	protected void doCustomFillModelObject(ModelObject<ClassType> modelObject,
			ClassType object) {
		super.doCustomFillModelObject(modelObject, object);
		PrefixedName pn = new PrefixedName(object.getName());
		modelObject.name = pn.getName();
		modelObject.pack = pn.getPrefix();
	}
	
	@Override
	protected String getSearchName(ModelObject<ClassType> modelObject,
			ClassType object) {
		return object.getName();
	}
	
	@Override
	public org.eclipse.uml2.uml.Class resolveElement(Package root, String name) {
		return UMLSearchUtils.classByName(root, name);
	}
	
	@Override
	protected void preSubHelpersPopulate(ModelObject<ClassType> modelObject,
			ExecutionCallback callback) {
		super.preSubHelpersPopulate(modelObject, callback);
//		populateParent(complexRegistry, modelObject, callback);
// 		populateMandatoryAspects(complexRegistry, modelObject, callback);
	}
	
	protected void populateParent(ModelObject<ClassType> modelObject,
			ExecutionCallback callback) {
		String parent = modelObject.source.getParent();
		if (parent!=null && parent.length()>0){
			ModelObject<Object> parentObj = 
					// new ModelObject<Object>(parent);
					complexRegistry().getObjectSmartFactory().getObject(parent);
			parentObj.setElement(AlfrescoUMLUtils.findAlfrescoClass(complexRegistry().getUmlRoot(), parent));
			complexRegistry().getObjectRegistry().add(parentObj);
			complexRegistry().getRelationRegistry().add(modelObject, parentObj, DependencyType.PARENT);
		}
		
	}
	
	protected void populateMandatoryAspects(ModelObject<ClassType> modelObject,
			ExecutionCallback callback) {
		if (modelObject.source.getMandatoryAspects() != null)
			for (String aspectName: modelObject.source.getMandatoryAspects().getAspect()){
				ModelObject<Object> aspectObj = 
						complexRegistry().getObjectSmartFactory().getObject(aspectName);
				aspectObj.setElement(AlfrescoUMLUtils.findAspect(aspectName, complexRegistry().getUmlRoot()));
				complexRegistry().getObjectRegistry().add(aspectObj);
				complexRegistry().getRelationRegistry().add(modelObject, aspectObj, DependencyType.MANDATORY_ASPECT);
			}
		
	}
	
	@Override
	protected org.eclipse.uml2.uml.Class createElement(ModelObject<ClassType> object) {
		ModelObject<?> moPackage = complexRegistry().getObjectRegistry().get(object.pack);
		if (moPackage==null)
			return null;
		if (moPackage.getElement()==null)
			return null;
		org.eclipse.uml2.uml.Class umlClass = ((Package)moPackage.getElement()).createOwnedClass(object.name, false);
		return umlClass;
	}
	
	protected abstract ClassMain getClassMain(StereotypedElement stereotypedElement, ModelObject<ClassType> object);
	
	@Override
	protected boolean processStereotypedElement(StereotypedElement stereotypedElement, ModelObject<ClassType> object) {
		super.processStereotypedElement(stereotypedElement, object);
		return processClassMain(getClassMain(stereotypedElement, object), object);
	}
	
	protected boolean processClassMain(ClassMain stereotypedElement, ModelObject<ClassType> object) {
		if (stereotypedElement==null)
			return false;
		Class theClass = (Class)object.source;
		if (theClass==null)
			return false;
		
		stereotypedElement.setTitle(theClass.getTitle());
		stereotypedElement.setDescription(theClass.getDescription());
		
		if (CommonUtils.isTrue(theClass.isArchive()))
			stereotypedElement.getOrCreate(Archive.class);
		else
			stereotypedElement.remove(Archive.class);
		
		return true;
	}
}
