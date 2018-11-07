package ru.neodoc.content.codegen.sdoc.dialog;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

import ru.neodoc.content.codegen.sdoc.dialog.fields.LabelTextButtonField;

public class SetPackageAndClassDialog extends TitleAreaDialog {

	protected LabelTextButtonField packageField;
	protected LabelTextButtonField classField;
	
	protected SelectionDialog classSelectionDialog = null;
	protected SelectionDialog packageSelectionDialog = null;
	
	protected IWizardPage callerPage = null;
	
	public IWizardPage getCallerPage() {
		return callerPage;
	}

	public void setCallerPage(IWizardPage callerPage) {
		this.callerPage = callerPage;
	}

	public SetPackageAndClassDialog(Shell parentShell) {
		super(parentShell);
		setHelpAvailable(false);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Define package and class name");
		setMessage("Define package and class name", IMessageProvider.INFORMATION);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite)super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		container.setLayout(gl);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		packageField = new LabelTextButtonField();
		packageField.create(container, "Target package", "", "Select...");

		packageField.getButtonField().addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if (packageSelectionDialog == null) {
					try {
						packageSelectionDialog = JavaUI.createPackageDialog(
								getShell(), 
								(callerPage==null?null:(callerPage.getWizard().getContainer())), 
								SearchEngine.createWorkspaceScope(), 
								true,
								false, 
								"");
					} catch (Exception e) {
						System.out.println(e);
					}
				}
				if (packageSelectionDialog != null){
					if (packageSelectionDialog.open() == Window.OK) {
						Object[] results = packageSelectionDialog.getResult();
						if (results != null){
							IPackageFragment selectedPackage = (IPackageFragment)results[0];
							packageField.getTextField().setText(selectedPackage.getElementName());
						}
					}
				}
				
			}
		});		
		
		classField = new LabelTextButtonField();
		classField.create(container, "Target class", "", "Select...");
		
		classField.getButtonField().addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if (classSelectionDialog == null) {
					try {
						classSelectionDialog = JavaUI.createTypeDialog(
								getShell(), 
								(callerPage==null?null:(callerPage.getWizard().getContainer())), 
								SearchEngine.createWorkspaceScope(), 
								 IJavaElementSearchConstants.CONSIDER_CLASSES, 
								false, 
								"");
					} catch (Exception e) {
						System.out.println(e);
					}
				}
				if (classSelectionDialog != null){
					if (classSelectionDialog.open() == Window.OK) {
						Object[] results = classSelectionDialog.getResult();
						if (results != null){
							IType selectedType = (IType)results[0];
							packageField.getTextField().setText(selectedType.getPackageFragment().getElementName());
							classField.getTextField().setText(selectedType.getElementName());
						}
					}
				}
				
			}
		});
		
		container.pack();
		
		return container;
	}
	
}
