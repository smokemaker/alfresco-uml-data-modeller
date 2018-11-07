package ru.neodoc.content.codegen.sdoc.generator.javascript.writer;

import ru.neodoc.content.codegen.sdoc.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.PropertyWriter;
import ru.neodoc.content.codegen.sdoc.wrap.PropertyWrapper;

public class JSPropertyWriter extends PropertyWriter {

	public JSPropertyWriter(PropertyWrapper baseWrapper, SdocGeneratorReporter reporter) {
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
