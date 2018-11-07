package ru.neodoc.content.codegen.sdoc2.generator.annotation;

public class SdocAnnotationFactoryFactory {
	
	public static SdocAnnotationFactory getDefaultFactory(){
		return new NullAnnotationFactory();
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
