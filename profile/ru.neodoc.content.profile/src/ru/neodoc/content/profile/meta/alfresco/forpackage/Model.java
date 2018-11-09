package ru.neodoc.content.profile.meta.alfresco.forpackage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.UMLFactory;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackageImport.ImportNamespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Named;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.StoredElement;
import ru.neodoc.content.profile.alfresco.AlfrescoProfileUtils;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchUtils;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model.class)
public class Model extends PackageMainAbstract implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model {

	protected StoredElement se;
	protected Named nd;
	
	public Model(CompositeMetaObject composite) {
		super(composite);
		se = createAndRegisterSubimplementor(StoredElement.class);
		nd = createAndRegisterSubimplementor(Named.class);
	}

	@Override
	public boolean getPredefined() {
		return se.getPredefined();
	}

	@Override
	public boolean getDetached() {
		return se.getDetached();
	}

	@Override
	public void setPredefined(boolean value) {
		se.setPredefined(value);
	}

	@Override
	public void setDetached(boolean value) {
		se.setDetached(value);
	}

	@Override
	public String getLocation() {
		return getString(LOCATION);
	}

	@Override
	public String getDesription() {
		return getString(DESCRIPTION);
	}

	@Override
	public String getAuthor() {
		return getString(AUTHOR);
	}

	@Override
	public String getPublished() {
		return getString(PUBLISHED);
	}

	@Override
	public String getVersion() {
		return getString(VERSION);
	}

	@Override
	public void setLocation(String value) {
		setAttribute(LOCATION, value);
	}

	@Override
	public void setDescription(String value) {
		setAttribute(DESCRIPTION, value);
	}

	@Override
	public void setAuthor(String value) {
		setAttribute(AUTHOR, value);
	}

	@Override
	public void setPublished(String value) {
		setAttribute(PUBLISHED, value);
	}

	@Override
	public void setVersion(String value) {
		setAttribute(VERSION, value);
	}

	@Override
	public Namespace getNamespace(String prefix, boolean create) {
		Package pack = (Package)this.element;
		if (pack==null)
			return null;
		Package p = AlfrescoProfileUtils.findNamespace(prefix, pack);
		if (p==null)
			if (create) {
				p = (Package)pack.createPackagedElement(prefix, UMLFactory.eINSTANCE.createPackage().eClass());
			} else 
				return null;
		Namespace result = Namespace._HELPER.getFor(p);
		return result;
	}

	@Override
	public ImportNamespace importNamespace(Namespace namespace) {
		Package p = this.getElementClassified();
		if (p==null)
			return null;
		PackageImport pi = p.getPackageImport(namespace.getElementClassified());
		if (pi==null)
			pi = p.createPackageImport(namespace.getElementClassified());
		ImportNamespace result = ImportNamespace._HELPER.getFor(pi);
		return result;
	}

	@Override
	public void dropImportNamespace(Namespace namespace) {
		PackageImport pi = this.getElementClassified().getPackageImport(namespace.getElementClassified());
		while (pi!=null) {
			pi.destroy();
			pi = this.getElementClassified().getPackageImport(namespace.getElementClassified());
			
		}
	}

	@Override
	public List<Namespace> getAllNamespaces() {
		List<Namespace> result = AlfrescoSearchHelperFactory.getNamespaceSearcher()
			.startWith(getElementClassified())
			.search()
			.convert(AlfrescoSearchUtils.PackageToNamespaceConverter.INSTANCE);
		return result;
	}

	@Override
	public List<Namespace> getImportedNamespaces() {
		List<Namespace> result = new ArrayList<>();
		for (Package p: this.getElementClassified().getImportedPackages()) {
			Namespace ns = Namespace._HELPER.getFor(p);
			if (ns!=null)
				result.add(ns);
		}
		return result;
	}

	@Override
	public String getName() {
		return nd.getName();
	}

	@Override
	public void setName(String value) {
		nd.setName(value);
	}

	@Override
	public List<Namespace> getRequiredNamespaces() {
		Set<Namespace> result = new HashSet<>();
		
		List<Namespace> namespaces = AlfrescoSearchHelperFactory.getNamespaceSearcher()
										.startWith(getElementClassified())
										.search()
										.convert(AlfrescoSearchUtils.PackageToNamespaceConverter.INSTANCE);
		
		for (Namespace ns: namespaces)
			result.addAll(ns.getRequiredNamespaces());
		
		for (Namespace ns: namespaces)
			result.remove(ns);
		
		return new ArrayList<>(result);
	}

}
