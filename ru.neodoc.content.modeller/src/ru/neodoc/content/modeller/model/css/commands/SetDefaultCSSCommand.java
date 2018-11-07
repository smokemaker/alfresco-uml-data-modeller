package ru.neodoc.content.modeller.model.css.commands;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.papyrus.infra.core.resource.ModelSet;

import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.modeller.model.AlfrescoModelUtils;

public class SetDefaultCSSCommand extends RecordingCommand {

	protected ModelSet modelSet;
	protected boolean createAlfrescoRoot;
	
	public SetDefaultCSSCommand(TransactionalEditingDomain domain, ModelSet modelSet) {
		super(domain);
		this.modelSet = modelSet;
		this.createAlfrescoRoot = false;
	}

	public SetDefaultCSSCommand(TransactionalEditingDomain domain, ModelSet modelSet, boolean createAlfrescoRoot) {
		this(domain, modelSet);
		this.createAlfrescoRoot = createAlfrescoRoot;
	}

	@Override
	protected void doExecute() {
		Alfresco alfresco = (Alfresco)AlfrescoModelUtils
				.getAlfrescoRootObject(AlfrescoModelUtils.getAlfrescoModel(modelSet), this.createAlfrescoRoot);
		if (alfresco != null)
			AlfrescoModelUtils.setDefaultCss(alfresco);
	}

}
