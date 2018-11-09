package ru.neodoc.content.profile.meta.alfresco.forproperty;

import org.eclipse.uml2.uml.Property;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.PropertyOptional;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(PropertyOptional.class)
public class PropertyOptinal extends ImplementationMetaObjectClassified<Property> implements PropertyOptional {

	public PropertyOptinal(CompositeMetaObject composite) {
		super(composite);
	}

}
