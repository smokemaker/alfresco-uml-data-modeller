package ru.neodoc.content.modeller.modelexplorer.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.infra.ui.command.AbstractCommandHandler;
// import org.eclipse.papyrus.views.modelexplorer.handler.AbstractCommandHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.common.edit.command.ChangeCommand;
import org.eclipse.uml2.uml.Package;

public abstract class AbstractAlfrescoHandler extends AbstractCommandHandler {

	protected EObject getExactSelectedElement() {
		EObject eObject = null;

		// Get current selection
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		Object selection = (activeWorkbenchWindow != null) ? activeWorkbenchWindow.getSelectionService().getSelection() : null;

		// Treat non-null selected object (try to adapt and return EObject)
		if(selection != null) {

			// Get first element if the selection is an IStructuredSelection
			if(selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection)selection;
				selection = structuredSelection.getFirstElement();
			}

			if(selection instanceof IAdaptable) {
				selection = ((IAdaptable)selection).getAdapter(EObject.class);
			}

			if (selection instanceof EObject)
				return (EObject) selection;
			
/*			Object businessObject = BusinessModelResolver.getInstance().getBusinessModel(selection);
			if(businessObject instanceof EObject) {
				eObject = (EObject)businessObject;
			}
*/		}

		return eObject;
	}
	
	
	public interface RunnableDialog extends Runnable {
		int getResultCode();
		Object getResult();
	}
	
	public abstract class AbstractAlfrescoCommand extends ChangeCommand {

		protected AbstractAlfrescoCommand(IEvaluationContext context, Runnable runnable){
			this(context, runnable, "LABEL", "DESC");
		}
		
		protected AbstractAlfrescoCommand(IEvaluationContext context, Runnable runnable, String label, String description) {
			super(AbstractAlfrescoHandler.this.getEditingDomain(context), runnable, label, description);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.common.command.AbstractCommand#canExecute()
		 * 
		 * @return
		 */
		@Override
		public boolean canExecute() {
			if(getSelectedElements().size() == 1) {
				return (getSelectedElement() instanceof Package);
			}
			return false;
		}
		
	} 
	
	public class BaseAlfrescoCommand extends AbstractAlfrescoCommand {
		public BaseAlfrescoCommand(IEvaluationContext context, Runnable runnable) {
			super(context, runnable);
		} 
	}
	
	public abstract class AbstractAlfrescoRunnable implements Runnable {
		
		protected Package selectedPackage;
		protected Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		protected IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		protected abstract class CommonDialog<ResultType> extends TitleAreaDialog implements RunnableDialog {
			
			protected ResultType result  = null;
			
			public CommonDialog() {
				super(shell);
			}

			protected int resultCode = 0;
			
			@Override
			public void run() {
				resultCode = this.open();
				
			}
			
			@Override
			public int getResultCode() {
				return resultCode;
			}
			
			@Override
			public Object getResult() {
				return result;
			}
			
			@Override
			protected void okPressed() {
				fillResult();
				String errorMessage = validateResult(); 
				if (errorMessage!=null)
					setErrorMessage(errorMessage);
				else
					super.okPressed();
			}
			
			protected abstract void fillResult();
			
			protected abstract String validateResult(); 
		}
		
		protected abstract class SimpleDialog extends CommonDialog<Map<String, Object>>{
			
			public SimpleDialog(){
				super();
				result = new HashMap<String, Object>();
			}
			
		}
		
		protected class Message implements RunnableDialog {

			private int _options = 0;
			private int _messageResult = 0;
			private Shell _shell = null;
			private String _text = null;
			private String _message = null;
			
			public Message() {
			}
			
			private boolean isValid(){
				return _shell!=null;
			}
			
			@Override
			public void run() {
				if (!isValid())
					return;
				MessageBox mb = new MessageBox(this._shell, 
						this._options);
				mb.setText(this._text);
				mb.setMessage(this._message);
				_messageResult = mb.open();
			}

			public Message options(int options){
				this._options = options;
				return this;
			}
			
			public Message shell(Shell shell){
				this._shell = shell;
				return this;
			}
			
			public Message text(String text){
				this._text = text;
				return this;
			}
			
			public Message message(String message){
				this._message = message;
				return this;
			}
			
			public int getMessageResult() {
				return _messageResult;
			}
			
			@Override
			public Object getResult() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getResultCode() {
				return getMessageResult();
			}
		}
		
		protected int errorMessage(String text, String message){
			return commonErrorMessage(text, message, 0);
		}

		protected int errorConfirmMessage(String text, String message){
			return commonErrorMessage(text, message, SWT.YES | SWT.NO);
		}
		
		protected int commonErrorMessage(String text, String message, int options){
			int optionsToSet = options==0?SWT.OK:options;
			
			Message m = (new Message())
					.options(SWT.ICON_ERROR | optionsToSet)
					.text(text)
					.message(message)
					.shell(shell);
			return openDialog(m);
		}
		
		protected int openDialog(RunnableDialog dialog){
			Display display = shell.getDisplay();
			display.syncExec(dialog);
			return dialog.getResultCode();
		}
		
		@Override
		public void run() {
			if (!preRun()) {
				processPreRunFail();
				return;
			}
			doRun();
			postRun();
		}
		
		protected boolean preRun(){
			EObject eObject = getSelectedElement();
			if (eObject == null || !(eObject instanceof Package))
				return false;
			
			selectedPackage = (Package) eObject;
			
			return true;
		}
		
		protected void processPreRunFail(){
			
		}
		
		protected abstract void doRun();
		
		protected void postRun(){
			
		}
	}

}
