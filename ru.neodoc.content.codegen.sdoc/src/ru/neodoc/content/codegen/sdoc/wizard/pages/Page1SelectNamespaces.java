package ru.neodoc.content.codegen.sdoc.wizard.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import ru.neodoc.content.codegen.sdoc.wizard.SdocCodegenWizardPage;
import ru.neodoc.content.modeller.utils.uml.elements.Namespace;

public class Page1SelectNamespaces extends SdocCodegenWizardPage {

	public final static String PAGE_NAME = "Page1SelectNamespaces";
	
	protected List<Namespace> namespaces = new ArrayList<>();
	protected Map<Namespace, Button> buttons = new HashMap<>();
	
	public Page1SelectNamespaces(){
		this(Page1SelectNamespaces.PAGE_NAME);
	}
	
	public Page1SelectNamespaces(String pageName) {
		super(pageName);
		this.setTitle("Select namespaces to generate");
	}

	@Override
	protected String getPageSectionName() {
		return "nsselection";
	}
	
	@Override
	public void doCreateControl(Composite parent) {
		
		GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 1;
        
        namespaces = codegenManager.getNamespacesAvailable();
        
        for (Namespace ns: namespaces) {
        	Button button = new Button(container, SWT.CHECK);
        	button.setText(ns.getPrefix() + " {" + ns.getUri() + "}");
        	button.setData(ns);
//        	button.setSelection(codegenManager.isNamespaceToGenerate(ns));
        	button.addSelectionListener(new SelectionAdapter() {
        		@Override
        		public void widgetSelected(SelectionEvent e) {
        			Button btn = (Button)e.getSource();
        			Namespace nmsp = (Namespace)btn.getData();
        			if (btn.getSelection()){
        				codegenManager.addToGenerate(nmsp);
        			} else {
        				codegenManager.removeFromGenerate(nmsp);
        			}
        			getWizard().getContainer().updateButtons();
        		}
			});
        	buttons.put(ns, button);
        }
        
	}
	
	@Override
	protected void internaleRefresh() {
		super.internaleRefresh();
        
		for (Namespace ns: namespaces)
			if (buttons.containsKey(ns))
				buttons.get(ns).setSelection(codegenManager.isNamespaceToGenerate(ns));
		
        container.pack();
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return codegenManager.hasNamespacesToGenerate();
	}
	
	@Override
	public IWizardPage getNextPage() {
		return pageDispatcher.getNextPage();
	}
}