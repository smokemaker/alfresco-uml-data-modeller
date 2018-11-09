package ru.neodoc.content.codegen.handlers;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import ru.neodoc.content.codegen.generate.wizard.CodegenWizardDialog;
import ru.neodoc.content.codegen.generate.wizard.GenerateSourceCodeWizard;
import ru.neodoc.content.codegen.helpers.GenerateSourceCodeHelper;
import ru.neodoc.content.modeller.modelexplorer.handlers.AbstractAlfrescoHandler;

public class GenerateSourceCodeHandler extends AbstractAlfrescoHandler {

	@Override
	protected Command getCommand(IEvaluationContext context) {
		return new GenerateSourceCodeCommand(context);
	}
	
	protected interface ResultAcceptor {
		void setResult(boolean result);
	}
	
	public class GenerateSourceCodeCommand extends AbstractAlfrescoCommand 
			implements ResultAcceptor {
		
		protected boolean result = false;
		
		protected GenerateSourceCodeCommand(IEvaluationContext context, Runnable runnable, String label, String description) {
			super(context, runnable, label, description);
		}

		protected GenerateSourceCodeCommand(IEvaluationContext context, Runnable runnable) {
			super(context,runnable);
		}
		
		public GenerateSourceCodeCommand(IEvaluationContext context){
			super(context,
					new GenerateSourceCodeRunnable(),
					"LABEL",
					"DESC"
					);
		}
		@Override
		protected void doExecute() {
			((GenerateSourceCodeRunnable)getRunnable()).setAcceptor(this);
			super.doExecute();
			if (!result)
				throw new org.eclipse.emf.common.command.AbortExecutionException();
		}

		@Override
		public void setResult(boolean result) {
			this.result = result;
		}
	}	
	
	protected class GenerateSourceCodeRunnable implements Runnable {
		
		protected ResultAcceptor acceptor;
		
		public ResultAcceptor getAcceptor() {
			return acceptor;
		}

		public void setAcceptor(ResultAcceptor acceptor) {
			this.acceptor = acceptor;
		}

		@Override
		public void run() {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			EObject currentObject = getExactSelectedElement();
			Wizard w = new GenerateSourceCodeWizard(new GenerateSourceCodeHelper(currentObject));
			w.setWindowTitle("Generate source code");
			CodegenWizardDialog wd = new CodegenWizardDialog(shell, w);
			wd.setTitle("Generate source code");
			if (wd.open() == Window.OK) {
//                System.out.println("Ok pressed");
                acceptor.setResult(true);
			} else {
//                System.out.println("Cancel pressed");
                acceptor.setResult(false);
			}
		}
	}
	
}
