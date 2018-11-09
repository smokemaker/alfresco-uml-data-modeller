package ru.neodoc.content.modeller.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.datatype.XMLGregorianCalendar;

import org.alfresco.model.dictionary._1.Aspect;
import org.alfresco.model.dictionary._1.Association;
import org.alfresco.model.dictionary._1.Association.Source;
import org.alfresco.model.dictionary._1.Association.Target;
import org.alfresco.model.dictionary._1.ChildAssociation;
import org.alfresco.model.dictionary._1.Constraint;
import org.alfresco.model.dictionary._1.MandatoryDef;
import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.DataTypes.DataType;
import org.alfresco.model.dictionary._1.Property;
import org.alfresco.model.dictionary._1.Type;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.UMLFactory;

import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace;
import ru.neodoc.content.modeller.model.AlfrescoModelUtils;
import ru.neodoc.content.modeller.utils.NamespaceElementsCreator.DependencyInfo.DependencyType;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.TargetMandatory;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Archive;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Encrypted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Index;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Mandatory;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Enforced;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.AssociationComposer;
import ru.neodoc.content.utils.uml.UMLUtils;

@Deprecated
public class NamespaceElementsCreator
	implements IRunnableWithProgress {

//	protected TransactionalEditingDomain editingDomain;
	
	public Map<String, NamespaceSourceInfo> getSources() {
		return sources;
	}

	public void setSources(Map<String, NamespaceSourceInfo> sources) {
		this.sources = sources;
	}

	/*	public TransactionalEditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void setEditingDomain(TransactionalEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}
*/
	protected Package umlRoot;
	protected List<Namespace> namespacesToCreate;
	protected IProgressMonitor monitor;
	protected Package rootObject;  
	
	protected boolean createDiagrams = true;
	
	public boolean isCreateDiagrams() {
		return createDiagrams;
	}

	public void setCreateDiagrams(boolean createDiagrams) {
		this.createDiagrams = createDiagrams;
	}

	public EObject getRootObject() {
		return rootObject;
	}

	public void setRootObject(EObject rootObject) {
		this.rootObject = (rootObject instanceof Package?(Package)rootObject:null);
	}

	protected Package getStartingPackage(){
		return rootObject!=null?rootObject:umlRoot;
	}
	
	protected Map<String, NamespaceSourceInfo> sources = new HashMap<String, NamespaceSourceInfo>();
	
	public static class DialogHelper implements Runnable, RunnableWithResult<Integer> {

		protected enum DialogKind{
			ERORR, MESSAGE;
		}
		
		protected DialogKind dialogKind;
		protected String messageText;
		protected String messageContent;
		protected int messageStyle;
		
		private ErrorDialog errorDialog = null;
		private int resultCode = -1;
		private IStatus status;
		
		protected Shell dialogShell;
		
		public DialogHelper(ErrorDialog errorDialog){
			this.errorDialog = errorDialog;
		}
		
		@Override
		public void run() {
			if (dialogKind.equals(DialogKind.MESSAGE)){
				 MessageBox mb = new MessageBox(dialogShell, messageStyle);
		          mb.setText(messageText);
		          mb.setMessage(messageContent);
		          resultCode = mb.open();
			}
		}

		public int getResultCode() {
			return resultCode;
		}

		@Override
		public Integer getResult() {
			return new Integer(resultCode);
		}

		public IStatus getStatus() {
			return status;
		}

		public void setStatus(IStatus status) {
			this.status = status;
		}

		public ErrorDialog getErrorDialog() {
			return errorDialog;
		}

		public void setErrorDialog(ErrorDialog errorDialog) {
			this.errorDialog = errorDialog;
		}

		public DialogKind getDialogKind() {
			return dialogKind;
		}

		public void setDialogKind(DialogKind dialogKind) {
			this.dialogKind = dialogKind;
		}

		public String getMessageText() {
			return messageText;
		}

		public void setMessageText(String messageText) {
			this.messageText = messageText;
		}

		public String getMessageContent() {
			return messageContent;
		}

		public void setMessageContent(String messageContent) {
			this.messageContent = messageContent;
		}

		public int getMessageStyle() {
			return messageStyle;
		}

		public void setMessageStyle(int messageStyle) {
			this.messageStyle = messageStyle;
		}

		public Shell getDialogShell() {
			return dialogShell;
		}

		public void setDialogShell(Shell dialogShell) {
			this.dialogShell = dialogShell;
		}
		
		
		
	}
	
	protected DialogHelper dialogHelper;
	
	public DialogHelper getDialogHelper() {
		return dialogHelper;
	}

	public void setDialogHelper(DialogHelper dialogHelper) {
		this.dialogHelper = dialogHelper;
	}

	@Deprecated
	public static class ModelObject<T extends Object> {
		public T source = null;
		public String name = "";
		public String pack = "";
		public String uri = "";
		public List<ModelObject<T>> imports = new ArrayList<NamespaceElementsCreator.ModelObject<T>>();
		public Element element = null;
		public String location = "";
		public String model = "";

		
		public boolean isTransient = false;
		
/*		public static ModelObject<? extends Object> newInstance(Object object){
			ModelObject<? extends Object> result;
			if (object instanceof Model)
				result = new ModelObject<Model>((Model)object);
		}
*/		
		public ModelObject(){
			
		}

		public ModelObject(String fullName){
			if (fullName != null) {
				String[] data = fullName.split(":");
				name = data[data.length-1];
				if (data.length>1)
					pack = data[data.length-2];
			}
		}
		
		public ModelObject(T object){
			source = object;
			load(object);
		}
		
		public void load(T object){
			if (object instanceof org.alfresco.model.dictionary._1.Model){
				name = ((org.alfresco.model.dictionary._1.Model)object).getName();
			} else 
			if (object instanceof org.alfresco.model.dictionary._1.Model.Namespaces.Namespace){
				name = ((org.alfresco.model.dictionary._1.Model.Namespaces.Namespace)object).getPrefix();
				uri = ((org.alfresco.model.dictionary._1.Model.Namespaces.Namespace)object).getUri();
			} else 
			if (object instanceof org.alfresco.model.dictionary._1.Model.DataTypes.DataType) {
				String typeName = ((org.alfresco.model.dictionary._1.Model.DataTypes.DataType)object).getName();
				int index = typeName.indexOf(":"); 
				if (index>0){
					name = typeName.substring(index+1);
					pack = typeName.substring(0, index);
				}
			} else 
			if (object instanceof org.alfresco.model.dictionary._1.Type){
				String typeName = ((org.alfresco.model.dictionary._1.Type)object).getName();
				int index = typeName.indexOf(":"); 
				if (index>0){
					name = typeName.substring(index+1);
					pack = typeName.substring(0, index);
				}
			} else 
			if (object instanceof org.alfresco.model.dictionary._1.Aspect){
				String typeName = ((org.alfresco.model.dictionary._1.Aspect)object).getName();
				int index = typeName.indexOf(":"); 
				if (index>0){
					name = typeName.substring(index+1);
					pack = typeName.substring(0, index);
				}
			}
			if (object instanceof org.alfresco.model.dictionary._1.Constraint) {
				String typeName = ((org.alfresco.model.dictionary._1.Constraint)object).getName();
				PrefixedName pn = new PrefixedName(typeName);
				name = pn.getName();
				pack = pn.getPrefix();
			}
			
		}
		
		public boolean isModel(){
			return (source instanceof Model);
		}
		
		public boolean isPackage(){
			return (source instanceof org.alfresco.model.dictionary._1.Model.Namespaces.Namespace)
				|| (element instanceof Package) || (name!=null && (pack==null || pack.length()==0));
		}
		
		public boolean isVirtual(){
			return (source==null) && (element==null);
		}

		public String getName(){
			if (isPackage())
				return name;
			return pack + ":" + name;
		}
		
		public boolean isToCreate(){
			return source!=null;
		}
		
	}
	
	@Deprecated
	public static class ModelObjectSmartFactory {
		
		ObjectRegistry objReg;
		
		public ModelObjectSmartFactory(ObjectRegistry or) {
			this.objReg = or;
		}
		
		public <T extends Object> ModelObject<T> getObject(String fullName){
			
			String[] splitted = fullName.split(":");
			String name = splitted[splitted.length - 1];
			
			@SuppressWarnings("unchecked")
			ModelObject<T> result = (ModelObject<T>)objReg.get(name);
			if (result == null) {
				result = new ModelObject<>(fullName);
			}
			return result;
		}
		
	}
	
	@Deprecated
	public static class DependencyInfo{
		
		@Deprecated
		public enum DependencyType {
			PARENT("parent"), MANDATORY_ASPECT("mandatory-aspect"), 
			CHILD("child"), PEER("peer"), 
			IMPORT("import"),
			CONSTRAINT("constraint"),
			CONSTRAINT_INLINE("constraint-inline"),
			DEPENDENCY("dependency"),
			UNKNOWN("unknown");
			
			private String name;
			
			private DependencyType(String name){
				this.name = name;
			}
			
			public String getName(){
				return this.name;
			}
			
		}
		
		public ModelObject<? extends Object> source = null;
		public ModelObject<? extends Object> target = null;
		public DependencyType dependencyType;
		public Object dependencyObject;
		
		public DependencyInfo(ModelObject<? extends Object> src, 
				ModelObject<? extends Object> tgt,
				DependencyType type){
			this.source = src;
			this.target = tgt;
			this.dependencyType = type;
		}
		
		public DependencyInfo(ModelObject<? extends Object> src, 
				ModelObject<? extends Object> tgt,
				DependencyType type, Object depObj){
			this(src, tgt, type);
			this.dependencyObject = depObj;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof DependencyInfo))
				return false;
			DependencyInfo di = (DependencyInfo)obj;
			
			boolean result = true;
			
			result = result && 
					((this.source==null && di.source == null)
							|| (this.source.getName()==null && di.source.getName() == null)
							|| (this.source.getName().equals(di.source.getName())));

			result = result && 
					((this.target==null && di.target == null)
							|| (this.target.getName()==null && di.target.getName() == null)
							|| (this.target.getName().equals(di.target.getName())));
			
			result = result &&
					(this.dependencyType.equals(di.dependencyType));
			
			return result;
		}
		
		@Override
		public int hashCode() {
			return this.toString().hashCode();
		}
		
		@Override
		public String toString() {
			return this.dependencyType.getName()
					+ ":" + source.getName() + "->" + target.getName();
		}
		
		public boolean isValid(){
			return (source != null && target != null && !source.isVirtual() && !target.isVirtual());
		}
		
		public boolean isAssociation(){
			return (dependencyObject!=null && (
					(dependencyObject instanceof ChildAssociation) || (dependencyObject instanceof Association)
					));
		}
		
		public AssociationInfo getAssociationInfo(){
			if (isAssociation())
				return new AssociationInfo(dependencyObject);
			return null;
		}
		
	}

	@Deprecated
	public static class AssociationInfo{
		
		public String sourceRole = "";
		public int sourceMin = 0;
		public int sourceMax = 1;

		public String targetRole = "";
		public int targetMin = 0;
		public int targetMax = 1;
		public boolean targetForce = false; 
		
		public String childName = null;
		public Boolean isDuplicate = null;
		public Boolean isPropogate = null;
		
		public boolean isChild = false;
		
		public String name = "";
		public String title = "";
		public String description = "";
		
		protected void load(Association pa) {
			
			if (pa.getName()!=null)
				name = pa.getName();
			
			if (pa.getTitle()!=null)
				title = pa.getTitle();
			
			if (pa.getDescription()!=null)
				description = pa.getDescription();
			
			if (pa.getSource()!=null) {
				Source src = pa.getSource();
				if (src.getRole() != null && ! "null".equals(src.getRole()))
					sourceRole = src.getRole();
				
				if (src.isMandatory()!=null) {
					if (src.isMandatory().booleanValue())
						sourceMin = 1;
				}

				if (src.isMany()!=null) {
					if (src.isMany().booleanValue())
						sourceMax = -1;
				}
			}

			if (pa.getTarget()!=null) {
				Target tgt = pa.getTarget();
				if (tgt.getRole() != null && ! "null".equals(tgt.getRole()))
					targetRole = tgt.getRole();
				
				if (tgt.getMandatory()!=null) {
					String content = tgt.getMandatory().getContent();
					if (content!=null && "true".equals(content.toLowerCase()))
						targetMin = 1;
					
					targetForce = (tgt.getMandatory().isEnforced()!=null)
						&& (tgt.getMandatory().isEnforced().booleanValue());
					
				}

				if (tgt.isMany()!=null) {
					if (tgt.isMany().booleanValue())
						targetMax = -1;
				}
			}
			
		}
		
		protected void load(ChildAssociation ca) {
			load((Association)ca);
			isChild = true;
			childName = ca.getChildName();
			isDuplicate = ca.isDuplicate();
			isPropogate = ca.isPropagateTimestamps();
		}
		
		public AssociationInfo(Association pa){
			load(pa);
		}

		public AssociationInfo(ChildAssociation ca){
			this ((Association)ca);
		}

		public AssociationInfo(Object obj){
			if (obj instanceof ChildAssociation)
				load((ChildAssociation)obj);
			else if (obj instanceof Association)
				load((Association)obj);
		}
		
		
	}
	
	@Deprecated
	protected static class DependencyRegistry{
		Set<DependencyInfo> dependencies = Collections.newSetFromMap(new ConcurrentHashMap<NamespaceElementsCreator.DependencyInfo, Boolean>());
		List<DependencyInfo> unsatisfiedDependencies = new ArrayList<NamespaceElementsCreator.DependencyInfo>();
		
		private ObjectRegistry objRegistry; 
		
		public DependencyRegistry(ObjectRegistry registry){
			objRegistry = registry;
		}
		
		public void add(DependencyInfo info){
			if (dependencies.contains(info))
				return;
			dependencies.add(info);
			
			// auto create package imports
			if (!info.dependencyType.equals(DependencyType.IMPORT)
					&& !info.dependencyType.equals(DependencyType.DEPENDENCY)				
					){
				String srcPackage = info.source.pack;
				String tgtPackage = info.target.pack;
				if (srcPackage!=null && tgtPackage != null 
						&& !srcPackage.equals("") && !tgtPackage.equals("")
						&& !srcPackage.equals(tgtPackage)){
					ModelObject<Object> moSrc = objRegistry.get(srcPackage);
					ModelObject<Object> moTgt = objRegistry.get(tgtPackage);
					if (!moSrc.imports.contains(moTgt)){
						moSrc.imports.add(moTgt);
					}
					add(moSrc, moTgt, DependencyType.DEPENDENCY);
				}
			}
		}
		
		public <T extends Object, S extends Object> void add(ModelObject<T> src, ModelObject<S> tgt, DependencyType type){
			if (src==null || tgt==null)
				return;
			ModelObject<Object> s = objRegistry.get(src.getName());
			ModelObject<Object> t = objRegistry.get(tgt.getName());
			add(new DependencyInfo(s!=null?s:src, t!=null?t:tgt, type));
		}
		
		public <T extends Object, S extends Object> void add(ModelObject<T> src, ModelObject<S> tgt, 
				DependencyType type, Object dependencyObject){
			ModelObject<Object> s = objRegistry.get(src.getName());
			ModelObject<Object> t = objRegistry.get(tgt.getName());
			add(new DependencyInfo(s!=null?s:src, t!=null?t:tgt, type, dependencyObject));
		}
		
		public List<DependencyInfo> getUnsatisfiedDependencies(){
			List<DependencyInfo> result = new ArrayList<NamespaceElementsCreator.DependencyInfo>();
			for (DependencyInfo di: dependencies)
				if (!di.isValid())
					result.add(di);
			return result;
		}
	}

	@Deprecated
	protected static class ObjectRegistry{
		Map<String, ModelObject<Object>> objects = new HashMap<String, NamespaceElementsCreator.ModelObject<Object>>();
		
		Map<String, List<String>> classifiedObjects = new HashMap<String, List<String>>();
		
		public <T extends Object> void add(ModelObject<T> object){
			add(object, false);
		}
		@SuppressWarnings("unchecked")
		public <T extends Object> void add(ModelObject<T> object, boolean reload){
			String name = object.getName();
			
			// check if the namespace exists and define it if not
			String pack = object.pack;
			if (pack!=null 
					&& pack.trim().length()>0 
					&& objects.get(pack)==null){
				objects.put(pack, new ModelObject<Object>(pack));
			}
			
			ModelObject<Object> storedObject = objects.get(name);
			if (storedObject==null) {
				objects.put(object.getName(), (ModelObject<Object>)object);
				
				if (object.source != null) {
					String className = object.source.getClass().getName();
					if (!classifiedObjects.containsKey(className))
						classifiedObjects.put(className, new ArrayList<String>());
					
					if (!classifiedObjects.get(className).contains(object.getName()))
						classifiedObjects.get(className).add(object.getName());
				}
				return;
			}
			
			if (reload && (object.source!=null))
				storedObject.load(object.source);
			
			if (storedObject.source == null){
				storedObject.source = object.source;

				if (object.source != null){
					String className = object.source.getClass().getName();
					if (!classifiedObjects.containsKey(className))
						classifiedObjects.put(className, new ArrayList<String>());
					
					if (!classifiedObjects.get(className).contains(object.getName()))
						classifiedObjects.get(className).add(object.getName());
				}
			}
			if (storedObject.element == null)
				storedObject.element = object.element;
			if (storedObject.location == null || 
					storedObject.location.trim().length()==0)
				storedObject.location = object.location;
			if (storedObject.model == null || 
					storedObject.model.trim().length()==0)
				storedObject.model = object.model;
		}
		
		public ModelObject<Object> get(String name){
			return objects.get(name);
		}
		
		public int size(){
			return objects.values().size();
		}
		
		public List<String> getObjectsByClass(java.lang.Class<?> clazz){
			return getObjectsByClass(clazz.getName());
		}
		
		public List<String> getObjectsByClass(String className){
			return classifiedObjects.get(className);
		}
		
	}
	
	protected ObjectRegistry objectRegistry = new ObjectRegistry();
	protected ModelObjectSmartFactory objectFactory = new ModelObjectSmartFactory(objectRegistry);
	protected DependencyRegistry dependencyRegistry = new DependencyRegistry(objectRegistry);
	
	protected Shell parentShell = null;
	
	protected String messageToDisplay = "";
	protected int messageResult;
	protected Shell messageShell;
	
	public Shell getParentShell() {
		return parentShell;
	}

	public void setParentShell(Shell parentShell) {
		this.parentShell = parentShell;
	}

	public NamespaceElementsCreator(Package root, List<Namespace> namespaces){
		umlRoot = root;
		namespacesToCreate = namespaces;
	}
	
	
	
	@Override
	public void run(IProgressMonitor mon) throws InvocationTargetException,
			InterruptedException {
		
		this.monitor = mon;
		monitor.beginTask("Creating elements from namespaces", 100);
		
		/* stage 1 structuring */
//		SubProgressMonitor sm = new SubProgressMonitor(monitor, 10);
		SubMonitor sm = SubMonitor.convert(monitor, 10);
		sm.beginTask("Structuring", namespacesToCreate.size());
		stage1(sm);
		sm.done();
		
		/* stage 2 checking model availability */
//		sm = new SubProgressMonitor(monitor, 10);
		sm = SubMonitor.convert(monitor, 10);
		sm.beginTask("Loading models", sources.values().size());
		List<NamespaceSourceInfo> nsi = stage2(sm);
		sm.done();
		if (nsi.size()>0){
			for (int i=0; i<nsi.size(); i++){
				NamespaceSourceInfo si = nsi.get(i);
				messageToDisplay += "File " + si.dictionaryLocation + " couldn't be loaded:\n";
				for (Namespace ns: si.namespaces)
					messageToDisplay += "\tNamespace: " + ns.getAlias() + "{" + ns.getUrl() + "}\n";
			}
			
			
			Display display = parentShell.getDisplay();//Display.getDefault(); 
			display.syncExec(
					new Runnable() {
				        public void run() {
				          MessageBox mb = new MessageBox(parentShell, 
				        		  SWT.ICON_ERROR | SWT.OK);
				          mb.setText("Some namespaces couldn't be loaded");
				          mb.setMessage(messageToDisplay);
				          messageResult = mb.open();
				        };
					}
		    );
			
/*			ErrorDialog ed = new ErrorDialog(
					parentShell==null?PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell():parentShell, 
					"Some namespaces couldn't be loaded", 
					message, 
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, ""), 
					IStatus.CANCEL);
			ed.open();*/
			return;
		}
		
		/* stage 3 calculating dependencies */
//		sm = new SubProgressMonitor(monitor, 30);
		sm = SubMonitor.convert(monitor, 30);
		sm.beginTask("Calculating dependencies", sources.values().size()*5);
		stage3(sm);
		
		List<DependencyInfo> dis = dependencyRegistry.getUnsatisfiedDependencies();
		List<DependencyInfo> dis2 = new ArrayList<NamespaceElementsCreator.DependencyInfo>();
		sm.done();
		if (dis.size()>0){
			for (DependencyInfo di: dis)
				if (!tryToSatisfy(di))
					dis2.add(di);
		}
		if (dis2.size()>0){ // still have unsatisfied dependencies
			messageToDisplay = "";
			for (int i=0; i<dis2.size(); i++){
				DependencyInfo di = dis2.get(i);
				messageToDisplay += di.source.getName() + " -> " + di.target.getName() 
						+ "[" + di.dependencyType.getName() + "]\n";
			}
/*			ErrorDialog ed = new ErrorDialog(
					parentShell==null
						?PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
						:parentShell
					, 
					"Some dependencies are not satisfied", 
					messageToDisplay, 
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, ""), 
					IStatus.ERROR);
*//*			DialogHelper dh = new DialogHelper(ed);
			Display.getDefault().asyncExec(dh);
*/			
			
			Display display = parentShell.getDisplay();//Display.getDefault(); 
			display.syncExec(
					new Runnable() {
		        public void run() {
		          MessageBox mb = new MessageBox(parentShell, 
		        		  SWT.ICON_ERROR | SWT.YES | SWT.NO);
		          mb.setText("Some dependencies are not satisfied");
		          mb.setMessage(messageToDisplay + "\n\n" + "Would you  like to ignore it and continue?");
		          messageResult = mb.open();
		        }
			}
			);
			
/*			dialogHelper.setDialogKind(DialogKind.MESSAGE);
			dialogHelper.setDialogShell(parentShell);
			dialogHelper.setMessageText("Some dependencies are not satisfied");
			dialogHelper.setMessageContent(messageToDisplay + "\n\n" + "Would you  like to ignore it and continue?");
			dialogHelper.setMessageStyle(SWT.ICON_ERROR | SWT.YES | SWT.NO);
*/			
/*			display.syncExec(dialogHelper);
			
			messageResult = dialogHelper.getResultCode();
*/			
			if (messageResult != SWT.YES)
				return;
			
			for (DependencyInfo di: dis2) {
				if (di.source.isVirtual())
					objectRegistry.objects.remove(di.source.name);
				if (di.target.isVirtual())
					objectRegistry.objects.remove(di.target.name);
				dependencyRegistry.dependencies.remove(di);
			}
		}
		
		
		
		/* stage 4 create UML elements */
//		sm = new SubProgressMonitor(monitor, 30);
		sm = SubMonitor.convert(monitor, 30);
		sm.beginTask("Creating elements", objectRegistry.size());
		stage4(sm);
		sm.done();
		
		/* stage 5 create UML relations */
//		sm = new SubProgressMonitor(monitor, 30);
		sm = SubMonitor.convert(monitor, 30);
		sm.beginTask("Creating links", objectRegistry.size());
		stage5(sm);
		sm.done();

		
	}

	/**
	 * create NamespaceSourceInfo for existing UML model
	 */
	
	public void addModelToSources(String name, String location){
		NamespaceSourceInfo nsi = new NamespaceSourceInfo();
		nsi.dictionaryName = name;
		nsi.dictionaryLocation = location;
		sources.put(location, nsi);
	}
	
	/**
	 * preparing structure
	 */
	protected void stage1(IProgressMonitor mon){
		String location;
		for (Namespace ns: namespacesToCreate) {
			Dictionary d = (Dictionary)ns.eContainer();
			location = d.getLocation();
			if (!sources.containsKey(location)){
				NamespaceSourceInfo si = new NamespaceSourceInfo(d);
				sources.put(location, si);
			}
			sources.get(location).namespaces.add(ns);
			mon.worked(1);
		}
	}
	
	/**
	 * check source availability
	 * @return list of sources where model couldn't be loaded
	 */
	protected List<NamespaceSourceInfo> stage2(IProgressMonitor mon){
		List<NamespaceSourceInfo> result = new ArrayList<NamespaceSourceInfo>();
		for (NamespaceSourceInfo si: sources.values()){
			if (!si.loadModel())
				result.add(si);
			mon.worked(1);
		}
		return result;
	}

	protected void stage3(IProgressMonitor mon){
		Model currentModel;
		for (NamespaceSourceInfo source: sources.values()) {
			currentModel = source.model;

			ModelObject<Model> modelObj = new ModelObject<Model>(currentModel);
			modelObj.element = AlfrescoUMLUtils.findModel(currentModel.getName(), umlRoot);
			modelObj.uri = source.dictionaryLocation;
			modelObj.location = source.dictionaryLocation;

			objectRegistry.add(modelObj);
			
			if (currentModel.getImports()!=null)
				for (org.alfresco.model.dictionary._1.Model.Imports.Import imp: currentModel.getImports().getImport()){
					
					ModelObject<org.alfresco.model.dictionary._1.Model.Namespaces.Namespace> mo =
//							new ModelObject<org.alfresco.model.dictionary._1.Model.Namespaces.Namespace>(imp.getPrefix());
							objectFactory.getObject(imp.getPrefix());
					
					mo.element = AlfrescoUMLUtils.findNamespace(imp.getPrefix(), umlRoot);
					
					// if importing existing namespace we don't recreate or change it
					if (mo.element != null)
						mo.isTransient = true;
					
					objectRegistry.add(mo);
					dependencyRegistry.add(modelObj, mo, DependencyType.IMPORT);
					
				}
			
			if (currentModel.getNamespaces() != null)
				for (org.alfresco.model.dictionary._1.Model.Namespaces.Namespace ns: currentModel.getNamespaces().getNamespace()){
					ModelObject<org.alfresco.model.dictionary._1.Model.Namespaces.Namespace> mo =
//							new ModelObject<org.alfresco.model.dictionary._1.Model.Namespaces.Namespace>(ns);
							objectFactory.getObject(ns.getPrefix());
					mo.source = ns;
					mo.load(ns);
					mo.element = AlfrescoUMLUtils.findNamespace(ns.getPrefix(), umlRoot);
					mo.location = source.dictionaryLocation;
					mo.model = currentModel.getName();
					objectRegistry.add(mo, true);
				}
			mon.worked(1);
			
			if (currentModel.getConstraints() != null) {
				for (Constraint cn: currentModel.getConstraints().getConstraint()) {
					ModelObject<Constraint> mo = objectFactory.getObject(cn.getName());
					mo.source = cn;
					mo.load(cn);
					mo.element = AlfrescoUMLUtils.findConstraint(cn.getName(), umlRoot);
					objectRegistry.add(mo);
				}
			}
			
			if (currentModel.getDataTypes() != null)
				for (DataType dt: currentModel.getDataTypes().getDataType()){
					ModelObject<DataType> mo = 
							// new ModelObject<Model.DataTypes.DataType>(dt);
							objectFactory.getObject(dt.getName());
					mo.source = dt;
					mo.load(dt);
					mo.element = AlfrescoUMLUtils.findDataType(dt.getName(), umlRoot);
					objectRegistry.add(mo);
				}
			
			if (currentModel.getTypes() != null)
				classesStage3process(currentModel.getTypes().getType(), AlfrescoProfile.ForClass.Type._NAME);
			mon.worked(2);

			if (currentModel.getAspects() != null)
				classesStage3process(currentModel.getAspects().getAspect(), AlfrescoProfile.ForClass.Aspect._NAME);
			mon.worked(2);
			
		};
	}
	
	protected <ElementType extends org.alfresco.model.dictionary._1.Class> void classesStage3process(
				List<ElementType> elements,
				String stereotypeToSearch
			){
		for (ElementType type: elements){
			ModelObject<org.alfresco.model.dictionary._1.Class> mo = 
					new ModelObject<org.alfresco.model.dictionary._1.Class>(type);
			
			Element el = AlfrescoUMLUtils.findAlfrescoClass(umlRoot, type.getName());
			if (el!=null && AlfrescoUMLUtils.hasStereotype(el, stereotypeToSearch))
				mo.element = el;
			
			objectRegistry.add(mo);
			
			// add parent link
			String parent = type.getParent();
			if (parent!=null && parent.length()>0){
				ModelObject<Object> parentObj = 
						// new ModelObject<Object>(parent);
						objectFactory.getObject(parent);
				parentObj.element = AlfrescoUMLUtils.findAlfrescoClass(umlRoot, parent);
				objectRegistry.add(parentObj);
				dependencyRegistry.add(mo, parentObj, DependencyType.PARENT);
			}
			
			if (type.getAssociations()!=null) {
				// add peer associations
				for (Association assoc: type.getAssociations().getAssociation()){
					String targetClass = assoc.getTarget().getClazz();
					ModelObject<Object> targetObj = 
							// new ModelObject<Object>(targetClass);
							objectFactory.getObject(targetClass);
					targetObj.element = AlfrescoUMLUtils.findAlfrescoClass(umlRoot, targetClass);
					objectRegistry.add(targetObj);
					dependencyRegistry.add(mo, targetObj, DependencyType.PEER, assoc);
				}
			
				// add child associations
				for (ChildAssociation assoc: type.getAssociations().getChildAssociation()){
					String targetClass = assoc.getTarget().getClazz();
					ModelObject<Object> targetObj = 
							// new ModelObject<Object>(targetClass);
							objectFactory.getObject(targetClass);
					targetObj.element = AlfrescoUMLUtils.findAlfrescoClass(umlRoot, targetClass);
					objectRegistry.add(targetObj);
					dependencyRegistry.add(mo, targetObj, DependencyType.CHILD, assoc);
				}
			}
			
			// add mandatory aspects
			if (type.getMandatoryAspects() != null)
				for (String aspectName: type.getMandatoryAspects().getAspect()){
					ModelObject<Object> aspectObj = 
							// new ModelObject<Object>(aspectName);
							objectFactory.getObject(aspectName);
					aspectObj.element = AlfrescoUMLUtils.findAspect(aspectName, umlRoot);
					objectRegistry.add(aspectObj);
					dependencyRegistry.add(mo, aspectObj, DependencyType.MANDATORY_ASPECT);
				}
			
			// add dependencies for property types and referenced constraints
			if (type.getProperties()!=null)
				for (Property prop: type.getProperties().getProperty()){
					String pType = prop.getType();
					if (pType!=null) {
						String pTypeNs = pType.split(":")[0];
						ModelObject<org.alfresco.model.dictionary._1.Model.Namespaces.Namespace> nsObj = 
								// new ModelObject<org.alfresco.model.dictionary._1.Model.Namespaces.Namespace>(pTypeNs);
								objectFactory.getObject(pTypeNs);
						nsObj.element = AlfrescoUMLUtils.findNamespace(pTypeNs, umlRoot);
						
						if (nsObj.element != null)
							nsObj.isTransient = true;
						
						objectRegistry.add(nsObj);
						dependencyRegistry.add(
								objectRegistry.get(mo.pack), 
								nsObj, 
								DependencyType.DEPENDENCY);
					}
					if (prop.getConstraints()!=null) {
						for (Constraint constraint: prop.getConstraints().getConstraint()) {
							if (CommonUtils.isValueable(constraint.getRef())) {
								PrefixedName pn = new PrefixedName(constraint.getRef());
								if (CommonUtils.isValueable(pn.getPrefix())) {
									ModelObject<org.alfresco.model.dictionary._1.Model.Namespaces.Namespace> nsObj = 
										objectFactory.getObject(pn.getPrefix());
									nsObj.element = AlfrescoUMLUtils.findNamespace(pn.getPrefix(), umlRoot);
									
									if (nsObj.element != null)
										nsObj.isTransient = true;
									
									objectRegistry.add(nsObj);
									dependencyRegistry.add(
											objectRegistry.get(mo.pack), 
											nsObj, 
											DependencyType.DEPENDENCY);
								}
							}
						}
					}
				}
		}
		
	}
	
	protected boolean tryToSatisfy(DependencyInfo di){
		@SuppressWarnings("unchecked")
		ModelObject<Object> mo = (ModelObject<Object>)di.target;
		if (mo.isPackage())
			mo.element = AlfrescoUMLUtils.findNamespace(mo.name, umlRoot);
		else
			mo.element = AlfrescoUMLUtils.findAlfrescoClass(umlRoot, mo.getName());
		return di.isValid();
	}
	
	/**
	 * create UML elements
	 * @param mon
	 */
	protected void stage4(IProgressMonitor mon){
		
		List<ModelObject<Object>> delayed = new ArrayList<NamespaceElementsCreator.ModelObject<Object>>();
		
		List<String> models = objectRegistry.getObjectsByClass(Model.class);
		for (String model: models){
			ModelObject<Object> mo = objectRegistry.objects.get(model);
			processModel(mo);
		}
		
		for (ModelObject<Object> mo: objectRegistry.objects.values()){
			
/*			if (mo.element!=null){
				mon.worked(1);
				continue;
			}
*/			
			if (mo.isModel()){
				// noop
				//processModel(mo);
			} else if (mo.isPackage()){
				processPackage(mo);
			} else {
				if (mo.source instanceof DataType){
					ModelObject<Object> moPackage = objectRegistry.get(mo.pack);
					if (moPackage.element == null)
						processPackage(moPackage);
					processDataType(mo, moPackage);
				} else {
					delayed.add(mo);
					continue;
				}
			}
			mon.worked(1);
		}
		
		for (ModelObject<Object> mo: delayed) {
			ModelObject<Object> moPackage = objectRegistry.get(mo.pack);
			if (moPackage.element == null)
				processPackage(moPackage);
			processClass(mo, moPackage);
			mon.worked(1);
		}
		
	}

	protected void processModel(ModelObject<Object> mo){
		
		Package newPackage;
		Package startingPackage = getStartingPackage();
		boolean createDiagram = true;
		
		if (mo.element == null || !AlfrescoUMLUtils.isModel(mo.element))
			newPackage = (Package)startingPackage.createPackagedElement(mo.name, UMLFactory.eINSTANCE.createPackage().eClass());
		else {
			newPackage = (Package)mo.element;
			createDiagram = false;
		}
		
		newPackage.setName(mo.name);
		newPackage.setURI(mo.uri);
		mo.element = newPackage;
		
		// AlfrescoUMLUtils.makeModel(newPackage);
		AlfrescoProfile.ForPackage.Model theModel = AlfrescoProfile.ForPackage.Model._HELPER.getFor(newPackage); 
		//	= AlfrescoProfile.asType(newPackage, AlfrescoProfile.ForPackage.Model.class);
		
		Model model = (Model)mo.source;
		
		if (createDiagram)
			// AlfrescoModelUtils.createEmptyClassDiagram(rootObject, newPackage);
			AlfrescoModelUtils.createEmptyPackageDiagram(newPackage, mo.name);
		
/*		AlfrescoUMLUtils.setStereotypeValue(AlfrescoProfile.ForPackage.Model._NAME, 
				newPackage, 
				AlfrescoProfile.ForPackage.Model.LOCATION, 
				mo.location);
		
		AlfrescoUMLUtils.setStereotypeValue(AlfrescoProfile.ForPackage.Model._NAME, newPackage, 
				AlfrescoProfile.ForPackage.Model.DESCRIPTION, model.getDescription());
		AlfrescoUMLUtils.setStereotypeValue(AlfrescoProfile.ForPackage.Model._NAME, newPackage, 
				AlfrescoProfile.ForPackage.Model.AUTHOR, model.getAuthor());
		AlfrescoUMLUtils.setStereotypeValue(AlfrescoProfile.ForPackage.Model._NAME, newPackage, 
				AlfrescoProfile.ForPackage.Model.PUBLISHED, 
				model.getPublished()==null?""
						:((XMLGregorianCalendar)model.getPublished()).toString());
		AlfrescoUMLUtils.setStereotypeValue(AlfrescoProfile.ForPackage.Model._NAME, newPackage, 
				AlfrescoProfile.ForPackage.Model.VERSION, model.getVersion());
*/		
		theModel.setLocation(mo.location);
		theModel.setDescription(model.getDescription());
		theModel.setAuthor(model.getAuthor());
		theModel.setPublished(model.getPublished()==null?""
						:((XMLGregorianCalendar)model.getPublished()).toString());
		theModel.setVersion(model.getVersion());
	}

	protected void processPackage(ModelObject<Object> mo){
		
		if (mo.isTransient)
			return;
		
		Package model = AlfrescoUMLUtils.findModel(mo.model, umlRoot);
		
		// we DO NOT allow creating namespaces not inside the model
		if (model==null)
			return;
		
		AlfrescoProfile.ForPackage.Model theModel = AlfrescoProfile.ForPackage.Model._HELPER.getFor(model);
		
		// TODO add method hasNamespace to model
		ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace theNamespace
			= theModel.getNamespace(mo.name, false);
		boolean exists = theNamespace!=null;
		if (theNamespace==null)
			theNamespace = theModel.getNamespace(mo.name, true);
		
//		Package owner = model==null?umlRoot:model;
		Package thePackage = theNamespace.getElementClassified();
/*		boolean exists = ((mo.element != null) && AlfrescoUMLUtils.isNamespace(mo.element));
		
		if (exists)
			thePackage = (Package)mo.element;
		else
			thePackage = (Package)owner.createPackagedElement(mo.name, UMLFactory.eINSTANCE.createPackage().eClass());
*/		
//		thePackage.setName(mo.name);
		thePackage.setURI(mo.uri);
//		AlfrescoUMLUtils.makeNamespace(thePackage);
		mo.element = thePackage;
		
		if (!exists){
			AlfrescoModelUtils.createEmptyClassDiagram(rootObject, thePackage);
			AlfrescoModelUtils.createEmptyPackageDiagram(thePackage, thePackage.getName());
		}
/*		AlfrescoUMLUtils.setStereotypeValue(AlfrescoProfile.ForPackage.Namespace._NAME, 
				thePackage, 
				AlfrescoProfile.ForPackage.Namespace.DEFINED_IN_FILES, 
				Arrays.asList(new String[]{mo.location}));
*/
		theNamespace.setDefinedInFiles(Arrays.asList(new String[]{mo.location}));
	}
	
	protected void processDataType(ModelObject<Object> mo, ModelObject<Object> moPackage){
		PrimitiveType newType = (PrimitiveType)mo.element;
		if (newType==null) {
			newType =  ((Package)moPackage.element).createOwnedPrimitiveType(mo.name);
			// AlfrescoUMLUtils.makeDataType(newType);
		}
		DataType dt = (DataType) mo.source;
		AlfrescoProfile.ForPrimitiveType.DataType theDataType 
			= AlfrescoProfile.ForPrimitiveType.DataType._HELPER.getFor(newType); 
		
		
/*		AlfrescoUMLUtils.setTitle(newType, dt.getTitle());
		AlfrescoUMLUtils.setDescription(newType, dt.getDescription());
*/		
		theDataType.setTitle(dt.getTitle());
		theDataType.setDescription(dt.getDescription());
		
		
/*		AlfrescoUMLUtils.setStereotypeValue(
				AlfrescoProfile.ForPrimitiveType.DataType._NAME, 
				newType, 
				AlfrescoProfile.ForPrimitiveType.DataType.ANAYLYZER_CLASS, 
				dt.getAnalyserClass());
		AlfrescoUMLUtils.setStereotypeValue(
				AlfrescoProfile.ForPrimitiveType.DataType._NAME, 
				newType, 
				AlfrescoProfile.ForPrimitiveType.DataType.JAVA_CLASS, 
				dt.getJavaClass());
*/		
		Object obj = dt.getAnalyserClass();
		String analyzerClass=null;
		if (obj!=null)
			if (obj instanceof org.w3c.dom.Element)
				analyzerClass = ((org.w3c.dom.Element)obj).getTextContent();
			else
				analyzerClass = obj.toString();
		theDataType.setAnalyzerClass(analyzerClass);
		
		obj = dt.getJavaClass();
		String javaClass=null;
		if (obj!=null)
			if (obj instanceof org.w3c.dom.Element)
				javaClass = ((org.w3c.dom.Element)obj).getTextContent();
			else
				javaClass = obj.toString();
		
		theDataType.setJavaClass(javaClass);
		
		mo.element = newType;
	}
	
	protected void processConstraint(ModelObject<Object> mo, ModelObject<Object> moPackage){
		
	}
	
	protected void processClass(ModelObject<Object> mo, ModelObject<Object> moPackage){
		boolean exists = (mo.element != null);
		Class umlClass = exists
				?(Class)mo.element
				:((Package)moPackage.element).createOwnedClass(mo.name, false);
				
		org.alfresco.model.dictionary._1.Class theClass;
		
		if (!(mo.source instanceof org.alfresco.model.dictionary._1.Class)) {
			return;
		}
		theClass = (org.alfresco.model.dictionary._1.Class)mo.source;
		
		AlfrescoProfile.ForClass.ClassMain cm = null;
		
		if (mo.source instanceof Type) 
			cm = AlfrescoProfile.ForClass.Type._HELPER.getFor(umlClass);
		else if (mo.source instanceof Aspect) 
			cm = AlfrescoProfile.ForClass.Aspect._HELPER.getFor(umlClass);
		
		if (cm==null)
			return;

		cm.setTitle(theClass.getTitle());
		cm.setDescription(theClass.getDescription());

		if (CommonUtils.isTrue(theClass.isArchive()))
			cm.getOrCreate(Archive.class);
		else
			cm.remove(Archive.class);
				
		
		
/*		if (mo.source instanceof Aspect) {
			AlfrescoUMLUtils.makeAspect(umlClass);
		
			AlfrescoUMLUtils.makeArchive(umlClass, isTrue(theClass.isArchive()));
			
			AlfrescoUMLUtils.setTitle(umlClass, theClass.getTitle());
			AlfrescoUMLUtils.setDescription(umlClass, theClass.getDescription());
		}
		
*/		if (theClass.getProperties()!=null)
		// {BEGIN} create properties
			for (Property prop: theClass.getProperties().getProperty()){
				
				PrefixedName name = new PrefixedName(prop.getName());
				String propertyType = prop.getType();
				PrimitiveType pType = null;
				
				// here we check if type is set
				// type MUST be set, but the XML model can be edited manually,
				// so we must avoid exceptions and just create property with undefined type
				if (CommonUtils.isValueable(propertyType)) {
					PrefixedName type = new PrefixedName(prop.getType());
					Package typePackage = type.isPrefixed() ?AlfrescoUMLUtils.findNamespace(type.getPrefix(), umlRoot)  :umlRoot;
					
					pType = AlfrescoUMLUtils.findDataType(type.getName(), typePackage);
				}
				
				String pName = name.getName();
				
				// property = umlClass.getOwnedAttribute(pName, null, true, null, /*create if not found*/true);
				/**
				 * don't know why previous returns null
				 * but we must check it
				 */
/*				if (property == null)
					property = umlClass.createOwnedAttribute(pName, pType);
				else if (pType != null)
					property.setType(pType);
				
				AlfrescoUMLUtils.makeProperty(property);
*/
				AlfrescoProfile.ForProperty.Property theProperty = cm.getProperty(pName, pType, true);
				
				// AlfrescoUMLUtils.setTitle(property, prop.getTitle());
				// AlfrescoUMLUtils.setDescription(property, prop.getDescription());
				theProperty.setTitle(prop.getTitle());
				theProperty.setDescription(prop.getDescription());
				
/*				if (CommonUtils.isTrue(prop.isProtected()))
					property.setVisibility(VisibilityKind.PROTECTED_LITERAL);
				else
					property.setVisibility(VisibilityKind.PUBLIC_LITERAL);
*/
				theProperty.setProtected(CommonUtils.isTrue(prop.isProtected()));
				
				if (prop.getIndex()!=null) {
					// Stereotype s = AlfrescoUMLUtils.makeIndex(property);
					Index index = theProperty.getOrCreate(Index.class);
					
/*						property.setValue(
								s, 
								AlfrescoProfile.ForProperty.Index.ENABLED, 
								new Boolean(prop.getIndex().isEnabled()));
*/
					index.setEnabled(prop.getIndex().isEnabled());
					
/*						if (prop.getIndex().isAtomic()!=null)
							property.setValue(
									s, 
									AlfrescoProfile.ForProperty.Index.ATOMIC, 
									prop.getIndex().isAtomic());
*/		
					index.setAtomic(prop.getIndex().isAtomic());
					
/*					if (prop.getIndex().isStored()!=null)
							property.setValue(
									s, 
									AlfrescoProfile.ForProperty.Index.STORED, 
									prop.getIndex().isStored());
*/						
					index.setStored(prop.getIndex().isStored());
					
/*					if (prop.getIndex().getTokenised()!=null)
							property.setValue(
									s, 
									AlfrescoProfile.ForProperty.Index.TOKENISED, 
									prop.getIndex().getTokenised());
*/
					index.setTokenised(prop.getIndex().getTokenised());
//					}
				} else {
					// AlfrescoUMLUtils.unmakeIndex(property);
					theProperty.remove(Index.class);
				}
				
				// AlfrescoUMLUtils.makeEncrypted(property, CommonUtils.isTrue(prop.isEncrypted()));
				if (CommonUtils.isTrue(prop.isEncrypted()))
					theProperty.getOrCreate(Encrypted.class);
				else
					theProperty.remove(Encrypted.class);

/*				property.setLower(0);
				property.setUpper(1);
*/
			
				if (prop.getMandatory()!=null){
					// Stereotype st= AlfrescoUMLUtils.makeMandatory(property);
					Mandatory mandatory = theProperty.getOrCreate(Mandatory.class);

					String s = prop.getMandatory().getContent(); 
/*					if (s!=null)
						if ("true".equals(s.toLowerCase().trim()))
							property.setLower(1);
*/					mandatory.setMandatory(null!=s && "true".equals(s.toLowerCase().trim()));
					
/*					Boolean b = prop.getMandatory().isEnforced();
					if (b!=null)
						property.setValue(st, AlfrescoProfile.ForProperty.Mandatory.ENFORCED, b);
*/					mandatory.setEnforced(CommonUtils.isTrue(prop.getMandatory().isEnforced()));
				} else {
					theProperty.remove(Mandatory.class);
				}
				
/*				if (CommonUtils.isTrue(prop.isMultiple()))
					property.setUpper(-1);
*/
				theProperty.setMultiple(CommonUtils.isTrue(prop.isMultiple()));	
			}
		// {END} create properties
		
		mo.element = umlClass;
	}
	
	protected void stage5(IProgressMonitor mon){
		for (DependencyInfo di: dependencyRegistry.dependencies){
			
			switch (di.dependencyType){
			case IMPORT:
				AlfrescoProfile.ForPackage.Model model = AlfrescoProfile.ForPackage.Model._HELPER.getFor((Package)di.source.element);
				if (model!=null) {
					if (model.importNamespace(AlfrescoProfile.ForPackage.Namespace._HELPER.getFor((Package)di.target.element))!=null)
						dependencyRegistry.add(di.source, objectRegistry.objects.get(di.source.model), DependencyType.DEPENDENCY);;
					
				}
				break;
			case DEPENDENCY:
				if (UMLUtils.isPackage(di.source.element, di.target.element)) {
					AlfrescoProfile.ForPackage.Namespace._HELPER
						.getFor((Package)di.source.element)
						.dependent(AlfrescoProfile.ForPackage.Namespace._HELPER
								.getFor((Package)di.target.element)
							);
					// AlfrescoUMLUtils.dependentPackage(di.source.element, di.target.element);
				}
				break;
			case PARENT:
				if (UMLUtils.isClass(di.source.element, di.target.element))
					AlfrescoProfile.asUntyped(di.source.element)
						.get(AlfrescoProfile.ForClass.ClassMain.class)
						.inherit(AlfrescoProfile.asUntyped(di.target.element)
								.get(AlfrescoProfile.ForClass.ClassMain.class)
								);
				//AlfrescoUMLUtils.generalize(di.source.element, di.target.element);
				break;
			case CHILD:
				// AlfrescoUMLUtils.childAssociation(di);
				if (UMLUtils.isClass(di.source.element, di.target.element)) {
					ChildAssociation origin = (ChildAssociation)di.dependencyObject; 
					AlfrescoProfile.ForAssociation.ChildAssociation ca = AlfrescoProfile.asUntyped(di.source.element)
							.get(AlfrescoProfile.ForClass.ClassMain.class)
							.addChildAssociation(
									AlfrescoProfile.asUntyped(di.target.element)
										.get(AlfrescoProfile.ForClass.ClassMain.class), 
									origin.getName());
					ca.setTitle(origin.getTitle());
					ca.setDescription(origin.getDescription());
					
					AssociationComposer ab = AssociationComposer.create(ca.getElementClassified());
					AssociationInfo ai = di.getAssociationInfo();
					
					ab.source()
						.lower(ai.sourceMin)
						.upper(ai.sourceMax)
						.roleName(ai.sourceRole)
						.builder()
					.target()
						.type((Class)di.target.element)
						.lower(ai.targetMin)
						.upper(ai.targetMax)
						.roleName(ai.targetRole);
					
					ca.setChildName(origin.getChildName());
					ca.setDuplicate(origin.isDuplicate());
					ca.setPropagateTimestamps(origin.isPropagateTimestamps());
					
				}
				
				break;
			case PEER:
				// AlfrescoUMLUtils.peerAssociation(di);
				if (UMLUtils.isClass(di.source.element, di.target.element)) {
					Association origin = (Association)di.dependencyObject; 
					AlfrescoProfile.ForAssociation.Association pa = AlfrescoProfile.asUntyped(di.source.element)
							.get(AlfrescoProfile.ForClass.ClassMain.class)
							.addPeerAssociation(
									AlfrescoProfile.asUntyped(di.target.element)
										.get(AlfrescoProfile.ForClass.ClassMain.class), 
									origin.getName());
					pa.setTitle(origin.getTitle());
					pa.setDescription(origin.getDescription());
					
					AssociationComposer ab = AssociationComposer.create((org.eclipse.uml2.uml.Association)pa.getElementClassified());
					AssociationInfo ai = di.getAssociationInfo();
					
					ab.source()
						.lower(ai.sourceMin)
						.upper(ai.sourceMax)
						.roleName(ai.sourceRole)
						.builder()
					.target()
						.type((Class)di.target.element)
						.lower(ai.targetMin)
						.upper(ai.targetMax)
						.roleName(ai.targetRole);
					
					if (ai.targetForce) {
						TargetMandatory tm = pa.getOrCreate(TargetMandatory.class);
						tm.setEnforced(true);
					} else
						pa.remove(TargetMandatory.class);
					
				}
				break;
			case MANDATORY_ASPECT:
				if (UMLUtils.isClass(di.source.element, di.target.element))
					AlfrescoProfile.asUntyped(di.source.element)
						.get(AlfrescoProfile.ForClass.ClassMain.class)
						.addMandatoryAspect(AlfrescoProfile.asUntyped(di.target.element)
							.get(AlfrescoProfile.ForClass.Aspect.class)
							);
					
//				AlfrescoUMLUtils.mandatoryAspect(di);
				break;
			case UNKNOWN:;
			}
			
		}
		
		List<PackageImport> brokenImports = new ArrayList<>();
		
		for (String modelName: objectRegistry.getObjectsByClass(Model.class)){
			Package p = AlfrescoUMLUtils.findModel(modelName, umlRoot);
			if (p==null) continue;
			Package model;
			for (PackageImport pi: p.getPackageImports()){
				
				if (pi.getImportedPackage() == null) {
					brokenImports.add(pi);
					continue;
				}
				
				model = AlfrescoUMLUtils.getModel(pi.getImportedPackage());
				if (model!=null && !model.getName().equals(modelName)){
					AlfrescoUMLUtils.dependentModel(p, model);
				}
			}
			
		}
		
		for (PackageImport pi: brokenImports)
			pi.destroy();
		
	}
	
}
