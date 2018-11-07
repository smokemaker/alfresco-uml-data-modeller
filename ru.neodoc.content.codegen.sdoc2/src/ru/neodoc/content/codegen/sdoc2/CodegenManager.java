package ru.neodoc.content.codegen.sdoc2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import ru.neodoc.content.codegen.CodegenSubject;
import ru.neodoc.content.codegen.sdoc2.config.PreferencesAwareConfiguration;
import ru.neodoc.content.codegen.sdoc2.extension.ExtensionManager;
import ru.neodoc.content.codegen.sdoc2.extension.SdocCodegenExtension;
import ru.neodoc.content.codegen.sdoc2.extension.SdocCodegenExtensionConfiguration;
import ru.neodoc.content.codegen.sdoc2.preferences.PreferenceConstants;
import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.WrapperFactory;
// import ru.neodoc.content.codegen.sdoc2.wrap.old.NamespaceWrapper;
//import ru.neodoc.content.codegen.sdoc2.wrap.old.WrapperFactory;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;

public class CodegenManager extends PreferencesAwareConfiguration {

	public static final String PROP_NAME = "_CODEGEN_MANAGER";
	
	protected CodegenSubject codegenSubject = null;

	protected Model modelElement = null;
	protected Namespace namespaceElement = null;

	// protected boolean automaticallySelectAllNamespaces;
	protected List<Namespace> availableNamespaces = new ArrayList<>();
	protected List<Namespace> namespacesToGenerate = new ArrayList<>();
	protected List<NamespaceWrapper> wrappedNamespaceList = null;
	protected Map<String, NamespaceWrapper> wrappedNamespaceMap = new HashMap<>();
	
	protected boolean requiredNamespacesAreDirty = true;
	protected List<Namespace> requiredNamespaces = new ArrayList<>();
	protected List<NamespaceWrapper> requiredNamespacesWrapped = new ArrayList<>();
	
/*	protected List<Namespace> importedNamespaces = new ArrayList<>();
	protected List<NamespaceWrapper> wrappedImportedNamespaces = new ArrayList<>();
	protected Map<String, NamespaceWrapper> wrappedImportedMap = new HashMap<>();

	protected boolean importsCollected = false;
	protected boolean needToRecollect = false;
*/	
	protected Map<SdocCodegenExtensionConfiguration, SdocCodegenExtension> extensions = new LinkedHashMap<>();
	
	
	public CodegenManager(CodegenSubject codegenSubject) {
		super(SdocCodegenPlugin.getStore());
		
		this.codegenSubject = codegenSubject;
		setValue(PROP_NAME, this);
		
		modelElement = this.codegenSubject.asModel();
		namespaceElement = this.codegenSubject.asNamespace();
		
		// automaticallySelectAllNamespaces = SdocCodegenPlugin.getBoolean(PreferenceConstants.P_SELECT_ALL_NAMESPACES);
		
		if (getBoolean(PreferenceConstants.P_SELECT_ALL_NAMESPACES) || this.codegenSubject.isNamespace()) {
			for (Namespace ns: getNamespacesAvailable())
				addToGenerate(ns);
		}
		
		fillMap();
	}
	
	protected void fillMap() {
		for (SdocCodegenExtension ext: ExtensionManager.getExtensions())
			if (ext!=null)
				extensions.put(ext.getConfiguration(), ext);
	}

	public List<SdocCodegenExtension> getExtensions(){
		return new ArrayList<>(extensions.values());
	}
	
	public List<SdocCodegenExtensionConfiguration> getConfigurations(){
		return new ArrayList<>(extensions.keySet());
	}

	public List<SdocCodegenExtensionConfiguration> getActiveConfigurations(){
		List<SdocCodegenExtensionConfiguration> result = new ArrayList<>(extensions.keySet());
		result.removeIf(new Predicate<SdocCodegenExtensionConfiguration>() {

			@Override
			public boolean test(SdocCodegenExtensionConfiguration t) {
				return !t.isActive();
			}
		});
		return result;
	}
	
	
	public List<SdocCodegenExtension> getActiveExtensions(){
		List<SdocCodegenExtension> result = new LinkedList<>();
		for (Map.Entry<SdocCodegenExtensionConfiguration, SdocCodegenExtension> entry: extensions.entrySet())
			if (entry.getKey().isActive())
				result.add(entry.getValue());
		return result;
	}

	// --- --- 
	
	public List<Namespace> getNamespacesAvailable() {
		if (availableNamespaces.isEmpty()){
			if ((modelElement!=null))
				availableNamespaces.addAll(modelElement.getAllNamespaces());
			else if (namespaceElement != null)
				availableNamespaces.add(namespaceElement);
		}
		return availableNamespaces;
	}
	
	public void addToGenerate(Namespace ns){
		if (!namespacesToGenerate.contains(ns)){
			namespacesToGenerate.add(ns);
			requiredNamespacesAreDirty = true;
//			needToRecollect = true;
		}
	}
	
	public boolean isNamespaceToGenerate(Namespace ns){
		return namespacesToGenerate.contains(ns);
	}
	
	public boolean needToChooseNamespaces(){
		return namespacesToGenerate.isEmpty();
	}
	
	public void removeFromGenerate(Namespace ns){
		if (namespacesToGenerate.contains(ns)){
			namespacesToGenerate.remove(ns);
			requiredNamespacesAreDirty = true;
//			needToRecollect = true;
		}
	}
	
	public boolean hasNamespacesToGenerate(){
		return !namespacesToGenerate.isEmpty();
	}

	public List<Namespace> getNamespacesToGenerate() {
		return namespacesToGenerate;
	}

	protected void generateWrappers(){
		wrappedNamespaceList = new ArrayList<>();
		for (Namespace ns: namespacesToGenerate) {
//			NamespaceWrapper nw = new NamespaceWrapper(ns);
			NamespaceWrapper nw = WrapperFactory.get(ns);
			nw.fill();
			wrappedNamespaceList.add(nw);
		}
		
	}
	
	public List<NamespaceWrapper> getWrappedNamespaceList(){
		if (wrappedNamespaceList == null || wrappedNamespaceList.isEmpty())
			generateWrappers();
		return wrappedNamespaceList;
	}
	
	protected void recalculateRequirements() {
		
		// collect requirements
		synchronized (requiredNamespaces) {
			requiredNamespaces.clear();
			Set<Namespace> namespaces = new HashSet<>();
			for (Namespace namespace: namespacesToGenerate) {
				namespaces.addAll(namespace.getRequiredNamespaces());
			} 
			
			requiredNamespaces.clear();
			requiredNamespaces.addAll(namespaces);
			
			requiredNamespaces.sort(new Comparator<Namespace>() {

				@Override
				public int compare(Namespace o1, Namespace o2) {
					return o1.getPrefix().compareTo(o2.getPrefix());
				}
			});

		}
		// wrap namespaces
		synchronized (requiredNamespacesWrapped) {
			requiredNamespacesWrapped.clear();
			for (Namespace ns: requiredNamespaces) {
				NamespaceWrapper nsw = WrapperFactory.get(ns);
				nsw.fill();
				requiredNamespacesWrapped.add(nsw);
			}
		}
		
		requiredNamespacesAreDirty = false;
	}
	
	public List<Namespace> getRequiredNamespaces(){
		if (requiredNamespacesAreDirty)
			recalculateRequirements();
		List<Namespace> result = new ArrayList<>();
		Collections.copy(result, requiredNamespaces);
		return result;
	}
	
	public List<NamespaceWrapper> getRequiredNamespacesWrapped(){
		if (requiredNamespacesAreDirty)
			recalculateRequirements();
		return requiredNamespacesWrapped;
	}
	
	@Deprecated
	public boolean targetsDefined() {
		if (wrappedNamespaceList == null)
			generateWrappers();
		boolean result = true;
		
/*		for (NamespaceWrapper nw: wrappedNamespaceList)
			result = result 
				&& CommonUtils.isValueable(nw.getTargetJavaLocation())
				&& CommonUtils.isValueable(nw.getTargetJavaPackage())
				&& CommonUtils.isValueable(nw.getTargetJavaName());
*/		
		return result;
	}
	
	@Deprecated
	public boolean targetsDefinedJS() {
		if (wrappedNamespaceList == null)
			generateWrappers();
		boolean result = true;
		
/*		for (NamespaceWrapper nw: wrappedNamespaceList)
			result = result 
				&& CommonUtils.isValueable(nw.getTargetJavaScriptLocation())
				&& CommonUtils.isValueable(nw.getTargetJavaScriptName());
*/		
		return result;
	}
	
	@Deprecated
	public boolean importsDefined() {
//		collectImports();
		
		boolean result = true;
		
/*		for (NamespaceWrapper nw: wrappedImportedNamespaces)
			result = result 
				&& CommonUtils.isValueable(nw.getTargetJavaPackage())
				&& CommonUtils.isValueable(nw.getTargetJavaName());
*/		
		return result;		
	}
/*	
	protected void collectImports(){
		if (importsCollected && !needToRecollect)
			return;
		
		Map<String, Namespace> map = new HashMap<>();
		
*//*		for (Namespace namespace: namespacesToGenerate) {
			List<Namespace> list = namespace.getRequiredNamespaces();
			for (Namespace ns: list)
				if (ns!=null)
					if (!map.containsKey(ns.getPrefix()))
						map.put(ns.getPrefix(), ns);
		} 
*/		
/*		importedNamespaces.clear();
		importedNamespaces.addAll(map.values());
		
		importedNamespaces.sort(new Comparator<Namespace>() {

			@Override
			public int compare(Namespace o1, Namespace o2) {
				return o1.getPrefix().compareTo(o2.getPrefix());
			}
		});
		
		wrappedImportedNamespaces.clear();
		for (Namespace ns: importedNamespaces){
*///			NamespaceWrapper nsw = new NamespaceWrapper(ns);
/*			NamespaceWrapper nsw = WrapperFactory.get(ns);
			wrappedImportedNamespaces.add(nsw);
			wrappedImportedMap.put(nsw.getKey(), nsw);
		}
		
		importsCollected = true;
		needToRecollect = false;
	}
*/
/*	public List<Namespace> getImportedNamespaces() {
		collectImports();
		return importedNamespaces;
	}

	public List<NamespaceWrapper> getWrappedImportedNamespaces() {
		collectImports();
		return wrappedImportedNamespaces;
	}
	
	public SdocAnnotationFactory getAnnotationFactory(){
		return SdocAnnotationFactoryFactory.getFactory();
	}
	
	public Set<String> getImportedAnnotations(Namespace ns){
		SdocAnnotationFactory factory = getAnnotationFactory();
		Set<String> result = new HashSet<>();
		
		result.addAll(factory.getAnnotation(ns).imports);
		
		for (DataTypeElement dt: ns.getDataTypes())
			result.addAll(factory.getAnnotation(dt).imports);
		
		for (Aspect asp: ns.getAspects()){
			result.addAll(factory.getAnnotation(asp).imports);
			collectImportedAnnotations(asp, factory, result);
		}
		
		for (Type typ: ns.getTypes()) {
			result.addAll(factory.getAnnotation(typ).imports);
			collectImportedAnnotations(typ, factory, result);
		}
		
		return result;
	}

	protected void collectImportedAnnotations(BaseClassElement element, SdocAnnotationFactory factory, Set<String> target){
		for (PeerAssociation pa: element.getPeerAssociations())
			target.addAll(factory.getAnnotation(pa).imports);
		for (ChildAssociation ca: element.getChildAssociations())
			target.addAll(factory.getAnnotation(ca).imports);
		for (Property p: element.getProperties())
			target.addAll(factory.getAnnotation(p).imports);
	}
	
	public List<BaseWrapper> getWrappedImportedElements(Namespace ns){
		Map<String, BaseWrapper> result = new HashMap<>();
		
		List<DataTypeElement> dtes = ns.getRequiredDataTypes();
		for (DataTypeElement dte: dtes) {
//			DataTypeWrapper dtw = new DataTypeWrapper(dte);
			DataTypeWrapper dtw = WrapperFactory.get(dte);
			Namespace parent = dte.getNamespace();
			if (parent == null)
				continue;
			
			String parentKey = NamespaceWrapper.getKey(parent);
			NamespaceWrapper parentWrapper = wrappedImportedMap.get(parentKey);
			if (parentWrapper == null)
				continue;
			
			String itemKey = parentKey + "$" + dte.getName();
			if (result.containsKey(itemKey))
				continue;
			
			dtw.setOwner(parentWrapper);
			parentWrapper.addChild(dtw);
			result.put(itemKey, dtw);
		}
		
		List<BaseClassElement> ces = new ArrayList<>();
		ces.addAll(ns.getRequiredTypes());
		ces.addAll(ns.getRequiredAspects());
		for (BaseClassElement ce: ces) {
			ClassWrapper cw = null;
			if (Aspect.class.isAssignableFrom(ce.getClass()))
				cw = WrapperFactory.get(ce);
				//cw = new AspectWrapper((Aspect)ce);
			else if (Type.class.isAssignableFrom(ce.getClass()))
				//cw = new TypeWrapper((Type)ce);
				cw = WrapperFactory.get(ce);
			else
				continue;
					
			Namespace parent = ce.getNamespace();
			if (parent == null)
				continue;
			
			String parentKey = NamespaceWrapper.getKey(parent);
			NamespaceWrapper parentWrapper = wrappedImportedMap.get(NamespaceWrapper.getKey(parent));
			if (parentWrapper == null)
				continue;
			
			String itemKey = parentKey + "$" + ce.getName();
			if (result.containsKey(itemKey))
				continue;
			
			cw.setOwner(parentWrapper);
			parentWrapper.addChild(cw);
			result.put(itemKey, cw);
		}
		
		return new ArrayList<>(result.values());
	}
	
	public BaseWrapper lookup(String prefixedName) {
		if (prefixedName == null)
			return null;
		
		String[] splitted = prefixedName.split(":");
		if (splitted.length == 2)
			return doLookup(splitted[0], splitted[1]);
		
		return null;
	}
	
	public BaseWrapper lookup(String prefix, String name) {
		if ((prefix == null) || (name == null))
			return null;
		return doLookup(prefix, name);
	}
	
	protected BaseWrapper doLookup(String prefix, String name) {
		NamespaceWrapper nsw = null;
		BaseWrapper result = null;
		
		for (NamespaceWrapper nw: wrappedImportedNamespaces)
			if (nw.getNamespace().getPrefix().toLowerCase().equals(prefix.toLowerCase())) {
				nsw = nw;
				break;
			}
		
		if (nsw == null)
			for (NamespaceWrapper nw: wrappedNamespaceList)
				if (nw.getNamespace().getPrefix().toLowerCase().equals(prefix.toLowerCase())) {
					nsw = nw;
					break;
				}
		
		if (nsw == null)
			return null;
		
		for (BaseWrapper bw: nsw.getChildren())
			if (bw.getName().toLowerCase().equals(name.toLowerCase())){
				result = bw;
				break;
			}
		
		return result;
	}
*/	
}
