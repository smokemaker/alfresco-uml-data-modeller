package ru.neodoc.content.codegen.sdoc2.extension.java.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ru.neodoc.content.codegen.sdoc2.SdocCodegenPlugin;
import ru.neodoc.content.codegen.sdoc2.dialog.fields.ClassSelectionField;
import ru.neodoc.content.codegen.sdoc2.extension.java.JavaCodegenManager;
import ru.neodoc.content.codegen.sdoc2.extension.java.WrapperJavaExtension;
import ru.neodoc.content.codegen.sdoc2.extension.java.annotation.AnnotationFactoryInfo;
import ru.neodoc.content.codegen.sdoc2.generator.annotation.SdocAnnotationFactory;
import ru.neodoc.content.codegen.sdoc2.wizard.SdocCodegenWizardPage;
import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;
import ru.neodoc.content.utils.CommonUtils;

public class Page2JavaSetImports extends SdocCodegenWizardPage {

	final public static String PAGE_NAME = "Page2JavaSetImports";
	
	final protected static String SEC_CLASSES = "classes";
	protected IDialogSettings settings;
	
	protected Map<String, ClassSelectionField> fields = new HashMap<>();
	protected Button ignoreAllCheckbox;
	protected List<Button> ignoreCheckboxes = new ArrayList<>();
	protected Map<String, Label> labels = new HashMap<>();
	
	protected GridLayout gl = null;
	protected Composite topContainer;
	protected Composite bottomContainer;

	private Combo annotationFactoryCombo;
	List<AnnotationFactoryInfo> annotationFactoryInfo = new ArrayList<>();
	
	public Page2JavaSetImports(String pageName) {
		super(pageName);
		setTitle("Set java import classes");
	}

	public Page2JavaSetImports(){
		this(PAGE_NAME);
	}
	
	@Override
	protected String getPageSectionName() {
		return "javaimports";
	}
	
	protected void processAnnotationFactorySelection() {
		int index = annotationFactoryCombo.getSelectionIndex();
		if (index<0)
			return;
		AnnotationFactoryInfo afi = annotationFactoryInfo.get(index);
		if (afi==null)
			return;
		SdocAnnotationFactory af = afi.getComponent();
		if (af==null)
			return;
		getConfiguration().setValue(SdocAnnotationFactory.PROP_NAME, af);
	}
	
	@Override
	protected void doCreateControl(Composite parent) {
		
		settings = getSubsection(SEC_CLASSES);
		
//		container.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		
		GridLayout mainGL = new GridLayout();
		mainGL.numColumns = 1;
		container.setLayout(mainGL);
		
		topContainer = new Composite(container, SWT.NONE);
		topContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		topContainer.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		GridLayout topGL = new GridLayout();
		topGL.numColumns = 2;
		topContainer.setLayout(topGL);
		
		Label annotationFactoryLabel = new Label(topContainer, SWT.NONE);
		annotationFactoryLabel.setText("Annotation factory:");
		
		annotationFactoryCombo = new Combo(topContainer, SWT.READ_ONLY);
		annotationFactoryCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		annotationFactoryCombo.addSelectionListener(
				
			new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					processAnnotationFactorySelection();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					
				}
			}
				
		);
		
		ignoreAllCheckbox = new Button(topContainer, SWT.CHECK);
		Label ignoreAllLabel = new Label(topContainer, SWT.NONE);
		ignoreAllLabel.setText("Ignore all imports");
		ignoreAllCheckbox.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (((Button)e.getSource()).getSelection())
					for (Button b: ignoreCheckboxes) {
						b.setSelection(false);
						updateIgnoreStatus(b);
					}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		bottomContainer = new Composite(container, SWT.NONE);
//		bottomContainer.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
//		GridData bottomGD = new GridData(SWT.FILL, SWT.FILL, true, true);
		//bottomContainer.setLayoutData(bottomGD);
	}
	
	protected void updateIgnoreStatus(Button b) {
		ClassSelectionField csf = (ClassSelectionField)b.getData();
		if (csf!=null) {
			csf.setEnabled(b.getSelection());
			NamespaceWrapper nw = (NamespaceWrapper)csf.getTextField().getData();
			WrapperJavaExtension wje = WrapperJavaExtension.get(nw);
			wje.setIgnored(!b.getSelection());
		}
	}
	
	@Override
	protected void internalRefresh() {
		
		JavaCodegenManager jcm = (JavaCodegenManager)getConfiguration().getValue(JavaCodegenManager.class.getName());
		annotationFactoryInfo = jcm.getAnnotationFactoryInfo();
		annotationFactoryCombo.removeAll();
		for (AnnotationFactoryInfo afi: annotationFactoryInfo)
			annotationFactoryCombo.add(afi.getId());
		
		if (annotationFactoryCombo.getItemCount()>0) {
			annotationFactoryCombo.select(0);
			processAnnotationFactorySelection();
		}
		
		gl = new GridLayout();
		gl.numColumns = 5;
		bottomContainer.setLayout(gl);
		
		super.internalRefresh();
		
		for (Label l: labels.values())
			l.dispose();
		labels.clear();
		
		for (ClassSelectionField csf: fields.values())
			csf.dispose();
		fields.clear();
		
		for (Button b: ignoreCheckboxes)
			b.dispose();
		ignoreCheckboxes.clear();
		
		for (NamespaceWrapper ns: getCodegenManager().getRequiredNamespacesWrapped()) {
			
			ClassSelectionField csf = new ClassSelectionField();

			Button b = new Button(bottomContainer, SWT.CHECK);
			b.setData(csf);
			b.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					Button b = (Button)e.getSource();
					updateIgnoreStatus(b);
					getWizard().getContainer().updateButtons();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			b.setSelection(true);
			ignoreCheckboxes.add(b);
			
			Label label = new Label(bottomContainer, SWT.NONE);
			label.setText(ns.getClassifiedWrappedElement().getPrefix());
			
			csf.setDisplayClassOnly(false);
			csf.setConsiderEnums(true);
			
			csf.create(bottomContainer, 
					getShell(), getWizard().getContainer(), 
					ns.getClassifiedWrappedElement().getUri(), "", "Select...");
			csf.getTextField().setData(ns);
			
			csf.getTextField().addModifyListener(new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent e) {
					String data = ((Text)e.getSource()).getText();
					NamespaceWrapper nsw = (NamespaceWrapper)((Text)e.getSource()).getData();
					WrapperJavaExtension wje = WrapperJavaExtension.get(nsw);
					
					if (data == null){
						wje.setTargetJavaName("");
						wje.setTargetJavaPackage("");
					} else {
						int index = data.lastIndexOf(".");
						if (index < 0) {
							wje.setTargetJavaName(data);
							wje.setTargetJavaPackage("");
						} else {
							wje.setTargetJavaName(data.substring(index+1));
							wje.setTargetJavaPackage(data.substring(0, index));
						}
							
					}
					String toStore = data==null?"":data;
					settings.put(nsw.getClassifiedWrappedElement().getPrefix(), toStore);
					SdocCodegenPlugin.getDefault().saveDialogSettings();
					getWizard().getContainer().updateButtons();
				}
			});
			
			String savedValue = settings.get(ns.getClassifiedWrappedElement().getPrefix());
			if (savedValue == null)
				savedValue = "";
			
			csf.getTextField().setText(savedValue);
			
			fields.put(ns.getClassifiedWrappedElement().getPrefix(), csf);
			labels.put(ns.getClassifiedWrappedElement().getPrefix(), label);
		}
/*		container.update();
		container.redraw();
*/		
		for (ClassSelectionField csf: fields.values())
			csf.pack();
		
		bottomContainer.pack();
		bottomContainer.setSize(bottomContainer.getSize().x+1, bottomContainer.getSize().y);
//		container.getParent().pack(true);
	}
	
	protected boolean importsDefined() {
		boolean result = true;
		for (NamespaceWrapper nsw: getCodegenManager().getRequiredNamespacesWrapped()) {
			WrapperJavaExtension wje = WrapperJavaExtension.get(nsw);
			result = result
					&& (
						(CommonUtils.isValueable(wje.getFinalJavaPackage())
						&& CommonUtils.isValueable(wje.getTargetJavaName())
						)
						|| wje.isIgnored());
		}
		return result;
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return importsDefined();
	}
}
