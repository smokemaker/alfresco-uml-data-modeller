package ru.neodoc.content.codegen.sdoc2.extension.java.generator;

import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.generator.AbstractSdocGenerator;
import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;

public class JavaGenerator extends AbstractSdocGenerator {

	protected List<NamespaceGenerator> namespaceGenerators = new ArrayList<>();
	
	public JavaGenerator(Configuration configuration) {
		super(configuration);
	}

	@Override
	public void setReporter(SdocGeneratorReporter reporter) {
		super.setReporter(reporter);
		for (NamespaceGenerator nsg: namespaceGenerators)
			nsg.setReporter(reporter);
	}
	
	@Override
	protected boolean doInit() {
		List<NamespaceWrapper> wrappers = getCodegenManager().getWrappedNamespaceList();
		for (NamespaceWrapper nsw: wrappers){
			NamespaceGenerator nsg = createNamespaceGenerator(nsw);
			nsg.setReporter(reporter);
			namespaceGenerators.add(nsg);
		}
		return true;
	}
	
	protected NamespaceGenerator createNamespaceGenerator(NamespaceWrapper nsw){
		NamespaceGenerator nsg = new NamespaceGenerator(getConfiguration(), nsw);
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
