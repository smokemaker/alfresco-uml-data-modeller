package ru.neodoc.content.modeller.modelexplorer.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace;
import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;
import ru.neodoc.content.modeller.model.AlfrescoModel;
import ru.neodoc.content.modeller.model.AlfrescoModelUtils;
import ru.neodoc.content.modeller.ui.dialogs.AlfrescoNamespacesCheckedTreeDialog;
import ru.neodoc.content.modeller.ui.dialogs.NS2EProgressMonitorDialog;
import ru.neodoc.content.modeller.utils.ImportsAndDependenciesUpdater;
import ru.neodoc.content.modeller.utils.NamespaceElementsCreator;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.xml2uml.XML2UMLGenerator;
import ru.neodoc.content.utils.uml.UMLUtils;
import ru.neodoc.content.utils.uml.search.UMLSearchUtils;

public class CreateElementsFromDictionariesHandler2 extends
		AbstractAlfrescoHandler {

	protected class CreateElementsFromDictionariesCommand extends AbstractAlfrescoCommand {
		public CreateElementsFromDictionariesCommand(IEvaluationContext context){
			super(context, 
					new CreateElementsFromDictionariesRunnable(),  
					"LABEL", "DESC");
		}
		
	}
	
	@Override
	protected EObject getSelectedElement() {
		return super.getSelectedElement();
	}
	
	protected class CreateElementsFromDictionariesRunnable implements Runnable {
		public void run() {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			AlfrescoModel model = AlfrescoModelUtils.getAlfrescoModel(getSelectedElement());
			Alfresco root = null;
			if (model!=null) {
				if (model.getResource() == null)
					model.loadModel(model.getModelSet().getURIWithoutExtension());
				root = AlfrescoModelUtils.getAlfrescoRoot(getSelectedElement());
			}
			AlfrescoNamespacesCheckedTreeDialog dialog = 
					new AlfrescoNamespacesCheckedTreeDialog(root, UMLUtils.getUMLRoot(getSelectedElement()));
			dialog.setInput(root);
			dialog.open();
			if (dialog.getReturnCode() == Window.OK){
				Object[] result = dialog.getResult();
				
				List<Namespace> namespaces = new ArrayList<>();
				for (int i=0; i<result.length; i++)
					if (result[i] instanceof Namespace)
						namespaces.add((Namespace)result[i]);
				
				NS2EProgressMonitorDialog pmdialog = new NS2EProgressMonitorDialog(shell);
				try {
					XML2UMLGenerator generator = new XML2UMLGenerator(AlfrescoUMLUtils.getUMLRoot(getSelectedElement()));
					generator.getManager().setNamespacesToCreate(namespaces);
					generator.setParentShell(shell);
					generator.setRootObject((org.eclipse.uml2.uml.Package)getSelectedElement());
					
/*					NamespaceElementsCreator creator = new NamespaceElementsCreator(
							UMLUtils.getUMLRoot(getSelectedElement()), namespaces);
					creator.setParentShell(shell);
					creator.setRootObject(getSelectedElement());
*/
//					creator.setEditingDomain(getEditingDomain());
					
/*					PackageableElement pe =  ((Model)getSelectedElement()).
							createPackagedElement("test", UMLFactory.eINSTANCE.createPackage().eClass());
					AlfrescoUMLUtils.makeNamespace((org.eclipse.uml2.uml.Package)pe);
*/
/*					NamespaceElementsCreator.DialogHelper dh = new NamespaceElementsCreator.DialogHelper(null);
					creator.setDialogHelper(dh);
*//*					RunnableWithResult<Integer> rwr = (RunnableWithResult<Integer>)getEditingDomain().createPrivilegedRunnable(
							dh
						);
*/					pmdialog.run(false, true, generator);

					Set<Package> models = new HashSet<>();
					Package start = (Package)getSelectedElement();
					for (Namespace ns: namespaces) {
						Dictionary d = (Dictionary)ns.eContainer();
						Package p = UMLSearchUtils.packageByName(start, d.getName());
						if (p!=null)
							models.add(p);
					}
					
					for (Package p: models) {
						ImportsAndDependenciesUpdater updater = new ImportsAndDependenciesUpdater(p);
						try {
							pmdialog.run(false, true, updater);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				} catch (Exception e) {
					AlfrescoEditorPlugin.INSTANCE.log(e);
				};
/*				MessageBox mb = new MessageBox(shell);
				mb.setMessage(root==null?"null":root.getClass().getName());
				mb.open();
*/			}
		}
	}
	
	@Override
	protected Command getCommand(IEvaluationContext context) {
		return new CreateElementsFromDictionariesCommand(context);
	}
	
}
