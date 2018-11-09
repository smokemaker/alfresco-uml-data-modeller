package ru.neodoc.content.codegen.sdoc2.wrap;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect;

public class MandatoryAspectWrapper extends AssociationMainWrapper<MandatoryAspect> {

	public MandatoryAspectWrapper(MandatoryAspect wrappedElement) {
		super(wrappedElement);
		// TODO Auto-generated constructor stub
	}

	public AspectWrapper getTargetAspectWrapper() {
		return WrapperFactory.get(getClassifiedWrappedElement().getAspect());
	}
	
}
