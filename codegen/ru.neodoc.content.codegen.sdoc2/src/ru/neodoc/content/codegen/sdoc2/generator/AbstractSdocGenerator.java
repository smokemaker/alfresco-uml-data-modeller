package ru.neodoc.content.codegen.sdoc2.generator;

import ru.neodoc.content.codegen.sdoc2.CodegenManager;
import ru.neodoc.content.codegen.sdoc2.config.CommonConfigurable;
import ru.neodoc.content.codegen.sdoc2.config.Configuration;

abstract public class AbstractSdocGenerator extends CommonConfigurable implements SdocGenerator {

	protected SdocGeneratorReporter reporter;
	
	private boolean isInited = false;
	
	public AbstractSdocGenerator(Configuration configuration){
		super();
		setConfiguration(configuration);
	}

	protected CodegenManager getCodegenManager() {
		return (CodegenManager)getConfiguration().getValue(CodegenManager.PROP_NAME);
	}
	
	public SdocGeneratorReporter getReporter() {
		return reporter;
	}

	public void setReporter(SdocGeneratorReporter reporter) {
		this.reporter = reporter;
	}
	
	protected final boolean init(){
		if (!isInited)
			isInited = doInit();
		return isInited;
	}

	protected abstract boolean doInit();
	
	@Override
	public final int countOperations() {
		if (!init())
			return 0;
		return doCountOperations();
	}
	
	protected abstract int doCountOperations();
	
	@Override
	public final void generate() {
		if (!init())
			return;
		doGenerate();
	}
	
	protected abstract void doGenerate();
	
	@Override
	public void simulate() {
		if (!init())
			return;
		doSimulate();
	}
	
	protected abstract void doSimulate();
}
