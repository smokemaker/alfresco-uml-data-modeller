package ru.neodoc.content.modeller.xml2uml.helper.relation;

import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Imports.Import;
import org.alfresco.model.dictionary._1.Model.Namespaces.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;

import ru.neodoc.content.modeller.tasks.ExecutionCallback;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractRelationSubHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.ModelHelper;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo.DependencyType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackageImport.ImportNamespace;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class ImportSubHelper extends AbstractRelationSubHelper<Model, Import, PackageImport> {

	static {
		HELPER_REGISTRY.register(ImportSubHelper.class, ModelHelper.class, Import.class, PackageImport.class);
	}
	
	@Override
	protected List<Import> getElementsFromContainer(Model container) {
		if (container.getImports()!=null)
			if (container.getImports().getImport()!=null)
				return container.getImports().getImport();
		return Collections.emptyList();
	}

	@Override
	protected ModelObject<?> getTargetModelObject(ModelObject<Import> modelObject) {
		ModelObject<Namespace> mo =
				complexRegistry().getObjectSmartFactory().getObject(modelObject.source.getPrefix(), Package.class);
		return mo;
	}
	
	@Override
	protected PackageImport getElement(ModelObject<Import> modelObject, Import object,
			RelationInfo relationInfo, ModelObject<?> sourceModelObject, ModelObject<?> targetModelObject) {
		if (!ModelObject.hasElement(sourceModelObject))
			return null;
		
		if (sourceModelObject.getElement() instanceof Package) {
			Package modelPackage = (Package)sourceModelObject.getElement();
			for (PackageImport pi: modelPackage.getPackageImports()) {
				Package imported = pi.getImportedPackage();
				if (imported.getURI().equalsIgnoreCase(object.getUri()) 
						&& imported.getName().equalsIgnoreCase(object.getPrefix()))
					return pi;
			}
		}
			
		return null;
	}
	
	@Override
	protected void preSubHelpersPopulate(ModelObject<Import> modelObject,
			ExecutionCallback callback) {
		super.preSubHelpersPopulate(modelObject, callback);
		complexRegistry().getRelationRegistry().add(
				containerModelObject, 
				getTargetModelObject(modelObject), 
				DependencyType.DEPENDENCY);
	}
	
	@Override
	protected PackageImport createElement(ModelObject<Import> modelObject, Import object,
			RelationInfo relationInfo, ModelObject<?> sourceModelObject, ModelObject<?> targetModelObject) {
		if (!ModelObject.hasElement(sourceModelObject, targetModelObject))
			return null;
		AlfrescoProfile.ForPackage.Model model = 
				AbstractProfile.asUntyped(sourceModelObject.getElement()).get(AlfrescoProfile.ForPackage.Model.class);
		AlfrescoProfile.ForPackage.Namespace namespace = 
				AbstractProfile.asUntyped(targetModelObject.getElement()).get(AlfrescoProfile.ForPackage.Namespace.class);
		ImportNamespace imp = model.importNamespace(namespace);
		return imp==null?null:imp.getElementClassified();
	}

	@Override
	protected boolean processStereotypedElement(StereotypedElement stereotypedElement,
			ModelObject<Import> object) {
		stereotypedElement.getOrCreate(AlfrescoProfile.ForPackageImport.ImportNamespace.class);
		return super.processStereotypedElement(stereotypedElement, object);
	}
	
	@Override
	protected DependencyType getRelationType() {
		return DependencyType.IMPORT;
	}
	
}
