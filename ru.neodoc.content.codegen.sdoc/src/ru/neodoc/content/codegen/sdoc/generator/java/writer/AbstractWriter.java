package ru.neodoc.content.codegen.sdoc.generator.java.writer;

import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.codegen.sdoc.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;

public abstract class AbstractWriter {

	protected List<AbstractWriter> children = new ArrayList<>();
	
	protected AbstractWriter parent = null;
	
	protected BaseWrapper baseWrapper;
	
	protected SdocGeneratorReporter reporter;
	
	protected AnnotationProvider annotationProvider;
	protected LookupProvider lookupProvider;
	protected CommentProvider commentProvider;
	
	public <T extends BaseWrapper> AbstractWriter(T baseWrapper, SdocGeneratorReporter reporter){
		super();
		this.baseWrapper = baseWrapper;
		this.reporter = reporter;
	}

	public <T extends BaseWrapper> AbstractWriter(T baseWrapper, SdocGeneratorReporter reporter, 
			AnnotationProvider annotationProvider){
		super();
		this.baseWrapper = baseWrapper;
		this.reporter = reporter;
		this.annotationProvider = annotationProvider;
	}
	
	public AbstractWriter getParent() {
		return parent;
	}

	public void setParent(AbstractWriter parent) {
		this.parent = parent;
	}

	public AbstractWriter applyParent(AbstractWriter parent){
		setParent(parent);
		return this;
	}
	
	public LookupProvider getLookupProvider() {
		return lookupProvider;
	}

	public void setLookupProvider(LookupProvider lookupProvider) {
		this.lookupProvider = lookupProvider;
	}

	public AbstractWriter applyLookupProvider(LookupProvider lookupProvider) {
		setLookupProvider(lookupProvider);
		return this;
	}
	
	public AnnotationProvider getAnnotationProvider() {
		return annotationProvider;
	}

	public void setAnnotationProvider(AnnotationProvider annotationProvider) {
		this.annotationProvider = annotationProvider;
	}

	public AbstractWriter applyAnnotationProvider(AnnotationProvider annotationProvider) {
		setAnnotationProvider(annotationProvider);
		return this;
	}
	
	public CommentProvider getCommentProvider() {
		return commentProvider;
	}

	public void setCommentProvider(CommentProvider commentProvider) {
		this.commentProvider = commentProvider;
	}

	public AbstractWriter applyCommentProvider(CommentProvider commentProvider) {
		setCommentProvider(commentProvider);
		return this;
	}

	public void updateChildrenProviders(){
		updateChildrenProviders(true);
	}
	
	public void updateChildrenProviders(boolean recoursive){
		for (AbstractWriter aw: children) {
			aw.setAnnotationProvider(annotationProvider);
			aw.setLookupProvider(lookupProvider);
			aw.setCommentProvider(commentProvider);
			if (recoursive)
				aw.updateChildrenProviders();
		}
	}
	
	protected void setChldrenAnnotationProvider(){
		for (AbstractWriter aw: children)
			aw.setAnnotationProvider(this.annotationProvider);
	}
	
	public void write(StringBuffer target, int level){
		writeStart(target, level);
		writeContent(target, level);
		writeEnd(target, level);
	} 
	
	protected void writeStart(StringBuffer sb, int level){
		 writeStartComment(sb, level);
		 writeAnnotation(sb, level);
		 writeStartElement(sb, level);
	};
	
	protected void writeStartComment(StringBuffer sb, int level){
		if (commentProvider == null)
			return;
		String comment = commentProvider.getComment(baseWrapper);
		if (comment == null)
			return;
		indented(sb, level, comment);
	};
	
	protected final void writeAnnotation(StringBuffer sb, int level){
		if (annotationProvider!=null)
			doWriteAnnotation(sb, level);
	};
	
	protected abstract void doWriteAnnotation(StringBuffer sb, int level);
	
	protected abstract void writeStartElement(StringBuffer sb, int level);
	
	protected abstract void writeContent(StringBuffer sb, int level);
	
	protected abstract void writeEnd(StringBuffer sb, int level);
	
	public String getURIStringForQName(){
		String result = getOwnURIForQName();
		if ((result == null) && (this.parent != null))
			result = parent.getURIStringForQName();
		if (result == null)
			result = "";
		return result;
	}
	
	protected String getOwnURIForQName(){
		return null;
	}
	
	// utils
	
	protected void indent(StringBuffer sb, int level){
		for (int i=0; i<level; i++)
			sb.append("\t");
	}
	
	protected void indented(StringBuffer sb, int level, String message){
		indented(sb, level, message, null);
	}
	
	protected void indented(StringBuffer sb, int level, String message, String prefix){
		indented(sb, level, message, prefix, true);
	}
	
	protected void indented(StringBuffer sb, int level, String message, String prefix, boolean noFirstLinePrefix){
		
		String toPrepend = prefix==null?"":prefix;
		
		if ((message == null) || (message.trim().length()==0)){
			// indent(sb, level);
			return;
		}
		
		String[] splitted = message.split("\n");
		for (int i=0; i<splitted.length; i++) {
			indent(sb, level);
			if ((i>0) || !noFirstLinePrefix)
				sb.append(toPrepend);
			sb.append(splitted[i]);
			ln(sb);
		}
		
	}
	
	protected void ln(StringBuffer sb){
		sb.append("\n");
	}
	
	protected void open(StringBuffer sb) {
		sb.append("{");
	}

	protected void openln(StringBuffer sb) {
		open(sb);
		ln(sb);
	}

	protected void close(StringBuffer sb) {
		sb.append("}");
	}
	
	protected void closeln(StringBuffer sb) {
		close(sb);
		ln(sb);
	}
	
	protected String createQNameString(){
		StringBuffer sb = new StringBuffer();
		sb.append("public static final ")
			.append("QName ")
			.append(baseWrapper.getTargetJavaName())
			.append(" = ")
			.append("QName.createQName(")
			.append(getURIStringForQName())
			.append(", \"")
			.append(baseWrapper.getName())
			.append("\");");
		
		return sb.toString();
	}
}
