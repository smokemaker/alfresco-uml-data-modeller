package ru.neodoc.content.profile.meta.alfresco.forassociation;

import org.eclipse.uml2.uml.Class;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.utils.uml.AssociationComposer;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(AlfrescoProfile.ForAssociation.ChildAssociation.class)
public class ChildAssociation extends Association
		implements AlfrescoProfile.ForAssociation.ChildAssociation {

	public ChildAssociation(CompositeMetaObject composite) {
		super(composite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isDuplicate() {
		return getBoolean(DUPLICATE);
	}

	@Override
	public boolean isPropagateTimestamps() {
		return getBoolean(PROPAGATE_TIMESTAMPS);
	}

	@Override
	public String getChildName() {
		return getString(CHILD_NAME);
	}

	@Override
	public void setDuplicate(Boolean value) {
		setAttribute(DUPLICATE, value);
	}

	@Override
	public void setPropagateTimestamps(Boolean value) {
		setAttribute(PROPAGATE_TIMESTAMPS, value);
	}

	@Override
	public void setChildName(String value) {
		setAttribute(CHILD_NAME, value);
	}

	@Override
	public ClassMain getComposite() {
		AssociationComposer ab = AssociationComposer.create(getElementClassified());
		Class cl = (Class)ab.source().getType();
		return AbstractProfile.asUntyped(cl).get(ClassMain.class);
	}

	@Override
	public ClassMain getChild() {
		AssociationComposer ab = AssociationComposer.create(getElementClassified());
		Class cl = (Class)ab.target().getType();
		return AbstractProfile.asUntyped(cl).get(ClassMain.class);
	}

}
