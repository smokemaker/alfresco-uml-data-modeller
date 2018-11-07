package ru.neodoc.content.codegen.sdoc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.neodoc.content.codegen.CodegenSubject;
import ru.neodoc.content.codegen.sdoc.generator.java.annotation.SdocAnnotationFactory;
import ru.neodoc.content.codegen.sdoc.generator.java.annotation.SdocAnnotationFactoryFactory;
import ru.neodoc.content.codegen.sdoc.preferences.PreferenceConstants;
import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.ClassWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.DataTypeWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.NamespaceWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.WrapperFactory;
import ru.neodoc.content.modeller.utils.uml.elements.Aspect;
import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.ChildAssociation;
import ru.neodoc.content.modeller.utils.uml.elements.DataTypeElement;
import ru.neodoc.content.modeller.utils.uml.elements.Model;
import ru.neodoc.content.modeller.utils.uml.elements.Namespace;
import ru.neodoc.content.modeller.utils.uml.elements.PeerAssociation;
import ru.neodoc.content.modeller.utils.uml.elements.Property;
import ru.neodoc.content.modeller.utils.uml.elements.Type;

public class CodegenManager {

	protected CodegenSubject codegenSubject = null;
	
	protected List<Namespace> availableNamespaces = new ArrayList<>();
	
	protected List<Namespace> namespacesToGenerate = new ArrayList<>();
	protected List<NamespaceWrapper> wrappedNamespaceList = null;
	protected Map<String, NamespaceWrapper> wrappedNamespaceMap = new HashMap<>();
	
	protected List<Namespace> importedNamespaces = new ArrayList<>();
	protected List<NamespaceWrapper> wrappedImportedNamespaces = new ArrayList<>();
	protected Map<String, NamespaceWrapper> wrappedImportedMap = new HashMap<>();
	
	protected boolean importsCollected = false;
	protected boolean needToRecollect = false;
	
	protected Model modelElement = null;
	protected Namespace namespaceElement = null;
	
	protected boolean generateJava = SdocCodegenPlugin.getBoolean(PreferenceConstants.P_DEFAULT_GENERATE_JAVA);
	protected boolean generateJavaScript = SdocCodegenPlugin.getBoolean(PreferenceConstants.P_DEFAULT_GENERATE_JAVASCRIPT);
	
	protected boolean automaticallySelectAllNamespaces;
	
	public CodegenManager(CodegenSubject codegenSubject) {
		this.codegenSubject = codegenSubject;
		modelElement = this.codegenSubject.asModelElement();
		namespaceElement = this.codegenSubject.asNamespaceElement();

		automaticallySelectAllNamespaces = SdocCodegenPlugin.getBoolean(PreferenceConstants.P_SELECT_ALL_NAMESPACES);
		
		if (automaticallySelectAllNamespaces || this.codegenSubject.isNamespace()) {
			for (Namespace ns: getNamespacesAvailable())
				addToGenerate(ns);
		}
		
/*		if (this.codegenSubject.isNamespace())
			if (namespaceElement != null)
				if (namespaceElement.isValid())
					namespacesToGenerate.add(namespaceElement);
		
*/	}
	
	public boolean isNamespaceToGenerate(Namespace ns){
		return namespacesToGenerate.contains(ns);
	}
	
	public List<Namespace> getNamespacesAvailable() {
		if (availableNamespaces.isEmpty()){
			if ((modelElement!=null) && modelElement.isValid())
				availableNamespaces.addAll(modelElement.getNamespaces());
			else if (namespaceElement != null)
				availableNamespaces.add(namespaceElement);
		}
		return availableNamespaces;
	}

	public boolean needToChooseNamespaces(){
		return namespacesToGenerate.isEmpty();
	}
	
	public void addToGenerate(Namespace ns){
		if (!namespacesToGenerate.contains(ns)){
			namespacesToGenerate.add(ns);
			needToRecollect = true;
		}
	}
	
	public void removeFromGenerate(Namespace ns){
		if (namespacesToGenerate.contains(ns)){
			namespacesToGenerate.remove(ns);
			needToRecollect = true;
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
		if (wrappedNamespaceList == null)
			generateWrappers();
		return wrappedNamespaceList;
	}
	
	public boolean isGenerateJava() {
		return generateJava;
	}

	public void setGenerateJava(boolean generateJava) {
		this.generateJava = generateJava;
	}

	public boolean isGenerateJavaScript() {
		return generateJavaScript;
	}

	public void setGenerateJavaScript(boolean generateJavaScript) {
		this.generateJavaScript = generateJavaScript;
	}

	public boolean hasSourceToGenerate(){
		return isGenerateJava() || isGenerateJavaScript();
	}
	
	private boolean isValid(String s) {
		boolean result = true;
		
		result = result && (s != null) && (s.length()>0);
		
		return result;
	}
	
	public boolean targetsDefined() {
		if (wrappedNamespaceList == null)
			generateWrappers();
		boolean result = true;
		
		for (NamespaceWrapper nw: wrappedNamespaceList)
			result = result 
				&& isValid(nw.getTargetJavaLocation())
				&& isValid(nw.getTargetJavaPackage())
				&& isValid(nw.getTargetJavaName());
		
		return result;
	}
	
	public boolean targetsDefinedJS() {
		if (wrappedNamespaceList == null)
			generateWrappers();
		boolean result = true;
		
		for (NamespaceWrapper nw: wrappedNamespaceList)
			result = result 
				&& isValid(nw.getTargetJavaScriptLocation())
				&& isValid(nw.getTargetJavaScriptName());
		
		return result;
	}
	
	public boolean importsDefined() {
		collectImports();
		
		boolean result = true;
		
		for (NamespaceWrapper nw: wrappedImportedNamespaces)
			result = result 
				&& isValid(nw.getTargetJavaPackage())
				&& isValid(nw.getTargetJavaName());
		
		return result;		
	}
	
	protected void collectImports(){
		if (importsCollected && !needToRecollect)
			return;
		
		Map<String, Namespace> map = new HashMap<>();
		
		for (Namespace namespace: namespacesToGenerate) {
			List<Namespace> list = namespace.getRequiredNamespaces();
			for (Namespace ns: list)
				if (ns!=null)
					if (!map.containsKey(ns.getPrefix()))
						map.put(ns.getPrefix(), ns);
		} 
		
		importedNamespaces.clear();
		importedNamespaces.addAll(map.values());
		
		importedNamespaces.sort(new Comparator<Namespace>() {

			@Override
			public int compare(Namespace o1, Namespace o2) {
				return o1.getPrefix().compareTo(o2.getPrefix());
			}
		});
		
		wrappedImportedNamespaces.clear();
		for (Namespace ns: importedNamespaces){
//			NamespaceWrapper nsw = new NamespaceWrapper(ns);
			NamespaceWrapper nsw = WrapperFactory.get(ns);
			wrappedImportedNamespaces.add(nsw);
			wrappedImportedMap.put(nsw.getKey(), nsw);
		}
		
		importsCollected = true;
		needToRecollect = false;
	}

	public List<Namespace> getImportedNamespaces() {
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
}
