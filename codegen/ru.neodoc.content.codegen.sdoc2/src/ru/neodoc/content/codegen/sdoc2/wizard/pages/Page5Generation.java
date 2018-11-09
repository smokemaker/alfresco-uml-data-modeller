package ru.neodoc.content.codegen.sdoc2.wizard.pages;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.IJobFunction;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import ru.neodoc.content.codegen.sdoc2.generator.GenerationStatusReporter;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerationManager;
import ru.neodoc.content.codegen.sdoc2.wizard.SdocCodegenWizardPage;

public class Page5Generation extends SdocCodegenWizardPage {

	final public static String PAGE_NAME = "Page5Generation";
	
	protected Text logText;
	protected ProgressBar progressBar;
	
	protected boolean isStarted = false;
	
	protected SdocGenerationManager generationManager;

	protected Job theJob;
	
	public Page5Generation(){
		this(PAGE_NAME);
	}
	
	protected Page5Generation(String pageName) {
		super(pageName);
		setTitle("Source code generation");
	}

	@Override
	protected String getPageSectionName() {
		// TODO Auto-generated method stub
		return "generate";
	}

	@Override
	protected void doCreateControl(Composite parent) {

		GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 1;
        
        /*final Text */logText = new Text(container, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
//        logText.setEnabled(false);
        logText.setEditable(false);
        Display display = Display.getCurrent();
        Color color = display.getSystemColor(SWT.COLOR_GRAY);
        logText.setBackground(color);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = SWT.DEFAULT;
		gridData.heightHint = SWT.DEFAULT;
		logText.setLayoutData(gridData);
		
/*		Button button = new Button(container, SWT.PUSH);
		button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		button.setText("Start");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runInContainer();
			}
		});
*/		
		
		/*final ProgressBar*/ progressBar = new ProgressBar(container, SWT.SMOOTH);
		GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData2.widthHint = SWT.DEFAULT;
		gridData2.heightHint = SWT.DEFAULT;
		progressBar.setLayoutData(gridData2);
		progressBar.setMaximum(1000);
		progressBar.setSelection(0);
		
		isStarted = true;
		
	}
	
	@Override
	protected void internalRefresh() {
		super.internalRefresh();
		generationManager = new SdocGenerationManager(getCodegenManager());
		generationManager.setStatusReporter(new Page5StatusReporter(false, true));
		
		theJob = Job.create("Generate sources", new IJobFunction() {
			
			@Override
			public IStatus run(IProgressMonitor monitor) {
				Page5Generation.this.generationManager.generate();
				return Status.OK_STATUS;
			}
		});
	}
	
	@Override
	public void refresh() {
		super.refresh();
		if (isControlCreated())
			if (generationManager.prepare()){
				theJob.schedule();
			}
	}
	
	protected void appendToLog(String value){
		appendToLog(value, false);
	}
	
	protected void appendToLog(final String value, boolean async) {
		if (async) {
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					logText.append(value);
				}
			});			
		} else {
			logText.append(value);
		}
	}
	
	protected void setProgress(int value){
		setProgress(value, false);
	}
	
	protected void setProgress(final int value, boolean async) {
		if (async) {
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					progressBar.setSelection(value);
				}
			});			
		} else {
			progressBar.setSelection(value);
		}
	}
	
	protected void runInContainer(){
		try {
			getWizard().getContainer().run(true, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						int globalTaskCount = 3;
						final SubMonitor globalMonitor = SubMonitor.convert(monitor, "Task 1", globalTaskCount);
						for (int task=1; task<=globalTaskCount; task++){
							
							final SubMonitor taskMonitor = globalMonitor.newChild(10);
							
							final int taskNumber = task;
							
							appendToLog("Starting tast: " + taskNumber + "\n", true);
							
							for (int i=0; i<10; i++) {
								Thread.sleep(1000);
								taskMonitor.internalWorked(1);
								final int j = i + 1;
								
								appendToLog("Subtask done: " + (j) + "\n", true);
								setProgress(
										Math.round(
												1000/globalTaskCount*(taskNumber-1)
												+ 1000/globalTaskCount/10*j), true
										);
								
							}

							appendToLog("Finished task: " + taskNumber + "\n", true);
							appendToLog("--- ======== ---\n", true);
							
						}
						
						monitor.done();
					} catch (Exception e) {
						System.out.println(e);
					}				
				}
			});
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		
	}
	
	protected void runTheTask(){
		
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				runInContainer();
				
			}
		});

	}
	
	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		super.setVisible(visible);
		
		if (!visible)
			return;
		
		if (isStarted)
			return;
		
		isStarted = true;
		
/*		container.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub

				System.out.println(e);
				
				if (isStarted)
					return;
				
				isStarted = true;*/
				
					
				/*}
			}
		);*/
		
	}
	
	@Override
	public boolean canFlipToNextPage() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isLastPage() {
		// TODO Auto-generated method stub
		return true;
	}
	
	protected class Page5StatusReporter implements GenerationStatusReporter {

		protected boolean useDirectAccess = true;
		protected boolean useIndirectAsync = false;
		
		
		public Page5StatusReporter(boolean direct, boolean async){
			super();
			this.useDirectAccess = direct;
			this.useIndirectAsync = async;
		}
		
		@Override
		public void log(final String message) {
			if (useDirectAccess) {
				Page5Generation.this.logText.append(message);
				return;
			}
			if (useIndirectAsync) {
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						Page5Generation.this.logText.append(message);
					}
				});
			} else {
				Display.getDefault().syncExec(new Runnable() {
					
					@Override
					public void run() {
						Page5Generation.this.logText.append(message);
					}
				});
				
			}
		}

		@Override
		public void logln(String message) {
			log (message + "\n");
		}

		@Override
		public void totalUnits(final int count) {
			if (useDirectAccess) {
				Page5Generation.this.progressBar.setMaximum(count);
				return;
			}
			if (useIndirectAsync) {
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						Page5Generation.this.progressBar.setMaximum(count);
					}
				});
			} else {
				Display.getDefault().syncExec(new Runnable() {
					
					@Override
					public void run() {
						Page5Generation.this.progressBar.setMaximum(count);
					}
				});
				
			}
			
		}

		@Override
		public void worked(final int count) {
			if (useDirectAccess) {
				Page5Generation.this.progressBar.setSelection(Page5Generation.this.progressBar.getSelection() + count);
				return;
			}
			if (useIndirectAsync) {
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						Page5Generation.this.progressBar.setSelection(Page5Generation.this.progressBar.getSelection() + count);
					}
				});
			} else {
				Display.getDefault().syncExec(new Runnable() {
					
					@Override
					public void run() {
						Page5Generation.this.progressBar.setSelection(Page5Generation.this.progressBar.getSelection() + count);
					}
				});
				
			}
		}
		
	}
	
}
