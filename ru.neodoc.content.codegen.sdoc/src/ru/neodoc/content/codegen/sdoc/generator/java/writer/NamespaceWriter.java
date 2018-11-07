package ru.neodoc.content.codegen.sdoc.generator.java.writer;

import ru.neodoc.content.codegen.sdoc.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.ClassWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.NamespaceWrapper;

public class NamespaceWriter extends AbstractWriter {

	protected NamespaceWrapper namespaceWrapper;
	
	protected String uriFieldName;
	protected String className;
	protected String prefix;
	
	public NamespaceWriter(NamespaceWrapper baseWrapper, SdocGeneratorReporter reporter) {
		super(baseWrapper, reporter);
		this.namespaceWrapper = baseWrapper;
		createChildren();
	}
	
	public NamespaceWriter(NamespaceWrapper baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		this(baseWrapper, reporter);
		setAnnotationProvider(annotationProvider);
		setChldrenAnnotationProvider();
		
		this.prefix = this.namespaceWrapper.getNamespace().getPrefix().trim();
		defineClassName();
		this.uriFieldName = (this.prefix.toUpperCase()+"_URI");
	}

	protected void defineClassName(){
		this.className = /*this.prefix.substring(0, 1).toUpperCase()
				+ this.prefix.substring(1)
				+ "Model"*/this.namespaceWrapper.getTargetJavaName();
	}
	
	protected void createChildren(){
		for (BaseWrapper bw: this.baseWrapper.getChildren())
			if (bw instanceof ClassWrapper) {
				this.children.add(
						createClassWriter((ClassWrapper)bw).applyParent(this)
				);
			}
	}
	
	protected AbstractWriter createClassWriter(ClassWrapper cw){
		return new ClassWriter(cw, this.reporter, this.annotationProvider);
	}
	
	@Override
	protected void writeStartComment(StringBuffer sb, int level) {
		writeWarning(sb, level);
		super.writeStartComment(sb, level);
	}

	protected void writeWarning(StringBuffer sb, int level){
		ln(sb);
		indent(sb, level); 
		sb.append("/* DON'T CHANGE THIS FILE MANUALLY */");
		ln(sb);
		ln(sb);
	}
	
	@Override
	protected void doWriteAnnotation(StringBuffer sb, int level) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void writeStartElement(StringBuffer sb, int level) {
		reporter.started("Creating namespace", baseWrapper);
		indent(sb, level);
		sb.append("public class ");
		sb.append(this.className).append(" ");
		openln(sb);
	}

	@Override
	protected void writeContent(StringBuffer sb, int level) {
		
		ln(sb);
		indented(sb, level+1, "/** Class of constants with default constructor\nNo instantiation needed */");
		indented(sb, level+1, "protected " + this.className + "(){};");
		
		ln(sb);
		indented(sb, level+1, "/** PREFIX */");
		indented(sb, level+1, 
				"public static final String " + this.prefix.toUpperCase() + "_PREFIX = \"" + this.prefix + "\";");
		ln(sb);
		indented(sb, level+1, "/** URI */");
		indented(sb, level+1, 
				"public static final String " + this.prefix.toUpperCase() + "_URI = \"" + this.namespaceWrapper.getNamespace().getUri() + "\";");
		ln(sb);
		
		for (AbstractWriter aw: children)
			aw.write(sb, level+1);
	}

	@Override
	protected void writeEnd(StringBuffer sb, int level) {
		reporter.objectDone(baseWrapper);
		indent(sb, level);
		close(sb);
	}

	@Override
	protected String getOwnURIForQName() {
		return this.uriFieldName;
	}
	
}
