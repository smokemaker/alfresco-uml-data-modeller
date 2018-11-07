package ru.neodoc.content.codegen.sdoc.generator.java.annotation;

import java.util.HashSet;
import java.util.Set;

public class AnnotationInfo {
	
	public Set<String> imports = new HashSet<>();
	public String annotation = null;
	
	public AnnotationInfo setAnnotation(String s){
		this.annotation = s;
		return this;
	}
	
	public AnnotationInfo addImport(String s) {
		imports.add(s);
		return this;
	}
	
	public AnnotationInfo addImports(Set<String> s){
		imports.addAll(s);
		return this;
	}
}
