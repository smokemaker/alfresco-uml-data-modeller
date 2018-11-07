package ru.neodoc.content.modeller.xml2uml.helper.relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Class;
import org.eclipse.uml2.uml.Generalization;

import ru.neodoc.content.modeller.xml2uml.helper.AbstractHelper;
import ru.neodoc.content.modeller.xml2uml.helper.AbstractRelationSubHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.ClassHelper;
import ru.neodoc.content.modeller.xml2uml.helper.relation.proxy.ParentProxy;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo;
import ru.neodoc.content.modeller.xml2uml.structure.RelationInfo.DependencyType;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.UMLUtils;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.search.UMLSearchUtils;

public class ParentHelper extends AbstractRelationSubHelper<Class, ParentProxy, Generalization> {
	
	static {
		HELPER_REGISTRY.register( 
				ParentHelper.class, 
				(java.lang.Class<? extends AbstractHelper<?, ?>>) ClassHelper.class,
				Generalization.class, ParentProxy.class);
	}
	
	@Override
	protected List<ParentProxy> getElementsFromContainer(Class container) {
		if (!CommonUtils.isValueable(container.getParent()))
			return Collections.emptyList();
		return new ArrayList<>(
					Arrays.asList(new ParentProxy[]{new ParentProxy(container.getParent())})
				);
	}

	@Override
	protected Generalization getElement(ModelObject<ParentProxy> modelObject,
			ParentProxy object, RelationInfo relationInfo, ModelObject<?> sourceModelObject,
			ModelObject<?> targetModelObject) {
		if (!ModelObject.hasElement(sourceModelObject))
			return null;
		
		/*ClassMain cm = AbstractProfile.asUntyped(sourceModelObject.getElement()).get(ClassMain.class);
		if (cm==null)
			return null;
		for (Inherit inherit: cm.getInherits())
			if (inherit.getGeneral().getElementClassified().equals(targetModelObject.getElement()))
				return inherit.getElementClassified();*/
		org.eclipse.uml2.uml.Class sourceClass = (org.eclipse.uml2.uml.Class)sourceModelObject.getElement();
		org.eclipse.uml2.uml.Class targetClass = (org.eclipse.uml2.uml.Class)targetModelObject.getElement();
		if (targetClass == null) {
			targetClass = UMLSearchUtils.classByName(complexRegistry().getUmlRoot(), targetModelObject.getName());
		}
		if (targetClass==null)
			return null;
		for (Generalization gen: sourceClass.getGeneralizations()) {
			if (gen.getGeneral().equals(targetClass))
				return gen;
		}
		return null;
	}
	
	@Override
	protected Generalization createElement(ModelObject<ParentProxy> modelObject,
			ParentProxy object, RelationInfo relationInfo, ModelObject<?> sourceModelObject,
			ModelObject<?> targetModelObject) {
		if (UMLUtils.isClass(relationInfo.source.getElement(), relationInfo.target.getElement()))
			return AbstractProfile.asUntyped(relationInfo.source.getElement())
				.get(AlfrescoProfile.ForClass.ClassMain.class)
				.inherit(AbstractProfile.asUntyped(relationInfo.target.getElement())
						.get(AlfrescoProfile.ForClass.ClassMain.class)
						)
				.getElementClassified();
		
		return null;
	}

	@Override
	protected ModelObject<?> getTargetModelObject(ModelObject<ParentProxy> modelObject) {
		return complexRegistry().getObjectSmartFactory().getObject(modelObject.source.asString(), org.eclipse.uml2.uml.Class.class);
	}

	@Override
	protected DependencyType getRelationType() {
		return DependencyType.PARENT;
	}
}
