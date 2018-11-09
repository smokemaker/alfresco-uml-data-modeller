package ru.neodoc.content.codegen.sdoc2.utils;

public interface Skippable {

	boolean canSkip();
	void skipped();
	
}
