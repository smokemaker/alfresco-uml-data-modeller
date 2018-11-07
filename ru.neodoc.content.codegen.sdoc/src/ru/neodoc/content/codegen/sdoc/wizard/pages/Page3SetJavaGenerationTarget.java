package ru.neodoc.content.codegen.sdoc.wizard.pages;

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
import org.eclipse.swt.widgets.TreeItem;

import ru.neodoc.content.codegen.sdoc.dialog.SelectSourceFolderDialog;
import ru.neodoc.content.codegen.sdoc.dialog.SetNamespacePackageandClassDialog;
import ru.neodoc.content.codegen.sdoc.wizard.SdocCodegenWizardPage;
import ru.neodoc.content.codegen.sdoc.wizard.provider.BaseWrapperTreeContentProvider;
import ru.neodoc.content.codegen.sdoc.wizard.provider.JavaClassLabelProvider;
import ru.neodoc.content.codegen.sdoc.wizard.provider.NameLabelProvider;
import ru.neodoc.content.codegen.sdoc.wizard.provider.TitleLabelProvider;
import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.NamespaceWrapper;
import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.Property;

public class Page3SetJavaGenerationTarget extends SdocCodegenWizardPage {

	public final static String PAGE_NAME = "Page3SetGenerationTarget";
	
	protected final static String SETTING_SOURCE_FOLDER = "sourcefolder";
	protected final static String SET_CLASSES = "classes";
	
	protected final int TEXT_MARGIN = 2; 
	
	protected IPackageFragmentRoot selectedRoot = null;
	protected IDialogSettings settings;
	protected IDialogSettings classesSettings;
	
	protected List<? extends BaseWrapper> namespaces = new ArrayList<>();
	
	protected Label label;
	protected Text text1;
	protected TreeViewer treeViewer;
	
	public Page3SetJavaGenerationTarget(){
		this(PAGE_NAME);
		setTitle("Set java source generation parameters");
	}
	
	public Page3SetJavaGenerationTarget(String pageName) {
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
		
		namespaces = codegenManager.getWrappedNamespaceList();

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
					for (BaseWrapper bw: namespaces)
						bw.setTargetJavaLocation(text1.getText());
					
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
		
		BaseWrapperTreeContentProvider contentProvider = new BaseWrapperTreeContentProvider(codegenManager);
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
				
				SetNamespacePackageandClassDialog dialog = 
						new SetNamespacePackageandClassDialog(getShell(), namespace);
				dialog.setCallerPage(Page3SetJavaGenerationTarget.this);
				if (dialog.open() == Window.OK) {
					treeViewer.update(namespace, null);
					List<BaseWrapper> affectedChildren = new ArrayList<>();
					getAffectedObjects(namespace, affectedChildren);
					treeViewer.update((Object[])affectedChildren.toArray(), null);
					
					String toStore = namespace.getTargetJavaPackage();
					if (toStore == null) {
						toStore = "";
					} else {
						toStore += ".";
					}
					toStore += namespace.getTargetJavaName();
					classesSettings.put(namespace.getNamespace().getPrefix(), toStore);
					
					getWizard().getContainer().updateButtons();
				};
				
			}
		});
		
		// refresh();
	}	

	@Override
	protected void internaleRefresh() {
		super.internaleRefresh();

		settings = getPageSettings();
		classesSettings = getSubsection(SET_CLASSES);
		
		String savedValue = settings.get(SETTING_SOURCE_FOLDER);
		if (savedValue!=null)
			text1.setText(savedValue);
		
		for (BaseWrapper bw: namespaces)
			bw.setTargetJavaLocation(text1.getText());
		
		
		for (BaseWrapper bw: namespaces) {
			if (bw instanceof NamespaceWrapper){
				String prefix = ((NamespaceWrapper)bw).getNamespace().getPrefix();
				String storedValue = classesSettings.get(prefix);
				if (storedValue != null && storedValue.trim().length()>0) {
					storedValue = storedValue.trim();
					int index = storedValue.lastIndexOf(".");
					if (index>0) {
						bw.setTargetJavaName(storedValue.substring(index+1));
						bw.setTargetJavaPackage(storedValue.substring(0, index));
					} else {
						bw.setTargetJavaName(storedValue);
					}
				}
			}
		}
		treeViewer.setInput(namespaces);
		
		getWizard().getContainer().updateButtons();
	}
	
	protected void getAffectedObjects(BaseWrapper root, List<BaseWrapper> result) {
		for (BaseWrapper bw: root.getChildren()) {
			result.add(bw);
			getAffectedObjects(bw, result);
		}
	}
	
	protected void processClassElement(BaseClassElement bce, TreeItem classItem){
		List<Property> properties = bce.getProperties();
		for (Property prop: properties) {
			TreeItem propItem = new TreeItem(classItem, SWT.NONE);
			propItem.setText(0, prop.getName());
		}
		
	}
	
	protected boolean isValid(String s) {
		return (s!=null) && (s.length()>0);
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return this.codegenManager.targetsDefined();
	}
}
