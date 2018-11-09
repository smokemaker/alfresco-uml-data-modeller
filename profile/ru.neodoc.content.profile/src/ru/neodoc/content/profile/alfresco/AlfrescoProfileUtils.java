package ru.neodoc.content.profile.alfresco;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.UMLUtils;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.search.filter.SearchFilterFactory;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilter;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterByName;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterByStereotype;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterLogicalNot;
import ru.neodoc.content.utils.uml.search.helper.SearchHelperFactory;
import ru.neodoc.content.utils.uml.search.helper.UMLSearchHelper;

public class AlfrescoProfileUtils extends UMLUtils {

	public static enum PackageType {
		PT_ALFRESCO,
		PT_MODEL,
		PT_NAMESPACE,
		PT_NORMAL,
		PT_NOT_A_PACKAGE
	}

	@Deprecated
	public static boolean isAlfresco(Element element) {
		if (!(element instanceof Package))
			return false;
		Package pack = (Package) element;
		return hasStereotype(pack, AlfrescoProfile.ForModel.Alfresco._NAME);
	}

	@Deprecated
	public static boolean isModel(Element element) {
		if (element==null)
			return false;
		if (!(element instanceof Package))
			return false;
		Package pack = (Package) element;
		return hasStereotype(pack, AlfrescoProfile.ForPackage.Model._NAME);
	}

	@Deprecated
	public static boolean isNamespace(Element element) {
		if (!(element instanceof Package))
			return false;
		Package pack = (Package) element;
		return hasStereotype(pack, AlfrescoProfile.ForPackage.Namespace._NAME);
	}

	public static boolean isSimplePackage(Element element) {
		return !(isAlfresco(element) || isModel(element) || isNamespace(element));
	}

	@SuppressWarnings("unchecked")
	public static List<Package> getModels(Package alfresco) {
		return (List<Package>)AlfrescoSearchHelperFactory
				.getModelSearcher()
				.startWith(alfresco)
				.search();
	}

	@SuppressWarnings("unchecked")
	public static List<Package> getNamespaces(Package model) {
		return (List<Package>)AlfrescoSearchHelperFactory
				.getNamespaceSearcher()
				.startWith(model)
				.search();
	}

	@SuppressWarnings("unchecked")
	public static List<DataType> getDataTypes(Package pack) {
		return (List<DataType>)AlfrescoSearchHelperFactory
				.getDataTypeSearcher()
				.startWith(pack)
				.search();
	}

	@SuppressWarnings("unchecked")
	public static List<Constraint> getConstraints(Package pack) {
		return (List<Constraint>) AlfrescoSearchHelperFactory
				.getConstraintSearcher()
				.startWith(pack)
				.filter((new UMLSearchFilterLogicalNot<>())
						.filter((new UMLSearchFilterByStereotype())
								.value(AlfrescoProfile.ForConstraint.Inline._NAME)
							)
					)
				.search();
	}
	
	public static Package findModel(String name, Package root) {
		List<? extends Package> results = AlfrescoSearchHelperFactory
			.getModelSearcher()
			.filter((new UMLSearchFilterByName().value(name)))
			.startWith(root)
			.search();
		return results.size()==0?null:(Package)results.get(0);
	}

	public static Package findNamespace(String name, Package root) {
		return findNamespace(name, root, false);
	}
	
	public static Package findNamespace(String name, Package root, boolean useGlobal) {
	/*		Stereotype sNamespace = getNamespaceStereotype(root);
			EList<Element> elements = root.allOwnedElements();
	*/		
			UMLSearchHelper<Package, Package> sh = 
					AlfrescoSearchHelperFactory.getNamespaceSearcher()
					.filter(
							(new UMLSearchFilterByName())
							.value(name)
							)
					.startWith(root);
			
			List<? extends Package> list = sh.search();
			if (list.size()>0)
				return list.get(0);
			if (useGlobal)
				return globalFindNamespace(name, root);
			return null;
		}

	public static Package globalFindNamespace(String name, Package root) {
		ResourceSet resourceSet = root.eResource().getResourceSet();
		Package result = null;
		
		CopyOnWriteArrayList<Resource> list = new CopyOnWriteArrayList<>(resourceSet.getResources());
		for (Resource resource: list) {
			try {
				EObject rootObject = resource.getContents().get(0);
				if (!(rootObject instanceof Package))
					continue;
				AlfrescoProfile.ForModel.Alfresco alfresco =
						AbstractProfile.asUntyped((Package)rootObject).get(AlfrescoProfile.ForModel.Alfresco.class);
				if (alfresco==null)
					continue;
				for (Package p: AlfrescoSearchHelperFactory
						.getModelSearcher()
						.startWith(alfresco.getElementClassified())
						.search()) {
					AlfrescoProfile.ForPackage.Model model = AbstractProfile.asUntyped(p).get(AlfrescoProfile.ForPackage.Model.class);
					if (model==null)
						continue;
					for (AlfrescoProfile.ForPackage.Namespace namespace: model.getAllNamespaces()) {
						if (namespace.getPrefix().equals(name))
							return namespace.getElementClassified();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static org.eclipse.uml2.uml.PrimitiveType findDataType(String name, Package root) {
		return findDataType(name, root, false);
	}
	
	@SuppressWarnings("unchecked")
	public static org.eclipse.uml2.uml.PrimitiveType findDataType(String name, Package root, boolean useGlobal) {
		if (name == null || name.length()==0)
			return null;
		PrefixedName pn = new PrefixedName(name);
		Package namespace = pn.getPrefix().length()==0?root:findNamespace(pn.getPrefix(), root, useGlobal);
		if (namespace!=null)
			for (DataType t: (List<DataType>)
					AlfrescoSearchHelperFactory
						.getDataTypeSearcher()
						.target(PrimitiveType.class)
						.startWith(namespace)
						.search()){
				if (t.getName().equals(pn.getName()))
					return (org.eclipse.uml2.uml.PrimitiveType) t;
			}
		return null;
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public static Constraint findConstraint(String name, Package root) {
		if (!CommonUtils.isValueable(name))
			return null;
		PrefixedName pn = new PrefixedName(name);
		Package namespace = pn.getPrefix().length()==0?root:findNamespace(pn.getPrefix(), root);
		if (namespace!=null) {
			for (Constraint constraint: (List<Constraint>)AlfrescoSearchHelperFactory
					.getConstraintSearcher()
					.target(Constraint.class)
					.startWith(root)
					.search()) {
				if (constraint.getName().equalsIgnoreCase(pn.getName()))
					return constraint;
			}
		}
		return null;
	}
	
	public static org.eclipse.uml2.uml.Class findType(String name, Package root) {
		return findClass(root, name, getTypeStereotype(root));
	}

	public static org.eclipse.uml2.uml.Class findAspect(String name, Package root) {
		return findClass(root, name, getAspectStereotype(root));
	}

	public static org.eclipse.uml2.uml.Class findClass(Package root, String name, Stereotype stereotype) {
			if (name == null || name.length()==0)
				return null;
			String[] data = name.split(":");
			String className = data[data.length-1];
			String packageName = data.length>1?data[data.length-2]:"";
			Package namespace = packageName.length()==0?root:findNamespace(packageName, root);
			if (namespace!=null){
				UMLSearchHelper<Package, Type> sh = ru.neodoc.content.utils.uml.search.helper.SearchHelperFactory.getTypeSearcher()
					.target(Class.class)
					.target(stereotype.getName())
					.filter(SearchFilterFactory.elementByName(className))
					.startWith(namespace);
	
				return (Class)sh.first(); 
	
	/*			for (PackageableElement pe: namespace.getPackagedElements()){
					if (pe instanceof org.eclipse.uml2.uml.Class 
							&& pe.isStereotypeApplied(stereotype)
							&& pe.getName().equals(className))
						return (org.eclipse.uml2.uml.Class) pe;
				}
	*/		
			}
			return null;
		}

	@Deprecated
	public static org.eclipse.uml2.uml.Class findAlfrescoClass(Package root, String name) {
		return findAlfrescoClass(root, name, false);
	}
	
	public static org.eclipse.uml2.uml.Class findAlfrescoClass(Package root, String name, boolean useGlobal) {
		if (name == null || name.length()==0)
			return null;
	
		String[] data = name.split(":");
		String className = data[data.length-1];
		String packageName = data.length>1?data[data.length-2]:"";
		Package namespace = packageName.length()==0?root:findNamespace(packageName, root, useGlobal);
		
		if (namespace != null) {
			UMLSearchHelper<Package, Type> sh = SearchHelperFactory.getTypeSearcher()
				.target(Class.class)
				.filter(SearchFilterFactory.elementByName(className))
				.filter(SearchFilterFactory.or()
							.filter(SearchFilterFactory.elementByStereotype(AlfrescoProfile.ForClass.Aspect._NAME))
							.filter(SearchFilterFactory.elementByStereotype(AlfrescoProfile.ForClass.Type._NAME))
						)
				.startWith(namespace);
			
			return (Class)sh.first();
					
		}
			
		return null;
	}
	public static org.eclipse.uml2.uml.NamedElement findAlfrescoObject(Package root, String name, boolean useGlobal) {
		return findAlfrescoObject(root, name, useGlobal, NamedElement.class);
	}
	
	public static <T extends NamedElement> T findAlfrescoObject(Package root, String name, boolean useGlobal, java.lang.Class<T> elementClass) {
		if (name == null || name.length()==0)
			return null;
	
		String[] data = name.split(":");
		String className = data[data.length-1];
		String packageName = data.length>1?data[data.length-2]:"";
		Package namespace = packageName.length()==0?root:findNamespace(packageName, root, useGlobal);
		
		if (namespace != null) {
			UMLSearchHelper<Package, T> sh = SearchHelperFactory.<T>getNamedElementSearcher(elementClass)
				.filter(SearchFilterFactory.elementByName(className))
				.startWith(namespace);
			
			return sh.first();
					
		}
			
		return null;
	}

	public static Stereotype getModelStereotype(Element element) {
		return getStereotype(element, AlfrescoProfile.ForPackage.Model._NAME);
	}

	public static Stereotype getNamespaceStereotype(Element element) {
		return getStereotype(element, AlfrescoProfile.ForPackage.Namespace._NAME);
	}

	public static Stereotype getDataTypeStereotype(Element element) {
		return getStereotype(element, AlfrescoProfile.ForPrimitiveType.DataType._NAME);
	}

	public static Stereotype getTypeStereotype(Element element) {
		return getStereotype(element, AlfrescoProfile.ForClass.Type._NAME);
	}

	public static Stereotype getAspectStereotype(Element element) {
		return getStereotype(element, AlfrescoProfile.ForClass.Aspect._NAME);
	}

}
