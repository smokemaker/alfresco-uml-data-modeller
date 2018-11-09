package ru.neodoc.content.codegen.sdoc2.extension;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import ru.neodoc.content.codegen.sdoc2.config.Configurable;
import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.extension.preferences.ExtensionPreferenceConstants;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.PageDispatcher;

public abstract class SdocCodegenExtensionImpl implements SdocCodegenExtension {

	protected String optionMessage = null;
	protected IPreferenceStore preferenceStore = null;
	
	protected static Map<String, IPreferenceStore> preferenceStores = new HashMap<>();
	
	public static IPreferenceStore getPreferenceStore(String qualifier) {
		if (!preferenceStores.containsKey(qualifier)) {
			preferenceStores.put(qualifier, new ScopedPreferenceStore(InstanceScope.INSTANCE, qualifier));
		}
		return preferenceStores.get(qualifier);
	}
	
	public SdocCodegenExtensionImpl() {
		super();
		initOptionMessage();
		preferenceStore = getPreferenceStore();
	}
	
	protected IPreferenceStore getPreferenceStore() {
		return getPreferenceStore(getPreferenceQualifier());
	} 
	
	protected abstract String getPreferenceQualifier();
	
	protected abstract void initOptionMessage();
	
	@Override
	public String getOptionMessage() {
		return optionMessage;
	}

	@Override
	public SdocCodegenExtensionConfiguration getConfiguration() {
		SdocCodegenExtensionConfiguration result = new SdocCodegenExtensionConfigurationImpl(preferenceStore, this);
		result.setActive(result.getBoolean(ExtensionPreferenceConstants.P_DEFAULT_ACTIVE));
		return result;
	}

	@Override
	public PageDispatcher getPageDispatcher(Configuration configuration) {
		PageDispatcher pd = doGetPageDispatcher(configuration);
		if (pd instanceof Configurable)
			((Configurable)pd).setConfiguration(configuration);
		initPageDispatcher(pd);
		return pd;
	}

	protected abstract PageDispatcher doGetPageDispatcher(Configuration configuration);
	
	protected void initPageDispatcher(PageDispatcher pageDispatcher) {
		// to override
	}
	
	@Override
	public SdocGenerator getGenerator(Configuration configuration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Control getControl(final Configuration configuration, Composite container) {
        Button button = new Button(container, SWT.CHECK);
        button.setText(getOptionMessage());
        button.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));
        button.setSelection(configuration.getBoolean(ExtensionPreferenceConstants.P_DEFAULT_ACTIVE));
        button.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		Button b = (Button)e.getSource();
        		configuration.setActive(b.getSelection());
        		// FIXME implement
        		// getWizard().getContainer().updateButtons();
        	}
		});
        return button;
	}
	
}
