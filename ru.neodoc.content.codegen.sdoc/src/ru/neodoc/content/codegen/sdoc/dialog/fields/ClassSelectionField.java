package ru.neodoc.content.codegen.sdoc.dialog.fields;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

public class ClassSelectionField extends LabelTextButtonField {
	
	protected SelectionDialog selectionDialog;
	protected Shell shell;
	protected IRunnableContext runnableContext;
	
	protected boolean displayClassOnly = true;
	protected boolean considerEnums = false;
	
	public void create(Composite parent, Shell shell, IRunnableContext runnableContext, String... strings) {
		super.create(parent, strings);

		this.shell = shell;
		this.runnableContext = runnableContext;
		
		this.selectionDialog = createSelectionDialog();
		
		this.button.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if (selectionDialog.open() == Window.OK) {
					Object[] results = selectionDialog.getResult();
					if (results != null){
						IType selectedType = (IType)results[0];
						if (displayClassOnly)
							getTextField().setText(selectedType.getElementName());
						else
							getTextField().setText(
									selectedType.getFullyQualifiedName()
								);
					}					
				}
			}
		});
	}
	
	public boolean isDisplayClassOnly() {
		return displayClassOnly;
	}

	public void setDisplayClassOnly(boolean displayClassOnly) {
		this.displayClassOnly = displayClassOnly;
	}
	
	public boolean isConsiderEnums() {
		return considerEnums;
	}

	public void setConsiderEnums(boolean considerEnums) {
		this.considerEnums = considerEnums;
	}

	protected SelectionDialog createSelectionDialog(){
		SelectionDialog classSelectionDialog = null;
		int style = considerEnums
				?IJavaElementSearchConstants.CONSIDER_CLASSES_AND_ENUMS
				:IJavaElementSearchConstants.CONSIDER_CLASSES;
		try {
			classSelectionDialog = JavaUI.createTypeDialog(
					shell, 
					runnableContext, 
					SearchEngine.createWorkspaceScope(), 
					style, 
					false, 
					"");
		} catch (Exception e) {
			System.out.println(e);
		}	
		return classSelectionDialog;
	}
	
	
}
