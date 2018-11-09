package ru.neodoc.content.modeller.extensions.root;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.uml2.uml.Model;

import ru.neodoc.content.modeller.extensions.common.CommonModellerExtensions;
import ru.neodoc.content.modeller.extensions.core.ExecutorExtension;
import ru.neodoc.content.modeller.extensions.core.SubTaskToAddDescriptor;
import ru.neodoc.content.modeller.extensions.jaxb.ExtensionsJAXBHelper;
import ru.neodoc.content.modeller.tasks.ExecutionCallback;
import ru.neodoc.content.modeller.tasks.ExecutionContext;
import ru.neodoc.content.modeller.tasks.ExecutionResult;
import ru.neodoc.content.modeller.tasks.Executor;
import ru.neodoc.content.modeller.tasks.ExtendedSubTask;
import ru.neodoc.content.modeller.xml2uml.XML2UMLGenerationManager;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;

public class RootExecutorExtension extends ExecutorExtension{

	@Override
	public List<SubTaskToAddDescriptor> getTasksToAdd(Executor executor) {
		SubTasksHelper helper = new SubTasksHelper(executor, this.getOwnerExtension());
		helper.insertAsFirst(new InitializeModellerExtensions());
		helper.toTheEnd(new SaveModellerExtensions());
		return helper.getList();
	}
	
	public static class XML2UMLFillExtensionsObject extends XML2UMLGenerationManager.XML2UMLExtendedSubTask{

		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			CommonModellerExtensions.setExtensionsObject(getExecutionContext(), context().get(ComplexRegistry.class).getUmlRoot());
			return ExecutionResult.RESULT_OK;
		}
		
	}
	
	public static class InitializeModellerExtensions extends ExtendedSubTask {
		
		protected ExecutionContext.Listener setRootModelListener = new ExecutionContext.Listener() {
			
			@Override
			public void changed(String parameterName, Object value, ExecutionContext sourceExecutionContext) {
				Model model = sourceExecutionContext.getUMLContextRootModel(); 
				String filePath = model.eResource().getURI().path();
				String fileName = filePath.substring(0, filePath.lastIndexOf(model.eResource().getURI().fileExtension()) - 1);
				String finalPath = fileName + ".ame";
				if (finalPath.startsWith("/resource"))
					finalPath = finalPath.replaceFirst("/resource", "");
				IPath path = new Path(finalPath);
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				getExecutionContext().get(ExtensionsJAXBHelper.class).load(file);
			}
		};
		
		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			getExecutionContext().put(new ExtensionsJAXBHelper());
			if (getExecutionContext().getUMLContextRootModel()!=null) {
				setRootModelListener.changed(
						ExecutionContext.UML_CONTEXT_ROOT_MODEL, 
						getExecutionContext().getUMLContextRootModel(), 
						getExecutionContext());
			} else {
				getExecutionContext().addListener(ExecutionContext.UML_CONTEXT_ROOT_MODEL, setRootModelListener);
			}
			return ExecutionResult.RESULT_OK;
		}
		
	}
	public static class SaveModellerExtensions extends ExtendedSubTask {

		@Override
		public ExecutionResult execute(ExecutionCallback callback) {
			getExecutionContext().get(ExtensionsJAXBHelper.class).save();
			return ExecutionResult.RESULT_OK;
		}
		
	}
}