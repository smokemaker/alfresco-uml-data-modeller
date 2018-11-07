package ru.neodoc.content.codegen.sdoc2.extension;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import ru.neodoc.content.codegen.sdoc2.config.PreferencesAwareConfiguration;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.PageDispatcher;

public class SdocCodegenExtensionConfigurationImpl extends PreferencesAwareConfiguration
		implements SdocCodegenExtensionConfiguration {

	protected SdocCodegenExtension owner;
	
	public SdocCodegenExtensionConfigurationImpl(IPreferenceStore store, SdocCodegenExtension owner) {
		super(store);
		this.owner = owner;
	}

	@Override
	public SdocCodegenExtension getExtension() {
		return owner;
	}

	@Override
	public PageDispatcher getPageDispatcher() {
		return owner.getPageDispatcher(this);
	}

	@Override
	public SdocGenerator getGenerator() {
		return owner.getGenerator(this);
	}

	@Override
	public Control getControl(Composite container) {
		return owner.getControl(this, container);
	}

}
