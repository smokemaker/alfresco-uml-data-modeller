package ru.neodoc.content.codegen.sdoc2.extension.java.generator.writer;

import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc2.generator.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc2.wrap.AssociationMainWrapper;

public class AssociationWriter extends AbstractJavaWriter<AssociationMainWrapper<?>> {
	
	public AssociationWriter(AssociationMainWrapper<?> baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter, annotationProvider);
		// TODO Auto-generated constructor stub
	}

	public AssociationWriter(AssociationMainWrapper<?> baseWrapper, SdocGeneratorReporter reporter) {
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
		String annotation = annotationProvider==null?null:annotationProvider.getAnnotation(baseWrapper);
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
