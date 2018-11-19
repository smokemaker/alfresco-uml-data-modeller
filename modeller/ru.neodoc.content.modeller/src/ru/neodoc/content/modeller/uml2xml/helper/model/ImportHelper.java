package ru.neodoc.content.modeller.uml2xml.helper.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Imports;
import org.alfresco.model.dictionary._1.Model.Imports.Import;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelper;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackageImport.ImportNamespace;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ListComparator;

public class ImportHelper extends
		AbstractSubHelper<Package, PackageImport, Model.Imports, Model.Imports.Import, AlfrescoProfile.ForPackageImport.ImportNamespace> {

	static {
		HELPER_REGISTRY.register(ImportHelper.class, ImportsHelper.class).asContained();
	}
	
	@Override
	public List<PackageImport> getSubElements(Package container) {
		AlfrescoProfile.ForPackage.Model umlModel = AlfrescoProfile.ForPackage.Model._HELPER.findNearestFor(container);
		if (umlModel!=null) {
			Package modelPackage = umlModel.getElementClassified();
			List<Namespace> namespaces = umlModel.getImportedNamespaces();
			List<PackageImport> result = new ArrayList<>();
			for (Namespace ns: namespaces) {
				Package p = ns.getElementClassified();
				result.add(modelPackage.getPackageImport(p));
			}
			return result;
		}
		return Collections.emptyList();
	}

	
	
	@Override
	protected List<Import> getOrCreateObjects(Imports container) {
		return getObjects(container);
	}

	@Override
	public List<Import> getObjects(Imports container) {
		return container.getImport();
	}

	@Override
	protected Class<ImportNamespace> getStereotypeClass() {
		return ImportNamespace.class;
	}

	@Override
	protected Import doCreateObject(PackageImport element) {
		Package p = element.getImportedPackage();
		Namespace ns = Namespace._HELPER.getFor(p);
		if (ns!=null) {
			Import imp = JaxbUtils.ALFRESCO_OBJECT_FACTORY.createModelImportsImport();
			return imp;
		}
		return null;
	}

	@Override
	protected List<ObjectContainer<Import>> addObjectsToContainer(ObjectContainer<Imports> container,
			List<ObjectContainer<Import>> objectsToAdd) {
		container.getObject().getImport().addAll((List<Import>)ObjectContainer.FACTORY.extractTyped(objectsToAdd));
		return ObjectContainer.FACTORY.createListTyped(container.getObject().getImport());
	}
	
	@Override
	protected Import doFillObjectProperties(Import object, PackageImport element, ImportNamespace stereotyped) {
		if (stereotyped!=null) {
			object.setPrefix(stereotyped.getPrefix());
			object.setUri(stereotyped.getUri());
		}
		return object;
	}

	@Override
	protected void updateProperties(Import objectToUpdate, ObjectContainer<Import> newObjectContainer) {
		objectToUpdate.setPrefix(newObjectContainer.getObject().getPrefix());
		objectToUpdate.setUri(newObjectContainer.getObject().getUri());
	}

	@Override
	public ListComparator<Import> getComparator() {
		return new CommonUtils.BaseListComparator<Import>() {

			@Override
			public String itemHash(Import item) {
				if (item==null)
					return "";
				return item.getPrefix() + "::" + item.getUri();
			}
		};
	}

}
