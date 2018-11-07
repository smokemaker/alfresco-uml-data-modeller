package ru.neodoc.content.codegen.sdoc2.generator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.codegen.sdoc2.CodegenManager;
import ru.neodoc.content.codegen.sdoc2.extension.SdocCodegenExtensionConfiguration;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Named;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

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

		for (SdocCodegenExtensionConfiguration config: codegenmanager.getActiveConfigurations()) {
			SdocGenerator generator = config.getGenerator();
			if (generator!=null)
				generators.add(generator);
		}
		
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
		
		if (AbstractWrapper.class.isAssignableFrom(object.getClass())){
			AbstractWrapper bw = (AbstractWrapper)object;
			return bw.getName();
		}
		
		if (ProfileStereotype.class.isAssignableFrom(object.getClass())){
			ProfileStereotype ps = (ProfileStereotype)object;
			Named named = ps.get(Named.class);
			Element el = ps.getElement();
			return (named==null?"":named.getName()) + 
					"[" + el.getClass().getSimpleName() + "]";
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
