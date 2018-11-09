package ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher;

import ru.neodoc.content.codegen.sdoc2.config.Configurable;
import ru.neodoc.content.codegen.sdoc2.config.Configuration;

public abstract class ConfigurableDispatchedPageImpl extends DispatchedPageImpl implements Configurable {

	protected Configuration configuration;
	
	protected ConfigurableDispatchedPageImpl(String pageName) {
		super(pageName);
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}

}
