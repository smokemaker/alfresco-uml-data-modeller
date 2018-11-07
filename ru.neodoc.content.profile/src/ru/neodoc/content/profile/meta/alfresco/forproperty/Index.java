package ru.neodoc.content.profile.meta.alfresco.forproperty;

import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Index.class)
public class Index extends PropertyOptionalAbstract
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Index {

	public Index(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public Boolean isEnabled() {
		return getBoolean(ENABLED);
	}

	@Override
	public void setEnabled(Boolean value) {
		setAttribute(ENABLED, value);
	}

	@Override
	public Boolean isAtomic() {
		return getBoolean(ATOMIC);
	}

	@Override
	public void setAtomic(Boolean atomic) {
		setAttribute(ATOMIC, atomic);
	}

	@Override
	public Boolean isStored() {
		return getBoolean(STORED);
	}

	@Override
	public void setStored(Boolean stored) {
		setAttribute(STORED, stored);
	}

	@Override
	public String getTokenised() {
		return (String)getAttribute(TOKENISED);
	}

	@Override
	public void setTokenised(String value) {
		setAttribute(TOKENISED, value);
	}

}
