package ru.neodoc.content.codegen.sdoc.generator.java.annotation;

import ru.neodoc.content.codegen.sdoc.generator.java.annotation.factory.DefaultAnnotationFactory;

public class SdocAnnotationFactoryFactory {
	
	public static SdocAnnotationFactory getDefaultFactory(){
		return new DefaultAnnotationFactory();
	}
	
	public static SdocAnnotationFactory getFactory(){
		// read settings and if none, then
		return getDefaultFactory();
	}
	
	public static SdocAnnotationFactory getFactory(String name){
		// read settings and if none, then
		return getDefaultFactory();
	}
}
