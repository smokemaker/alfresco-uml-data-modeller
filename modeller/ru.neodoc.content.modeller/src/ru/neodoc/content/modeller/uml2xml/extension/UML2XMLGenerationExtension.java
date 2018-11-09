package ru.neodoc.content.modeller.uml2xml.extension;

import ru.neodoc.content.modeller.tasks.ExecutionResult;

public interface UML2XMLGenerationExtension {

	public ExecutionResult prepare(Package model, String location);
	
}
