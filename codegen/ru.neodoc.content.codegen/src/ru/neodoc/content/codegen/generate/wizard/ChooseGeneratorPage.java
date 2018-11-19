package ru.neodoc.content.codegen.generate.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import ru.neodoc.content.codegen.CodegenHelper;
import ru.neodoc.content.codegen.CodegenPlugin;
import ru.neodoc.content.codegen.SourceCodeGeneratorInfo;
import ru.neodoc.content.codegen.preferences.PreferenceConstants;
import ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizard;
import ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizardPageImpl;

public class ChooseGeneratorPage extends SourceCodeGeneratorWizardPageImpl {
	
	private Composite container;
	
	private Map<String, SourceCodeGeneratorInfo> acceptedGenerators = new HashMap<>();
	
	private SourceCodeGeneratorInfo currentGenerator = null;
	
	private Combo combo1;
	private Label generatorInfo;
	private List<String> generatorsIDList = new ArrayList<>();
	
	public ChooseGeneratorPage(){
		super("FirstPage");
		setTitle("Choose source code generator");
	}
	
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 2;
        
        Label label3 = new Label(container, SWT.NONE);
        label3.setText("Choose generator: ");
        
        combo1 = new Combo(container, SWT.READ_ONLY);
        
        generatorInfo = new Label(container, SWT.WRAP);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.widthHint = 500;
		gridData.heightHint = SWT.DEFAULT;
		gridData.horizontalSpan = 2;
//		gridData.verticalSpan = 4;
		generatorInfo.setLayoutData(gridData);
        
        combo1.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*String gId = // combo1.getText();
						generatorsIDList.get(combo1.getSelectionIndex());
				if (acceptedGenerators.containsKey(gId))
					currentGenerator = acceptedGenerators.get(gId);
				else
					currentGenerator = null;
				
				if (currentGenerator == null) {
					generatorInfo.setText("");
				} else {
					generatorInfo.setText("["
							+ currentGenerator.getGeneratorID() + "]  "
							+ currentGenerator.getDescription());
				}
				
				getWizard().getContainer().updateButtons();*/
				processGeneratorSelection();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        combo1.add("<NONE>");
        combo1.select(0);
        
        generatorsIDList.add(null);
        
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        combo1.setLayoutData(gd);
        
        List<SourceCodeGeneratorInfo> gi = CodegenHelper.getAvailableGenerators();
        acceptedGenerators.clear();
        for (SourceCodeGeneratorInfo scgi: gi) {
        	scgi.getGenerator().setSubject(codegenSubject);
        	if (scgi.getGenerator().isAccepatable()) {
        		acceptedGenerators.put(scgi.getGeneratorID(), scgi);
        		combo1.add(scgi.getGeneratorName());
        		// generatorsIDList.set(combo1.indexOf(scgi.getGeneratorName()), scgi.getGeneratorID());
        		generatorsIDList.add(scgi.getGeneratorID());
        	}
        }
        
		boolean chooseOnlyGenerator = CodegenPlugin.getBoolean(PreferenceConstants.P_USE_ONLY_GENERATOR);
		if (chooseOnlyGenerator && (generatorsIDList.size()==2)){
			combo1.select(1);
			processGeneratorSelection();
		}
        
		container.pack();
		
        // required to avoid an error in the system
        setControl(container);
        setPageComplete(false);
	}

	protected void processGeneratorSelection(){
		String gId = // combo1.getText();
				generatorsIDList.get(combo1.getSelectionIndex());
		if (acceptedGenerators.containsKey(gId))
			currentGenerator = acceptedGenerators.get(gId);
		else
			currentGenerator = null;
		
		if (currentGenerator == null) {
			generatorInfo.setText("");
		} else {
			generatorInfo.setText("["
					+ currentGenerator.getGeneratorID() + "]  "
					+ currentGenerator.getDescription());
		}
		
		getWizard().getContainer().updateButtons();	
	}
	
	@Override
	public IWizardPage getNextPage() {
		if (currentGenerator != null) {
			SourceCodeGeneratorWizard wizard = currentGenerator.getGenerator().getWizard();
			wizard.setWindowTitle(currentGenerator.getGeneratorName());
			wizard.addPages();
			return wizard.getStartPage();
		}
		return super.getNextPage();
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return this.currentGenerator!=null;
	}
	
	@Override
	public void nextPressed() {
		if (currentGenerator != null)
			currentGenerator.getGenerator().getWizard().setInitiatorPage(this);
		super.nextPressed();
	}
}
