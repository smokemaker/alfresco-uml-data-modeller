package ru.neodoc.content.profile.meta.alfresco.forassociation;

import org.eclipse.uml2.uml.Association;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.AssociationMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.utils.uml.profile.annotation.AWraps;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AWraps(AssociationMain.class)
public class AssociationMainAbstract extends ImplementationMetaObjectClassified<Association> implements AssociationMain {

	protected AssociationMain am;
	
	public AssociationMainAbstract(CompositeMetaObject composite) {
		super(composite);
		am = createAndRegisterSubimplementor(AssociationMain.class);
	}

	@Override
	public ClassMain getSource() {
		return am.getSource();
	}

	@Override
	public ClassMain getTarget() {
		return am.getTarget();
	}

}
