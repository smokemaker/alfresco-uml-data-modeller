package ru.neodoc.content.codegen.sdoc.generator.java;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.codegen.sdoc.CodegenManager;
import ru.neodoc.content.codegen.sdoc.generator.AbstractSdocGenerator;
import ru.neodoc.content.codegen.sdoc.generator.java.annotation.AnnotationInfo;
import ru.neodoc.content.codegen.sdoc.generator.java.annotation.SdocAnnotationFactory;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.CommentProvider;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.LookupProvider;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.ResourceWriter;
import ru.neodoc.content.codegen.sdoc.wrap.AspectWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.AssociationWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.DataTypeWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.NamespaceWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.PropertyWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.TypeWrapper;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;
import ru.neodoc.content.modeller.utils.uml.elements.Namespace;

public class NamespaceGenerator extends AbstractSdocGenerator {

	protected NamespaceWrapper namespaceWrapper;
	protected Namespace namespace;
	
	protected String targetFilePath;
	protected String targetFileName;
	
	protected Set<String> imports = new HashSet<>();
	
	protected StringBuffer buffer = new StringBuffer();
	protected ResourceWriter writer;
	
	public NamespaceGenerator(CodegenManager codegenManager, NamespaceWrapper wrapper) {
		super(codegenManager);
		this.namespaceWrapper = wrapper;
	}
	
	@Override
	protected boolean doInit() {
		if (namespaceWrapper == null)
			return false;
		
		initNamespace();

		initTargets();
		
		collectImports();
		
		initWriter();
		
		return true;
	}

	protected void initNamespace(){
		this.namespace = namespaceWrapper.getNamespace();
	}
	
	protected void initTargets(){
		String targetLocation = namespaceWrapper.getTargetJavaLocation();
		// String workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		
		// targetLocation = /*workspaceRoot + "/" + */targetLocation;
		
		String[] splitted = namespaceWrapper.getTargetJavaPackage().split("\\.");
		for (int i=0; i<splitted.length; i++) {
			if ((splitted[i]==null) || (splitted[i].trim().length()==0))
				continue;
			targetLocation = targetLocation + "/" + splitted[i];
		}

		// targetLocation = targetLocation.replace("//", "/").replace("//", "/").replace("/", "\\");
		
		targetFilePath = targetLocation;
		targetFileName = namespaceWrapper.getTargetJavaName() + ".java";
	}
	
	protected void initWriter(){
		writer = new ResourceWriter(namespaceWrapper, reporter, new JavaAnnotaionProvider(codegenManager));
		writer.setLookupProvider(new JavaLookupProvider(codegenManager));
		writer.setCommentProvider(new JavaCommentProvider(codegenManager));
		writer.updateChildrenProviders();
		writer.setImports(imports);
	}
	
	protected void collectImports(){

		imports.add("org.alfresco.service.namespace.QName");
		
		List<BaseWrapper> list = codegenManager.getWrappedImportedElements(namespace);
		for (BaseWrapper bw: list){
			if (DataTypeWrapper.class.isAssignableFrom(bw.getClass()))
				// data types are enclosed in enums, so we need only namespace
				addImport(bw.getFinalJavaPackage() + "." + bw.getOwner().getFullTargetJavaName());
			else
				addImport(bw.getFinalJavaPackage() + "." + bw.getFullTargetJavaName());
		}
		
		imports.addAll(codegenManager.getImportedAnnotations(namespace));
	}
	
	protected void addImport(String s){
		if (!imports.contains(s))
			imports.add(s);
	}
	
	@Override
	protected int doCountOperations() {
		int result = 1; // create namespace
		
		result += namespaceWrapper.getChildren().size(); // create children
		
		result += 1; // final writing 
		
		return result;
	}

	@Override
	protected void doGenerate() {
		reporter.message("--- === GENERATION === --- ");
		reporter.message("Namespace " + namespace.getPrefix() + "{" + namespace.getUri() + "}" 
				+ " will be created in " + targetFilePath + "/" + targetFileName);
		reporter.message("\n");
		
		writer.write(buffer, 0);
		
		String targetJavaLocation = /*namespaceWrapper.getTargetJavaLocation()*/this.targetFilePath;
		if (targetJavaLocation.startsWith("/"))
			targetJavaLocation = targetJavaLocation.substring(1);
		
		try{
			IWorkspace ws = ResourcesPlugin.getWorkspace();
			String[] splitted = targetJavaLocation.split("/");
			
			IProject project = ws.getRoot().getProject(splitted[0]);
			if (!project.isOpen())
				project.open(null);
			
			IFolder folder = null;
			for (int i=1; i<splitted.length; i++) {
				if ((splitted[i]==null) || (splitted[i].trim().length()==0))
					continue;
				if (folder==null)
					folder = project.getFolder(splitted[i]);
				else folder = folder.getFolder(splitted[i]);
				if (!folder.exists())
					folder.create(false, true, null);
			}
			
			/*splitted = namespaceWrapper.getTargetJavaPackage().split("\\.");
			for (int i=0; i<splitted.length; i++) {
				if ((splitted[i]==null) || (splitted[i].trim().length()==0))
					continue;
				if (folder==null)
					folder = project.getFolder(splitted[i]);
				else folder = folder.getFolder(splitted[i]);
				if (!folder.exists())
					folder.create(false, true, null);
			}*/
			
			IFile file = folder.getFile(/*namespaceWrapper.getTargetJavaName() + ".java"*/this.targetFileName);
			if (!file.exists())
				file.create(new ByteArrayInputStream(buffer.toString().getBytes(project.getDefaultCharset())), false, null);
			else {
				file.setContents(new ByteArrayInputStream(buffer.toString().getBytes(project.getDefaultCharset())), true, true, null);
			}
			
			
		} catch (Exception e) {
			reporter.error(e);
		}
		
		reporter.objectDone(null); // final writing
	}

	@Override
	protected void doSimulate() {
		reporter.message("--- === SIMULATION === --- ");
		reporter.message("Namespace " + namespace.getPrefix() + "{" + namespace.getUri() + "}" 
				+ " will be created in " + targetFilePath + "/" + targetFileName);
		reporter.message("\n");
		
		writer.write(buffer, 0);
		
		reporter.message("\n\n ----- CONTENT TO WRITE ----- \n");
		reporter.message(buffer.toString());
		
		reporter.objectDone(null); // final writing
	}

	public static class JavaAnnotaionProvider implements AnnotationProvider {
		
		protected SdocAnnotationFactory annotaionFactory;
		
		public JavaAnnotaionProvider(CodegenManager codegenManager){
			super();
			if (codegenManager != null)
				this.annotaionFactory = codegenManager.getAnnotationFactory();
		}
		
		@Override
		public String getAnnotation(BaseWrapper baseWrapper) {
			if (this.annotaionFactory == null)
				return null;
			AnnotationInfo ai = annotaionFactory.getAnnotation(baseWrapper);
			if (ai == null)
				return null;
			return ai.annotation;
		}
		
	}
	
	public static class JavaLookupProvider implements LookupProvider {
		
		protected CodegenManager codegenManager;
		
		public JavaLookupProvider(CodegenManager codegenManager) {
			super();
			this.codegenManager = codegenManager;
		}
		
		@Override
		public String lookupTargetName(BaseElement element) {
			BaseWrapper bw = findWrapper(element);
			
			if (bw == null)
				return null;
			
			return getNameFromWrapper(bw);
		}
		
		protected BaseWrapper findWrapper(BaseElement element){
			if ((element == null) || (element.getElement() == null))
				return null;
			Element el = element.getElement();
			if (!(el instanceof NamedElement))
				return null;
			
			String name = ((NamedElement)element.getElement()).getName();
			
			Package namespace = AlfrescoUMLUtils.getNearestNamespace(el);
			if (namespace == null)
				return null;
			String prefix = namespace.getName();
			
			BaseWrapper bw = codegenManager.lookup(prefix, name);
			if (bw == null)
				return null;
			
			return bw;
		}
		
		protected String getNameFromWrapper(BaseWrapper bw){
			return bw.getFullTargetJavaName();
		}
	}
	
	public static class JavaCommentProvider implements CommentProvider {
		
		protected CodegenManager codegenManager;
		
		public JavaCommentProvider(CodegenManager codegenManager){
			super();
			this.codegenManager = codegenManager;
		}
		
		@Override
		public String getComment(BaseWrapper baseWrapper) {

			if (baseWrapper instanceof NamespaceWrapper){
				return getNamespaceComment((NamespaceWrapper)baseWrapper);
			}
			
			if (baseWrapper instanceof TypeWrapper) {
				return getTypeComment((TypeWrapper)baseWrapper);
			}
			
			if (baseWrapper instanceof AspectWrapper) {
				return getAspectComment((AspectWrapper)baseWrapper);
			}
			
			if (baseWrapper instanceof PropertyWrapper) {
				return getPropertyComment((PropertyWrapper)baseWrapper);
			}
			
			if (baseWrapper instanceof AssociationWrapper) {
				return getAssociationComment((AssociationWrapper)baseWrapper);
			}
			
			return getDefaultComment(baseWrapper);
		}
		
		protected String getDefaultComment(BaseWrapper wrapper){
			return null;
		}
		
		protected String getNamespaceComment(NamespaceWrapper wrapper){
			return null;
		}
		
		protected String getTypeComment(TypeWrapper w){
			String title = w.getTitle();
			if (title == null)
				title = "";
			return "/** Type " + title + " (" + w.getPrefixedName() + ")\n*/";
		}
		
		protected String getAspectComment(AspectWrapper w){
			String title = w.getTitle();
			if (title == null)
				title = "";
			return "/** Aspect " + title + " (" + w.getPrefixedName() + ")\n*/";
		}
		
		protected String getPropertyComment(PropertyWrapper w){
			String title = w.getTitle();
			if (title == null)
				title = "";
			return "/** Attribute " + title + " (" + w.getPrefixedName() + ") */";
		}
		
		protected String getAssociationComment(AssociationWrapper w){
			String title = w.getTitle();
			if (title == null)
				title = "";
			return "/** Association " + title + " (" + w.getPrefixedName() + ") */";
		}
	}
}
