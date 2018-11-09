package ru.neodoc.content.codegen.sdoc2.generator;

public interface SdocGenerator {

	void setReporter(SdocGeneratorReporter reporter);
	
	int countOperations();
	void generate();
	void simulate();
	
	public static interface SdocGeneratorReporter {
		
		void message(String message);
		
		void started(String operationName, Object object);
		void finished(String operationName);
		void objectDone(Object object);
	
		void error(Exception e);
		void error(String message);
		
	}
	
}
