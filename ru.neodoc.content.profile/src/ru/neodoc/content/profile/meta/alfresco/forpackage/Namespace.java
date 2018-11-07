package ru.neodoc.content.profile.meta.alfresco.forpackage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Aspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Type;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPrimitiveType.DataType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfileUtils;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchUtils;
import ru.neodoc.content.utils.uml.UMLUtils;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.search.converter.UMLSearchConverter;

@AImplements(AlfrescoProfile.ForPackage.Namespace.class)
public class Namespace extends PackageMainAbstract
		implements AlfrescoProfile.ForPackage.Namespace {

	public Namespace(CompositeMetaObject composite) {
		super(composite);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDefinedInFiles() {
		return (List<String>)getAttribute(DEFINED_IN_FILES);
	}

	@Override
	public void setDefinedInFiles(List<String> value) {
		setAttribute(DEFINED_IN_FILES, value);
	}

	@Override
	public Aspect getAspect(String name, boolean create) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType(String name, boolean create) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ConstraintMain getConstraint(String name, boolean create) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public DataType getDataType(String name, boolean create) {
		PrimitiveType pt = AlfrescoProfileUtils.findDataType(name, getElementClassified());
		if (pt == null)
			if (!create)
				return null;
			else {
				pt = getElementClassified().createOwnedPrimitiveType(name);
			}
		if (pt==null)
			return null;
		return DataType._HELPER.getFor(pt);
	}
	
	@Override
	public List<DataType> getAllDataTypes() {
		List<DataType> result = AlfrescoSearchHelperFactory.getDataTypeSearcher()
				.startWith(getElementClassified())
				.search()
				.convert(AlfrescoSearchUtils.TypeToDataTypeConverter.INSTANCE);
		return result;
	}
	
	@Override
	public List<Aspect> getAllAspects() {
		List<Aspect> result = AlfrescoSearchHelperFactory.getClassSearcher()
				.startWith(getElementClassified())
				.search()
				.convert(AlfrescoSearchUtils.ClassToAspectConverter.INSTANCE);
		return result;
	}
	
	@Override
	public List<Type> getAllTypes() {
		List<Type> result = AlfrescoSearchHelperFactory.getClassSearcher()
				.startWith(getElementClassified())
				.search()
				.convert(AlfrescoSearchUtils.ClassToTypeConverter.INSTANCE);
		return result;
	}
	
	@Override
	public List<ClassMain> getAllClasses() {
		List<ClassMain> result = AlfrescoSearchHelperFactory.getClassSearcher()
				.startWith(getElementClassified())
				.search()
				.<ClassMain>convert(AlfrescoSearchUtils.ClassToClassMainConverter.INSTANCE);
		return result;
	}
	
	@Override
	public List<ConstraintMain> getAllConstraints() {
		List<ConstraintMain> result = AlfrescoSearchHelperFactory.getConstraintSearcher()
				.startWith(getElementClassified())
				.search()
				.<ConstraintMain>convert(AlfrescoSearchUtils.ConstraintToConstraintMainConverter.INSTANCE);
		return result;
	}
	
	protected Dependency findDependency(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace namespace) {
		Package p = getElementClassified();
		if (p==null)
			return null;
		for (Dependency d: p.getClientDependencies())
			if (d.getSuppliers().contains(namespace.getElementClassified()))
				return d;
		return null;
	}
	
	@Override
	public Dependency dependent(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace namespace) {
		Package p = getElementClassified();
		if (p==null)
			return null;
		Dependency result = findDependency(namespace);
		if (result==null)
				result = UMLUtils.dependentPackage(p, namespace.getElementClassified());
		return result;
	}
	
	@Override
	public boolean isDpendent(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace namespace) {
		return findDependency(namespace)!=null;
	}
	
	@Override
	public void idependent(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace namespace) {
		Dependency d = findDependency(namespace);
		if (d!=null)
			d.destroy();
	}

	@Override
	public void setUri(String value) {
		getElementClassified().setURI(value);
	}

	@Override
	public void setPrefix(String value) {
		getElementClassified().setName(value);
	}

	@Override
	public String getUri() {
		return getElementClassified().getURI();
	}

	@Override
	public String getPrefix() {
		return getElementClassified().getName();
	}

	@Override
	public List<AlfrescoProfile.ForPackage.Namespace> getRequiredNamespaces() {
		Set<AlfrescoProfile.ForPackage.Namespace> result = new HashSet<>();
		
		for (ClassMain cm: getAllClasses())
			result.addAll(cm.getRequiredNamespaces());
		
		result.remove(this);
		
		return new ArrayList<>(result);
	}
}
