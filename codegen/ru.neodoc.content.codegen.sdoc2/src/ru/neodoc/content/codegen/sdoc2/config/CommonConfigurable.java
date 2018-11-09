package ru.neodoc.content.codegen.sdoc2.config;

public abstract class CommonConfigurable implements Configurable {

	protected Configuration configuration;
	
	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}

}
