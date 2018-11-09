package ru.neodoc.content.codegen.sdoc2.extension.javascript.generator.writer;

import ru.neodoc.content.codegen.sdoc2.extension.java.generator.writer.AssociationWriter;
import ru.neodoc.content.codegen.sdoc2.extension.java.generator.writer.ClassWriter;
import ru.neodoc.content.codegen.sdoc2.extension.java.generator.writer.PropertyWriter;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.WrapperJSExtension;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc2.generator.writer.AbstractWriter;
import ru.neodoc.content.codegen.sdoc2.generator.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractAlfrescoClassWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.AspectWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.AssociationMainWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.PropertyWrapper;

public class JSClassWriter extends ClassWriter {

	public JSClassWriter(AbstractAlfrescoClassWrapper<?> baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter, annotationProvider);
		// TODO Auto-generated constructor stub
	}

	public JSClassWriter(AbstractAlfrescoClassWrapper<?> baseWrapper, SdocGeneratorReporter reporter) {
		super(baseWrapper, reporter);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected AssociationWriter createAssociationWriter(AssociationMainWrapper<?> bw) {
		return new JSAssociationWriter(bw, reporter, annotationProvider);
	}
	
	@Override
	protected PropertyWriter createPropertyWriter(PropertyWrapper pw) {
		return new JSPropertyWriter(pw, reporter);
	}
	
	@Override
	protected void writeStartElement(StringBuffer sb, int level) {
		indent(sb, level);
		sb.append(this.baseWrapper.getName().toUpperCase())
			.append(": ");
		openln(sb);
		
	}
	
	@Override
	protected void writeContent(StringBuffer sb, int level) {
		super.writeContent(sb, level);
		
		ln(sb);
		indent(sb, level+1);
		sb.append("/** ")
			.append((this.baseWrapper instanceof AspectWrapper?"Aspect":"Type"))
			.append(" '")
			.append(this.baseWrapper.getName())
			.append("' */");
		
		ln(sb);
		indent(sb, level+1);
		sb.append(WrapperJSExtension.get(this.baseWrapper).getTargetJSName())
			.append(": ")
			.append("'")
			.append(this.baseWrapper.getName())
			.append("'");
		ln(sb);
	}
	
	@Override
	protected void separateChild(StringBuffer sb, int level, boolean isFirst, boolean isLast) {
		sb.append(",");
		ln(sb);
	}
}
