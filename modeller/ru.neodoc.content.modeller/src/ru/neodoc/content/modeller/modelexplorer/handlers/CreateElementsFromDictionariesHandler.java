package ru.neodoc.content.modeller.modelexplorer.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace;
import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;
import ru.neodoc.content.modeller.model.AlfrescoModel;
import ru.neodoc.content.modeller.model.AlfrescoModelUtils;
import ru.neodoc.content.modeller.ui.dialogs.AlfrescoNamespacesCheckedTreeDialog;
import ru.neodoc.content.modeller.ui.dialogs.NS2EProgressMonitorDialog;
import ru.neodoc.content.modeller.utils.NamespaceElementsCreator;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;

public class CreateElementsFromDictionariesHandler extends
		AbstractAlfrescoHandler {

	protected class CreateElementsFromDictionariesCommand extends AbstractAlfrescoCommand {
		public CreateElementsFromDictionariesCommand(IEvaluationContext context){
			super(context, 
					new CreateElementsFromDictionariesRunnable(),  
					"LABEL", "DESC");
		}
		
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
					new AlfrescoNamespacesCheckedTreeDialog(root, AlfrescoUMLUtils.getUMLRoot(getSelectedElement()));
			dialog.setInput(root);
			dialog.open();
			if (dialog.getReturnCode() == Window.OK){
				Object[] result = dialog.getResult();
				
				List<Namespace> namespaces = new ArrayList<Namespace>();
				for (int i=0; i<result.length; i++)
					if (result[i] instanceof Namespace)
						namespaces.add((Namespace)result[i]);
				
				NS2EProgressMonitorDialog pmdialog = new NS2EProgressMonitorDialog(shell);
				try {
					NamespaceElementsCreator creator = new NamespaceElementsCreator(
							AlfrescoUMLUtils.getUMLRoot(getSelectedElement()), namespaces);
					creator.setParentShell(shell);
					creator.setRootObject(getSelectedElement());
//					creator.setEditingDomain(getEditingDomain());
					
/*					PackageableElement pe =  ((Model)getSelectedElement()).
							createPackagedElement("test", UMLFactory.eINSTANCE.createPackage().eClass());
					AlfrescoUMLUtils.makeNamespace((org.eclipse.uml2.uml.Package)pe);
*/
					NamespaceElementsCreator.DialogHelper dh = new NamespaceElementsCreator.DialogHelper(null);
					creator.setDialogHelper(dh);
/*					RunnableWithResult<Integer> rwr = (RunnableWithResult<Integer>)getEditingDomain().createPrivilegedRunnable(
							dh
						);
*/					pmdialog.run(false, true, creator);
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
