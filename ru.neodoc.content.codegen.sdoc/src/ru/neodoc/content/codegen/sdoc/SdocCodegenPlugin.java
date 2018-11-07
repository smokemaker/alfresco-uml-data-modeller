package ru.neodoc.content.codegen.sdoc;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class SdocCodegenPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "ru.neodoc.content.codegen.sdoc"; //$NON-NLS-1$

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}
	
	// The shared instance
	private static SdocCodegenPlugin plugin;
	
	public SdocCodegenPlugin(){
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		plugin = null;
		super.stop(bundleContext);
	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static SdocCodegenPlugin getDefault() {
		return plugin;
	}

	@Override
	public void saveDialogSettings() {
		// TODO Auto-generated method stub
		super.saveDialogSettings();
	}
	
	public static IPreferenceStore getStore(){
		return SdocCodegenPlugin.getDefault().getPreferenceStore();
	}
	
	public static boolean getBoolean(String name){
		return SdocCodegenPlugin.getStore().getBoolean(name);
	}
	
}
