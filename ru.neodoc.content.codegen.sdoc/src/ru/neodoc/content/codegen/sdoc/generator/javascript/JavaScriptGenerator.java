package ru.neodoc.content.codegen.sdoc.generator.javascript;

import ru.neodoc.content.codegen.sdoc.CodegenManager;
import ru.neodoc.content.codegen.sdoc.generator.java.JavaGenerator;
import ru.neodoc.content.codegen.sdoc.generator.java.NamespaceGenerator;
import ru.neodoc.content.codegen.sdoc.wrap.NamespaceWrapper;

public class JavaScriptGenerator extends JavaGenerator {

	public JavaScriptGenerator(CodegenManager codegenManager) {
		super(codegenManager);
	}

	@Override
	protected NamespaceGenerator createNamespaceGenerator(NamespaceWrapper nsw) {
		return new JSNamespaceGenerator(codegenManager, nsw);
	}
	
}
