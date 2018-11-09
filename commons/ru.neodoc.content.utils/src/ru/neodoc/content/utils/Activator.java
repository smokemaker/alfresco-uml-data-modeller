package ru.neodoc.content.utils;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ru.neodoc.content.utils.uml.profile.dataconverter.Preloader;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		Preloader.preload();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}

}
