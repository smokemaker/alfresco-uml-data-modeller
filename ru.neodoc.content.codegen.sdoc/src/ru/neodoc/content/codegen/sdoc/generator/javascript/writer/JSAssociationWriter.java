package ru.neodoc.content.codegen.sdoc.generator.javascript.writer;

import ru.neodoc.content.codegen.sdoc.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.AssociationWriter;
import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;

public class JSAssociationWriter extends AssociationWriter {

	public <T extends BaseWrapper> JSAssociationWriter(T baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter, annotationProvider);
		// TODO Auto-generated constructor stub
	}

	public <T extends BaseWrapper> JSAssociationWriter(T baseWrapper, SdocGeneratorReporter reporter) {
		super(baseWrapper, reporter);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void writeContent(StringBuffer sb, int level) {
		indent(sb, level);
		sb.append(baseWrapper.getTargetJavaScriptName())
			.append(": ")
			.append("'")
			.append(baseWrapper.getName())
			.append("'");
	}
	
	@Override
	protected void writeEnd(StringBuffer sb, int level) {
		// NOOP
	}
}
