package ru.neodoc.content.modeller.xml2uml.helper;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo.DependencyType;

public abstract class AbstractRelationSubHelper<ContainerType, ElementType, E extends Element> 
		extends AbstractSubHelper<ContainerType, ElementType, E> {

	@Override
	protected ModelObject<ElementType> store(ModelObject<ElementType> modelObject) {
		ModelObject<?> source = getSourceModelObject(modelObject);
		ModelObject<?> target = getTargetModelObject(modelObject);
		complexRegistry().getObjectRegistry().add(source);
		complexRegistry().getObjectRegistry().add(target);
		RelationInfo ri = complexRegistry().getRelationRegistry()
				.relationInfo(source, target, getRelationType(), modelObject);
		complexRegistry().getRelationRegistry().add(ri);
		return modelObject;
	}
	
	protected ModelObject<?> getSourceModelObject(ModelObject<ElementType> modelObject){
		return containerModelObject;
	}

	protected abstract ModelObject<?> getTargetModelObject(ModelObject<ElementType> modelObject);

	protected abstract DependencyType getRelationType();

	protected RelationInfo getRelationInfo(ModelObject<ElementType> modelObject, ElementType object) {
		RelationInfo relationInfo = complexRegistry().getRelationRegistry().getRelationByObject(object);
		if (relationInfo==null)
			relationInfo = complexRegistry().getRelationRegistry().getRelationByObject(modelObject);
		return relationInfo;
	}
	
	@Override
	protected final E getElement(ModelObject<ElementType> modelObject, ElementType object) {
		RelationInfo relationInfo = getRelationInfo(modelObject, object);
		return getElement(modelObject, object, 
				relationInfo,
				relationInfo!=null?relationInfo.source:getSourceModelObject(modelObject),
				relationInfo!=null?relationInfo.target:getTargetModelObject(modelObject));
	}
	
	protected abstract E getElement(ModelObject<ElementType> modelObject, 
			ElementType object,
			RelationInfo relationInfo,
			ModelObject<?> sourceModelObject,
			ModelObject<?> targetModelObject) ;

	@Override
	protected final E createElement(ModelObject<ElementType> object) {
		RelationInfo relationInfo = getRelationInfo(object, object.source);
		return createElement(object, object.source, 
				relationInfo,
				relationInfo!=null?relationInfo.source:null,
				relationInfo!=null?relationInfo.target:null);
	}
	
	protected abstract E createElement(ModelObject<ElementType> modelObject, 
			ElementType object,
			RelationInfo relationInfo,
			ModelObject<?> sourceModelObject,
			ModelObject<?> targetModelObject) ;
	
	/* now the dependency is satisfied, so we can try to find element once again */
	@Override
	protected boolean doBeforeProcessing(ModelObject<ElementType> modelObject) {
		super.doBeforeProcessing(modelObject);
		modelObject.setElement(getElement(modelObject, modelObject.source));
		return true;
	}
}
