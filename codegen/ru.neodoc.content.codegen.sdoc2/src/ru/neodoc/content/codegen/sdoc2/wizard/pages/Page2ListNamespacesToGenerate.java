package ru.neodoc.content.codegen.sdoc2.wizard.pages;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import ru.neodoc.content.codegen.sdoc2.extension.SdocCodegenExtensionConfiguration;
import ru.neodoc.content.codegen.sdoc2.wizard.SdocCodegenWizardPage;
//import ru.neodoc.content.modeller.utils.uml.elements.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;

public class Page2ListNamespacesToGenerate extends SdocCodegenWizardPage {

	public final static String PAGE_NAME = "Page2ListNamespacesToGenerate";
	
	protected Button createJavaScript;
	protected Button createJava;
	
	protected List<Namespace> namespaces;
	
	protected Table table;

	public Page2ListNamespacesToGenerate(){
		this(Page2ListNamespacesToGenerate.PAGE_NAME);
	}
	
	public Page2ListNamespacesToGenerate(String pageName) {
		super(pageName);
		this.setTitle("Namespaces to generate");
	}

	@Override
	protected String getPageSectionName() {
		return "nslist";
	}
	
	@Override
	protected void doCreateControl(Composite parent) {
		GridLayout layout = new GridLayout();
        container.setLayout(layout);
        //layout.numColumns = 2;

        /*
         * CREATE SCROLLABLE TABLE
         */
        ScrolledComposite scrolled = new ScrolledComposite(container, SWT.NONE);
        scrolled.setLayout(new GridLayout());
        scrolled.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        table = new Table(scrolled, SWT.NO_SCROLL | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);
        
        scrolled.setContent(table);
        scrolled.setExpandHorizontal(true);
        scrolled.setExpandVertical(true);
        scrolled.setAlwaysShowScrollBars(true);
        scrolled.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        TableColumn prefixColumn = new TableColumn(table, SWT.NONE);
        prefixColumn.setText("Prefix");

        TableColumn uriColumn = new TableColumn(table, SWT.NONE);
        uriColumn.setText("URI");

        scrolled.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
/*        createJava = new Button(container, SWT.CHECK);
        createJava.setText("Create Java sources");
        createJava.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));
        createJava.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		Button b = (Button)e.getSource();
        		codegenManager.setGenerateJava(b.getSelection());
        		getWizard().getContainer().updateButtons();
        	}
		});
        
        createJavaScript = new Button(container, SWT.CHECK);
        createJavaScript.setText("Create JavaScript sources");
        createJavaScript.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));
        createJavaScript.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		Button b = (Button)e.getSource();
        		codegenManager.setGenerateJavaScript(b.getSelection());
        		getWizard().getContainer().updateButtons();
        	}
		});
        
*/       
        SelectionAdapter updater = new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		getWizard().getContainer().updateButtons();
        	}        	
		}; 
        		
        for (SdocCodegenExtensionConfiguration config: getCodegenManager().getConfigurations()) {
        	Control control = config.getControl(container);
        	if (control!=null) {
        		control.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));
        		if (control instanceof Button)
        			((Button)control).addSelectionListener(updater);
        	}
        }
    }
	
	@Override
	protected void internalRefresh() {
		super.internalRefresh();

        namespaces = getCodegenManager().getNamespacesToGenerate();
        
        Collections.sort(namespaces, new Comparator<Namespace>() {

			@Override
			public int compare(Namespace o1, Namespace o2) {
				return o1.getPrefix().compareToIgnoreCase(o2.getPrefix());
			}
		});

        table.removeAll();
        
        for (Namespace ns: namespaces) {
        	TableItem item = new TableItem(table, SWT.NONE);
        	item.setData(ns);
        	item.setText(0, ns.getPrefix());
        	item.setText(1, ns.getUri());
        }
        

        
        table.getColumn(0).pack();
        table.getColumn(1).pack();
		
		
/*		createJava.setSelection(codegenManagerOld.isGenerateJava());
        createJavaScript.setSelection(codegenManagerOld.isGenerateJavaScript());
*/	}
	
	@Override
	public boolean canFlipToNextPage() {
		return !getCodegenManager().getActiveConfigurations().isEmpty() && super.canFlipToNextPage();
	}
	
}
