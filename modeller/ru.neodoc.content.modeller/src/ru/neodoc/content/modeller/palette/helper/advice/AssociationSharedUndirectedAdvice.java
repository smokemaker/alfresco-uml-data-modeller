package ru.neodoc.content.modeller.palette.helper.advice;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.ConfigureElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.papyrus.uml.service.types.utils.RequestParameterConstants;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;

import ru.neodoc.content.modeller.ContentModellerPlugin;

public class AssociationSharedUndirectedAdvice extends AbstractEditHelperAdvice {
	
	@Override
	protected ICommand getBeforeCreateRelationshipCommand(CreateRelationshipRequest request) {
		request.setParameter(RequestParameterConstants.AFFECTS_TARGET, false);
		return super.getBeforeCreateRelationshipCommand(request);
	}
	
	@Override
	protected ICommand getAfterCreateRelationshipCommand(CreateRelationshipRequest request) {
		ContentModellerPlugin.getDefault().log("getAfterCreateRelationshipCommand");
		return super.getAfterCreateRelationshipCommand(request);
	}
	
	@Override
	protected ICommand getAfterCreateCommand(CreateElementRequest request) {
		ContentModellerPlugin.getDefault().log("getAfterCreateCommand");
		return super.getAfterCreateCommand(request);
	}
	
	@Override
	protected ICommand getAfterSetCommand(SetRequest request) {
		ContentModellerPlugin.getDefault().log("getAfterSetCommand");
		return super.getAfterSetCommand(request);
	}
	
	@Override
	protected ICommand getAfterConfigureCommand(ConfigureRequest request) {
		
		final ConfigureRequest theRequest = request;
		
		return new ConfigureElementCommand(theRequest) {
			
			final Association association = (Association) theRequest.getElementToConfigure();
			
			@Override
			protected CommandResult doExecuteWithResult(IProgressMonitor arg0, IAdaptable arg1) throws ExecutionException {
				try {
					association.getMemberEnds().get(0).setAggregation(AggregationKind.SHARED_LITERAL);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return CommandResult.newOKCommandResult(association);
			}
		};
	}
	
	
	
}
