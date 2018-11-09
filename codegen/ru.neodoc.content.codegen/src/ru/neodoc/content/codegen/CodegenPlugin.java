package ru.neodoc.content.codegen;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CodegenPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "ru.neodoc.content.codegen"; //$NON-NLS-1$

	// extension point schema constants
	public static final String EP_GENERATORS = "generators"; //$NON-NLS-1$
	public static final String EP_GEN_PROP_ID = "id";
	public static final String EP_GEN_PROP_NAME = "display_name";
	public static final String EP_GEN_PROP_DESCRIPTION = "description";
	public static final String EP_GEN_PROP_COMPONENT = "component";
	
	// The shared instance
	private static CodegenPlugin plugin;
	
	/**
	 * The constructor
	 */
	public CodegenPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static CodegenPlugin getDefault() {
		return plugin;
	}

	public static IPreferenceStore getStore(){
		return CodegenPlugin.getDefault().getPreferenceStore();
	}
	
	public static boolean getBoolean(String name){
		return CodegenPlugin.getStore().getBoolean(name);
	}
}
