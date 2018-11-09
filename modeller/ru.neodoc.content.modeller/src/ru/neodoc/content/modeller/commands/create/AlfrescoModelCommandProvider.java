package ru.neodoc.content.modeller.commands.create;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.papyrus.infra.architecture.Activator;
import org.eclipse.papyrus.infra.architecture.commands.IModelCommandProvider;
import org.eclipse.papyrus.infra.architecture.commands.IModelCreationCommand;
import org.eclipse.papyrus.infra.core.resource.ModelSet;

public class AlfrescoModelCommandProvider implements IModelCommandProvider {

	public AlfrescoModelCommandProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Command getModelCreationCommand(final ModelSet modelSet, String contextId) {
		// org.eclipse.papyrus.uml.architecture.Profile
		// org.eclipse.papyrus.infra.services.edit.TypeContext
		return new RecordingCommand(modelSet.getTransactionalEditingDomain()) {
			@Override
			protected void doExecute() {
				try {
					IModelCreationCommand creationCommand = (IModelCreationCommand) (new CreateAlfrescoContentModelCommand());
					creationCommand.createModel(modelSet);
				} catch (Exception e) {
					Activator.log.error(e);
				}
			}
		};
	}

	@Override
	public Command getModelConversionCommand(ModelSet modelSet, String contextId) {
		// TODO Auto-generated method stub
		return null;
	}

}
