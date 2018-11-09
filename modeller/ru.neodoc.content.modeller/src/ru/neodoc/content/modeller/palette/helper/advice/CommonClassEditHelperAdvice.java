package ru.neodoc.content.modeller.palette.helper.advice;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.UnexecutableCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.papyrus.infra.services.edit.utils.RequestParameterConstants;

public class CommonClassEditHelperAdvice extends AbstractEditHelperAdvice {

	protected static boolean stop = false;
	
	public static boolean stopExecution = false;
	
	@Override
	protected ICommand getBeforeEditContextCommand(GetEditContextRequest request) {
		// TODO Auto-generated method stub
		return super.getBeforeEditContextCommand(request);
	}
	
	@Override
	protected ICommand getBeforeCreateRelationshipCommand(CreateRelationshipRequest request) {
		if (Boolean.TRUE.equals(request.getParameter(CommandParameters.IS_ALFRESCO_RELATION)))
			request.setParameter(RequestParameterConstants.AFFECTS_TARGET, false);
		return super.getBeforeCreateRelationshipCommand(request);
	}
	
	@Override
	protected ICommand getAfterCreateRelationshipCommand(CreateRelationshipRequest request) {
		if (Boolean.TRUE.equals(request.getParameter(CommandParameters.IS_ALFRESCO_RELATION))) {
			if (!RelationshipHelper.checkStereotype(request))
				return UnexecutableCommand.INSTANCE;
/*			if ((request.getSource()!=null) && (request.getTarget()!=null)) {
				request.setParameter("breakExecution", Boolean.TRUE);
				request.setParameter("oldTarget", request.getTarget());
			}
*/		}
		return super.getAfterCreateRelationshipCommand(request);
	}
	
	
}
