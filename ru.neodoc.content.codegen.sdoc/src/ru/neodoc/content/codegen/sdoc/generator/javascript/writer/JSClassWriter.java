package ru.neodoc.content.codegen.sdoc.generator.javascript.writer;

import ru.neodoc.content.codegen.sdoc.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.AbstractWriter;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc.generator.java.writer.ClassWriter;
import ru.neodoc.content.codegen.sdoc.wrap.AspectWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.ClassWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.PropertyWrapper;

public class JSClassWriter extends ClassWriter {

	public JSClassWriter(ClassWrapper baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter, annotationProvider);
		// TODO Auto-generated constructor stub
	}

	public JSClassWriter(ClassWrapper baseWrapper, SdocGeneratorReporter reporter) {
		super(baseWrapper, reporter);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected AbstractWriter createAssociationWriter(BaseWrapper bw) {
		return new JSAssociationWriter(bw, reporter, annotationProvider);
	}
	
	@Override
	protected AbstractWriter createPropertyWriter(PropertyWrapper pw) {
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
		sb.append(this.baseWrapper.getTargetJavaScriptName())
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
