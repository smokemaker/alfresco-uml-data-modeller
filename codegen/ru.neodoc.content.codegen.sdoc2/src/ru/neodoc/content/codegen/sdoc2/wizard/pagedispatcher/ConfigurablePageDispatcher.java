package ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher;

import ru.neodoc.content.codegen.sdoc2.config.Configurable;
import ru.neodoc.content.codegen.sdoc2.config.Configuration;

public class ConfigurablePageDispatcher extends CommonPageDispatcher implements Configurable {

	protected Configuration configuration;
	
	public ConfigurablePageDispatcher() {
		super();
	}
	
	public ConfigurablePageDispatcher(Configuration configuration) {
		this();
		setConfiguration(configuration);
	}
	
	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}

	@Override
	protected void initPage(DispatchedPage page) {
		setPageConfiguration(page);
		super.initPage(page);
	}
	
	protected void setPageConfiguration(DispatchedPage page) {
		if (Configurable.class.isAssignableFrom(page.getClass())) {
			Configuration config = ((Configurable)page).getConfiguration();
			if (config==null)
				((Configurable)page).setConfiguration(getConfiguration());
		}
		
	}
}
