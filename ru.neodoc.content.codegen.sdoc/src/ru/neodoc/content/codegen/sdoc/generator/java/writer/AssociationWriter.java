package ru.neodoc.content.codegen.sdoc.generator.java.writer;

import ru.neodoc.content.codegen.sdoc.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;

public class AssociationWriter extends AbstractWriter {
	
	public <T extends BaseWrapper> AssociationWriter(T baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter, annotationProvider);
		// TODO Auto-generated constructor stub
	}

	public <T extends BaseWrapper> AssociationWriter(T baseWrapper, SdocGeneratorReporter reporter) {
		super(baseWrapper, reporter);
		// TODO Auto-generated constructor stub
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
