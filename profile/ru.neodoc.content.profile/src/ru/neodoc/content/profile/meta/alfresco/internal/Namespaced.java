package ru.neodoc.content.profile.meta.alfresco.internal;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObject;

@AImplements(AlfrescoProfile.Internal.Namespaced.class)
public class Namespaced extends ImplementationMetaObject implements AlfrescoProfile.Internal.Namespaced {

	protected AlfrescoProfile.Internal.Named _named;
	
	public Namespaced(CompositeMetaObject composite) {
		super(composite);
		_named = createAndRegisterSubimplementor(AlfrescoProfile.Internal.Named.class);
	}

	@Override
	public String getName() {
		return _named.getName();
	}

	@Override
	public void setName(String value) {
		_named.setName(value);
	}

	@Override
	public Namespace getNamespace() {
		return Namespace._HELPER.findNearestFor((Element)getElement());
	}

	@Override
	public String getPrfixedName() {
		PrefixedName pn = new PrefixedName(getName());
		try {
			pn.setPrefix(getNamespace().getPrefix());
		} catch (Exception e) {
			
		}
		return pn.getFullName();
	}

}
