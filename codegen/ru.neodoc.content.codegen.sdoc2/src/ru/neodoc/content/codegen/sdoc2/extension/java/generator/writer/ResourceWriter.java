package ru.neodoc.content.codegen.sdoc2.extension.java.generator.writer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.neodoc.content.codegen.CodegenPlugin;
import ru.neodoc.content.codegen.sdoc2.SdocCodegenPlugin;
import ru.neodoc.content.codegen.sdoc2.extension.java.WrapperJavaExtension;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc2.generator.writer.AbstractWriter;
import ru.neodoc.content.codegen.sdoc2.generator.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc2.utils.ReferencedElementDescriptor;
import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;

public class ResourceWriter extends AbstractWriter<NamespaceWrapper> {

	protected Set<String> imports = new HashSet<>();
	
	public ResourceWriter(NamespaceWrapper baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter);
		setAnnotationProvider(annotationProvider);
		this.children.add(createNamespaceWriter(baseWrapper).applyParent(this));
	}
	
	protected NamespaceWriter createNamespaceWriter(NamespaceWrapper baseWrapper){
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
		Namespace namespace = baseWrapper.getClassifiedWrappedElement();
		Model model = Model._HELPER.findNearestFor(namespace);
		String startComment = "/**\nThe file is generated automatically\n"
				+ "from model for namespace \""
				+ namespace.getPrefix() + "\" {"
				+ namespace.getUri() + "}\n\n"
				+ "Generated with:\n"
				+ "\t" + CodegenPlugin.PLUGIN_ID + ": " + CodegenPlugin.getDefault().getBundle().getVersion() + "\n"
				+ "\t" + SdocCodegenPlugin.PLUGIN_ID + ": " + SdocCodegenPlugin.getDefault().getBundle().getVersion() + "\n"
				+ "\n"
				+ "@author " + "n/a\n"
				+ "@since " + ((new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime())) + "\n"
				+ "@version " + (model==null?"unknown":model.getVersion()) + "\n"
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
		sb.append("package ").append(WrapperJavaExtension.get(baseWrapper).getFinalJavaPackage()).append(";\n\n");

		writeImports(sb, level);
		ln(sb);
		
	}

	public static class ImportedPackage extends ReferencedElementDescriptor {
		public ImportedPackage(String packageName) {
			super(packageName);
		}
		public String getPackageName() {
			return elementName;
		}
	}
	
	protected void writeImports(StringBuffer sb, int level){
		List<ImportedPackage> importedPackages = new ArrayList<>();
		for (String imp: imports) {
			ImportedPackage ip = new ImportedPackage(imp);
			if (ip.isValid())
				importedPackages.add(ip);
		}
			
		Collections.sort(importedPackages, new Comparator<ImportedPackage>() {
			@Override
			public int compare(ImportedPackage o1, ImportedPackage o2) {
				if (o1 == null)
					return -1;
				return o1.getPackageName().compareTo(o2.getPackageName());
			}
		});
		
		for (ImportedPackage imp: importedPackages) {
			if (imp.isIgnored())
				sb.append("// ignored: ");
			sb.append("import ").append(imp.getPackageName()).append(";\n");
		}
	}
	
	@Override
	protected void writeContent(StringBuffer sb, int level) {
		for (AbstractWriter<?> aw: children)
			aw.write(sb, level);
	}

	@Override
	protected void writeEnd(StringBuffer sb, int level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAnnotationProvider(AnnotationProvider annotationProvider) {
		super.setAnnotationProvider(annotationProvider);
		for (AbstractWriter<?> aw: children)
			aw.setAnnotationProvider(annotationProvider);
	}
	
}
