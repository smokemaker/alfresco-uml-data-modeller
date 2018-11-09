package ru.neodoc.content.codegen.sdoc2.extension.javascript.dialog;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ru.neodoc.content.codegen.sdoc2.dialog.fields.LabelTextButtonField;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.WrapperJSExtension;

public class SetNamespaceClassNameDialog extends TitleAreaDialog {

	protected WrapperJSExtension wrapperJSExtension = null;
	
	public SetNamespaceClassNameDialog(Shell parentShell, WrapperJSExtension wje) {
		super(parentShell);
		wrapperJSExtension = wje;
		setHelpAvailable(false);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}
	
	protected IWizardPage callerPage = null;
	
	protected Label label;
	protected Text text;
	
	public IWizardPage getCallerPage() {
		return callerPage;
	}

	public void setCallerPage(IWizardPage callerPage) {
		this.callerPage = callerPage;
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
		gl.numColumns = 2;
		container.setLayout(gl);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		label = new Label(container, SWT.NONE);
		
		text = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		text.setLayoutData(gd);
		text.setText(wrapperJSExtension.getTargetJSName());
		container.pack();
		
		return container;
	}
	
	@Override
	protected void okPressed() {
		wrapperJSExtension.setTargetJSName(text.getText());
		super.okPressed();
	}
}
