package ru.neodoc.content.codegen.sdoc2.extension.javascript.dialog;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import ru.neodoc.content.codegen.sdoc2.dialog.SetPackageAndClassDialog;
import ru.neodoc.content.codegen.sdoc2.extension.java.WrapperJavaExtension;

public class SetNamespacePackageandClassDialog extends SetPackageAndClassDialog {

	protected WrapperJavaExtension namespace;
	
	public SetNamespacePackageandClassDialog(Shell parentShell, WrapperJavaExtension namespace) {
		super(parentShell);
		this.namespace = namespace;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control toReturn = super.createDialogArea(parent);
		
		packageField.getTextField().setText(namespace.getTargetJavaPackage()==null?"":namespace.getTargetJavaPackage());
		classField.getTextField().setText(namespace.getTargetJavaName()==null?"":namespace.getTargetJavaName());
		
		return toReturn;
	}
	
	@Override
	protected void okPressed() {
		namespace.setTargetJavaPackage(packageField.getText());
		namespace.setTargetJavaName(classField.getText());
		super.okPressed();
	}
	
}
