package ru.neodoc.content.codegen.sdoc2.extension.javascript.generator;

import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.extension.java.generator.JavaGenerator;
import ru.neodoc.content.codegen.sdoc2.extension.java.generator.NamespaceGenerator;
import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;

public class JSGenerator extends JavaGenerator {

	public JSGenerator(Configuration configuration) {
		super(configuration);
	}

	@Override
	protected NamespaceGenerator createNamespaceGenerator(NamespaceWrapper nsw) {
		return new JSNamespaceGenerator(configuration, nsw);
	}
	
}
