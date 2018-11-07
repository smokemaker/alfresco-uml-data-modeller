package ru.neodoc.content.codegen.sdoc.generator.java.writer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.neodoc.content.codegen.sdoc.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc.wrap.AspectWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.ChildAssociationWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.ClassWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.PeerAssociationWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.PropertyWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.TypeWrapper;
import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;
import ru.neodoc.content.modeller.utils.uml.elements.MandatoryAspect;

public class ClassWriter extends AbstractWriter {

	public ClassWriter(ClassWrapper baseWrapper, SdocGeneratorReporter reporter) {
		super(baseWrapper, reporter);
	}

	public ClassWriter(ClassWrapper baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter, annotationProvider);
		
		List<AbstractWriter> associationWriters = new ArrayList<>();
		List<AbstractWriter> propertyWriters = new ArrayList<>();
		
		for (BaseWrapper bw: baseWrapper.getChildren()) {
			if (PeerAssociationWrapper.class.isAssignableFrom(bw.getClass())
					|| ChildAssociationWrapper.class.isAssignableFrom(bw.getClass())){
				// it's association
				associationWriters.add(
						(createAssociationWriter(bw))
						.applyParent(this)
					);
			} else 
			if (PropertyWrapper.class.isAssignableFrom(bw.getClass())){
				propertyWriters.add(
						(createPropertyWriter((PropertyWrapper)bw))
						.applyParent(this)
						.applyAnnotationProvider(annotationProvider)
					);
			}
		}
		
		this.children.addAll(propertyWriters);
		this.children.addAll(associationWriters);
	}

	protected AbstractWriter createAssociationWriter(BaseWrapper bw) {
		return new AssociationWriter(bw, reporter, annotationProvider);
	}
	
	protected AbstractWriter createPropertyWriter(PropertyWrapper pw){
		return new PropertyWriter(pw, reporter);
	}
	
	@Override
	public void setAnnotationProvider(AnnotationProvider annotationProvider) {
		super.setAnnotationProvider(annotationProvider);
		setChldrenAnnotationProvider();
	}
	
	@Override
	protected void writeStart(StringBuffer sb, int level) {
		ln(sb);
		super.writeStart(sb, level);
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
		String s = "element";
		if (baseWrapper instanceof AspectWrapper)
			s = "aspect";
		else if (baseWrapper instanceof TypeWrapper)
			s = "type";
		
		reporter.started("Creating " + s + ": ", baseWrapper);

		indent(sb, level);
		sb.append("public static ")
			.append(baseWrapper instanceof AspectWrapper?"interface ":"class ")
			.append(baseWrapper.getTargetJavaName());
		
		BaseElement be = baseWrapper.getWrappedElement();
		if ((be instanceof BaseClassElement) && (lookupProvider!=null)) {
			BaseClassElement bce = (BaseClassElement)be;
			// extends
			BaseClassElement parentBce = bce.getParent();
			if (parentBce != null) {
				String javaName = lookupProvider.lookupTargetName(parentBce);
				if ((javaName!=null) && (javaName.trim().length()>0))
					sb.append(" extends ")
						.append(javaName.substring(javaName.indexOf('.')+1));
			}
			
			// implements
			List<MandatoryAspect> mas =	bce.getMandatoryAspects();
			List<String> interfaces = new ArrayList<>();
			
			for (MandatoryAspect ma: mas) {
				BaseClassElement targetBce = ma.getTarget();
				String javaName = lookupProvider.lookupTargetName(targetBce);
				if ((javaName!=null) && (javaName.trim().length()>0))
					interfaces.add(javaName);
			}
			
			if (!interfaces.isEmpty()) {
				boolean isFirst = true;
				sb.append(" implements ");
				for (String name: interfaces) {
					if (!isFirst)
						sb.append(", ");
					sb.append(name.substring(name.indexOf('.')+1));
					isFirst = false;
				}
			}
		}
		
		sb.append(" ");
		openln(sb);
		
	}

	@Override
	protected void writeContent(StringBuffer sb, int level) {
		boolean isFirst = true;
		Iterator<AbstractWriter> iter = children.iterator();
		while (iter.hasNext()){
			AbstractWriter aw = iter.next();
			aw.write(sb, level+1);
			separateChild(sb, level+1, isFirst, !iter.hasNext());
			isFirst = false;
		}
	}

	protected void separateChild(StringBuffer sb, int level, boolean isFirst, boolean isLast){
		// NOOP
	}
	
	@Override
	protected void writeEnd(StringBuffer sb, int level) {
		reporter.objectDone(baseWrapper);
		indent(sb, level);
		closeln(sb);
	}

}
