package ru.neodoc.content.modeller.extensions.enum2constraint;

import java.util.Collections;
import java.util.List;

import ru.neodoc.content.modeller.extensions.common.AbstractModellerExtension;
import ru.neodoc.content.modeller.extensions.core.ExecutorExtension;
import ru.neodoc.content.modeller.extensions.core.SubTaskToAddDescriptor;
import ru.neodoc.content.modeller.extensions.enum2constraint.uml2xml.EnumPropertyConstraintHelper;
import ru.neodoc.content.modeller.tasks.Executor;
import ru.neodoc.eclipse.utils.Preloader;

public class Enum2ConstraintModellerExtension extends AbstractModellerExtension {

	protected static Preloader uml2xmlPreloader = null;
	protected static Preloader xml2umlPreloader = null;
	
	@Override
	public void initialize() {
		super.initialize();
		if (uml2xmlPreloader == null) {
			uml2xmlPreloader = UML2XMLExecutorExtension.getPreloader();
			uml2xmlPreloader.load();
		}
	}
	
	public static class UML2XMLExecutorExtension extends ExecutorExtension {

		public static Preloader getPreloader() {
			return new Preloader(Enum2ConstraintModellerExtension.class.getClassLoader()) {
				
				@Override
				public Class<?>[] getClasses() {
					return new Class<?>[] {
						EnumPropertyConstraintHelper.class
					};
				}
			};
		}
		
		@Override
		public List<SubTaskToAddDescriptor> getTasksToAdd(Executor executor) {
			return Collections.emptyList();
		}
		
	}
	
}
