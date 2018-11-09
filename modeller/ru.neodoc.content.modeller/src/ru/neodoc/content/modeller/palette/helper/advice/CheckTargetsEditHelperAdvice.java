package ru.neodoc.content.modeller.palette.helper.advice;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

public class CheckTargetsEditHelperAdvice extends AbstractEditHelperAdvice {

	@Override
	protected ICommand getBeforeCreateRelationshipCommand(CreateRelationshipRequest request) {
		
		return super.getBeforeCreateRelationshipCommand(request);
	}
	
}
