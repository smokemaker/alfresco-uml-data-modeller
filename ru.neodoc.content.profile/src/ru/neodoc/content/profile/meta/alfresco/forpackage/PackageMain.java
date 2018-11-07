package ru.neodoc.content.profile.meta.alfresco.forpackage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.PackageMain.class)
public class PackageMain extends ImplementationMetaObjectClassified<Package> implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.PackageMain {

	public PackageMain(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public List<Namespace> getRequiredNamespaces() {
		// to implement in subclasses
		return new ArrayList<>();
	}

}
