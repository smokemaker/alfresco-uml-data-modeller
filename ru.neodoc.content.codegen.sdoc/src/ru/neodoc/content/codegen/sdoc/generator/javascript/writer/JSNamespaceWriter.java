package ru.neodoc.content.codegen.sdoc.generator.javascript.writer;

import ru.neodoc.content.codegen.sdoc.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.AbstractWriter;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.NamespaceWriter;
import ru.neodoc.content.codegen.sdoc.wrap.ClassWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.NamespaceWrapper;

public class JSNamespaceWriter extends NamespaceWriter {

	public JSNamespaceWriter(NamespaceWrapper baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter, annotationProvider);
		// TODO Auto-generated constructor stub
	}

	public JSNamespaceWriter(NamespaceWrapper baseWrapper, SdocGeneratorReporter reporter) {
		super(baseWrapper, reporter);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void defineClassName() {
		this.className = /*this.prefix.substring(0, 1).toUpperCase()
				+ this.prefix.substring(1)
				+ "Model"*/this.namespaceWrapper.getTargetJavaScriptName();	
	}
	
	@Override
	protected AbstractWriter createClassWriter(ClassWrapper cw) {
		return new JSClassWriter(cw, reporter, annotationProvider);
	}
	
	@Override
	protected void writeStartElement(StringBuffer sb, int level) {
		reporter.started("Creating namespace", baseWrapper);
		indent(sb, level);
		sb.append("model.")
		.append(this.className).append(" = ");
		openln(sb);
	}
	
	@Override
	protected void writeContent(StringBuffer sb, int level) {
		
		for (AbstractWriter aw: children)
			aw.write(sb, level+1);	

		ln(sb);
		indented(sb, level+1, "/** PREFIX */");
		indented(sb, level+1, 
				this.prefix.toUpperCase() + ": '" + this.prefix + "',");
		ln(sb);
		indented(sb, level+1, "/** URI */");
		indented(sb, level+1, 
				this.prefix.toUpperCase() + "_URI: '" + this.namespaceWrapper.getNamespace().getUri() + "'");
		ln(sb);	
	}
	
	@Override
	protected void writeEnd(StringBuffer sb, int level) {
		super.writeEnd(sb, level);
		sb.append(";");
	}
}
