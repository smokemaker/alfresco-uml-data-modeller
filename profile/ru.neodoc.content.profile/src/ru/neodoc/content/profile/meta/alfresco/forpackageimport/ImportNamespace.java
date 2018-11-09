package ru.neodoc.content.profile.meta.alfresco.forpackageimport;

import org.eclipse.uml2.uml.PackageImport;

import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackageImport.ImportNamespace.class)
public class ImportNamespace extends ImplementationMetaObjectClassified<PackageImport>
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackageImport.ImportNamespace {

	public ImportNamespace(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public String getUri() {
		return getElementClassified().getImportedPackage().getURI();
	}

	@Override
	public String getPrefix() {
		return getElementClassified().getImportedPackage().getName();
	}

}
