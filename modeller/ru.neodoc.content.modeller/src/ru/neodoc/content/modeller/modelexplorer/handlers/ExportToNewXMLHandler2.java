package ru.neodoc.content.modeller.modelexplorer.handlers;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;

public class ExportToNewXMLHandler2 extends ExportToXMLHandler2 {
	
	@Override
	protected Command getCommand(IEvaluationContext context) {
		return new ExportToXMLCommand(context, true);
	}
}
