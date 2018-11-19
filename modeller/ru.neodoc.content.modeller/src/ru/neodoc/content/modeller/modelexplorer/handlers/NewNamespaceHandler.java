package ru.neodoc.content.modeller.modelexplorer.handlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.content.utils.uml.search.filter.SearchFilterFactory;
import ru.neodoc.content.utils.uml.search.helper.UMLSearchHelper;

public class NewNamespaceHandler extends AbstractAlfrescoHandler {

	public class NewNamespaceRunnable extends AbstractAlfrescoRunnable {

		public class NewNamespaceDialog extends SimpleDialog {

			protected org.eclipse.uml2.uml.Package selectedPackage = null;
			protected org.eclipse.uml2.uml.Package umlRoot = null;
			
			protected Text txtPrefix;
			protected Text txtUri;
			
			protected String dlgTitle = "";
			protected String dlgMessage = null;
			
			public NewNamespaceDialog(org.eclipse.uml2.uml.Package selectedPackage){
				super();
				this.selectedPackage = selectedPackage;
				this.umlRoot = AlfrescoUMLUtils.getUMLRoot(this.selectedPackage);
			}

			public void setDlgTitle(String dlgTitle) {
				this.dlgTitle = dlgTitle;
			}

			public void setDlgMessage(String dlgMessage) {
				this.dlgMessage = dlgMessage;
			}

			@Override
			public void create() {
				super.create();
				setTitle(dlgTitle);
				setMessage(dlgMessage);
			}
			
			@Override
			protected Control createDialogArea(Composite parent) {
				Composite area = (Composite)super.createDialogArea(parent);

				Composite container = new Composite(area, SWT.NONE);
			    container.setLayoutData(new GridData(GridData.FILL_BOTH));
			    GridLayout layout = new GridLayout(2, false);
			    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			    container.setLayout(layout);
				
			    createFields(container);
			    
				return area;
			}
			
			protected void createFields(Composite container){
				txtPrefix = createTextField(container, "Prefix");
				txtUri = createTextField(container, "URI");
			}
			
			protected Text createTextField(Composite container, String label){
				return createTextField(container, label, null);
			}
			protected Text createTextField(Composite container, String label, Object defaultValue){
			    Label lbl = new Label(container, SWT.NONE);
			    lbl.setText(label);

			    GridData dataFirstName = new GridData();
			    dataFirstName.grabExcessHorizontalSpace = true;
			    dataFirstName.horizontalAlignment = GridData.FILL;

			    Text txt = new Text(container, SWT.BORDER);
			    txt.setLayoutData(dataFirstName);
			    if (defaultValue!=null)
			    	txt.setText((String)defaultValue);
			    
			    return txt;
			}
			
			@Override
			protected void fillResult() {
				result.put("prefix", txtPrefix.getText());
				result.put("uri", txtUri.getText());
			}

			@Override
			protected String validateResult() {
				String vr = null;
				
				try {
					/*URI u = */new URI(txtUri.getText());
				} catch (URISyntaxException use) {
					vr = "Malformed URI \n";
				}
				
				
				UMLSearchHelper<Package, Package> sh = AlfrescoSearchHelperFactory
					.getNamespaceSearcher()
					.filter(SearchFilterFactory.elementByName(txtPrefix.getText()))
					.startWith(umlRoot);
				
				Package found = sh.first(); 
				if (found!=null) {
					if (vr==null)
						vr = "";
					Package model = AlfrescoUMLUtils.getNearestModel(found);
					vr += "Prefix already exists at " + 
							(model!=null?"model " + model.getName():"unknown model");
				}
				
				return vr;
			}
		}

		protected Map<String, Object> creationInfo = null;
		
		@SuppressWarnings("unchecked")
		@Override
		protected boolean preRun() {
			super.preRun();
			
			NewNamespaceDialog dialog = createDialog();
			openDialog(dialog);
			if (dialog.getResultCode()==Window.CANCEL)
				return false;
			
			creationInfo = (Map<String, Object>)dialog.getResult();
			
			return true;
		}
		
		protected NewNamespaceDialog createDialog(){
			NewNamespaceDialog d = new NewNamespaceDialog(selectedPackage);
			d.setDlgTitle("Create new Namespace");
			d.setDlgMessage("Fill in new namespace info");
			return d;
		}
		
		@Override
		protected void doRun() {
			String nsPrefix = (String)creationInfo.get("prefix");
			Package ns = AlfrescoUMLUtils.createNamespace(selectedPackage, nsPrefix);
			if (ns!=null)
				ns.setURI((String)creationInfo.get("uri"));
		}
	}
	
	@Override
	protected Command getCommand(IEvaluationContext context) {
		return new BaseAlfrescoCommand(context, new NewNamespaceRunnable());
	}

}
