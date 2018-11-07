package ru.neodoc.content.codegen.sdoc.generator;

import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.codegen.sdoc.CodegenManager;
import ru.neodoc.content.codegen.sdoc.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc.generator.java.JavaGenerator;
import ru.neodoc.content.codegen.sdoc.generator.javascript.JavaScriptGenerator;
import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;
import ru.neodoc.content.modeller.utils.uml.elements.BaseNamedElement;

public class SdocGenerationManager implements SdocGeneratorReporter {
	
	protected CodegenManager codegenmanager;
	
	protected GenerationStatusReporter statusReporter = new NullGenerationStatusReporter();
	
	protected boolean isPrepared = false;
	
	protected List<SdocGenerator> generators = new ArrayList<>();
	
	public SdocGenerationManager(CodegenManager manager){
		super();
		this.codegenmanager = manager;
	}

	public GenerationStatusReporter getStatusReporter() {
		return statusReporter;
	}

	public void setStatusReporter(GenerationStatusReporter statusReporter) {
		if (statusReporter != null)
			this.statusReporter = statusReporter;
		else
			this.statusReporter = new NullGenerationStatusReporter();
	}
	
	public boolean prepare(){
		if (codegenmanager == null){
			isPrepared = false;
			return isPrepared;
		}
		
		if (codegenmanager.isGenerateJava())
			generators.add(new JavaGenerator(codegenmanager));
		if (codegenmanager.isGenerateJavaScript())
			generators.add(new JavaScriptGenerator(codegenmanager));
		
		for (SdocGenerator g: generators){
			g.setReporter(this);
		}
		
		int countOfUnits = countUnits();
		statusReporter.totalUnits(countOfUnits);
		
		isPrepared = true;
		return isPrepared;
	}
	
	public void generate(){
		
		statusReporter.logln(" --- --- --- --- --- ");
		statusReporter.logln("Starting generation");
		statusReporter.logln(" --- --- --- --- --- ");
		statusReporter.logln("");
		
		for (SdocGenerator g: generators)
			g.generate();
		
//		List<NamespaceWrapper> wrappers = codegenmanager.getWrappedNamespaceList();
		
/*		for (NamespaceWrapper nsw: wrappers) {
			// generate namespace itself
			Namespace ns = nsw.getNamespace();
			statusReporter.logln("Generating namespace " + ns.getPrefix() + "{" + ns.getUri() + "}");
			statusReporter.worked(1);
			
			// generate children
			for (BaseWrapper bw: nsw.getChildren()) {
				if (bw instanceof TypeWrapper) {
					statusReporter.log("\tGenerating type ");
				}
				if (bw instanceof AspectWrapper) {
					statusReporter.log("\tGenerating aspect ");
				}
				
				statusReporter.logln(bw.getName() + " [" + bw.getTargetJavaName() + "]");
				
				statusReporter.worked(1);
			}
			
			statusReporter.logln("Generating imports");
			statusReporter.worked(1);
			
		}
*/		
		statusReporter.logln("");
		statusReporter.logln("/* ---------------");
		statusReporter.logln("Done");
		statusReporter.logln("--------------- */");
	}
	
	protected int countUnits(){
		
		int result = 0;
/*		List<NamespaceWrapper> namespaces = codegenmanager.getWrappedNamespaceList();
		for (NamespaceWrapper nsw: namespaces) {
			int nsUnits = 1; // namespace itself
			nsUnits += 1; // imports
			List<BaseWrapper> list = nsw.getChildren();
			if (list != null)
				nsUnits += list.size();
			result += nsUnits;
		}*/
		
		for (SdocGenerator g: generators)
			result += g.countOperations();
		
		return result;
	}

	protected String object2String(Object object){
		if (object == null)
			return "";
		
		if (BaseWrapper.class.isAssignableFrom(object.getClass())){
			BaseWrapper bw = (BaseWrapper)object;
			return bw.getName() + " [" + bw.getFullTargetJavaName() + "]";
		}
		
		if (BaseNamedElement.class.isAssignableFrom(object.getClass())){
			BaseNamedElement bne = (BaseNamedElement)object;
			return bne.getName() + 
					(bne.isValid()
					?("[" + bne.getElement().getClass().getSimpleName() + "]")
					:"");
		}
		
		return object.toString();
	}
	
	@Override
	public void message(String message) {
		statusReporter.logln(message);
	}

	@Override
	public void started(String operationName, Object object) {
		statusReporter.logln("OPERATION STARTED: " + operationName + ";"
				+(object!=null
				?" OBJECT: " + object2String(object)
				:""));
	}

	@Override
	public void finished(String operationName) {
		statusReporter.logln("OPERATION FINISHED: " + operationName);
	}

	@Override
	public void objectDone(Object object) {
		if (object != null)
			statusReporter.logln("OBJECT PROCESSED: " + object2String(object));
		statusReporter.worked(1);
	}

	@Override
	public void error(Exception e) {
		statusReporter.logln(e.toString());
	}

	@Override
	public void error(String message) {
		statusReporter.logln("ERROR: " + message);
	}

}
