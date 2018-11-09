package ru.neodoc.content.utils.uml.profile.meta;

import java.util.List;
import java.util.UUID;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.uml.profile.annotation.AStereotype.AApplication;
import ru.neodoc.content.utils.uml.profile.descriptor.StereotypeDescriptor;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public abstract class MetaObject implements StereotypedElement {

	protected Element element;

	protected String uniqueId = UUID.randomUUID().toString();
	
	@Override
	public String getUniqueId() {
		return this.uniqueId;
	}
	
	protected boolean canBeAssigned(Class<?> clazz) {
		StereotypeDescriptor meta = StereotypeDescriptor.find(clazz);
		if (!meta.isValid() || meta.isAbstract())
			return false;
		return isApplicable(meta);
	}
	
	protected boolean isApplicable(Class<?> clazz) {
		StereotypeDescriptor meta = StereotypeDescriptor.find(clazz);
		return isApplicable(meta);
	}
	
	protected boolean isApplicable(StereotypeDescriptor meta) {
		if (!meta.isValid())
			return false;
		
		List<AApplication> aApplications = meta.getEffectiveApplications();
		if (aApplications.isEmpty())
			return true;
		
		boolean result = false;
		for (AApplication aApplication: aApplications) {
			if (result)
				break;
			result = result || checkApplication(aApplication);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	protected boolean checkApplication(AApplication aApplication) {
		
		Class<? extends Element>[] classes = aApplication.classes();
		boolean classesResult = (classes==null) || (classes.length==0);
		if (!classesResult)
			for (int i=0; i<classes.length && !classesResult; i++)
				classesResult = classesResult || classes[i].isAssignableFrom(this.element.getClass());
		if (!classesResult) return false;
		
		Class<?>[] required = aApplication.requires();
		boolean requiredResult = (required==null) || (required.length==0);
		if (!requiredResult) {
			boolean res = true;
			for (int i=0; i<required.length && res; i++) {
				StereotypeDescriptor meta = StereotypeDescriptor.find(required[i]);
				res = res 
						&& meta.isValid()
						&& ProfileStereotype.class.isAssignableFrom(required[i])
						&& (get((Class<ProfileStereotype>)required[i])!=null);
				
			}
			requiredResult = res;
		}
		if (!requiredResult) return false;
		
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		if (obj instanceof ProfileStereotype)
			return getElement()!=null && getElement().equals(((ProfileStereotype) obj).getElement());
		if (obj instanceof Element)
			return getElement()!=null && getElement().equals(obj);
		return super.equals(obj);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Element> T getElement() {
		return (T)element;
	}
	
}
