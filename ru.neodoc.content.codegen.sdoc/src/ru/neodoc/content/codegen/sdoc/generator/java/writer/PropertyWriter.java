package ru.neodoc.content.codegen.sdoc.generator.java.writer;

import ru.neodoc.content.codegen.sdoc.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc.wrap.PropertyWrapper;

public class PropertyWriter extends AbstractWriter {

	public PropertyWriter(PropertyWrapper baseWrapper, SdocGeneratorReporter reporter) {
		super(baseWrapper, reporter);
	}

	@Override
	public void write(StringBuffer target, int level) {
		ln(target);
		super.write(target, level);
	}
	
	@Override
	protected void doWriteAnnotation(StringBuffer sb, int level) {
		String annotation = annotationProvider.getAnnotation(baseWrapper);
		if (annotation!=null){
			indent(sb, level);
			sb.append("@").append(annotation);
			ln(sb);
		}
	}

	@Override
	protected void writeStartElement(StringBuffer sb, int level) {

	}

	@Override
	protected void writeContent(StringBuffer sb, int level) {
		indent(sb, level);
		sb.append(createQNameString());
	}

	@Override
	protected void writeEnd(StringBuffer sb, int level) {
		ln(sb);
	}

}
