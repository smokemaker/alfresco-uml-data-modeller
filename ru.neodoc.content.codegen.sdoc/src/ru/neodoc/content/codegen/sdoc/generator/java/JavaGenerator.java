package ru.neodoc.content.codegen.sdoc.generator.java;

import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.codegen.sdoc.CodegenManager;
import ru.neodoc.content.codegen.sdoc.generator.AbstractSdocGenerator;
import ru.neodoc.content.codegen.sdoc.wrap.NamespaceWrapper;

public class JavaGenerator extends AbstractSdocGenerator {

	protected List<NamespaceGenerator> namespaceGenerators = new ArrayList<>();
	
	public JavaGenerator(CodegenManager codegenManager) {
		super(codegenManager);
	}

	@Override
	public void setReporter(SdocGeneratorReporter reporter) {
		super.setReporter(reporter);
		for (NamespaceGenerator nsg: namespaceGenerators)
			nsg.setReporter(reporter);
	}
	
	@Override
	protected boolean doInit() {
		List<NamespaceWrapper> wrappers = codegenManager.getWrappedNamespaceList();
		for (NamespaceWrapper nsw: wrappers){
			NamespaceGenerator nsg = createNamespaceGenerator(nsw);
			nsg.setReporter(reporter);
			namespaceGenerators.add(nsg);
		}
		return true;
	}
	
	protected NamespaceGenerator createNamespaceGenerator(NamespaceWrapper nsw){
		NamespaceGenerator nsg = new NamespaceGenerator(codegenManager, nsw);
		return nsg;
	} 
	
	@Override
	protected int doCountOperations() {
		int result = 0;
		for (NamespaceGenerator nsg: namespaceGenerators)
			result += nsg.countOperations();
		return result;
	}
	
	@Override
	protected void doGenerate() {
		for (NamespaceGenerator nsg: namespaceGenerators)
			nsg.generate();
	}

	@Override
	protected void doSimulate() {
		for (NamespaceGenerator nsg: namespaceGenerators)
			nsg.simulate();
	}
	
}
