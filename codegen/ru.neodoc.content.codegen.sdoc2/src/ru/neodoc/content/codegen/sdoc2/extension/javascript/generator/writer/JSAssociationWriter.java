package ru.neodoc.content.codegen.sdoc2.extension.javascript.generator.writer;

import ru.neodoc.content.codegen.sdoc2.extension.java.generator.writer.AssociationWriter;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.WrapperJSExtension;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc2.generator.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc2.wrap.AssociationMainWrapper;

public class JSAssociationWriter extends AssociationWriter {

	public JSAssociationWriter(AssociationMainWrapper<?> baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter, annotationProvider);
	}

	public JSAssociationWriter(AssociationMainWrapper<?> baseWrapper, SdocGeneratorReporter reporter) {
		super(baseWrapper, reporter);
	}
	
	@Override
	protected void writeContent(StringBuffer sb, int level) {
		indent(sb, level);
		sb.append(WrapperJSExtension.get(baseWrapper).getTargetJSName())
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
