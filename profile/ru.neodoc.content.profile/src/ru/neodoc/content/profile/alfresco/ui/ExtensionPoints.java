package ru.neodoc.content.profile.alfresco.ui;

public enum ExtensionPoints {
	
	REGISTERED_MODEL("ru.neodoc.content.extensionpoint.registeredModel");
	
	String id;
	
	private ExtensionPoints(String value) {
		this.id = value;
	}
	
	public String id() {
		return this.id;
	}
	
}
