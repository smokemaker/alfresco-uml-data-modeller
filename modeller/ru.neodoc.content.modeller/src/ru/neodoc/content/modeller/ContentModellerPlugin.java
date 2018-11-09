package ru.neodoc.content.modeller;

import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ContentModellerPlugin extends EclipseUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "ru.neodoc.content.modeller"; //$NON-NLS-1$

	// The shared instance
	private static ContentModellerPlugin plugin;
	
	/**
	 * The constructor
	 */
	public ContentModellerPlugin() {
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
	public static ContentModellerPlugin getDefault() {
		return plugin;
	}

}
