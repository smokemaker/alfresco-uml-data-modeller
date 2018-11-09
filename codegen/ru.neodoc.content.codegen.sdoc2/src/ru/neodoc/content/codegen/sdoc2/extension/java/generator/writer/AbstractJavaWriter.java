package ru.neodoc.content.codegen.sdoc2.extension.java.generator.writer;

import ru.neodoc.content.codegen.sdoc2.extension.java.WrapperJavaExtension;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc2.generator.writer.AbstractWriter;
import ru.neodoc.content.codegen.sdoc2.generator.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;;

public abstract class AbstractJavaWriter<T extends AbstractWrapper> extends AbstractWriter<T> {

	public AbstractJavaWriter(T baseWrapper, SdocGeneratorReporter reporter) {
		super(baseWrapper, reporter);
		// TODO Auto-generated constructor stub
	}
	
	public AbstractJavaWriter(T baseWrapper, SdocGeneratorReporter reporter, 
			AnnotationProvider annotationProvider){
		super(baseWrapper, reporter, annotationProvider);
		this.baseWrapper = baseWrapper;
		this.reporter = reporter;
		this.annotationProvider = annotationProvider;
	}
	
	protected WrapperJavaExtension getWrapperExtension() {
		return WrapperJavaExtension.get(baseWrapper);
	}
	
	protected String createQNameString(){
		StringBuffer sb = new StringBuffer();
		sb.append("public static final ")
			.append("QName ")
			.append(getWrapperExtension().getTargetJavaName())
			.append(" = ")
			.append("QName.createQName(")
			.append(getURIStringForQName())
			.append(", \"")
			.append(baseWrapper.getName())
			.append("\");");
		
		return sb.toString();
	}

}
