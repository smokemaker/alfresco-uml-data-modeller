package ru.neodoc.content.modeller.modelexplorer.handlers;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.papyrus.infra.ui.command.AbstractCommandHandler;
import org.eclipse.papyrus.infra.viewpoints.policy.PolicyChecker;
import org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.uml2.uml.Package;

public class Test2Handler extends AbstractCommandHandler {

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	protected EObject getSelectedElement(List<?> selection) {
		EObject eObject = null;

		// Treat non-null selected object (try to adapt and return EObject)
		if (!selection.isEmpty()) {

			// Get first element if the selection is an IStructuredSelection
			Object first = selection.get(0);

			EObject businessObject = EMFHelper.getEObject(first);
			if (businessObject != null) {
				eObject = businessObject;
			}
		}

		return eObject;
	}	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		List<?> selectionList = (selection instanceof IStructuredSelection) ? ((IStructuredSelection) selection).toList() : Collections.EMPTY_LIST;
		
		
		EObject owner = this.getSelectedElement(selectionList);
		
		if (!(owner instanceof Package))
			return null;
		
		Package pack = (Package) owner;
		
		ViewPrototype targetProto = null;
		
		for (final ViewPrototype proto : PolicyChecker.getFor(owner).getPrototypesFor(owner)) {
			/*if (proto.getConfiguration() instanceof PapyrusDiagram) {
				if ("Package Diagram".equals(proto.getConfiguration().getName())){
					targetProto = proto;
					break;
				}
			}*/
		}
		
		if (targetProto != null) {
			targetProto.instantiateOn(owner, pack.getName());
		}
		
		return null;
	}
	
	protected boolean computeEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	protected Command getCommand(IEvaluationContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
