package ru.neodoc.content.modeller.uml2xml.helper.constraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Constraints;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;

public class ModelConstraintHelper extends ConstraintOwnedHelper<Package, Model.Constraints> {

	static {
		HELPER_REGISTRY.register(ModelConstraintHelper.class, ModelConstraintsHelper.class);
	}


	@Override
	public List<Constraint> getSubElements(Package container) {
		AlfrescoProfile.ForPackage.Model model = AlfrescoProfile.ForPackage.Model._HELPER.findNearestFor(container);
		if (model==null)
			return Collections.emptyList();
		List<Constraint> result = new ArrayList<>();
		for (AlfrescoProfile.ForPackage.Namespace ns: model.getAllNamespaces()) {
			for (ConstraintMain cm: ns.getAllConstraints())
				if (!cm.has(AlfrescoProfile.ForConstraint.Inline.class))
					result.add(cm.getElementClassified());
		}
		return result;
	}


	@Override
	protected List<ObjectContainer<org.alfresco.model.dictionary._1.Constraint>> addObjectsToContainer(
			ObjectContainer<Constraints> container,
			List<ObjectContainer<org.alfresco.model.dictionary._1.Constraint>> objectsToAdd) {
		container.getObject().getConstraint().addAll(ObjectContainer.FACTORY.extractTyped(objectsToAdd));
		return ObjectContainer.FACTORY.createListTyped(container.getObject().getConstraint());
	}


	@Override
	protected List<org.alfresco.model.dictionary._1.Constraint> getOrCreateObjects(Constraints container) {
		return getObjects(container);
	}


	@Override
	public List<org.alfresco.model.dictionary._1.Constraint> getObjects(Constraints container) {
		return container.getConstraint();
	}

}
