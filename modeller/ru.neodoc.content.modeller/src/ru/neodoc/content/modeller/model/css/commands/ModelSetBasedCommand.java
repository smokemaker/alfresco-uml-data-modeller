package ru.neodoc.content.modeller.model.css.commands;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.papyrus.infra.core.resource.ModelSet;

public abstract class ModelSetBasedCommand extends RecordingCommand {

	protected ModelSet modelSet;
	
	public ModelSetBasedCommand(ModelSet modelSet) {
		super(modelSet.getTransactionalEditingDomain());
	}

}
