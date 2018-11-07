package ru.neodoc.content.codegen.sdoc.generator.java.writer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.neodoc.content.codegen.CodegenPlugin;
import ru.neodoc.content.codegen.sdoc.SdocCodegenPlugin;
import ru.neodoc.content.codegen.sdoc.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc.wrap.NamespaceWrapper;

public class ResourceWriter extends AbstractWriter {

	protected Set<String> imports = new HashSet<>();
	
	public ResourceWriter(NamespaceWrapper baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter);
		setAnnotationProvider(annotationProvider);
		this.children.add(createNamespaceWriter(baseWrapper).applyParent(this));
	}
	
	protected AbstractWriter createNamespaceWriter(NamespaceWrapper baseWrapper){
		return new NamespaceWriter(baseWrapper, this.reporter, this.annotationProvider);
	}
	
	public Set<String> getImports() {
		return imports;
	}

	public void setImports(Set<String> imports) {
		this.imports = imports;
	}

	@Override
	protected void writeStartComment(StringBuffer sb, int level) {
		String startComment = "/**\nThe file is generated automatically\n"
				+ "from model for namespace \""
				+ ((NamespaceWrapper)baseWrapper).getNamespace().getPrefix() + "\" {"
				+ ((NamespaceWrapper)baseWrapper).getNamespace().getUri() + "}\n\n"
				+ "Generated with:\n"
				+ "\t" + CodegenPlugin.PLUGIN_ID + ": " + CodegenPlugin.getDefault().getBundle().getVersion() + "\n"
				+ "\t" + SdocCodegenPlugin.PLUGIN_ID + ": " + SdocCodegenPlugin.getDefault().getBundle().getVersion() + "\n"
				+ "\n"
				+ "@author " + "n/a\n"
				+ "@since " + ((new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime())) + "\n"
				+ "@version " + ((NamespaceWrapper)baseWrapper).getNamespace().getModel().getVersion() + "\n"
				+ "\n\n*/";
		indented(sb, level, startComment, "* ");
	}

	@Override
	protected void doWriteAnnotation(StringBuffer sb, int level) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void writeStartElement(StringBuffer sb, int level) {

		indent(sb, level);
		sb.append("package ").append(baseWrapper.getFinalJavaPackage()).append(";\n\n");

		writeImports(sb, level);
		ln(sb);
		
	}

	protected void writeImports(StringBuffer sb, int level){
		List<String> importList = new ArrayList<String>(imports); 
		Collections.sort(importList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (o1 == null)
					return -1;
				return o1.compareTo(o2);
			}
		});
		
		for (String imp: importList)
			sb.append("import ").append(imp).append(";\n");
	}
	
	@Override
	protected void writeContent(StringBuffer sb, int level) {
		for (AbstractWriter aw: children)
			aw.write(sb, level);
	}

	@Override
	protected void writeEnd(StringBuffer sb, int level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAnnotationProvider(AnnotationProvider annotationProvider) {
		super.setAnnotationProvider(annotationProvider);
		for (AbstractWriter aw: children)
			aw.setAnnotationProvider(annotationProvider);
	}
	
}
