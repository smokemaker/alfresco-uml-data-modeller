package ru.neodoc.content.codegen.sdoc2.wrap;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.AssociationSolid;

public class AssociationSolidWrapper<T extends AssociationSolid> extends AssociationMainWrapper<T> {

	protected AssociationSolidWrapper(T wrappedElement) {
		super(wrappedElement);
		// TODO Auto-generated constructor stub
	}

	public AbstractAlfrescoClassWrapper<?> getTargetClassWrapper(){
		return WrapperFactory.get(getClassifiedWrappedElement().getTarget());
	}
	
}
