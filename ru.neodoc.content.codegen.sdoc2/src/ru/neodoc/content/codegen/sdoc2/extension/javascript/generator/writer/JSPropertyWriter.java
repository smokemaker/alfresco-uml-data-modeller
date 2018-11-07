package ru.neodoc.content.codegen.sdoc2.extension.javascript.generator.writer;

import ru.neodoc.content.codegen.sdoc2.extension.java.generator.writer.PropertyWriter;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.WrapperJSExtension;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc2.wrap.PropertyWrapper;

public class JSPropertyWriter extends PropertyWriter {

	public JSPropertyWriter(PropertyWrapper baseWrapper, SdocGeneratorReporter reporter) {
		super(baseWrapper, reporter);
		// TODO Auto-generated constructor stub
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
