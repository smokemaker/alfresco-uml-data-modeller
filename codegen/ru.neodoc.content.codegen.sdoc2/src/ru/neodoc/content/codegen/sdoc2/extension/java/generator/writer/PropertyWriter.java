package ru.neodoc.content.codegen.sdoc2.extension.java.generator.writer;

import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc2.wrap.PropertyWrapper;

public class PropertyWriter extends AbstractJavaWriter<PropertyWrapper> {

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
