package ru.neodoc.content.codegen.sdoc2.extension.java.generator.writer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.neodoc.content.codegen.sdoc2.extension.java.WrapperJavaExtension;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator.SdocGeneratorReporter;
import ru.neodoc.content.codegen.sdoc2.generator.writer.AbstractWriter;
import ru.neodoc.content.codegen.sdoc2.generator.writer.AnnotationProvider;
import ru.neodoc.content.codegen.sdoc2.utils.ReferencedElementDescriptor;
import ru.neodoc.content.codegen.sdoc2.utils.ReferencedElementDescriptorList;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractAlfrescoClassWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.AspectWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.AssociationMainWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.ChildAssociationWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.PeerAssociationWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.PropertyWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.TypeWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.WrapperFactory;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForGeneralization.Inherit;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;

public class ClassWriter extends AbstractJavaWriter<AbstractAlfrescoClassWrapper<?>> {

	public ClassWriter(AbstractAlfrescoClassWrapper<?> baseWrapper, SdocGeneratorReporter reporter) {
		super(baseWrapper, reporter);
	}

	public ClassWriter(AbstractAlfrescoClassWrapper<?> baseWrapper, SdocGeneratorReporter reporter,
			AnnotationProvider annotationProvider) {
		super(baseWrapper, reporter, annotationProvider);
		
		List<AbstractWriter<?>> associationWriters = new ArrayList<>();
		List<AbstractWriter<?>> propertyWriters = new ArrayList<>();
		
		for (AbstractWrapper bw: baseWrapper.getChildren()) {
			if (PeerAssociationWrapper.class.isAssignableFrom(bw.getClass())
					|| ChildAssociationWrapper.class.isAssignableFrom(bw.getClass())){
				// it's association
				associationWriters.add(
						(createAssociationWriter((AssociationMainWrapper<?>)bw))
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

	protected AssociationWriter createAssociationWriter(AssociationMainWrapper<?> bw) {
		return new AssociationWriter(bw, reporter, annotationProvider);
	}
	
	protected PropertyWriter createPropertyWriter(PropertyWrapper pw){
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

	protected void writeList(StringBuffer sb, int level, ReferencedElementDescriptorList list, String header) {
		if (!list.isEmpty()) {
			String headerToWrite = " " + header + " ";
			if (list.ignoredCount() == list.size())
				// all are ignored
				headerToWrite = " /* " + header + " */ ";
			String concatedList = list.sort().concat();
			concatedList = concatedList
					.replaceAll(",#", " /*, ignored: ")
					.replaceAll("#,", " */")
					.replaceFirst("#", " /* ignored: ")
					.replaceFirst("#", " */");
			sb.append(headerToWrite);
			sb.append(concatedList);
		}		
	}
	
	@Override
	protected void writeStartElement(StringBuffer sb, int level) {
		boolean isAspect = (baseWrapper instanceof AspectWrapper); 
		
		String s = "element";
		if (baseWrapper instanceof AspectWrapper)
			s = "aspect";
		else if (baseWrapper instanceof TypeWrapper)
			s = "type";
		
		reporter.started("Creating " + s + ": ", baseWrapper);

		indent(sb, level);
		sb.append("public static ")
			.append(isAspect?"interface ":"class ")
			.append(getWrapperExtension().getTargetJavaName());
		
		//BaseElement be = baseWrapper.getWrappedElement();
		ClassMain cm =  baseWrapper.getWrappedElement().get(ClassMain.class);
		ReferencedElementDescriptorList elementsToExtend = new ReferencedElementDescriptorList();
		ReferencedElementDescriptorList interfacesToimplement = new ReferencedElementDescriptorList();
		if ((cm!=null) && (lookupProvider!=null)) {
			// BaseClassElement bce = (BaseClassElement)be;
			
			// parent
			if (!cm.getInherits().isEmpty()) {
				Inherit inherit = cm.getInherits().get(0);
				ClassMain parent = inherit.getGeneral();
				elementsToExtend.add(new ReferencedElementDescriptor(
						getTargetJavaName(parent), 
						isIgnored(parent)));
			}
			
			// mandatory aspects
			List<MandatoryAspect> mas =	cm.getMandatoryAspects();
			ReferencedElementDescriptorList tempList = isAspect
					?elementsToExtend
					:interfacesToimplement;
			
			for (MandatoryAspect ma: mas) {
				ClassMain targetCm = ma.getTarget();
				tempList.add(new ReferencedElementDescriptor(
						getTargetJavaName(targetCm), 
						isIgnored(targetCm)));
			}
			
			// write extends
			writeList(sb, level, elementsToExtend, "extends");
			
			// write implements
			writeList(sb, level, interfacesToimplement, "implements");
			
/*			if (!interfaces.isEmpty()) {
				boolean isFirst = true;
				sb.append(" implements ");
				for (String name: interfaces) {
					if (!isFirst)
						sb.append(", ");
					sb.append(name.substring(name.indexOf('.')+1));
					isFirst = false;
				}
			}
*/		}
		
		sb.append(" ");
		openln(sb);
		
	}

	protected WrapperJavaExtension getWrapperJavaExtension(ProfileStereotype ps) {
		AbstractWrapper aw = WrapperFactory.get(ps);
		WrapperJavaExtension wje = WrapperJavaExtension.get(aw);
		return wje;
	}
	
	protected boolean isIgnored(ProfileStereotype ps) {
		return getWrapperJavaExtension(ps).isIgnored();
	}
	
	protected String getTargetJavaName(ProfileStereotype ps) {
		return getWrapperJavaExtension(ps).getTargetJavaName();
	}
	
	@Override
	protected void writeContent(StringBuffer sb, int level) {
		boolean isFirst = true;
		Iterator<AbstractWriter<?>> iter = children.iterator();
		while (iter.hasNext()){
			AbstractWriter<?> aw = iter.next();
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
