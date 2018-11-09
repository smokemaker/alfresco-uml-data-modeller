package ru.neodoc.content.modeller.commands.create;

import java.util.List;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;

public class MarkTemplateModels extends RecordingCommand {

	protected Model umlModel;

	public MarkTemplateModels(TransactionalEditingDomain domain) {
		super(domain);
	}
	
	public MarkTemplateModels(TransactionalEditingDomain domain, Model umlModel) {
		this(domain);
		this.umlModel = umlModel;
	}
	
	@Override
	protected void doExecute() {
		
		if (umlModel == null)
			return;
		
		@SuppressWarnings("unchecked")
		List<Package> models = (List<Package>)AlfrescoSearchHelperFactory
				.getModelSearcher()
				.startWith(umlModel)
				.search();
			
		for (Package m: models) {
			AlfrescoProfile.ForPackage.Model md = AlfrescoProfile.ForPackage.Model._HELPER.getFor(m);
			md.setPredefined(true);
			md.setDetached(true);
//			AlfrescoUMLUtils.setModelValue(m, AlfrescoProfile.ForPackage.Model.PREDEFINED, new Boolean(true));
//			AlfrescoUMLUtils.makeModelPredefined(m, true);
//			AlfrescoUMLUtils.setModelValue(m, AlfrescoProfile.ForPackage.Model.DETACHED, new Boolean(true));
		}	
	}

}
