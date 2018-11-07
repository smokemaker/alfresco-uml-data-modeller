package ru.neodoc.content.modeller.modelexplorer.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;
import ru.neodoc.content.modeller.ui.dialogs.NS2EProgressMonitorDialog;
import ru.neodoc.content.modeller.utils.NamespaceElementsCreator;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.CommonUtils;

public class UpdateFromXMLHandler extends AbstractAlfrescoHandler {

	public class UpdateFromXMLCommand extends AbstractAlfrescoCommand {
		
		@Override
		public boolean canExecute() {
			return !getSelectedElements().isEmpty();
		}
		
		public UpdateFromXMLCommand(IEvaluationContext context) {
			super(context, new UpdateFromXMLRunnable());
		}
		
	} 
	
	public class UpdateFromXMLRunnable extends AbstractAlfrescoRunnable {
		
		protected Package model = null;
		
		protected List<Package> models = new ArrayList<>();
		
		@Override
		protected boolean preRun() {
			List<EObject> elements = getSelectedElements();
			if (elements.isEmpty())
				return false;
/*			boolean result = super.preRun();
			if (result) {
				model = AlfrescoUMLUtils.isModel(selectedPackage)
						?selectedPackage
						:AlfrescoUMLUtils.getNearestModel(selectedPackage);
			}
			return result 
					&& (model!=null)
					&& CommonUtils.isValueable(
							(String)AlfrescoUMLUtils.getStereotypeValue(
									AlfrescoProfile.ForPackage.Model._NAME, 
									model, 
									AlfrescoProfile.ForPackage.Model.LOCATION)
						); 
*/		
			for (EObject element: elements) {
				if (element instanceof Package) {
					Package m = AlfrescoUMLUtils.isModel((Package)element)
							?(Package)element
							:AlfrescoUMLUtils.getNearestModel((Package)element);
					if ((m!=null)
							&& CommonUtils.isValueable(
									(String)AlfrescoUMLUtils.getStereotypeValue(
											AlfrescoProfile.ForPackage.Model._NAME, 
											m, 
											AlfrescoProfile.ForPackage.Model.LOCATION)))
						models.add(m);
				}
			}
			
			return !models.isEmpty();
		}
		
		@Override
		protected void doRun() {
			for (Package theModel: models) {
				NamespaceElementsCreator nec = new NamespaceElementsCreator(
						AlfrescoUMLUtils.getUMLRoot(theModel), Collections.<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace>emptyList());
				nec.setParentShell(shell);
				nec.setRootObject(theModel);
				
				nec.addModelToSources(
						AlfrescoUMLUtils.getFullName(theModel), 
						(String)AlfrescoUMLUtils.getStereotypeValue(
								AlfrescoProfile.ForPackage.Model._NAME, 
								theModel, 
								AlfrescoProfile.ForPackage.Model.LOCATION)
					);
				
				NS2EProgressMonitorDialog pmdialog = new NS2EProgressMonitorDialog(shell);
				try {
					pmdialog.run(false, true, nec);
				} catch (Exception e) {
					AlfrescoEditorPlugin.INSTANCE.log(e);
				}
			}
		}
		
	}
	
	@Override
	protected Command getCommand(IEvaluationContext context) {
		return new UpdateFromXMLCommand(context);
	}

}
