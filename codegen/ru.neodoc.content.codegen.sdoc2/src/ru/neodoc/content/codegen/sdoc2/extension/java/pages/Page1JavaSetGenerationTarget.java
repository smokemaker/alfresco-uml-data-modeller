package ru.neodoc.content.codegen.sdoc2.extension.java.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
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

import ru.neodoc.content.codegen.sdoc2.dialog.SelectSourceFolderDialog;
import ru.neodoc.content.codegen.sdoc2.extension.java.WrapperJavaExtension;
import ru.neodoc.content.codegen.sdoc2.extension.java.dialog.SetNamespacePackageandClassDialog;
import ru.neodoc.content.codegen.sdoc2.extension.java.pages.provider.AbstractWrapperTreeContentProvider;
import ru.neodoc.content.codegen.sdoc2.extension.java.pages.provider.JavaClassLabelProvider;
import ru.neodoc.content.codegen.sdoc2.extension.java.pages.provider.NameLabelProvider;
import ru.neodoc.content.codegen.sdoc2.extension.java.pages.provider.TitleLabelProvider;
import ru.neodoc.content.codegen.sdoc2.wizard.SdocCodegenWizardPage;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;
/*import ru.neodoc.content.codegen.sdoc2.wrap.old.BaseWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.old.NamespaceWrapper;
*//*import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.Property;
*/
public class Page1JavaSetGenerationTarget extends SdocCodegenWizardPage {

	public final static String PAGE_NAME = "Page1JavaSetGenerationTarget";
	
	protected final static String SETTING_SOURCE_FOLDER = "sourcefolder";
	protected final static String SET_CLASSES = "classes";
	
	protected final int TEXT_MARGIN = 2; 
	
	protected IPackageFragmentRoot selectedRoot = null;
	protected IDialogSettings settings;
	protected IDialogSettings classesSettings;
	
	protected List<? extends AbstractWrapper> namespaces = new ArrayList<>();
	
	protected Label label;
	protected Text text1;
	protected TreeViewer treeViewer;
	
	public Page1JavaSetGenerationTarget(){
		this(PAGE_NAME);
		setTitle("Set java source generation parameters");
	}
	
	public Page1JavaSetGenerationTarget(String pageName) {
		super(pageName);
	}

	@Override
	protected String getPageSectionName() {
		return "gentarget";
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
		
		Button button = new Button(container, SWT.PUSH);
		button.setText("Select ...");
		button.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				SelectSourceFolderDialog dialog = new SelectSourceFolderDialog(getShell());
				if (dialog.open() == Window.OK) {
					Object element= dialog.getFirstResult();
					if (element instanceof IJavaProject) {
						IJavaProject jproject= (IJavaProject)element;
						selectedRoot = jproject.getPackageFragmentRoot(jproject.getProject());
					} else if (element instanceof IPackageFragmentRoot) {
						selectedRoot = (IPackageFragmentRoot)element;
					}
					text1.setText(selectedRoot.getPath().toPortableString());
					for (AbstractWrapper aw: namespaces)
						WrapperJavaExtension.get(aw).setTargetJavaLocation(text1.getText());
					
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
		column.getColumn().setText("Java Class");
		column.getColumn().setWidth(500);
		column.setLabelProvider(JavaClassLabelProvider.getCellProvider());
		
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				Object selected = ((ITreeSelection)event.getSelection()).getFirstElement();
				if (selected == null)
					return;
				if (!(selected instanceof NamespaceWrapper))
					return;
				
				NamespaceWrapper namespace = (NamespaceWrapper) selected;
				WrapperJavaExtension namespaceExtension = WrapperJavaExtension.get(namespace);
				
				SetNamespacePackageandClassDialog dialog = 
						new SetNamespacePackageandClassDialog(getShell(), namespaceExtension);
				dialog.setCallerPage(Page1JavaSetGenerationTarget.this);
				if (dialog.open() == Window.OK) {
					treeViewer.update(namespace, null);
					List<AbstractWrapper> affectedChildren = new ArrayList<>();
					getAffectedObjects(namespace, affectedChildren);
					treeViewer.update((Object[])affectedChildren.toArray(), null);
					
					String toStore = namespaceExtension.getTargetJavaPackage();
					if (toStore == null) {
						toStore = "";
					} else {
						toStore += ".";
					}
					toStore += namespaceExtension.getTargetJavaName();
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

		namespaces = getCodegenManager().getWrappedNamespaceList();

		settings = getPageSettings();
		classesSettings = getSubsection(SET_CLASSES);
		
		String savedValue = settings.get(SETTING_SOURCE_FOLDER);
		if (savedValue!=null)
			text1.setText(savedValue);
		
		for (AbstractWrapper aw: namespaces)
			WrapperJavaExtension.get(aw).setTargetJavaLocation(text1.getText());
		
		
		for (AbstractWrapper aw: namespaces) {
			if (aw instanceof NamespaceWrapper){
				WrapperJavaExtension ext = WrapperJavaExtension.get(aw);
				String prefix = ((NamespaceWrapper)aw).getClassifiedWrappedElement().getPrefix();
				String storedValue = classesSettings.get(prefix);
				if (storedValue != null && storedValue.trim().length()>0) {
					storedValue = storedValue.trim();
					int index = storedValue.lastIndexOf(".");
					if (index>0) {
						ext.setTargetJavaName(storedValue.substring(index+1));
						ext.setTargetJavaPackage(storedValue.substring(0, index));
					} else {
						ext.setTargetJavaName(storedValue);
					}
				}
			}
		}
		treeViewer.setInput(namespaces);
		
		getWizard().getContainer().updateButtons();
	}
	
	protected void getAffectedObjects(AbstractWrapper root, List<AbstractWrapper> result) {
		for (AbstractWrapper aw: root.getChildren()) {
			result.add(aw);
			getAffectedObjects(aw, result);
		}
	}
	
/*	protected void processClassElement(BaseClassElement bce, TreeItem classItem){
		List<Property> properties = bce.getProperties();
		for (Property prop: properties) {
			TreeItem propItem = new TreeItem(classItem, SWT.NONE);
			propItem.setText(0, prop.getName());
		}
		
	}
*/	
/*	protected boolean isValid(String s) {
		return (s!=null) && (s.length()>0);
	}
*/	
	protected boolean targetsDefined() {
		boolean result = true; 
		for (AbstractWrapper aw: namespaces)
			result = result && WrapperJavaExtension.get(aw).isValid();
		return result;
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return targetsDefined();
	}
}
