package ru.neodoc.content.codegen.sdoc2.extension;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.PageDispatcher;

public interface SdocCodegenExtensionConfiguration extends Configuration {

	SdocCodegenExtension getExtension();
	PageDispatcher getPageDispatcher();
	SdocGenerator getGenerator();
	Control getControl(Composite container);

}
