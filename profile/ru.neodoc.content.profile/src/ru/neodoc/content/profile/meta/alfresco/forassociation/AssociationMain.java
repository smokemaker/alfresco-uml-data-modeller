package ru.neodoc.content.profile.meta.alfresco.forassociation;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.AssociationMain.class)
public class AssociationMain extends ImplementationMetaObjectClassified<Association>
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.AssociationMain {

	public AssociationMain(CompositeMetaObject composite) {
		super(composite);
		// TODO Auto-generated constructor stub
	}

	public Property getMemberEnd(int index) {
		Association association = getElementClassified();
		try {
			return association.getMemberEnds().get(index);
		} catch (Exception e) {
			return null;
		}
	}
	
	public Type getMemberEndType(int index) {
		Property me = getMemberEnd(index);
		if (me==null)
			return null;
		return me.getType();
	} 
	
	public ClassMain getClassMain(int index) {
		Type t = getMemberEndType(index);
		if (t==null)
			return null;
		if (t instanceof Class) {
			StereotypedElement ps = AbstractProfile.asUntyped((Class)t);
			return ps.get(ClassMain.class);
		}
		return null;
	}
	
	@Override
	public ClassMain getSource() {
		return getClassMain(1);
	}

	@Override
	public ClassMain getTarget() {
		return getClassMain(0);
	}

}
