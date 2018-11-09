package ru.neodoc.content.modeller.modelexplorer.handlers;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.Command;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;
import ru.neodoc.content.modeller.ContentModellerPlugin;
import ru.neodoc.content.modeller.preferences.PreferenceConstants;
import ru.neodoc.content.modeller.utils.ImportsAndDependenciesUpdater;
//import ru.neodoc.content.modeller.utils.AlfrescoProfile;
import ru.neodoc.content.modeller.utils.sync.ModelXMLSynchronizer;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class ExportToXMLHandler extends AbstractAlfrescoHandler {

	boolean saveAsNew = false;
	
	public class ExportToXMLCommand extends AbstractAlfrescoCommand {
		
		public ExportToXMLCommand(IEvaluationContext context){
			super(context, new ExportToXMLRunnable());
		}

		public ExportToXMLCommand(IEvaluationContext context, boolean createNew){
			super(context, new ExportToXMLRunnable(createNew));
		}
		
	} 
	
	public class ExportToXMLRunnable extends AbstractAlfrescoRunnable {
		
		boolean saveToNew = false;
		
		public ExportToXMLRunnable(){
			super();
		}

		public ExportToXMLRunnable(boolean saveAsNew){
			super();
			this.saveToNew = saveAsNew;
		}
		
		@Override
		protected void doRun() {
			
			String location = null;
			boolean attachToResource = false;
			
			Package model = this.selectedPackage;
			if (/*!AlfrescoUMLUtils.isModel(model)*/!AlfrescoProfile.ForPackage.Model._HELPER.is(model))
				return;
			
			AlfrescoProfile.ForPackage.Model theModel = AlfrescoProfile.ForPackage.Model._HELPER.getFor(model);
			
			if (!this.saveToNew) {
				try {
					location = theModel.getLocation()/*(String)model.getValue(
							AlfrescoUMLUtils.getStereotype(model, AlfrescoProfile.ForPackage.Model._NAME), 
							AlfrescoProfile.ForPackage.Model.LOCATION)*/;
					if (location == null || (location.trim().length() == 0)){
						this.saveToNew = true;
						attachToResource = true;
					}
				} catch (Exception e) {
					// NOOP
				}
			}
			
			if (this.saveToNew) {

				attachToResource = true;
				SaveAsDialog dialog = new SaveAsDialog(this.shell);
				dialog.setTitle("Model " + model.getName() + ": export to XML");
				String filePath = model.eResource().getURI().path();
				String fileName = filePath.substring(0, filePath.lastIndexOf(model.eResource().getURI().fileExtension()) - 1);
				String finalPath = fileName + ".xml";
				if (finalPath.startsWith("/resource"))
					finalPath = finalPath.replaceFirst("/resource", "");
				IPath path = new Path(finalPath);
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				dialog.setOriginalFile(file);
				if (dialog.open() == SaveAsDialog.OK){
					IPath result = dialog.getResult();
					location = result.toString();
					if (!"xml".equals(result.getFileExtension())) {
						if (!location.endsWith("."))
							location += ".";
						location += "xml";
					}
				} else {
					return;
				}
			}
			
			try {
				
				ContentModellerPlugin.getDefault().log(">>> BEGIN");
				
				ProgressMonitorDialog pmdialog = new ProgressMonitorDialog(this.shell);

				if (ContentModellerPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_UPDATE_BEFORE_EXPORT)) {
					// ContentModellerPlugin.getDefault().log(">>> before imports update");
					ImportsAndDependenciesUpdater updater = new ImportsAndDependenciesUpdater(getSelectedElement());
					pmdialog.run(false, true, updater);
					// ContentModellerPlugin.getDefault().log(">>> after imports update");
				}
				
				ModelXMLSynchronizer sync = new ModelXMLSynchronizer(model, location);
				sync.setMode(ModelXMLSynchronizer.SyncMode.SM_XML);
				
				// ContentModellerPlugin.getDefault().log(">>> before sync");
				pmdialog.run(false, true, sync);
				// ContentModellerPlugin.getDefault().log(">>> after sync");
				
				// ContentModellerPlugin.getDefault().log(">>> END");
				
			} catch (Exception e) {
				AlfrescoEditorPlugin.INSTANCE.log(e);
			};
			
			if (attachToResource) {
				model.setValue(
						AlfrescoUMLUtils.getStereotype(model, AlfrescoProfile.ForPackage.Model._NAME), 
						AlfrescoProfile.ForPackage.Model.LOCATION, 
						location);
				model.setURI(location);
			}
			
		}
	}
	
	@Override
	protected Command getCommand(IEvaluationContext context) {
		return new ExportToXMLCommand(context);
	}

}
