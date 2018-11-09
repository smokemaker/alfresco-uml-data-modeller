package ru.neodoc.content.modeller.extensions;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ContentModellerExtensionsPlugin extends AbstractUIPlugin {

	private static ContentModellerExtensionsPlugin _INSTANCE = new ContentModellerExtensionsPlugin();
	
	public ContentModellerExtensionsPlugin() {
		super();
	}

	public static ContentModellerExtensionsPlugin getDefault() {
		return _INSTANCE;
	}
	
}
