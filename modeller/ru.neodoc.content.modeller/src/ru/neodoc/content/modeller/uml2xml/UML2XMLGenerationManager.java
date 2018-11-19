package ru.neodoc.content.modeller.uml2xml;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;

import ru.neodoc.content.modeller.ContentModellerPlugin;
import ru.neodoc.content.modeller.tasks.ExecutionCallback;
import ru.neodoc.content.modeller.tasks.ExecutionContext;
import ru.neodoc.content.modeller.tasks.ExecutionResult;
import ru.neodoc.content.modeller.tasks.ExecutionResultByExceptionCritical;
import ru.neodoc.content.modeller.tasks.ExecutionResultImpl;
import ru.neodoc.content.modeller.tasks.ExtendedExecutor;
import ru.neodoc.content.modeller.tasks.ExtendedSubTask;
import ru.neodoc.content.modeller.tasks.ExtendedTaskDescriptor;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.modeller.uml2xml.helper.Preloader;
import ru.neodoc.content.modeller.uml2xml.helper.model.ModelHelper;
import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.modeller.utils.JaxbUtils.JaxbHelper;
import ru.neodoc.content.modeller.utils.xml.AlfrescoXMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForNamedElement.ConstraintedObject;

public class UML2XMLGenerationManager extends ExtendedExecutor {

	
	protected static boolean helpersPreloaded = false;
	
	static {
		if (!helpersPreloaded) {
			Preloader.preload();
			helpersPreloaded = true;
		}
	}
	
	protected static String P_PREFIX = "UML2XML.";
	
	//protected Package model = null;
	protected static String P_model = P_PREFIX + "model";
	//protected String location = null;
	protected static String P_location = P_PREFIX + "location";

	protected UML2XMLContext mainContext;

	@Override
	protected void initialize() {
		super.initialize();
		this.mainContext = new UML2XMLContext();
	}
	
	@Override
	protected ExtendedTaskDescriptor createMainTask() {
		return new MainTask();
	}
	
	@Override
	protected void populateMainTask() {
		// TODO Auto-generated method stub
		super.populateMainTask();
		this.mainTask
			.add((new CheckFile()).relativeCount(10).name("Check or create file"))
			.add((new ReadOrCreateModel()).relativeCount(10).name("Read or create model"))
			.add((new MoveRelationsToOwnerClass()).relativeCount(10).name("Move relations to owner class"))
			.add((new GenerateModel()).relativeCount(70).name("Generate model"))
			.add((new SaveModel()).relativeCount(10).name("Save model"));
	}
	
	/*
	 * BEGIN :: INNER VARS
	 */
	
	//protected IFile iFileToWrite = null;
	protected static String P_iFileToWrite = P_PREFIX + "iFileToWrite";
	//protected JaxbHelper<Model> jaxbHelper = null;
	protected static String P_jaxbHelper = P_PREFIX + "jaxbHelper";
	
	/* END :: INNER VARS */
	
	/*
	 * BEGIN :: TASKS
	 */
	
	
	
	public static class UML2XMLContext extends ExecutionContext.DefaultExecutionContext{
		
		public UML2XMLContext() {
			super();
			put(this);
			this.adapter(
					P_model, 
					new ExecutionContext.DefaultExecutionContext.ValueAdapter<Package>() {
					})
			.adapter(
					P_location,
					new ExecutionContext.DefaultExecutionContext.ValueAdapter<String>() {
					} 
					)
			.adapter(
					P_iFileToWrite,
					new ExecutionContext.DefaultExecutionContext.ValueAdapter<IFile>() {
					} 
					)
			.adapter(
					P_jaxbHelper,
					new ExecutionContext.DefaultExecutionContext.ValueAdapter<JaxbHelper<Model>>() {
					} 
					);
			
		}
		
		public Package model() {
			return adapted(P_model);
		}

		public Package model(Package value) {
			return adapted(P_model, value);
		}
		
		public String location() {
			return adapted(P_location);
		}
		
		public String location(String value) {
			return adapted(P_location, value);
		}
		
		public IFile iFileToWrite() {
			return adapted(P_iFileToWrite);
		}
		
		public IFile iFileToWrite(IFile value) {
			return adapted(P_iFileToWrite, value);
		}
		
		public JaxbHelper<Model> jaxbHelper(){
			return adapted(P_jaxbHelper);
		}
		
		public JaxbHelper<Model> jaxbHelper(JaxbHelper<Model> value){
			return adapted(P_jaxbHelper, value);
		}
	}
	
	protected class MainTask extends ExtendedTaskDescriptor {
		
		public MainTask() {
			super();
			this.executionContext = UML2XMLGenerationManager.this.mainContext;
		}
		
	}

	public static abstract class UML2XMLExtendedSubTask extends ExtendedSubTask {
		
		public UML2XMLContext context() {
			return this.executionContext.get(UML2XMLContext.class);
		}
		
	}
	
	protected class CheckFile extends UML2XMLExtendedSubTask {

		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			IPath path = new Path(context().location());
			IPath fsPath = ResourcesPlugin.getWorkspace().getRoot().getFile(path).getRawLocation();
			IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(fsPath);
			
			if (files.length == 0)
				return null;
			
			IFile result = files[0];
			if (!result.exists())
				try {
					result.create(new ByteArrayInputStream("".getBytes()), true, null);
				} catch (Exception e) {
					ContentModellerPlugin.getDefault().log(e);
					return (new ExecutionResultImpl())
							.ok(false)
							.ignorable(false)
							.message(e.getMessage())
							.errorObject(e);
				}
			
			context().iFileToWrite(result);
			return (new ExecutionResultImpl()).resultObject(result);
		}
		
	}
	
	protected class ReadOrCreateModel extends UML2XMLExtendedSubTask {

		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			JaxbHelper<Model> helper = null;
			IFile file = context().iFileToWrite();
			Model xmlModel = null;
			try {
				helper = JaxbUtils.readModel(file);
				xmlModel = helper.getObject();
			} catch (Exception e) {
				// NOOP
			}
			
			if (xmlModel == null) {
				xmlModel = AlfrescoXMLUtils.emptyModel(context().model());
				try {
					JaxbUtils.write(xmlModel, file);
					helper = JaxbUtils.readModel(file);
					xmlModel = helper.getObject();				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			context().jaxbHelper(helper);
			return (new ExecutionResultImpl()).resultObject(helper);
		}
		
	}

	protected class MoveRelationsToOwnerClass extends UML2XMLExtendedSubTask {
		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			AlfrescoProfile.ForPackage.Model theModel = AlfrescoProfile.ForPackage.Model._HELPER.getFor(context().model());
			for (AlfrescoProfile.ForPackage.Namespace namespace: theModel.getAllNamespaces()) {
				for (AlfrescoProfile.ForClass.ClassMain classMain: namespace.getAllClasses()) {
					
					Collection<?> elementsAdded = ((Collection<?>)classMain.getElement().eGet(UMLPackage.eINSTANCE.getClass_NestedClassifier()));
					
					List<Element> elementsToMove = new ArrayList<>();
					List<Element> elementsMoved = new ArrayList<>();
					
					List<AlfrescoProfile.ForNamedElement.ConstraintedObject<? extends NamedElement>> constraintedObjects = new ArrayList<>(); 
					
					for (AlfrescoProfile.ForAssociation.Association association: classMain.getPeerAssociations())
						elementsToMove.add(association.getElement());
					for (AlfrescoProfile.ForAssociation.ChildAssociation childAssociation: classMain.getChildAssociations())
						elementsToMove.add(childAssociation.getElement());
					for (AlfrescoProfile.ForAssociation.MandatoryAspect mandatoryAspect: classMain.getMandatoryAspects())
						elementsToMove.add(mandatoryAspect.getElement());
					for (AlfrescoProfile.ForDependency.PropertyOverride propertyOverride: classMain.getAllPropertyOverrides()) {
						elementsToMove.add(propertyOverride.getElementClassified());
						constraintedObjects.add(propertyOverride);
					}
					
					for (AlfrescoProfile.ForProperty.Property property: classMain.getAllProperties()) {
						constraintedObjects.add(property); 
					}
					
					for (ConstraintedObject<? extends NamedElement> constraintedObject: constraintedObjects) {
						for (AlfrescoProfile.ForDependency.Constrainted constrainted: constraintedObject.getAllConstraints())
							elementsToMove.add(constrainted.getElement());
					}
					
					for (Element element: elementsToMove) {
						if (!elementsAdded.contains(element))
							try {
								@SuppressWarnings("unchecked")
								Collection<Object> c = (Collection<Object>)classMain.getElement().eGet(UMLPackage.eINSTANCE.getClass_NestedClassifier());
								c.add((Object)element);
							} catch (Exception e) {
								continue;
							}
						elementsMoved.add(element);
					}
					
					elementsToMove.removeAll(elementsMoved);
					
					elementsMoved.clear();
					if (!elementsToMove.isEmpty())
						for (Element element: elementsToMove) {
							if (element instanceof NamedElement) {
								NamedElement namedElement = (NamedElement)element;
								try {
									namedElement.eSet(UMLPackage.eINSTANCE.getNamedElement_Namespace(), classMain.getElementClassified());
								} catch (Exception e) {
									continue;
								}
								elementsMoved.add(element);
							}
						}
					
				}
				
			}
			return ExecutionResult.RESULT_OK;
		}
	}
	
	
	protected class GenerateModel extends UML2XMLExtendedSubTask {

		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			ModelHelper modelHelper = new ModelHelper();
			modelHelper.setExecutionContext(context());
			ObjectContainer<Model> newModel = modelHelper.createObject(context().model());
			modelHelper.update(context().jaxbHelper().getObject(), newModel);
			return ExecutionResult.RESULT_OK;
		}
		
	}
	
	protected class SaveModel extends UML2XMLExtendedSubTask {

		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			try {
				//JaxbUtils.write(xmlModel, file);
				context().jaxbHelper().update();
				context().jaxbHelper().save();
			} catch (Exception e) {
				return new ExecutionResultByExceptionCritical(e);
			}			
			return ExecutionResult.RESULT_OK;
		}
		
	}
	
	/* END :: TASKS */
	
	// setters & getters
	
	public Package getModel() {
		return this.mainContext.model();
	}

	public void setModel(Package model) {
		this.mainContext.model(model);
		this.mainContext.setContextObject(model);
	}

	public String getLocation() {
		return this.mainContext.location();
	}

	public void setLocation(String location) {
		this.mainContext.location(location);
	}
	
}
