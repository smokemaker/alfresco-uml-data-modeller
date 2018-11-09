package ru.neodoc.content.modeller.palette;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.papyrus.uml.diagram.common.commands.ApplyStereotypeCommand;
import org.eclipse.papyrus.uml.diagram.common.service.ApplyStereotypeRequest;
import org.eclipse.uml2.uml.Element;

public class AspectEditHelperAdvice extends AbstractEditHelperAdvice {
	
	@Override
	public ICommand getBeforeEditCommand(IEditCommandRequest request) {
		ApplyStereotypeRequest rq = new ApplyStereotypeRequest(
				(Element)request.getElementsToEdit().get(0), 
				new ArrayList<String>(Arrays.asList("alfresco::aspect"))
				);
		ApplyStereotypeCommand command = new ApplyStereotypeCommand(request.getEditingDomain(), rq);
		return command;
	}
	@Override
	public boolean approveRequest(IEditCommandRequest request) {
		return true;
	}
}
