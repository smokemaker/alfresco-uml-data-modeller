package ru.neodoc.content.modeller.xml2uml;

import java.util.List;

import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace;
import ru.neodoc.content.modeller.tasks.Executor;
import ru.neodoc.content.modeller.tasks.RunnableExecutionContainer;

public class XML2UMLGenerator extends RunnableExecutionContainer {

	public XML2UMLGenerationManager manager;
	
	public XML2UMLGenerator(Package root) {
		super();
		this.manager = new XML2UMLGenerationManager();
		this.manager.setUmlRoot(root);
	}
	
	public XML2UMLGenerator(Package root, List<Namespace> namespaces) {
		this(root);
		this.manager.setNamespacesToCreate(namespaces);
	}
	
	@Override
	protected Executor getExecutor() {
		return this.manager;
	}
	
	/** INITIALIZING **/
	
	public void setRootObject(Package rootObject) {
		this.manager.setRootObject(rootObject);
	}

	public void addModelToSources(String name, String location){
		this.manager.addModelToSources(name, location);
	}
	
	
	public XML2UMLGenerationManager getManager() {
		return manager;
	}

}
