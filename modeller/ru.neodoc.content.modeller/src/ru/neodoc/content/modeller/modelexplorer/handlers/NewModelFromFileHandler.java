package ru.neodoc.content.modeller.modelexplorer.handlers;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Namespaces.Namespace;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.command.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;
import ru.neodoc.content.modeller.ui.dialogs.NS2EProgressMonitorDialog;
import ru.neodoc.content.modeller.utils.ImportsAndDependenciesUpdater;
import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.modeller.utils.JaxbUtils.JaxbHelper;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.xml2uml.XML2UMLGenerator;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.PrefixedName;

public class NewModelFromFileHandler extends NewModelHandler {

	public class NewModelFromFileRunnable extends NewModelRunnable {
		
		public class NewModelFromFileDialog extends NewModelDialog {

			protected Model initialModel = null; 
			
			public NewModelFromFileDialog(Package selectedPackage) {
				super(selectedPackage);
			}
			
			public NewModelFromFileDialog(Package selectedPackage, Model model) {
				super(selectedPackage);
				this.initialModel = model;
			}
			
			@Override
			protected void createFields(Composite container) {
				super.createFields(container);
				if (initialModel!=null) {
					loadData(initialModel);
					disableControls();
				}
			}
			
			public void loadData(Model model) {
				this.txtAuthor.setText(model.getAuthor());
				this.txtDescription.setText(model.getDescription());
				PrefixedName prefixedName = new PrefixedName(model.getName());
				this.txtName.setText(prefixedName.getName());
				this.txtPrefix.setText(prefixedName.getPrefix());
				for (Namespace namespace: model.getNamespaces().getNamespace())
					if (namespace.getPrefix()!=null)
						if (namespace.getPrefix().equals(prefixedName.getPrefix())) {
							this.txtUri.setText(namespace.getUri());
							break;
						}
				this.txtVersion.setText(model.getVersion());				
			}
			
			public void disableControls() {
				this.txtAuthor.setEnabled(false);
				this.txtDescription.setEnabled(false);
				this.txtName.setEnabled(false);
				this.txtPrefix.setEnabled(false);
				this.txtUri.setEnabled(false);
				this.txtVersion.setEnabled(false);
			}
			
		}
		
		protected JaxbHelper<Model> jaxbHelper = null;
		
		@Override
		protected boolean preRun() {
			
			IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
			ResourceListSelectionDialog selectionDialog = new ResourceListSelectionDialog(this.shell, workspaceRoot, IResource.FILE){
				
				@Override
				protected Control createDialogArea(Composite parent) {
					Control result = super.createDialogArea(parent);
					Control[] children = ((Composite)result).getChildren();
					Text pattern = null;
					for (int i = 0; i < children.length; i++) {
						Control control = children[i];
						if (control instanceof Text) {
							pattern = (Text)control;
							continue;
						}
						if ((control instanceof Label) && (pattern!=null)) {
							pattern.setText("*.xml");
							//pattern.setEnabled(false);
							adjustPattern();
							break;
						}
					}
					
					return result;
				}
			};
			selectionDialog.setTitle("Select Alfrecso content model file");
			if (Window.OK == selectionDialog.open()) {
				Object[] result = selectionDialog.getResult();
				if (result==null)
					return false;
				if (result.length==0)
					return false;
				if (!(result[0] instanceof IFile))
					return false;
				try {
					this.jaxbHelper = JaxbUtils.readModel((IFile)result[0]);
					if (this.jaxbHelper.getObject() == null) {
						return false;
					}
					return super.preRun();
				} catch (Exception e) {
					return false;
				}
			}
			
			return false;
			// return super.preRun();
		}
		
		@Override
		protected NewNamespaceDialog createDialog() {
			NewModelFromFileDialog dialog = new NewModelFromFileDialog(selectedPackage, this.jaxbHelper.getObject());
			dialog.setDlgTitle("Create new model form file");
			dialog.setDlgMessage("Check model info");
			// dialog.loadData(this.jaxbHelper.getObject());
			// dialog.disableControls();
			return dialog;		
		}
		
		@Override
		protected void doRun() {
			
			Package model = AlfrescoUMLUtils.createModel(
					selectedPackage, 
					(String)creationInfo.get("prefix") + ":" + (String)creationInfo.get("name"));
			
			ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model theModel = 
					ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model._HELPER.getFor(model);
			
			theModel.setLocation(
					this.jaxbHelper.getFile().getLocation().toString().substring(
							this.jaxbHelper.getFile().getLocation().toString().indexOf(
									this.jaxbHelper.getFile().getProject().getName()
							)-1)
					);
			
			XML2UMLGenerator generator = new XML2UMLGenerator(AlfrescoUMLUtils.getUMLRoot(model));
			generator.setParentShell(shell);
			generator.setRootObject(model);
			
			generator.addModelToSources(
					AlfrescoUMLUtils.getFullName(model), 
					(String)AlfrescoUMLUtils.getStereotypeValue(
							AlfrescoProfile.ForPackage.Model._NAME, 
							model, 
							AlfrescoProfile.ForPackage.Model.LOCATION)
				);
			
			
			NS2EProgressMonitorDialog pmdialog = new NS2EProgressMonitorDialog(shell);
			try {
				pmdialog.run(false, true, generator);
			} catch (Exception e) {
				AlfrescoEditorPlugin.INSTANCE.log(e);
			}
			
			ImportsAndDependenciesUpdater updater = new ImportsAndDependenciesUpdater(model);
			try {
				pmdialog.run(false, true, updater);
			} catch (Exception e) {
				AlfrescoEditorPlugin.INSTANCE.log(e);
			}			
		}
	}
	
	@Override
	protected Command getCommand(IEvaluationContext context) {
		return new BaseAlfrescoCommand(context, new NewModelFromFileRunnable());
	}	
}
