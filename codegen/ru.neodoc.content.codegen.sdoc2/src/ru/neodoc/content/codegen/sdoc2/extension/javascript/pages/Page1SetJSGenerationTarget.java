package ru.neodoc.content.codegen.sdoc2.extension.javascript.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import ru.neodoc.content.codegen.sdoc2.extension.java.WrapperJavaExtension;
import ru.neodoc.content.codegen.sdoc2.extension.java.pages.provider.AbstractWrapperTreeContentProvider;
import ru.neodoc.content.codegen.sdoc2.extension.java.pages.provider.NameLabelProvider;
import ru.neodoc.content.codegen.sdoc2.extension.java.pages.provider.TitleLabelProvider;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.WrapperJSExtension;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.dialog.SetNamespaceClassNameDialog;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.dialog.SetNamespacePackageandClassDialog;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.pages.provider.JavaScriptNameLabelProvider;
import ru.neodoc.content.codegen.sdoc2.wizard.SdocCodegenWizardPage;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;
/*import ru.neodoc.content.codegen.sdoc2.wizard.provider.BaseWrapperTreeContentProvider;
import ru.neodoc.content.codegen.sdoc2.wizard.provider.JavaScriptNameLabelProvider;
import ru.neodoc.content.codegen.sdoc2.wizard.provider.NameLabelProvider;
import ru.neodoc.content.codegen.sdoc2.wizard.provider.TitleLabelProvider;
import ru.neodoc.content.codegen.sdoc2.wrap.old.BaseWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.old.NamespaceWrapper;
import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.Property;
*/
public class Page1SetJSGenerationTarget extends SdocCodegenWizardPage {

	public final static String PAGE_NAME = "Page6SetJavascriptGenerationTarget";

	protected final static String SETTING_SOURCE_FOLDER = "sourcefolder";
	protected final static String SET_CLASSES = "classes";
	
	protected Label label;
	protected Text text1;

	protected TreeViewer treeViewer;
	
	protected List<NamespaceWrapper> namespaces = new ArrayList<>();

	protected IDialogSettings settings;
	protected IDialogSettings classesSettings;
	
	public Page1SetJSGenerationTarget(){
		this(PAGE_NAME);
		setTitle("Set JavaScript source generation parameters");
	}
	
	protected Page1SetJSGenerationTarget(String pageName) {
		super(pageName);
	}

	@Override
	protected String getPageSectionName() {
		return "gentargetjs";
	}

	@Override
	protected void doCreateControl(Composite parent) {
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		container.setLayout(gl);

		
		label = new Label(container, SWT.NONE);
		label.setText("Source folder");
		
		text1 = new Text(container, SWT.BORDER | SWT.SINGLE);
		text1.setText("");
		text1.setEnabled(false);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		text1.setLayoutData(gd);
		
		namespaces = getCodegenManager().getWrappedNamespaceList();

		Button button = new Button(container, SWT.PUSH);
		button.setText("Select ...");
		button.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), null, false, "Select folder for js file");
				if (dialog.open() == Window.OK) {
					Object[] result= dialog.getResult();
					if (result.length>0){
						text1.setText(((Path)result[0]).toString());
					}
					for (NamespaceWrapper bw: namespaces)
						WrapperJSExtension.get(bw).setTargetJSLocation(text1.getText());
					
					settings.put(SETTING_SOURCE_FOLDER, text1.getText());
					
					getWizard().getContainer().updateButtons();
				}
			}
		});

		treeViewer = new TreeViewer(container, 
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		GridData treeGD = new GridData();
		treeGD.verticalAlignment = GridData.FILL;
		treeGD.horizontalAlignment = GridData.FILL;
		treeGD.horizontalSpan = 3;
		treeGD.grabExcessHorizontalSpace = true;
		treeGD.grabExcessVerticalSpace = true;
		treeViewer.getTree().setLayoutData(treeGD);
		
		treeViewer.getTree().setHeaderVisible(true);
		treeViewer.getTree().setLinesVisible(false);
		
		AbstractWrapperTreeContentProvider contentProvider = new AbstractWrapperTreeContentProvider(getCodegenManager());
		// contentProvider.setRootElements(namespaces);
		treeViewer.setContentProvider(contentProvider);
		
		TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.LEFT);
		column.getColumn().setText("Name");
		column.getColumn().setWidth(200);
		column.setLabelProvider(NameLabelProvider.getCellProvider());
		
		column = new TreeViewerColumn(treeViewer, SWT.LEFT);
		column.getColumn().setText("Title");
		column.getColumn().setWidth(250);
		column.setLabelProvider(TitleLabelProvider.getCellProvider());
		
		column = new TreeViewerColumn(treeViewer, SWT.LEFT);
		column.getColumn().setText("JavaScript name");
		column.getColumn().setWidth(500);
		column.setLabelProvider(JavaScriptNameLabelProvider.getCellProvider());
		
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Object selected = ((ITreeSelection)event.getSelection()).getFirstElement();
				if (selected == null)
					return;
				if (!(selected instanceof NamespaceWrapper))
					return;
				
				NamespaceWrapper namespace = (NamespaceWrapper) selected;
				WrapperJSExtension wje = WrapperJSExtension.get(namespace);
				
				SetNamespaceClassNameDialog dialog = 
						new SetNamespaceClassNameDialog(getShell(), wje);
				dialog.setCallerPage(Page1SetJSGenerationTarget.this);
				if (dialog.open() == Window.OK) {
					treeViewer.update(namespace, null);
					List<AbstractWrapper> affectedChildren = new ArrayList<>();
					getAffectedObjects(namespace, affectedChildren);
					treeViewer.update((Object[])affectedChildren.toArray(), null);
					
					String toStore = wje.getTargetJSName();
					if (toStore == null) {
						toStore = "";
					};
					classesSettings.put(namespace.getClassifiedWrappedElement().getPrefix(), toStore);
					
					getWizard().getContainer().updateButtons();
				};
				
			}
		});
		
		// refresh();
	}	

	@Override
	protected void internalRefresh() {
		super.internalRefresh();

		settings = getPageSettings();
		classesSettings = getSubsection(SET_CLASSES);
		
		String savedValue = settings.get(SETTING_SOURCE_FOLDER);
		if (savedValue!=null)
			text1.setText(savedValue);
		
		for (NamespaceWrapper bw: namespaces)
			WrapperJSExtension.get(bw).setTargetJSLocation(text1.getText());
		
		
		for (NamespaceWrapper bw: namespaces) {
			String prefix = bw.getClassifiedWrappedElement().getPrefix();
			String storedValue = classesSettings.get(prefix);
			if (storedValue != null && storedValue.trim().length()>0) {
				storedValue = storedValue.trim();
				int index = storedValue.lastIndexOf(".");
				if (index>0) {
					WrapperJSExtension.get(bw).setTargetJSName(storedValue.substring(index+1));
				} else {
					WrapperJSExtension.get(bw).setTargetJSName(storedValue);
				}
			}
		}
		treeViewer.setInput(namespaces);
		
		getWizard().getContainer().updateButtons();
	}
	
	protected void getAffectedObjects(AbstractWrapper root, List<AbstractWrapper> result) {
		for (AbstractWrapper bw: root.getChildren()) {
			result.add(bw);
			getAffectedObjects(bw, result);
		}
	}
	
	protected boolean isValid(String s) {
		return (s!=null) && (s.length()>0);
	}
	
	protected boolean targetsDefined() {
		boolean result = true; 
		for (NamespaceWrapper aw: namespaces)
			result = result && WrapperJSExtension.get(aw).isValid();
		return result;
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return targetsDefined();
	}
	
}
