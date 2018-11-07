package ru.neodoc.content.codegen.sdoc.generator;

public interface GenerationStatusReporter {
	
	void log(String message);
	void logln(String message);
	
	void totalUnits(int count);
	void worked(int count);
	
}
