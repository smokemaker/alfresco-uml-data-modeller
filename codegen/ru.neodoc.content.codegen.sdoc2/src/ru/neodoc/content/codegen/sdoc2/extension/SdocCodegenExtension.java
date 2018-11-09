package ru.neodoc.content.codegen.sdoc2.extension;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.PageDispatcher;

public interface SdocCodegenExtension {

	String getOptionMessage();
	SdocCodegenExtensionConfiguration getConfiguration();
	
	PageDispatcher getPageDispatcher(final Configuration configuration);
	
	SdocGenerator getGenerator(final Configuration configuration);
	Control getControl(final Configuration configuration, Composite container);
}
