package ru.neodoc.content.profile.meta.alfresco.forproperty;

import org.eclipse.uml2.uml.Property;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.PropertyOptional;
import ru.neodoc.content.utils.uml.profile.annotation.AWraps;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AWraps(PropertyOptional.class)
public class PropertyOptionalAbstract extends ImplementationMetaObjectClassified<Property> implements PropertyOptional {

	protected PropertyOptional po;
	
	public PropertyOptionalAbstract(CompositeMetaObject composite) {
		super(composite);
		PropertyOptinal imo = new PropertyOptinal(composite);
		addSubimplemetor(imo);
		po = imo;
	}

}
