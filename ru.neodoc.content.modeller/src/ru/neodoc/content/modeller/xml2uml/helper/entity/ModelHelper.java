package ru.neodoc.content.modeller.xml2uml.helper.entity;

import javax.xml.datatype.XMLGregorianCalendar;

import org.alfresco.model.dictionary._1.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;

import ru.neodoc.content.modeller.model.AlfrescoModelUtils;
import ru.neodoc.content.modeller.utils.NamespaceSourceInfo;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractHelper;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class ModelHelper extends AbstractHelper<Model, Package> {

	static {
		HELPER_REGISTRY.register(ModelHelper.class, Model.class);
	}
	
/*	public ModelHelper(Model object) {
		super(object);
	}
*/
	@Override
	protected void doCustomFillModelObject(ModelObject<Model> modelObject, Model object) {
		super.doCustomFillModelObject(modelObject, object);
		modelObject.name = object.getName();
		NamespaceSourceInfo nsi = complexRegistry().get(NamespaceSourceInfo.class);
		if (nsi!=null) {
			modelObject.uri = nsi.dictionaryLocation;
			modelObject.location = nsi.dictionaryLocation;
		}
	}
	
	@Override
	protected boolean needToCreateElement(ModelObject<Model> object) {
		return super.needToCreateElement(object) || !AlfrescoUMLUtils.isModel(object.getElement());
	}
	
	protected Package getSearchRoot(ModelObject<Model> modelObject, Model object) {
		return (Package)complexRegistry().get(ComplexRegistry.PROP_ROOT_OBJECT);
	}

	protected String getSearchName(ModelObject<Model> modelObject, Model object) {
		return object.getName();
	}
	
	@Override
	protected Package getElement(ModelObject<Model> modelObject, Model object) {
		return resolveElement(
				getSearchRoot(modelObject, object), 
				getSearchName(modelObject, object)
				);
	}
	
	@Override
	public Package resolveElement(Package root, String name) {
		return AlfrescoUMLUtils.findModel(name, root);
	}
	
	@Override
	protected Package createElement(ModelObject<Model> object) {
		Package newPackage;
		Package startingPackage = complexRegistry().get("rootObject")==null
				?complexRegistry().getUmlRoot()
				:(Package)complexRegistry().get("rootObject");
		newPackage = (Package)startingPackage.createPackagedElement(object.name, UMLFactory.eINSTANCE.createPackage().eClass());
		complexRegistry().put("createDiagrams", true);
		return newPackage;
	}
	
	@Override
	protected boolean processElement(Package element, ModelObject<Model> object) {
		super.processElement(element, object);
		element.setName(object.name);
		element.setURI(object.uri);
		return true;
	}
	
	@Override
	protected boolean processStereotypedElement(StereotypedElement stereotypedElement, ModelObject<Model> object) {
		super.processStereotypedElement(stereotypedElement, object);
		AlfrescoProfile.ForPackage.Model theModel = AlfrescoProfile.ForPackage.Model._HELPER.getFor(stereotypedElement.getElement());
		
		Model model = (Model)object.source;
		
		theModel.setLocation(object.location);
		theModel.setDescription(model.getDescription());
		theModel.setAuthor(model.getAuthor());
		theModel.setPublished(model.getPublished()==null?""
						:((XMLGregorianCalendar)model.getPublished()).toString());
		theModel.setVersion(model.getVersion());
		
		return true;
	}

	@Override
	protected boolean doPostProcessing(ModelObject<Model> modelObject,
			StereotypedElement stereotypedElement) {
		super.doPostProcessing(modelObject, stereotypedElement);
		boolean b = CommonUtils.isTrue((Boolean)complexRegistry().get("crateDiagrams")); 
		if (b) {
			AlfrescoModelUtils.createEmptyPackageDiagram((Package)modelObject.getElement(), modelObject.name);
		}
		return true;
	}

	@Override
	protected ModelObject<Model> store(ModelObject<Model> modelObject) {
		complexRegistry().getObjectRegistry().add(modelObject);
		return (ModelObject<Model>)complexRegistry().getObjectRegistry().get(modelObject.getName());
	}
	
}
