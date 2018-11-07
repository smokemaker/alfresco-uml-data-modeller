package ru.neodoc.content.codegen.sdoc.wizard.pages;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ru.neodoc.content.codegen.sdoc.SdocCodegenPlugin;
import ru.neodoc.content.codegen.sdoc.dialog.fields.ClassSelectionField;
import ru.neodoc.content.codegen.sdoc.wizard.SdocCodegenWizardPage;
import ru.neodoc.content.codegen.sdoc.wrap.NamespaceWrapper;

public class Page4SetJavaImports extends SdocCodegenWizardPage {

	final public static String PAGE_NAME = "Page4SetJavaImports";
	
	final protected static String SEC_CLASSES = "classes";
	protected IDialogSettings settings;
	
	protected Map<String, ClassSelectionField> fields = new HashMap<>();
	
	public Page4SetJavaImports(String pageName) {
		super(pageName);
		setTitle("Set java import classes");
	}

	public Page4SetJavaImports(){
		this(PAGE_NAME);
	}
	
	@Override
	protected String getPageSectionName() {
		return "javaimports";
	}
	
	@Override
	protected void doCreateControl(Composite parent) {
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 4;
		container.setLayout(gl);
		
		settings = getSubsection(SEC_CLASSES);
		
	}
	
	@Override
	protected void internaleRefresh() {
		super.internaleRefresh();
		for (NamespaceWrapper ns: codegenManager.getWrappedImportedNamespaces()) {
			
			Label label = new Label(container, SWT.NONE);
			label.setText(ns.getNamespace().getPrefix());
			
			ClassSelectionField csf = new ClassSelectionField();
			
			csf.setDisplayClassOnly(false);
			csf.setConsiderEnums(true);
			
			csf.create(container, 
					getShell(), getWizard().getContainer(), 
					ns.getNamespace().getUri(), "", "Select...");
			csf.getTextField().setData(ns);
			
			csf.getTextField().addModifyListener(new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent e) {
					String data = ((Text)e.getSource()).getText();
					NamespaceWrapper nsw = (NamespaceWrapper)((Text)e.getSource()).getData();
					
					if (data == null){
						nsw.setTargetJavaName("");
						nsw.setTargetJavaPackage("");
					} else {
						int index = data.lastIndexOf(".");
						if (index < 0) {
							nsw.setTargetJavaName(data);
							nsw.setTargetJavaPackage("");
						} else {
							nsw.setTargetJavaName(data.substring(index+1));
							nsw.setTargetJavaPackage(data.substring(0, index));
						}
							
					}
					String toStore = data==null?"":data;
					settings.put(nsw.getNamespace().getPrefix(), toStore);
					SdocCodegenPlugin.getDefault().saveDialogSettings();
					getWizard().getContainer().updateButtons();
				}
			});
			
			String savedValue = settings.get(ns.getNamespace().getPrefix());
			if (savedValue == null)
				savedValue = "";
			
			csf.getTextField().setText(savedValue);
			
			fields.put(ns.getNamespace().getPrefix(), csf);
			
		}
		
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return codegenManager.importsDefined();
	}
}
