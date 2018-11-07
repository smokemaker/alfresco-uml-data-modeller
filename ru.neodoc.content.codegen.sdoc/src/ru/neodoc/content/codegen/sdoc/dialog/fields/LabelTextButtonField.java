package ru.neodoc.content.codegen.sdoc.dialog.fields;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class LabelTextButtonField {

	protected Label label;
	protected Text text;
	protected Button button;
	
	public void create(Composite parent, String...strings){
		label = new Label(parent, SWT.NONE);
		
		text = new Text(parent, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		text.setLayoutData(gd);
		
		button = new Button(parent, SWT.PUSH);
		
		if (strings != null){
			int length = strings.length;
			if (length > 0)
				label.setText(strings[0]);
			if (length > 1)
				text.setText(strings[1]);
			if (length > 2)
				button.setText(strings[2]);
		}
	}

	
	
	public Label getLabelField() {
		return label;
	}

	public Text getTextField() {
		return text;
	}

	public Button getButtonField() {
		return button;
	}
	
	public String getText(){
		return text.getText();
	}
	
}
