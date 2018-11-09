package ru.neodoc.content.modeller.modelexplorer.handlers;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.jface.window.Window;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.papyrus.uml.extensionpoints.Registry;
import org.eclipse.papyrus.uml.extensionpoints.library.FilteredRegisteredLibrariesSelectionDialog;
import org.eclipse.papyrus.uml.extensionpoints.library.IRegisteredLibrary;
import org.eclipse.papyrus.uml.extensionpoints.utils.Util;
import org.eclipse.papyrus.uml.profile.ui.dialogs.ElementImportTreeSelectionDialog.ImportSpec;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;

import ru.neodoc.content.modeller.extensionpoints.IRegisteredModel;
import ru.neodoc.content.modeller.extensionpoints.RegisteredModelRegistry;
import ru.neodoc.content.modeller.ui.dialogs.NamespaceImportDialog;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackageImport.ImportNamespace;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;
import ru.neodoc.org.eclipse.papyrus.uml.importt.handlers.ImportRegisteredPackageHandler;
import ru.neodoc.org.eclipse.papyrus.uml.importt.ui.PackageImportDialog;

public class ImportRegisteredNamespaceHandler extends ImportRegisteredPackageHandler {

	public static class NamespaceImportSpec extends ImportSpec<Package> {

		protected NamespaceImportSpec(Package element) {
			super(element);
		}
		
	}
	
	@Override
	protected ICommand getGMFCommand(IEvaluationContext context) {
		return new ImportRegisteredModelCommand(context);
	}
	
	protected Collection<ImportSpec<Package>> getNamespaceCollection(Collection<ImportSpec<Package>> source){
		Set<ImportSpec<Package>> result = new HashSet<>();
		
		for (ImportSpec<Package> sourceItem: source) {
			
			StereotypedElement se = AbstractProfile.asUntyped(sourceItem.getElement());
			if (se.has(Namespace.class)) {
				result.add(sourceItem);
				continue;
			}
			
			for (Package pack: AlfrescoSearchHelperFactory.getNamespaceSearcher().startWith(sourceItem.getElement()).search()) {
				ImportSpec<Package> newItem = new NamespaceImportSpec(pack);
				newItem.setAction(sourceItem.getAction());
				result.add(newItem);
			}
			
		}
		
		return result;
	}
	
	@Override
	protected void importLibraries(IRegisteredLibrary[] librariesToImport) {
		ResourceSet resourceSet = Util.createTemporaryResourceSet();

		try {
			for (int i = 0; i < librariesToImport.length; i++) {
				IRegisteredLibrary currentLibrary = (librariesToImport[i]);
				URI modelUri = currentLibrary.getUri();

				Resource modelResource = resourceSet.getResource(modelUri, true);
				NamespaceImportDialog dialog = new NamespaceImportDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), ((Package) modelResource.getContents().get(0)));

				if (dialog.open() == Window.OK) {
					Collection<ImportSpec<Package>> result = dialog.getResult();
					result = getNamespaceCollection(result);
					
					
					for (ImportSpec<Package> resultElement : result) {
						Package selectedPackage = resultElement.getElement();
						switch (resultElement.getAction()) {
						case COPY:
							handleCopyPackage(selectedPackage);
							break;
						case IMPORT:
							handleImportNamespace(selectedPackage);
							break;
						default: // Load
							handleLoadPackage(selectedPackage);
							break;
						}
					}
				}
			}
		} finally {
			EMFHelper.unload(resourceSet);
			;
		}
	}
	
	protected void handleImportNamespace(Package _package) {
		Package p = (Package)getSelectedElement();
		for (PackageImport imp: p.getPackageImports()) {
			Package importedPackage = imp.getImportedPackage();
			if (_package.getName().equals(importedPackage.getName()) 
					&& CommonUtils.isValueable(_package.getURI())
					&& _package.getURI().equals(importedPackage.getURI()))
				return;
		}
		PackageImport pi = handleImportPackage(_package);
		AbstractProfile.asType(pi, ImportNamespace.class);
	}
	
	public class ImportRegisteredModelCommand extends AbstractImportCommand {

		public ImportRegisteredModelCommand(IEvaluationContext context) {
			super(new Runnable() {

				public void run() {
					// Retrieve shell instance
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

					// get the set of registered libraries available
					IRegisteredModel[] allLibraries = RegisteredModelRegistry.getRegisteredModels()/*Registry.getRegisteredLibraries().toArray(new IRegisteredLibrary[0])*/;

					// Open Registered ModelLibrary selection dialog
					FilteredRegisteredLibrariesSelectionDialog dialog = new FilteredRegisteredLibrariesSelectionDialog(shell, true, allLibraries, getImportedLibraries());
					dialog.open();
					if (Window.OK == dialog.getReturnCode()) {
						// get the result, which is the set of libraries to import
						List<Object> librariesToImport = Arrays.asList(dialog.getResult());
						importLibraries(librariesToImport.toArray(new IRegisteredLibrary[librariesToImport.size()]));
					}
				}
			}, context, "Import Models", "Import Models from Repository");		}
		
	}
	
}
