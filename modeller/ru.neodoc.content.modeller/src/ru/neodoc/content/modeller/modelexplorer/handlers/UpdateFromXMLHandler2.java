package ru.neodoc.content.modeller.modelexplorer.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;
import ru.neodoc.content.modeller.ui.dialogs.NS2EProgressMonitorDialog;
import ru.neodoc.content.modeller.utils.ImportsAndDependenciesUpdater;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.xml2uml.XML2UMLGenerator;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model;
import ru.neodoc.content.utils.CommonUtils;

public class UpdateFromXMLHandler2 extends AbstractAlfrescoHandler {

	public class UpdateFromXMLCommand extends AbstractAlfrescoCommand {
		
		@Override
		public boolean canExecute() {
			return !getSelectedElements().isEmpty();
		}
		
		public UpdateFromXMLCommand(IEvaluationContext context) {
			super(context, new UpdateFromXMLRunnable());
		}
		
	} 
	
	@Override
	protected List<EObject> getSelectedElements(){
		return super.getSelectedElements();
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
/*					Package m = AlfrescoProfile.ForPackage.Model._HELPER.is((Package)element) 
							?(Package)element
							:AlfrescoUMLUtils.getNearestModel((Package)element);
*/					Model m = Model._HELPER.findNearestFor((Package)element);
					if ((m!=null)
							&& CommonUtils.isValueable(/*
									(String)AlfrescoUMLUtils.getStereotypeValue(
											AlfrescoProfile.ForPackage.Model._NAME, 
											m, 
											AlfrescoProfile.ForPackage.Model.LOCATION))*/m.getLocation()))
						models.add(m.getElementClassified());
				}
			}
			
			return !models.isEmpty();
		}
		
		@SuppressWarnings("static-access")
		@Override
		protected void doRun() {
			for (Package theModel: models) {
				XML2UMLGenerator generator = new XML2UMLGenerator(AlfrescoUMLUtils.getUMLRoot(theModel));
				generator.setParentShell(shell);
				generator.setRootObject(theModel);
				
				generator.addModelToSources(
						AlfrescoUMLUtils.getFullName(theModel), 
						(String)AlfrescoUMLUtils.getStereotypeValue(
								AlfrescoProfile.ForPackage.Model._NAME, 
								theModel, 
								AlfrescoProfile.ForPackage.Model.LOCATION)
					);
				
				
/*				NamespaceElementsCreator nec = new NamespaceElementsCreator(
						AlfrescoUMLUtils.getUMLRoot(theModel), 
						Collections.<ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace>emptyList());
				nec.setParentShell(shell);
				nec.setRootObject(theModel);
				
				nec.addModelToSources(
						AlfrescoUMLUtils.getFullName(theModel), 
						(String)AlfrescoUMLUtils.getStereotypeValue(
								AlfrescoProfile.ForPackage.Model._NAME, 
								theModel, 
								AlfrescoProfile.ForPackage.Model.LOCATION)
					);
				
*/				NS2EProgressMonitorDialog pmdialog = new NS2EProgressMonitorDialog(shell);
				try {
					pmdialog.run(false, true, generator);
				} catch (Exception e) {
					AlfrescoEditorPlugin.INSTANCE.log(e);
				}
				
				ImportsAndDependenciesUpdater updater = new ImportsAndDependenciesUpdater(theModel);
				try {
					pmdialog.run(false, true, updater);
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
