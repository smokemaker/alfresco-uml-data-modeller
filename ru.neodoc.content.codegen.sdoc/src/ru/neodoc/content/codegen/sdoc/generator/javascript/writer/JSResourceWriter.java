package ru.neodoc.content.codegen.sdoc.generator.javascript.writer;

import ru.neodoc.content.codegen.sdoc.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.AbstractWriter;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.ResourceWriter;
import ru.neodoc.content.codegen.sdoc.wrap.NamespaceWrapper;

public class JSResourceWriter extends ResourceWriter {

	public JSResourceWriter(NamespaceWrapper baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter, annotationProvider);
	}
	
	@Override
	protected AbstractWriter createNamespaceWriter(NamespaceWrapper baseWrapper) {
		return new JSNamespaceWriter(baseWrapper, this.reporter, this.annotationProvider);
	}
	
	@Override
	protected void writeStartElement(StringBuffer sb, int level) {
		indent(sb, level);
		sb.append("Ext.namespace(\"model\");");
		ln(sb);
	}
	
	
}
