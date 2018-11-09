package ru.neodoc.content.modeller.palette.helper.advice;

import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.papyrus.infra.services.edit.utils.RequestParameterConstants;
import org.eclipse.uml2.uml.NamedElement;

public class CASetNameEditHelperAdvice extends AbstractEditHelperAdvice {

	@Override
	public void configureRequest(IEditCommandRequest request) {
		super.configureRequest(request);
		if (!(request instanceof ConfigureRequest))
			return;
		final Object source = request.getParameter(CreateRelationshipRequest.SOURCE);
		final Object target = request.getParameter(CreateRelationshipRequest.TARGET);
		request.setParameter(RequestParameterConstants.NAME_TO_SET,
				getPrefix()
				+ (source instanceof NamedElement?((NamedElement)source).getName():"source") 
				+ "_"
				+ (target instanceof NamedElement?((NamedElement)target).getName():"target"));
	}
	
	protected String getPrefix() {
		return "ca_";
	}
/*	@Override
	protected ICommand getAfterConfigureCommand(ConfigureRequest request) {
		
		final ConfigureRequest theRequest = request;
		return new ConfigureElementCommand(theRequest) {
			
			final Association association = (Association) theRequest.getElementToConfigure();
			final Object source = theRequest.getParameter(CreateRelationshipRequest.SOURCE);
			final Object target = theRequest.getParameter(CreateRelationshipRequest.TARGET);
			
			@Override
			protected CommandResult doExecuteWithResult(IProgressMonitor arg0, IAdaptable arg1) throws ExecutionException {
				
				association.setName(
						"a_" 
						+ (source instanceof NamedElement?((NamedElement)source).getName():"source") 
						+ "_"
						+ (target instanceof NamedElement?((NamedElement)target).getName():"target"));
				return CommandResult.newOKCommandResult(association);
			}
		};
	}*/
	
}
