package ru.neodoc.content.modeller.modelexplorer.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.jface.window.Window;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.papyrus.uml.profile.ui.dialogs.ElementImportTreeSelectionDialog.ImportSpec;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;

import ru.neodoc.content.modeller.ui.dialogs.NamespaceImportDialog;
import ru.neodoc.content.modeller.ui.dialogs.NamespaceImportSourceDialog;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.org.eclipse.papyrus.uml.importt.handlers.ImportPackageFromUserModelHandler;

public class ImportNamespaceFromUserModelHandler extends ImportPackageFromUserModelHandler {

	@Override
	protected ICommand getGMFCommand(final IEvaluationContext context) {
		return new ImportNamespaceFromFileCommand(context);
	}	/**
	 * Specific {@link ChangeCommand} that imports libraries from repository
	 */
	
	protected void importModels(Collection<Package> packages) {
		NamespaceImportDialog dialog = new NamespaceImportDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), packages);

		if (dialog.open() == Window.OK) {
			Collection<ImportSpec<Package>> result = dialog.getResult();

			for (ImportSpec<Package> resultElement : result) {
				Package selectedPackage = resultElement.getElement();
				switch (resultElement.getAction()) {
				case COPY:
					handleCopyPackage(selectedPackage);
					break;
				case IMPORT:
					handleImportPackage(selectedPackage);
					break;
				default:
					handleLoadPackage(selectedPackage);
					break;
				}
			}
		}
		
	}
	
	@Override
	protected PackageImport handleImportPackage(Package _package) {
		
		List<? extends Package> namespacePackages = AlfrescoSearchHelperFactory.getNamespaceSearcher()
				.includeStartingPoint(true)
				.startWith(_package)
				.search();
		
		Package currentPackage = (Package)getSelectedElement();
		Model currentModel = Model._HELPER.getFor(currentPackage);
		if (currentModel==null)
			return null;
		
		for (Package namespacePackage: namespacePackages) {
			AlfrescoProfile.ForPackage.Namespace namespace = AlfrescoProfile.ForPackage.Namespace._HELPER.getFor(namespacePackage);
			if (namespace==null)
				continue;
			EMFHelper.reloadIntoContext(namespacePackage, currentPackage);
			currentModel.importNamespace(namespace);
		}
		
		return null;
	}	
	protected Collection<Package> getAlfrescoModels(Collection<Package> packages){
		List<Package> result = new ArrayList<>();
		for (Package _package: packages) {
			List<? extends Package> models = AlfrescoSearchHelperFactory.getModelSearcher().startWith(_package).search();
			for (Package model: models)
				if (!result.contains(model))
					result.add(model);
		}
		return result;
	}
	
	public class ImportNamespaceFromFileCommand extends AbstractImportCommand {

		/**
		 * Creates a new ImportLibraryFromRepositoryCommand
		 *
		 * @param editingDomain
		 *            editing domain that manages the changed objects
		 * @param runnable
		 *            process that executes the modifications
		 * @param label
		 *            the label of the command
		 * @param description
		 *            description of the command
		 */
		public ImportNamespaceFromFileCommand(final IEvaluationContext context) {
			super(new Runnable() {

				public void run() {

					// Retrieve shell instance
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

					Map<String, String> extensionFilters = new LinkedHashMap<String, String>();
					extensionFilters.put("*.uml", "UML (*.uml)");
					extensionFilters.put("*.profile.uml", "UML Profiles (*.profile.uml)");
					extensionFilters.put("*", "All (*)");

					NamespaceImportSourceDialog dialog = NamespaceImportSourceDialog.create(shell, "Select the models to import", getSelection(), extensionFilters);
					dialog.open();
					Collection<Package> packages = dialog.getSelectedPackages(); 

					if (packages != null) {

						packages = getAlfrescoModels(packages);
						
						if (!packages.isEmpty()) {
							importModels(packages);
						}
					}
					
					dialog.disposeResourceSet();
					
				}

			}, context, "Import Libraries", "Import Libraries from Workspace"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

}
