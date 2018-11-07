package ru.neodoc.content.modeller.xml2uml.helper;

import java.util.List;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.tasks.ExecutionCallback;
import ru.neodoc.content.modeller.tasks.ExecutionContext;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public abstract class AbstractHelper<T, E extends Element> {
	
	public static final HelperRegistry HELPER_REGISTRY = new HelperRegistry();

	protected ExecutionContext executionContext = ExecutionContext.create();

	public AbstractHelper() {
		super();
	}
	
	public AbstractHelper(ExecutionContext initialContext) {
		super();
		setExecutionContext(initialContext);
	}
	
	public ExecutionContext getExecutionContext() {
		return executionContext;
	}

	public void setExecutionContext(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	protected ComplexRegistry complexRegistry() {
		return executionContext.get(ComplexRegistry.class);
	}
	
	protected ModelObject<T> createNewModelObject(T object){
		return createNewCommonModelObject(object);
	}
	
	protected ModelObject<T> createNewCommonModelObject(T object){
		return new ModelObject<>(object);
	}
	
	protected ModelObject<T> createNewTempModelObject(T object){
		ModelObject<T> mo = createNewCommonModelObject(object);
		mo.noStore = true;
		return mo;
	}
	
	public ModelObject<T> getNewModelObject(T object){
		ModelObject<T> mo = createNewModelObject(object);
		fillModelObject(mo, object);
		return mo;
	}
	
	public ModelObject<T> getModelObject(T object){
/*		if (modelObject==null)
			modelObject = getNewModelObject();
		return modelObject;
*/		return getNewModelObject(object);
	}
	
	protected final void fillModelObject(ModelObject<T> modelObject, T object) {
		modelObject.source = object;
		modelObject.load(object);
		modelObject.createdBy = this;
		doCustomFillModelObject(modelObject, object);
		modelObject.setElement(getElement(modelObject, object));
	}
	
	protected void doCustomFillModelObject(ModelObject<T> modelObject, T object) {
		
	}
	
	protected E getElement(ModelObject<T> modelObject, T object) {
		return null;
	}
	
	public E resolveElement(Package root, String name) {
		return null;
	}
	
	public void populate(T object, ExecutionCallback callback) {
		populate(getModelObject(object), callback);
	}
	
	@SuppressWarnings("unchecked")
	protected Class<? extends AbstractHelper<?, ?>> getParentHelperClass(){
		return (Class<? extends AbstractHelper<?, ?>>)this.getClass();
	}
	
	public void populate(ModelObject<T> modelObject, ExecutionCallback callback) {
		// complexRegistry.getObjectRegistry().add(modelObject);
		ModelObject<T> mo = store(modelObject);
		callback.worked(1);
		preSubHelpersPopulate(mo, callback);
		subHelpersPopulate(mo, callback);
	}
	
	protected abstract ModelObject<T> store(ModelObject<T> modelObject);
	
	protected void preSubHelpersPopulate(ModelObject<T> modelObject, ExecutionCallback callback) {
		
	}
	
	protected void subHelpersPopulate(ModelObject<T> modelObject, ExecutionCallback callback) {
		List<Class<? extends AbstractHelper<?, ?>>> subHelpers = HELPER_REGISTRY.getSubhelpers(getParentHelperClass()); 
		for (Class<? extends AbstractHelper<?, ?>> ashClass: subHelpers) {
			try {
				AbstractHelper<?, ?> ah = ashClass.newInstance();
				if (ah instanceof SubHelper) {
					SubHelper<?, ?> subHelper = (SubHelper<?, ?>)ah;
					ah.setExecutionContext(getExecutionContext());
					subHelper.populateFromContainer(modelObject);
					callback.worked(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	protected boolean isNeedToProcess(ModelObject<T> object) {
		return !object.isTransient;
	}
	
	protected abstract E createElement(ModelObject<T> object);
	
	protected boolean needToCreateElement(ModelObject<T> object) {
		return object.getElement()==null; 
	}
	
	protected boolean processElement(E element, ModelObject<T> object) {
		return true;
	}
	
	protected boolean processStereotypedElement(StereotypedElement stereotypedElement, ModelObject<T> object) {
		return true;
	}
	
	protected boolean doBeforeProcessing(ModelObject<T> modelObject) {
		return true;
	}
	
	protected boolean doPreProcessing(ModelObject<T> modelObject, StereotypedElement stereotypedElement) {
		return true;
	}
	
	protected boolean doPostProcessing(ModelObject<T> modelObject, StereotypedElement stereotypedElement) {
		return true;
	}

	protected boolean doPreSubHelpersDeploy(ModelObject<T> modelObject, StereotypedElement stereotypedElement) {
		return true;
	}
	
	protected boolean subHelpersDeploy(ModelObject<T> modelObject, ExecutionCallback callback) {
		for (Class<? extends AbstractHelper<?, ?>> ashClass: HELPER_REGISTRY.getSubhelpers(getParentHelperClass())) {
			try {
				AbstractHelper<?, ?> ah = ashClass.newInstance();
				if (ah instanceof SubHelper) {
					SubHelper<?, ?> subHelper = (SubHelper<?, ?>)ah;
					ah.setExecutionContext(getExecutionContext());
					subHelper.deployFromContainer(modelObject);
					callback.worked(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return true;
	}
	
	protected boolean doPostSubHelpersDeploy(ModelObject<T> modelObject, StereotypedElement stereotypedElement) {
		return true;
	}
	
	public boolean deployToUML(ModelObject<T> object) {
		return deployToUML(object, ExecutionCallback.EMPTY_CALLBACK);
	}
	
	@SuppressWarnings("unchecked")
	public boolean deployToUML(ModelObject<T> object, ExecutionCallback callback) {
		if (!isNeedToProcess(object))
			return true;
		boolean result = true;
		if (!doBeforeProcessing(object))
				return false;
		if (needToCreateElement(object))
			object.setElement(createElement(object));
		if (object.getElement()==null)
			return false;
		StereotypedElement stereotypedElement = AbstractProfile.asUntyped(object.getElement());
		if (!doPreProcessing(object, stereotypedElement))
			return false;
		if (!processElement((E)object.getElement(), object))
			return false;
		if (!processStereotypedElement(stereotypedElement, object))
			return false;
		if (!doPostProcessing(object, stereotypedElement))
			return false;
		if (!doPreSubHelpersDeploy(object, stereotypedElement))
			return false;
		if (!subHelpersDeploy(object, callback))
			return false;
		if (!doPostSubHelpersDeploy(object, stereotypedElement))
			return false;
		return result;
	}
}
