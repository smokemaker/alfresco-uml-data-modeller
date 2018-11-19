package ru.neodoc.content.profile.meta.alfresco.forassociation;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Namespaced;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(AlfrescoProfile.ForAssociation.Association.class)
public class Association extends AssociationSolid implements AlfrescoProfile.ForAssociation.Association {

	protected Namespaced _namespaced;
	
	public Association(CompositeMetaObject composite) {
		super(composite);
		_namespaced = createAndRegisterSubimplementor(Namespaced.class);
	}

	@Override
	public String getName() {
		return _namespaced.getName();
	}

	@Override
	public void setName(String value) {
		_namespaced.setName(value);
	}

	@Override
	public Namespace getNamespace() {
		return _namespaced.getNamespace();
	}

	@Override
	public String getPrfixedName() {
		return _namespaced.getPrfixedName();
	}
	
}
