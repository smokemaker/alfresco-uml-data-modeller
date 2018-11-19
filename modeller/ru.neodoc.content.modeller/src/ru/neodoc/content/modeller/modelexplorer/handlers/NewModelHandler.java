package ru.neodoc.content.modeller.modelexplorer.handlers;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.content.utils.uml.search.filter.SearchFilterFactory;
import ru.neodoc.content.utils.uml.search.helper.UMLSearchHelper;

public class NewModelHandler extends NewNamespaceHandler {

	public class NewModelRunnable extends NewNamespaceRunnable {
		
		public class NewModelDialog extends NewNamespaceDialog {

			public NewModelDialog(Package selectedPackage) {
				super(selectedPackage);
			}
			
			protected Text txtName;
			protected Text txtDescription;
			protected Text txtVersion;
			protected Text txtAuthor;
			
			@Override
			protected void createFields(Composite container) {
				txtName = createTextField(container, "Name");
				
				super.createFields(container);
				
				txtVersion = createTextField(container, "Version", "1.0");
				txtAuthor = createTextField(container, "Author");
				txtDescription = createTextField(container, "Description");
			}
			
			@Override
			protected void fillResult() {
				super.fillResult();
				result.put("name", txtName.getText());
				result.put("description", txtDescription.getText());
				result.put("version", txtVersion.getText());
				result.put("author", txtAuthor.getText());
			}
			
			@Override
			protected String validateResult() {
				String vr = null;
				String fullName = txtPrefix.getText()+":"+txtName.getText();
				
				UMLSearchHelper<Package, Package> sh = AlfrescoSearchHelperFactory
						.getModelSearcher()
						.filter(SearchFilterFactory.elementByName(fullName))
						.startWith(umlRoot);
				
				Package found = sh.first();
				if (found!=null)
					vr = "Model " + fullName + " already exists\n";
				
				String vr1 = super.validateResult(); 
				
				if (vr1!=null) {
					if (vr==null)
						vr = "";
					vr += vr1;
				}
				
				return vr; 
			}
		} 
		
		@Override
		protected NewNamespaceDialog createDialog() {
			NewModelDialog dialog = new NewModelDialog(selectedPackage);
			dialog.setDlgTitle("Create new model");
			dialog.setDlgMessage("Fill in new model info");
			return dialog;
		}
		
		@Override
		protected void doRun() {
			Package model = AlfrescoUMLUtils.createModel(
					selectedPackage, 
					(String)creationInfo.get("prefix") + ":" + (String)creationInfo.get("name"));
			Model theModel = Model._HELPER.getFor(model);
/*			AlfrescoUMLUtils.setModelValue(
					model, AlfrescoProfile.ForPackage.Model.DESCRIPTION, creationInfo.get("description"));
*/			theModel.setDescription((String)creationInfo.get("description"));
/*			AlfrescoUMLUtils.setModelValue(
					model, AlfrescoProfile.ForPackage.Model.VERSION, creationInfo.get("version"));
*/			theModel.setVersion((String)creationInfo.get("version"));
/*			AlfrescoUMLUtils.setModelValue(
					model, AlfrescoProfile.ForPackage.Model.AUTHOR, creationInfo.get("author"));
*/			theModel.setAuthor((String)creationInfo.get("author"));			

			selectedPackage = model;
			super.doRun();
		}
	}
	
	@Override
	protected Command getCommand(IEvaluationContext context) {
		return new BaseAlfrescoCommand(context, new NewModelRunnable());
	}

}
