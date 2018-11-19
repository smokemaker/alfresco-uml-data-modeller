package ru.neodoc.content.modeller.tasks;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public abstract class RunnableExecutionContainer implements IRunnableWithProgress {

	protected Shell parentShell;
	protected IProgressMonitor rootMonitor;
	protected int messageResult;


	public void setParentShell(Shell parentShell) {
		this.parentShell = parentShell;
	}

	protected abstract Executor getExecutor();
	
	/** PROCESSING **/
	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		
		Executor executor = getExecutor();
		
		this.rootMonitor = monitor;
		TaskDescriptor rootTask = executor.getRootTask();
		executor.prepare(rootTask);
		
		monitor.beginTask(rootTask.name, rootTask.totalCount);
		
		Display display = this.parentShell.getDisplay();
		
		for (SubtaskDescriptor subtask: rootTask.subtasks) {
			executor.prepare(subtask);
			
			final SubMonitor subMonitor = SubMonitor.convert(this.rootMonitor, subtask.relativeCount);
			subMonitor.beginTask(subtask.name, subtask.totalCount);
			
			ExecutionResult result = executor.execute(
					subtask,
					new ExecutionCallback() {
						
						@Override
						public void worked(int amount) {
							subMonitor.worked(amount);
						}
						
						@Override
						public void done() {
							subMonitor.done();
						}
					});
			
			if (!result.isOk()) {
				display.syncExec(
						new Runnable() {
					        public void run() {
					          MessageBox mb = new MessageBox(RunnableExecutionContainer.this.parentShell, 
					        		  result.isIgnorable()
					        		  	?SWT.ICON_ERROR | SWT.YES | SWT.NO
					        		  	:SWT.ICON_ERROR | SWT.OK);
					          mb.setText(result.getMessage());
					          String description = result.getDescription();
					          if (result.isIgnorable())
					        	  description += "\n Would you like to ignore it and continue?";
					          mb.setMessage(description);
					          RunnableExecutionContainer.this.messageResult = mb.open();
					        };
						}
			    );
				if (!result.isIgnorable() || (this.messageResult!=SWT.YES))
					return;
			}
			
		}
		
	}

}
