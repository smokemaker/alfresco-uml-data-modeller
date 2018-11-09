package ru.neodoc.content.modeller.modelexplorer.handlers;

import org.alfresco.model.dictionary._1.Model;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.command.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;
import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.CommonUtils;

public class LoadFromXMLHandler extends UpdateFromXMLHandler {

	public class LoadFromXMLCommand extends AbstractAlfrescoCommand {
		
		public LoadFromXMLCommand(IEvaluationContext context) {
			super(context, new LoadFromXMLRunnable());
		}
		
	} 
	
	public class LoadFromXMLRunnable extends UpdateFromXMLRunnable {
		
		// protected Package model = null;
		protected Model loadedModel = null;
		
		@Override
		protected boolean preRun() {
			super.preRun();
			boolean result = true;
			IFile selectedXml = null; 
			
			ResourceListSelectionDialog dialog = new ResourceListSelectionDialog(shell, root, IResource.FILE);
			
			while (result && (selectedXml == null)){
				int dialogResult = dialog.open();
				if (dialogResult == Window.CANCEL){
					result = false;
					break;
				}
				Object[] objects = dialog.getResult();
				if (objects.length==0){
					errorMessage("No files were selected", "No files were selected");
					continue;
				}
				IFile f = (IFile)objects[0];
				try {
					loadedModel = JaxbUtils.read(f);
					selectedXml = f;
				} catch (Exception e) {
					errorMessage("Error loading file", "Please, check that resource \n" 
							+ f.getLocation().toString()
							+ "\n contains valid model"
							+ "\n\nSee log for details");
					AlfrescoEditorPlugin.INSTANCE.log(e);
				}

			}
			
			
			if (result) {
				model = AlfrescoUMLUtils.isModel(selectedPackage)
						?selectedPackage
						:AlfrescoUMLUtils.getNearestModel(selectedPackage);
				if (model!=null && loadedModel!=null){
					model.setValue(
							AlfrescoUMLUtils.getStereotype(model, AlfrescoProfile.ForPackage.Model._NAME), 
							AlfrescoProfile.ForPackage.Model.LOCATION, 
							selectedXml.getFullPath().toString());
					model.setName(loadedModel.getName());
				}
			}
			return result 
					&& (model!=null)
					&& CommonUtils.isValueable(
							(String)AlfrescoUMLUtils.getStereotypeValue(
									AlfrescoProfile.ForPackage.Model._NAME, 
									model, 
									AlfrescoProfile.ForPackage.Model.LOCATION)
						); 
		}
		
		@Override
		protected void doRun() {
			
			super.doRun();
			
		}
		
	}
	
	@Override
	protected Command getCommand(IEvaluationContext context) {
		return new LoadFromXMLCommand(context);
	}

}
