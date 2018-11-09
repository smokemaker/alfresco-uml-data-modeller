package ru.neodoc.content.modeller.xml2uml.helper.relation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Class;
import org.alfresco.model.dictionary._1.Class.MandatoryAspects;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.modeller.tasks.ExecutionCallback;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractHelper;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractRelationSubHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.ClassHelper;
import ru.neodoc.content.modeller.xml2uml.helper.relation.proxy.MandatoryAspectProxy;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo.DependencyType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfileUtils;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchUtils;
import ru.neodoc.content.utils.uml.UMLUtils;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.search.UMLSearchUtils;

@SuppressWarnings("unchecked")
public class MandatoryAspectHelper 
		extends AbstractRelationSubHelper<org.alfresco.model.dictionary._1.Class, MandatoryAspectProxy, org.eclipse.uml2.uml.Association> {

	static {
		HELPER_REGISTRY.register( 
				MandatoryAspectHelper.class, 
				(java.lang.Class<? extends AbstractHelper<?, ?>>) ClassHelper.class,
				Element.class, MandatoryAspectProxy.class);
	}
	
	@Override
	protected List<MandatoryAspectProxy> getElementsFromContainer(Class container) {
		MandatoryAspects ma = container.getMandatoryAspects();
		if (ma==null)
			return Collections.emptyList();
		List<MandatoryAspectProxy> result = new ArrayList<>();
		for (String s: ma.getAspect())
			result.add(new MandatoryAspectProxy(s));
		return result;
	}

/*	@Override
	public void populate(ComplexRegistry complexRegistry, ModelObject<MandatoryAspectProxy> modelObject,
			ExecutionCallback callback) {
		String aspectName = modelObject.source.asString();
		ModelObject<Object> aspectObj = 
				complexRegistry.getObjectSmartFactory().getObject(aspectName);
		aspectObj.setElement(AlfrescoProfileUtils.findAspect(aspectName, complexRegistry.getUmlRoot()));
		complexRegistry.getObjectRegistry().add(aspectObj);
		modelObject.source.setRelationInfo(
				complexRegistry.getRelationRegistry()
				.add(containerModelObject, aspectObj, DependencyType.MANDATORY_ASPECT)
				.relationObject(modelObject)		
				);
	}*/
	

	@Override
	protected org.eclipse.uml2.uml.Association createElement(ModelObject<MandatoryAspectProxy> modelObject,
			MandatoryAspectProxy object, RelationInfo relationInfo, ModelObject<?> sourceModelObject,
			ModelObject<?> targetModelObject) {
		// TODO Auto-generated method stub
		if (UMLUtils.isClass(relationInfo.source.getElement(), relationInfo.target.getElement()))
			return AbstractProfile.asUntyped(relationInfo.source.getElement())
				.get(AlfrescoProfile.ForClass.ClassMain.class)
				.addMandatoryAspect(AbstractProfile.asUntyped(relationInfo.target.getElement())
					.get(AlfrescoProfile.ForClass.Aspect.class)
					).getElementClassified();
		return null;
	}

	@Override
	protected ModelObject<?> getTargetModelObject(ModelObject<MandatoryAspectProxy> modelObject) {
		return complexRegistry().getObjectSmartFactory().getObject(modelObject.source.asString(), org.eclipse.uml2.uml.Class.class);
	}

	@Override
	protected DependencyType getRelationType() {
		return DependencyType.MANDATORY_ASPECT;
	}

	@Override
	protected org.eclipse.uml2.uml.Association getElement(ModelObject<MandatoryAspectProxy> modelObject,
			MandatoryAspectProxy object, RelationInfo relationInfo, ModelObject<?> sourceModelObject,
			ModelObject<?> targetModelObject) {
		if (!ModelObject.hasElement(sourceModelObject))
			return null;
		
		org.eclipse.uml2.uml.Class sourceClass = (org.eclipse.uml2.uml.Class)sourceModelObject.getElement();
		org.eclipse.uml2.uml.Class targetClass = (org.eclipse.uml2.uml.Class)targetModelObject.getElement();
		if (targetClass == null) {
			targetClass = UMLSearchUtils.classByName(complexRegistry().getUmlRoot(), targetModelObject.getName());
		}
		if (targetClass==null)
			return null;
		for (Association as: UMLSearchUtils.associationsByEndType(sourceClass, targetClass)) {
			if (AbstractProfile.isType(as, MandatoryAspect.class))
				return as;
		}
		return null;
	}
	
}
