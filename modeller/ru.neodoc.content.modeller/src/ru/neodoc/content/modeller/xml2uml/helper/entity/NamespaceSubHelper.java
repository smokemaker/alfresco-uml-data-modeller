package ru.neodoc.content.modeller.xml2uml.helper.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Namespaces.Namespace;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.model.AlfrescoModelUtils;
import ru.neodoc.content.modeller.utils.NamespaceSourceInfo;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractModelAwareSubHelper;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterByName;
import ru.neodoc.content.utils.uml.search.helper.UMLSearchHelper;

public class NamespaceSubHelper extends AbstractModelAwareSubHelper<Namespace, Package> {

	static {
		HELPER_REGISTRY.register(NamespaceSubHelper.class, ModelHelper.class, Namespace.class, Package.class);
	}
	
	@Override
	protected ModelObject<Namespace> createNewModelObject(Namespace object) {
		return complexRegistry().getObjectSmartFactory().getObject(object.getPrefix());
	}
	
	@Override
	protected String getSearchName(ModelObject<Namespace> modelObject,
			Namespace object) {
		return object.getPrefix();
	}
	
	@Override
	public Package resolveElement(Package root, String name) {
		UMLSearchHelper<Package, Package> sh = 
				AlfrescoSearchHelperFactory.getNamespaceSearcher()
				.filter(
						(new UMLSearchFilterByName())
						.value(name)
						)
				.startWith(root)
				.target((String)null);
		return /*AlfrescoUMLUtils.findNamespace(object.getPrefix(), (Package)complexRegistry.get(ComplexRegistry.PROP_ROOT_OBJECT))*/
				sh.search().first();
	}
	
	@Override
	protected void doCustomFillModelObject(ModelObject<Namespace> modelObject, Namespace object) {
		
		super.doCustomFillModelObject(modelObject, object);
		modelObject.name = object.getPrefix();
		modelObject.uri = object.getUri();
		modelObject.location = complexRegistry().get(NamespaceSourceInfo.class).dictionaryLocation;
		complexRegistry().getObjectRegistry().add(modelObject, true);
		
	}
	
	@Override
	protected List<Namespace> getElementsFromContainer(Model container) {
		if ((container.getNamespaces()!=null) && (container.getNamespaces().getNamespace()!=null))
			return container.getNamespaces().getNamespace();
		return Collections.emptyList();
	}
	
	@Override
	protected Package createElement(ModelObject<Namespace> object) {
		
		Package model = AlfrescoUMLUtils.findModel(object.model, complexRegistry().getUmlRoot());
		
		// we DO NOT allow creating namespaces not inside the model
		if (model==null)
			return null;
		
		AlfrescoProfile.ForPackage.Model theModel = AlfrescoProfile.ForPackage.Model._HELPER.getFor(model);
		
		// TODO add method hasNamespace to model
		ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace theNamespace
			= theModel.getNamespace(object.name, false);
		
		boolean exists = theNamespace!=null;
		if (theNamespace==null)
			theNamespace = theModel.getNamespace(object.name, true);

		if (!exists){
			complexRegistry().put(ComplexRegistry.PROP_CREATE_DIAGRAMS, true);
		}
		
		return theNamespace.getElementClassified();
	}
	
	@Override
	protected boolean processElement(Package element, ModelObject<Namespace> object) {
		super.processElement(element, object);
		element.setURI(object.uri);
		return true;
	}

	@Override
	protected boolean processStereotypedElement(StereotypedElement stereotypedElement, ModelObject<Namespace> object) {
		super.processStereotypedElement(stereotypedElement, object);
		stereotypedElement.getOrCreate(AlfrescoProfile.ForPackage.Namespace.class)
			.setDefinedInFiles(Arrays.asList(new String[]{object.location}));		
		return true;
	}
	
	@Override
	protected boolean doPostProcessing(ModelObject<Namespace> modelObject,
			StereotypedElement stereotypedElement) {
		super.doPostProcessing(modelObject, stereotypedElement);
		if (CommonUtils.isTrue((Boolean)complexRegistry().get(ComplexRegistry.PROP_CREATE_DIAGRAMS))) {
			AlfrescoModelUtils.createEmptyClassDiagram(
					complexRegistry().get(ComplexRegistry.PROP_ROOT_OBJECT)==null
						?complexRegistry().getUmlRoot()
						:(Package)complexRegistry().get(ComplexRegistry.PROP_ROOT_OBJECT), 
					(Package)stereotypedElement.getElement());
			AlfrescoModelUtils.createEmptyPackageDiagram((Package)stereotypedElement.getElement(), modelObject.name);
			complexRegistry().put(ComplexRegistry.PROP_CREATE_DIAGRAMS, false);
		}
		return true;
	}
}
