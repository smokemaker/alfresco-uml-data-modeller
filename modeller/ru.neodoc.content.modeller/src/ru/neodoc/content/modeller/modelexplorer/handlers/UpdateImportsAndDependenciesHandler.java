package ru.neodoc.content.modeller.modelexplorer.handlers;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;
import ru.neodoc.content.modeller.utils.ImportsAndDependenciesUpdater;

public class UpdateImportsAndDependenciesHandler extends
		AbstractAlfrescoHandler {

	protected class UpdateImportsAndDependenciesCommand extends AbstractAlfrescoCommand {
		public UpdateImportsAndDependenciesCommand(IEvaluationContext context) {
			super (context, new UpdateImportsAndDependenciesRaunnable());
		}
	} 
	
	protected class UpdateImportsAndDependenciesRaunnable implements Runnable {
		@Override
		public void run() {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			ProgressMonitorDialog pmdialog = new ProgressMonitorDialog(shell);
			try {
				ImportsAndDependenciesUpdater updater = new ImportsAndDependenciesUpdater(getSelectedElement());
				pmdialog.run(false, true, updater);
			} catch (Exception e) {
				AlfrescoEditorPlugin.INSTANCE.log(e);
			}
		}
	}
	
	@Override
	protected Command getCommand(IEvaluationContext context) {
		return new UpdateImportsAndDependenciesCommand(context);
	}

}
