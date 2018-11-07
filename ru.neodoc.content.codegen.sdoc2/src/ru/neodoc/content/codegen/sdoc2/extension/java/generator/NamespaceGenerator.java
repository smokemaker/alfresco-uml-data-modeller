package ru.neodoc.content.codegen.sdoc2.extension.java.generator;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

import ru.neodoc.content.codegen.sdoc2.CodegenManager;
import ru.neodoc.content.codegen.sdoc2.config.CommonConfigurable;
import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.extension.java.WrapperJavaExtension;
import ru.neodoc.content.codegen.sdoc2.extension.java.generator.writer.ResourceWriter;
import ru.neodoc.content.codegen.sdoc2.generator.AbstractSdocGenerator;
import ru.neodoc.content.codegen.sdoc2.generator.annotation.AnnotationInfo;
import ru.neodoc.content.codegen.sdoc2.generator.annotation.SdocAnnotationFactory;
import ru.neodoc.content.codegen.sdoc2.generator.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc2.generator.writer.CommentProvider;
import ru.neodoc.content.codegen.sdoc2.generator.writer.LookupProvider;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.AspectWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.AssociationSolidWrapper;
/*import ru.neodoc.content.codegen.sdoc2.generator.java.annotation.AnnotationInfo;
import ru.neodoc.content.codegen.sdoc2.generator.java.annotation.SdocAnnotationFactory;
import ru.neodoc.content.codegen.sdoc2.generator.java.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc2.generator.java.writer.CommentProvider;
import ru.neodoc.content.codegen.sdoc2.generator.java.writer.LookupProvider;
import ru.neodoc.content.codegen.sdoc2.generator.java.writer.ResourceWriter;
*/import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.PropertyWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.TypeWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.WrapperFactory;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model;
//import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;
//import ru.neodoc.content.modeller.utils.uml.elements.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPrimitiveType.DataType;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class NamespaceGenerator extends AbstractSdocGenerator {

	protected NamespaceWrapper namespaceWrapper;
	protected Namespace namespace;
	protected WrapperJavaExtension wrapperExtension;
	
	protected String targetFilePath;
	protected String targetFileName;
	
	protected Set<String> imports = new HashSet<>();
	
	protected StringBuffer buffer = new StringBuffer();
	protected ResourceWriter writer;
	
	protected JavaAnnotaionProvider annotationProvider = null;
	
	public NamespaceGenerator(Configuration configuration, NamespaceWrapper wrapper) {
		super(configuration);
		this.namespaceWrapper = wrapper;
		wrapperExtension = WrapperJavaExtension.get(namespaceWrapper);
		annotationProvider = new JavaAnnotaionProvider(getConfiguration());
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
		this.namespace = namespaceWrapper.getClassifiedWrappedElement();
	}
	
	protected void initTargets(){
		String targetLocation = wrapperExtension.getTargetJavaLocation();
		// String workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
		
		// targetLocation = /*workspaceRoot + "/" + */targetLocation;
		
		String[] splitted = wrapperExtension.getTargetJavaPackage().split("\\.");
		for (int i=0; i<splitted.length; i++) {
			if ((splitted[i]==null) || (splitted[i].trim().length()==0))
				continue;
			targetLocation = targetLocation + "/" + splitted[i];
		}

		// targetLocation = targetLocation.replace("//", "/").replace("//", "/").replace("/", "\\");
		
		targetFilePath = targetLocation;
		targetFileName = wrapperExtension.getTargetJavaName() + ".java";
	}
	
	protected void initWriter(){
		writer = new ResourceWriter(namespaceWrapper, reporter, annotationProvider);
		writer.setLookupProvider(new JavaLookupProvider(getConfiguration()));
		writer.setCommentProvider(new JavaCommentProvider(getConfiguration()));
		writer.updateChildrenProviders();
		writer.setImports(imports);
	}
	
	protected void collectImports(){

		imports.add("org.alfresco.service.namespace.QName");
		
		List<StereotypedElement> requiredElements = namespace.getRequiredElements();
		List<AbstractWrapper> list = new ArrayList<>();
		for (StereotypedElement se: requiredElements) {
			AbstractWrapper aw = WrapperFactory.get(se);
			if (aw.getOwner()==null) {
				// we must find the owner for the object
				if (se.has(Namespace.class))
					aw.setOwner(WrapperFactory.get(Model._HELPER.findNearestFor(se)));
				else 
					aw.setOwner(WrapperFactory.get(Namespace._HELPER.findNearestFor(se)));
			}
			if ((aw != namespaceWrapper) && (aw.getOwner() != namespaceWrapper))
				list.add(aw);
		}
		
		for (AbstractWrapper bw: list){
			WrapperJavaExtension wje = WrapperJavaExtension.get(bw);
			WrapperJavaExtension wjeOwner = WrapperJavaExtension.get(bw.getOwner());
			String toImport;
			if (bw.getWrappedElement().has(DataType.class))
				// data types are enclosed in enums, so we need only namespace
				toImport = wje.getFinalJavaPackage() + "." + wjeOwner.getFullTargetJavaName();
			else
				toImport = wje.getFinalJavaPackage() + "." + wje.getFullTargetJavaName();
			if (wje.isIgnored() || wjeOwner.isIgnored()) {
				if (toImport.startsWith(".")) {
					// we don't have package
					toImport = "{" 
							+ wjeOwner.getExtendedWrapper().getWrappedElement().get(AlfrescoProfile.ForPackage.Namespace.class).getUri()
							+ "} "
							+ toImport;
				}
				toImport = "#" + toImport;
			}
			addImport(toImport);
		}

		collectImportedAnnotation(namespaceWrapper, imports);
/*		for (AbstractWrapper aw: namespaceWrapper.getChildren())
			imports.addAll(annotationProvider.getAnnotationImports(aw));
*/	}
	
	protected void collectImportedAnnotation(AbstractWrapper wrapper, Set<String> importSet) {
		imports.addAll(annotationProvider.getAnnotationImports(wrapper));
		for (AbstractWrapper child: wrapper.getChildren())
			collectImportedAnnotation(child, importSet);
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

	public static class JavaAnnotaionProvider extends CommonConfigurable implements AnnotationProvider {
		
		protected SdocAnnotationFactory annotaionFactory;
		
		public JavaAnnotaionProvider(Configuration configuration){
			super();
			setConfiguration(configuration);
			this.annotaionFactory = (SdocAnnotationFactory)getConfiguration().getValue(SdocAnnotationFactory.PROP_NAME);
		}
		
		@Override
		public String getAnnotation(AbstractWrapper baseWrapper) {
			if (this.annotaionFactory == null)
				return null;
			AnnotationInfo ai = annotaionFactory.getAnnotation(baseWrapper);
			if (ai == null)
				return null;
			return ai.annotation;
		}

		@Override
		public String getAnnotation(StereotypedElement stereotypedElement) {
			return getAnnotation(WrapperFactory.get(stereotypedElement));
		}

		@Override
		public Set<String> getAnnotationImports(AbstractWrapper baseWrapper) {
			if (this.annotaionFactory == null)
				return Collections.emptySet();
			AnnotationInfo ai = annotaionFactory.getAnnotation(baseWrapper);
			if (ai == null)
				return Collections.emptySet();
			return ai.imports;
		}

		@Override
		public Set<String> getAnnotationImports(StereotypedElement stereotypedElement) {
			return getAnnotationImports(WrapperFactory.get(stereotypedElement));
		}
		
	}
	
	public static class JavaLookupProvider implements LookupProvider {
		
		public JavaLookupProvider(Configuration configuration) {
			super();
		}
		
		protected String getNameFromWrapper(AbstractWrapper aw){
			return WrapperJavaExtension.get(aw).getFullTargetJavaName();
		}

		@Override
		public String lookupTargetName(ProfileStereotype element) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public static class JavaCommentProvider extends CommonConfigurable implements CommentProvider {
		
		public JavaCommentProvider(Configuration configuration){
			super();
			setConfiguration(configuration);
		}
		
		protected CodegenManager getCodegenManager() {
			return (CodegenManager)getConfiguration().getValue(CodegenManager.PROP_NAME);
		}
		
		@Override
		public String getComment(AbstractWrapper abstractWrapper) {

			if (abstractWrapper instanceof NamespaceWrapper){
				return getNamespaceComment((NamespaceWrapper)abstractWrapper);
			}
			
			if (abstractWrapper instanceof TypeWrapper) {
				return getTypeComment((TypeWrapper)abstractWrapper);
			}
			
			if (abstractWrapper instanceof AspectWrapper) {
				return getAspectComment((AspectWrapper)abstractWrapper);
			}
			
			if (abstractWrapper instanceof PropertyWrapper) {
				return getPropertyComment((PropertyWrapper)abstractWrapper);
			}
			
			if (abstractWrapper instanceof AssociationSolidWrapper) {
				return getAssociationComment((AssociationSolidWrapper<?>)abstractWrapper);
			}
			
			return getDefaultComment(abstractWrapper);
		}
		
		protected String getDefaultComment(AbstractWrapper wrapper){
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
		
		protected String getAssociationComment(AssociationSolidWrapper<?> w){
			String title = w.getTitle();
			if (title == null)
				title = "";
			return "/** Association " + title + " (" + w.getPrefixedName() + ") */";
		}
	}
}
