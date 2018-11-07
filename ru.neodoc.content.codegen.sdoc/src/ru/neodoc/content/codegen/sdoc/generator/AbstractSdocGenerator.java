package ru.neodoc.content.codegen.sdoc.generator;

import ru.neodoc.content.codegen.sdoc.CodegenManager;

abstract public class AbstractSdocGenerator implements SdocGenerator {

	protected CodegenManager codegenManager;
	
	protected SdocGeneratorReporter reporter;
	
	private boolean isInited = false;
	
	public AbstractSdocGenerator(CodegenManager codegenManager){
		super();
		this.codegenManager = codegenManager;
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
