package ru.neodoc.content.modeller.extensions.modelexplorer.handlers;

import org.eclipse.core.commands.State;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.jface.commands.ToggleState;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.extensions.ModellingExtensionsProfile;
import ru.neodoc.content.modeller.modelexplorer.handlers.AbstractAlfrescoHandler;
import ru.neodoc.content.utils.uml.UMLUtils;

public class EnableModellingExtensionsHandler extends AbstractAlfrescoHandler {

	@Override
	protected Command getCommand(IEvaluationContext context) {
		
		return new EnableModellingExtensionsCommand(context);
	}

	
	public class EnableModellingExtensionsCommand extends AbstractAlfrescoCommand {

		public static final String COMMAND_ID = "ru.neodoc.content.modeller.extensions.core.enable.command";
		
		protected EnableModellingExtensionsCommand(IEvaluationContext context) {
			super(context, new EnableModellingExtensionsRunnable());
		}

	}
	
	public class EnableModellingExtensionsRunnable extends AbstractAlfrescoRunnable {

		boolean currentState = false;
		
		public EnableModellingExtensionsRunnable() {
			super();
		}
		
		public boolean isCurrentState() {
			return currentState;
		}

		public void setCurrentState(boolean currentState) {
			this.currentState = currentState;
		}



		@Override
		protected void doRun() {
			
			Package pack = this.selectedPackage;
			Model model = UMLUtils.getUMLRoot(pack);
			
			if (ModellingExtensionsProfile._INSTANCE.isApplied(model))
				ModellingExtensionsProfile._INSTANCE.unapply(model);
			else
				ModellingExtensionsProfile._INSTANCE.apply(model);
			
		}
		
	}
}
