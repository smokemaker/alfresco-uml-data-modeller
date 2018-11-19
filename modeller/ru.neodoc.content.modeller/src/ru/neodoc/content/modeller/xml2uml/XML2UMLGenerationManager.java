package ru.neodoc.content.modeller.xml2uml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.model.dictionary._1.Model;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace;
import ru.neodoc.content.modeller.tasks.ExecutionCallback;
import ru.neodoc.content.modeller.tasks.ExecutionContext;
import ru.neodoc.content.modeller.tasks.ExecutionResult;
import ru.neodoc.content.modeller.tasks.ExecutionResultImpl;
import ru.neodoc.content.modeller.tasks.ExtendedExecutor;
import ru.neodoc.content.modeller.tasks.ExtendedSubTask;
import ru.neodoc.content.modeller.tasks.ExtendedTaskDescriptor;
import ru.neodoc.content.modeller.utils.NamespaceSourceInfo;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractHelper;
import ru.neodoc.content.modeller.xml2uml.helper.Preloader;
import ru.neodoc.content.modeller.xml2uml.helper.entity.ModelHelper;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo;
import ru.neodoc.content.modeller.xml2uml.structure.RelationRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObjectSmartFactory;
import ru.neodoc.content.modeller.xml2uml.structure.ObjectRegistry;

public class XML2UMLGenerationManager extends ExtendedExecutor {

	protected static boolean helpersPreloaded = false;
	
	static {
		if (!helpersPreloaded) {
			Preloader.preload();
			helpersPreloaded = true;
		}
	}
	
	protected Package umlRoot;
	protected Package rootObject;
	
	protected List<Namespace> namespacesToCreate = new ArrayList<>();
	
	protected final Map<String, NamespaceSourceInfo> sources = new HashMap<>();
	
	
	public XML2UMLGenerationManager() {
		super();
		
/*		this.mainTask
			.add((new PrepareNamespaces()).relativeCount(10).name("Preparing namespaces"))
			.add((new LoadModels()).relativeCount(10).name("Loading models"))
			.add((new PopulateModels()).relativeCount(30).name("Populating models"))
			.add((new CheckDependencies()).relativeCount(30).name("Checking dependencies"))
			.add((new ClearUnsatisfiedDependencies()).relativeCount(10).name("Clearing dependencies"))
			.add((new CreateElements()).relativeCount(30).name("Creating elements"))
			.add((new CreateLinks()).relativeCount(30).name("Creating links"));*/
		
	}
	
	@Override
	protected ExtendedTaskDescriptor createMainTask() {
		return new MainTask();
	}
	
	@Override
	protected void populateMainTask() {
		this.mainTask
			.add((new PrepareNamespaces()).relativeCount(10).name("Preparing namespaces"))
			.add((new LoadModels()).relativeCount(10).name("Loading models"))
			.add((new PopulateModels()).relativeCount(30).name("Populating models"))
			.add((new CheckDependencies()).relativeCount(30).name("Checking dependencies"))
			.add((new ClearUnsatisfiedDependencies()).relativeCount(10).name("Clearing dependencies"))
			.add((new CreateElements()).relativeCount(30).name("Creating elements"))
			.add((new CreateLinks()).relativeCount(30).name("Creating links"));
	}
	
/* BEGIN :: main initial data */
	
	public Package getUmlRoot() {
		return this.umlRoot;
	}
	public void setUmlRoot(Package umlRoot) {
		this.umlRoot = umlRoot;
		this.complexRegistry.setUmlRoot(umlRoot);
	}
	public Package getRootObject() {
		return this.rootObject;
	}
	public void setRootObject(Package rootObject) {
		this.rootObject = rootObject;
		this.complexRegistry.put(ComplexRegistry.PROP_ROOT_OBJECT, this.rootObject);
	}
	public List<Namespace> getNamespacesToCreate() {
		return this.namespacesToCreate;
	}
	public void setNamespacesToCreate(List<Namespace> namespacesToCreate) {
		this.namespacesToCreate = namespacesToCreate;
	}
	
	public void addModelToSources(String name, String location){
		NamespaceSourceInfo nsi = new NamespaceSourceInfo();
		nsi.dictionaryName = name;
		nsi.dictionaryLocation = location;
		this.sources.put(location, nsi);
	}
	
/* END :: main initial data */

/* BEGIN :: execution fields */
	
	protected ObjectRegistry objectRegistry = new ObjectRegistry();
	protected RelationRegistry dependencyRegistry = new RelationRegistry(this.objectRegistry);
	protected ModelObjectSmartFactory objectSmartFactory = new ModelObjectSmartFactory(this.objectRegistry);
	
	protected ComplexRegistry complexRegistry = new ComplexRegistry(this.objectRegistry, this.dependencyRegistry, this.objectSmartFactory);
	
/* END :: execution fields */
	
/* BEGIN :: TASKS */

	public static class XML2UMLContext extends ExecutionContext.DefaultExecutionContext {
	
		protected String P_namespacesToCreate = "namespacesToCreate";
		protected String P_sources = "sources";
		
		public XML2UMLContext() {
			super();
			put(this);
		}
		
		@SuppressWarnings("unchecked")
		public List<Namespace> namespacesToCreate(){
			return (List<Namespace>) get(this.P_namespacesToCreate);
		}

		public XML2UMLContext namespacesToCreate(List<Namespace> value){
			put(this.P_namespacesToCreate, value);
			return this;
		}

		@SuppressWarnings("unchecked")
		public Map<String, NamespaceSourceInfo> sources(){
			return (Map<String, NamespaceSourceInfo>) get(this.P_sources);
		}

		public XML2UMLContext sources(Map<String, NamespaceSourceInfo> value){
			put(this.P_sources, value);
			return this;
		}
		
		
		
	}
	
	public class MainTask extends ExtendedTaskDescriptor {
		
		public MainTask() {
			super();
			this.executionContext = new XML2UMLContext();
		}
		
		@Override
		public void prepare() {
			super.prepare();
			XML2UMLGenerationManager.this.complexRegistry.put(ComplexRegistry.PROP_CREATE_DIAGRAMS, false);
			
			this.executionContext.put(XML2UMLGenerationManager.this.complexRegistry);
			this.executionContext.get(XML2UMLContext.class)
				.namespacesToCreate(XML2UMLGenerationManager.this.namespacesToCreate)
				.sources(XML2UMLGenerationManager.this.sources);
			this.executionContext.setContextObject(XML2UMLGenerationManager.this.complexRegistry.getUmlRoot());
		}
		
/*		public MainTask add(ExtendedSubTask subTask) {
			this.subtasks.add(subTask);
			return this;
		}
*/		
	}
	
	public static abstract class XML2UMLExtendedSubTask extends ExtendedSubTask {
		protected XML2UMLContext context() {
			return this.executionContext.get(XML2UMLContext.class);
		}
	}
	
	public static class PrepareNamespaces extends XML2UMLExtendedSubTask {

		@Override
		public void prepare() {
			super.prepare();
			this.totalCount = context().namespacesToCreate().size();
		}
		
		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			String location;
			for (Namespace ns: context().namespacesToCreate()) {
				Dictionary d = (Dictionary)ns.eContainer();
				location = d.getLocation();
				if (!context().sources().containsKey(location)){
					NamespaceSourceInfo si = new NamespaceSourceInfo(d);
					context().sources().put(location, si);
				}
				context().sources().get(location).namespaces.add(ns);
				callback.worked(1);
			}
			return ExecutionResult.RESULT_OK;
		}

		
	}

	public static class LoadModels extends XML2UMLExtendedSubTask {

		@Override
		public void prepare() {
			super.prepare();
			totalCount(context().sources().values().size());
		}
		
		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			List<NamespaceSourceInfo> errorObjects = new ArrayList<>();
			for (NamespaceSourceInfo si: context().sources().values()){
				if (!si.loadModel())
					errorObjects.add(si);
				callback.worked(1);
			}
			if (errorObjects.isEmpty()) {
				return ExecutionResult.RESULT_OK;
			} else {
				ExecutionResultImpl result = new ExecutionResultImpl();
				result.ok(false).ignorable(false).message("Some namespaces couldn't be loaded");
				result.getErrorObjects().addAll(errorObjects);
				String desc = "";
				for (NamespaceSourceInfo nsi: errorObjects) {
					desc += "File " + nsi.dictionaryLocation + " couldn't be loaded:\n";
					for (Namespace ns: nsi.namespaces)
						desc += "\tNamespace: " + ns.getAlias() + "{" + ns.getUrl() + "}\n";					
				}
				return result.description(desc);
			}		
		}

		
	}
	
	public static class PopulateModels extends XML2UMLExtendedSubTask {

		@Override
		public void prepare() {
			super.prepare();
			this.totalCount = context().sources().values().size() * (1 + AbstractHelper.HELPER_REGISTRY.getSubhelpers(
						/*ModelHelper.class*/AbstractHelper.HELPER_REGISTRY.getProcessorClassFor(Model.class)
					).size());
		}
		
		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			Model currentModel;
			ModelHelper modelHelper;
			for (NamespaceSourceInfo source: context().sources().values()) {
				context().get(ComplexRegistry.class).put(source);
				currentModel = source.model;
				modelHelper = new ModelHelper();
				modelHelper.setExecutionContext(context());
				modelHelper.populate(currentModel, callback);
				
//				objectRegistry.add(modelHelper.getModelObject(currentModel));
				
				
			}
			return ExecutionResult.RESULT_OK;
		}

		
	}

	public static class CheckDependencies extends XML2UMLExtendedSubTask {

		@Override
		public void prepare() {
			super.prepare();
			totalCount(context().get(ComplexRegistry.class).getRelationRegistry().getAllDependencies().size());
		}
		
		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			List<RelationInfo> errorObjects = new ArrayList<>();
			List<RelationInfo> unsatisfiedDependencies = context().get(ComplexRegistry.class).getRelationRegistry().getUnsatisfiedDependencies();
			callback.worked(this.totalCount - unsatisfiedDependencies.size());
			String description = "";
			for (RelationInfo di: unsatisfiedDependencies) {
				if (!di.isValid()) // it can be resolved while satisfying other dependency
					if (!tryToSatisfy(di)) {
						errorObjects.add(di);
						description += di.source.getName() + " -> " + di.target.getName() 
						+ "[" + di.dependencyType.getName() + "]\n";
					}
				callback.worked(1);
			}
			if (errorObjects.isEmpty())
				return ExecutionResult.RESULT_OK;
			ExecutionResultImpl er = new ExecutionResultImpl()
					.ok(false)
					.ignorable(true)
					.errorObjects(errorObjects)
					.message("Some dependencies are not satisfied")
					.description(description);
			return er;
			
		}

		@SuppressWarnings("unchecked")
		protected boolean tryToSatisfy(RelationInfo di){
			ModelObject<Object> mo = (ModelObject<Object>)di.target;
			
			Package root = context().get(ComplexRegistry.class).getUmlRoot();
			if (mo.isToCreate())
				root = (Package)context().get(ComplexRegistry.class).get(ComplexRegistry.PROP_ROOT_OBJECT);
			
			Element element = null;
			if (mo.elementClass!=null) {
				for (AbstractHelper<?, ?> helper: AbstractHelper.HELPER_REGISTRY.getElementProcessors(mo.elementClass)) {
					helper.setExecutionContext(context());
					element = helper.resolveElement(root, mo.getName());
					if (element!=null)
						break;
				}
			}
			
			if (element==null) 
				if (mo.isPackage())
					element = AlfrescoUMLUtils.findNamespace(mo.name, root, true);
				else {
					element = AlfrescoUMLUtils.findAlfrescoClass(root, mo.getName(), true);
					if (element==null)
						if ((mo.elementClass!=null) && (NamedElement.class.isAssignableFrom(mo.elementClass)))
							element = AlfrescoUMLUtils.findAlfrescoObject(root, mo.getName(), true, (Class<NamedElement>)mo.elementClass);
						else
							element = AlfrescoUMLUtils.findAlfrescoObject(root, mo.getName(), true);
				}
			
			mo.setElement(element);
			
			return di.isValid();
		}
		
	}
	
	public static class ClearUnsatisfiedDependencies extends XML2UMLExtendedSubTask {

		@Override
		public void prepare() {
			super.prepare();
			totalCount(context().get(ComplexRegistry.class).getRelationRegistry().getUnsatisfiedDependencies().size());
		}
		
		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			for (RelationInfo di: context().get(ComplexRegistry.class).getRelationRegistry().getUnsatisfiedDependencies()) {
				if (di.source.isVirtual())
					context().get(ComplexRegistry.class).getObjectRegistry().remove(di.source.name);
				if (di.target.isVirtual())
					context().get(ComplexRegistry.class).getObjectRegistry().remove(di.target.name);
				context().get(ComplexRegistry.class).getRelationRegistry().remove(di);
			}
			return ExecutionResult.RESULT_OK;
		}
		
	}
	
	public static class CreateElements extends XML2UMLExtendedSubTask {

		@Override
		public void prepare() {
			super.prepare();
			totalCount(context().get(ComplexRegistry.class).getObjectRegistry().size());
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public ExecutionResult execute(ExecutionCallback callback) {

			Set<ModelObject<?>> delayed = new HashSet<>();
			
			List<String> models = context().get(ComplexRegistry.class).getObjectRegistry().getObjectsByClass(Model.class);
			ModelHelper modelHelper = new ModelHelper();
			modelHelper.setExecutionContext(context());
			for (String model: models){
				ModelObject<Model> mo = (ModelObject<Model>)context().get(ComplexRegistry.class).getObjectRegistry().get(model);
				if (!modelHelper.deployToUML(mo))
					delayed.add(mo);
				else
					callback.worked(1);
			}
			
			for (ModelObject<?> mo: context().get(ComplexRegistry.class).getObjectRegistry().getObjects()){
				
				if (mo.isModel()){
					continue;
				} 
				
				Class<?> objectClass = mo.source==null?null:mo.source.getClass();
				if (objectClass == null)
					continue;
				
				Object creator = mo.createdBy;
				AbstractHelper<?, ?> helper = null;
				if (creator!=null)
					if (AbstractHelper.class.isAssignableFrom(creator.getClass()))
						helper = (AbstractHelper<?, ?>)creator;
				
				if (helper==null)
					helper = AbstractHelper.HELPER_REGISTRY.getProcessorFor(objectClass);
				
				if (helper==null)
					continue;
				
				helper.setExecutionContext(context());
				if (!helper.deployToUML((ModelObject) mo)) {
					delayed.add(mo);
				} else 
					callback.worked(1);
			}
			
			int delayedCount = Integer.MAX_VALUE;
			
			while((delayedCount > delayed.size()) && !delayed.isEmpty()) {
				delayedCount = delayed.size();
				Set<ModelObject<?>> toRemove = new HashSet<>();
				for (ModelObject<?> mo: delayed){
					
					Class<?> objectClass = mo.source==null?null:mo.source.getClass();
					if (objectClass == null)
						continue;
					
					Object creator = mo.createdBy;
					AbstractHelper<?, ?> helper = null;
					if (creator!=null)
						if (AbstractHelper.class.isAssignableFrom(creator.getClass()))
							helper = (AbstractHelper<?, ?>)creator;
					
					if (helper==null)
						helper = AbstractHelper.HELPER_REGISTRY.getProcessorFor(objectClass);
					if (helper==null)
						continue;
					
					helper.setExecutionContext(context());
					if (helper.deployToUML((ModelObject) mo)) {
						toRemove.add(mo);
						callback.worked(1);
					} 
				}
				delayed.removeAll(toRemove);
			}
			if (delayed.isEmpty())
				return ExecutionResult.RESULT_OK;
			else 
				return (new ExecutionResultImpl())
					.ok(false)
					.ignorable(false)
					.message("Some objects can not be created or updated")
					.description("Total failed objects: " + delayed.size())
					.errorObjects(new ArrayList<>(delayed));
		}

		
	}
	
	public static class CreateLinks extends XML2UMLExtendedSubTask {

		@Override
		public void prepare() {
			super.prepare();
			totalCount(context().get(ComplexRegistry.class).getRelationRegistry().getAllDependencies().size());
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			
			Set<RelationInfo> delayed = new HashSet<>();
			for (RelationInfo relationInfo: context().get(ComplexRegistry.class).getRelationRegistry().getAllDependencies()) {
				ModelObject<?> modelObject = relationInfo.relationObject;
				Object sourceObject = modelObject==null?null:modelObject.source;
				if (sourceObject==null) {
					// create default link - simple dependency
				} else {
					AbstractHelper<?, ?> helper;
					helper = AbstractHelper.HELPER_REGISTRY.getProcessorFor(sourceObject.getClass());
					if (helper!=null)
						helper.setExecutionContext(context());
					if ((helper!=null) && (modelObject!=null)) 
						if (!helper.deployToUML((ModelObject)modelObject, callback))
							delayed.add(relationInfo);
						else
							callback.worked(1);
				}
			}
			
			int delayedCount = Integer.MAX_VALUE;
			while ((delayedCount>delayed.size()) && !delayed.isEmpty()) {
				delayedCount = delayed.size();
				for (RelationInfo relationInfo: delayed) {
					ModelObject<?> modelObject = relationInfo.relationObject;
					Object sourceObject = modelObject==null?null:modelObject.source;
					if (sourceObject==null) {
						// create default link - simple dependency
					} else {
						AbstractHelper<?, ?> helper;
						helper = AbstractHelper.HELPER_REGISTRY.getProcessorFor(sourceObject.getClass());
						if (helper!=null)
							helper.setExecutionContext(context());
						if ((helper!=null) && (modelObject!=null)) 
							if (helper.deployToUML((ModelObject)modelObject, callback)) {
								delayed.remove(relationInfo);
								callback.worked(1);
							}
					}
				}
			}
			
			callback.done();
			return ExecutionResult.RESULT_OK;
		}

		
	}
/* END :: TASKS */
	
}
