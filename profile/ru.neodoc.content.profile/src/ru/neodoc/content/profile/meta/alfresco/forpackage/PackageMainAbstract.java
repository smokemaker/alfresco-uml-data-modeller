package ru.neodoc.content.profile.meta.alfresco.forpackage;

import java.util.List;

import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.PackageMain;
import ru.neodoc.content.utils.uml.profile.annotation.AWraps;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AWraps(PackageMain.class)
public abstract class PackageMainAbstract extends ImplementationMetaObjectClassified<Package> implements PackageMain {

	protected PackageMain pm;
	
	public PackageMainAbstract(CompositeMetaObject composite) {
		super(composite);
		pm = createAndRegisterSubimplementor(PackageMain.class);
	}

	@Override
	public abstract List<Namespace> getRequiredNamespaces();

}
