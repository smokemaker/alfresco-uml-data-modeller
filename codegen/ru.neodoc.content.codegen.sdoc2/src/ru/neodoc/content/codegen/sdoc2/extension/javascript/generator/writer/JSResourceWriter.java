package ru.neodoc.content.codegen.sdoc2.extension.javascript.generator.writer;

import ru.neodoc.content.codegen.sdoc2.extension.java.generator.writer.ResourceWriter;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc2.generator.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;

/*import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc2.generator.java.writer.AbstractWriter;
import ru.neodoc.content.codegen.sdoc2.generator.java.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc2.generator.java.writer.ResourceWriter;
import ru.neodoc.content.codegen.sdoc2.wrap.old.NamespaceWrapper;
*/
public class JSResourceWriter extends ResourceWriter {

	public JSResourceWriter(NamespaceWrapper baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter, annotationProvider);
	}
	
	@Override
	protected JSNamespaceWriter createNamespaceWriter(NamespaceWrapper baseWrapper) {
		return new JSNamespaceWriter(baseWrapper, this.reporter, this.annotationProvider);
	}
	
	@Override
	protected void writeStartElement(StringBuffer sb, int level) {
		indent(sb, level);
		sb.append("Ext.namespace(\"model\");");
		ln(sb);
	}
	
	
}
