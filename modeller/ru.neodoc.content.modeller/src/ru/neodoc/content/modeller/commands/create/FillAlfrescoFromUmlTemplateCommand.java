package ru.neodoc.content.modeller.commands.create;

import java.util.List;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;

import ru.neodoc.content.ecore.alfresco.model.AlfrescoModelHelper;
import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;

public class FillAlfrescoFromUmlTemplateCommand extends RecordingCommand {

	protected Alfresco alfrescoModel = null;
	protected Model umlModel = null;
	
	public FillAlfrescoFromUmlTemplateCommand(TransactionalEditingDomain domain) {
		super(domain);
	}

	public FillAlfrescoFromUmlTemplateCommand(TransactionalEditingDomain domain, Alfresco alfModel, Model umlModel) {
		super(domain);
		this.alfrescoModel = alfModel;
		this.umlModel = umlModel;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doExecute() {
		if (alfrescoModel==null || umlModel==null)
			return;
		
		List<Package> models = (List<Package>)AlfrescoSearchHelperFactory
			.getModelSearcher()
			.startWith(umlModel)
			.search();
		
		for (Package m: models) {
			Dictionary d = AlfrescoModelHelper.newDictionary(alfrescoModel);
			d.setName(m.getName());
			d.setIsReadOnly(true);
			d.setLocation("<internal>");
			
			List<Package> namespaces = (List<Package>) AlfrescoSearchHelperFactory
					.getNamespaceSearcher()
					.startWith(m)
					.search();
			
			for (Package ns: namespaces)
				AlfrescoModelHelper.newNamespace(d, ns.getName(), ns.getURI());
			
			for (PackageImport pi: m.getPackageImports()) {
				Package ip = pi.getImportedPackage();
				AlfrescoModelHelper.newImport(d, ip.getName(), ip.getURI());
			}
			
		}
		
	}

}
