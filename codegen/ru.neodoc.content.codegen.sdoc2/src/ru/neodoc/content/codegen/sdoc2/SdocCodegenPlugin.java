package ru.neodoc.content.codegen.sdoc2;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ru.neodoc.content.codegen.sdoc2.preferences.PreferenceInitializer;

public class SdocCodegenPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "ru.neodoc.content.codegen.sdoc2"; //$NON-NLS-1$

	public static interface EP_CODEGEN_EXTENSION {
	
		public static final String ID = PLUGIN_ID + "." + "codegenExtension";
		
		public static interface PROPERTIES {
			public static final String ID 			= "id";
			public static final String COMPONENT 	= "component";
			public static final String USE_AFTER 	= "useAfter";
			public static final String USE_BEFORE 	= "useBefore";
			public static final String USE_FIRST 	= "useFirst";
			public static final String USE_LAST 	= "useLast";
		}
		
	}
	
	public static interface EP_CODEGEN_ANNOTATION_FACTORY {
		
		public static final String ID = PLUGIN_ID + "." + "annotationFactory";
		
		public static interface PROPERTIES {
			public static final String ID 			= "id";
			public static final String COMPONENT 	= "component";
		}
	}
	
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
	
	@Override
	public IPreferenceStore getPreferenceStore() {
		IPreferenceStore store = super.getPreferenceStore(); 
		if (!PreferenceInitializer.initialized) {
			(new PreferenceInitializer()).initializeDefaultPreferences(store);
		}
		return store;
	}
}
