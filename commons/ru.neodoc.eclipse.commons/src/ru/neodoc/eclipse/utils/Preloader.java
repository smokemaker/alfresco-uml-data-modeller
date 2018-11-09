package ru.neodoc.eclipse.utils;

public abstract class Preloader {
	
	protected ClassLoader classLoader;
	
	protected Preloader(ClassLoader classLoader) {
		super();
		this.classLoader = classLoader;
	}
	
	public void load() {
		load(getClasses());
	}
	
	public void load(Class<?>...classes) {
		for (int i = 0; i < classes.length; i++) {
			Class<?> class1 = classes[i];
			try {
				Class.forName(class1.getName(), true, this.classLoader);
			} catch (ClassNotFoundException e) {
				// e.printStackTrace();
			}
			
		}
	}

	public abstract Class<?>[] getClasses();
}
