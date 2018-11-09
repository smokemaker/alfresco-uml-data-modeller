package ru.neodoc.content.modeller.uml2xml.helper.classes;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Aspect;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ListComparator;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.model.dictionary._1.Class.MandatoryAspects;
import org.eclipse.uml2.uml.Class;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelper;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;

public class MandatoryAspectHelper extends 
		AbstractSubHelper<
			org.eclipse.uml2.uml.Class, 
			org.eclipse.uml2.uml.Class, 
			org.alfresco.model.dictionary._1.Class.MandatoryAspects, 
			String, 
			AlfrescoProfile.ForClass.Aspect>{

	static {
		HELPER_REGISTRY.register(MandatoryAspectHelper.class, MandatoryAspectsHelper.class).asContained();
	}

	@Override
	public List<Class> getSubElements(Class container) {
		List<Class> result = new ArrayList<>();
		AlfrescoProfile.ForClass.ClassMain classMain = 
				AbstractProfile.asUntyped(container).get(AlfrescoProfile.ForClass.ClassMain.class);
		if (classMain!=null) {
			for (AlfrescoProfile.ForAssociation.MandatoryAspect ma: classMain.getMandatoryAspects()) {
				AlfrescoProfile.ForClass.Aspect aspect = ma.getAspect();
				if (aspect!=null)
					if (!result.contains(aspect.getElementClassified()))
						result.add(aspect.getElementClassified());
			}
		}
		return result;
	}

	@Override
	protected List<ObjectContainer<String>> addObjectsToContainer(ObjectContainer<MandatoryAspects> container,
			List<ObjectContainer<String>> objectsToAdd) {
		container.getObject().getAspect().addAll(ObjectContainer.FACTORY.extractTyped(objectsToAdd));
		return ObjectContainer.FACTORY.createListTyped(container.getObject().getAspect());
	}

	@Override
	public ListComparator<String> getComparator() {
		return new CommonUtils.BaseListComparator<String>() {

			@Override
			public String itemHash(String item) {
				if (item==null)
					return "";
				return item;
			}
			
		};
	}

	@Override
	protected List<String> getOrCreateObjects(MandatoryAspects container) {
		return getObjects(container);
	}

	@Override
	public List<String> getObjects(MandatoryAspects container) {
		return container.getAspect();
	}

	@Override
	protected java.lang.Class<Aspect> getStereotypeClass() {
		return Aspect.class;
	}

	@Override
	protected String doCreateObject(Class element) {
		Aspect aspect = Aspect._HELPER.is(element)?Aspect._HELPER.getFor(element):null;
		if (aspect!=null)
			return aspect.getPrfixedName();
		return "";
	}

/*	@Override
	protected ObjectContainer<String> fillObjectProperties(ObjectContainer<String> objectContainer, Class element,
			Aspect stereotyped) {
		objectContainer.setObject(stereotyped.getPrfixedName());
		return objectContainer;
	}

*/	@Override
	protected String doFillObjectProperties(String object, Class element, Aspect stereotyped) {
		// TODO Auto-generated method stub
		return object;
	}

	@Override
	protected void updateProperties(String objectToUpdate, ObjectContainer<String> newObjectContainer) {
		objectToUpdate = newObjectContainer.getObject();
	}
	
	
}
