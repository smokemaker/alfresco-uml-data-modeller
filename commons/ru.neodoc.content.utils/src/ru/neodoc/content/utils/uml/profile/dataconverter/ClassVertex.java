package ru.neodoc.content.utils.uml.profile.dataconverter;

public class ClassVertex {

	protected Class<?> objectClass;
	
	public ClassVertex(Class<?> objectClass) {
		super();
		this.objectClass = objectClass;
	}

	public Class<?> getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(Class<?> objectClass) {
		this.objectClass = objectClass;
	}
	
}
