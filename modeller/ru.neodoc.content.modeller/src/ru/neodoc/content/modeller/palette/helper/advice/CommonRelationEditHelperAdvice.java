package ru.neodoc.content.modeller.palette.helper.advice;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;

public class CommonRelationEditHelperAdvice extends AlfrescoRelationEditHelperAdvice {
	
	@Override
	protected boolean approveCreateAlfrescoRelationRequest(IEditCommandRequest request,
			CreateRelationshipRequest createRelationshipRequest) {
		return RelationshipHelper.checkStereotype(createRelationshipRequest);
	}
	
	@Override
	protected ICommand getBeforeReorientReferenceRelationshipCommand(ReorientReferenceRelationshipRequest request) {
		// TODO Auto-generated method stub
//		request.
		return super.getBeforeReorientReferenceRelationshipCommand(request);
	}
	
	@Override
	protected ICommand getBeforeReorientRelationshipCommand(ReorientRelationshipRequest request) {
		// TODO Auto-generated method stub
		return super.getBeforeReorientRelationshipCommand(request);
	}
}
