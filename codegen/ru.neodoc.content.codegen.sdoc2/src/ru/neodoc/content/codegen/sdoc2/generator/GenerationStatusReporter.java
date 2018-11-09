package ru.neodoc.content.codegen.sdoc2.generator;

public interface GenerationStatusReporter {
	
	void log(String message);
	void logln(String message);
	
	void totalUnits(int count);
	void worked(int count);
	
}
