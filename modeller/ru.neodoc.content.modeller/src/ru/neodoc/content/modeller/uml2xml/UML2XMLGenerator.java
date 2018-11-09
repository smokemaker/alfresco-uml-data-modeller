package ru.neodoc.content.modeller.uml2xml;

import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.tasks.Executor;
import ru.neodoc.content.modeller.tasks.RunnableExecutionContainer;

public class UML2XMLGenerator extends RunnableExecutionContainer {

	protected Package model = null;
	protected String location = null;
	
	protected UML2XMLGenerationManager manager;
	
	private UML2XMLGenerator(){
		super();
		this.manager = new UML2XMLGenerationManager();
	}

	public UML2XMLGenerator(Package model){
		this();
		this.model = model;
		this.manager.setModel(model);
	}

	public UML2XMLGenerator(Package model, String location){
		this(model);
		this.location = location;
		this.manager.setLocation(location);
	}
	
	@Override
	protected Executor getExecutor() {
		return this.manager;
	}

	// settergs & getters
	
	public Package getModel() {
		return model;
	}

	public void setModel(Package model) {
		this.model = model;
		this.manager.setModel(model);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
		this.manager.setLocation(location);
	}

	public UML2XMLGenerationManager getManager() {
		return manager;
	}
	
}
