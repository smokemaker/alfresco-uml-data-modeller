package ru.neodoc.content.utils.uml.profile.descriptor.scanner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.neodoc.content.utils.uml.profile.descriptor.AbstractDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.AbstractOwnedDescriptor;

public abstract class AbstractScanner<ParentClass, ChildClass> {
	
	public static interface ScannerValidator<TestedClass> {
		boolean isValid(TestedClass object);
	}
	
	protected Class<? extends ParentClass> parentClass;
	protected Class<? extends ChildClass> childClass;
	
	protected Map<Class<? extends Annotation>, Class<? extends AbstractOwnedDescriptor<ChildClass, ?, ? extends AbstractDescriptor<ParentClass, ?>>>>
			descriptorClassMap = new HashMap<>();
	protected Set<Class<? extends Annotation>> stopOn = new HashSet<>();
	protected final List<ScannerValidator<ChildClass>> validators = new ArrayList<>();
	
	public AbstractScanner(Class<ParentClass> parentClass, Class<ChildClass> childClass) {
		super();
		this.parentClass = parentClass;
		this.childClass = childClass;
	}
	
	public AbstractScanner<ParentClass, ChildClass> add(Class<? extends Annotation> annotationClass, Class<? extends AbstractOwnedDescriptor<ChildClass, ?, ? extends AbstractDescriptor<ParentClass, ?>>> descriptorClass) {
		descriptorClassMap.put(annotationClass, descriptorClass);
		return this;
	}
	
	public AbstractScanner<ParentClass, ChildClass> stopOn(Class<? extends Annotation> stopClass){
		this.stopOn.add(stopClass);
		return this;
	}
	
	public Map<Class<? extends Annotation>, List<ChildClass>> scan(AbstractDescriptor<ParentClass, ?> owner, ParentClass parent) {
		Map<Class<? extends Annotation>, List<ChildClass>> result = 
				new HashMap<>();
		for (Class<? extends Annotation> annotationClass: descriptorClassMap.keySet())
			result.put(annotationClass, new ArrayList<>());
		scan(owner, parent, result);
		return result;
	}
	
	protected void scan(AbstractDescriptor<ParentClass, ?> owner, 
			ParentClass parent,
			Map<Class<? extends Annotation>, List<ChildClass>> result
			) {
		if (childClass.isAssignableFrom(parent.getClass()))
			check(owner, childClass.cast(parent), result);
		
		for (ChildClass child: getChildren(parent)) {
			check(owner, child, result);
			if (parentClass.isAssignableFrom(child.getClass())
					&& !isToStop(child))
				scan(owner, parentClass.cast(child), result);
		}
	}
	
	protected boolean isToStop(ChildClass child) {
		for (Class<? extends Annotation> annotationClass: stopOn)
			if (getAnnotation(child, annotationClass)!=null)
				return true;
		return false;
	}
	
	protected abstract List<ChildClass> getChildren(ParentClass parent);
	
	protected boolean validate(ChildClass child) {
		for (ScannerValidator<ChildClass> validator: this.validators)
			if (!validator.isValid(child))
				return false;
		return true;
	}
	
	protected boolean check(
			AbstractDescriptor<ParentClass, ?> owner, 
			ChildClass child,
			Map<Class<? extends Annotation>, List<ChildClass>> result) {
		if (!validate(child))
			return false;
		for (Class<? extends Annotation> annotationClass: descriptorClassMap.keySet()) {
			Annotation obj = getAnnotation(child, annotationClass);
			if ((obj!=null)
					&& (result.get(annotationClass)!=null)
					&& !result.get(annotationClass).contains(child)) {
				try {
					if (!result.get(annotationClass).contains(child))
						result.get(annotationClass).add(child);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (owner!=null)
					if (descriptorClassMap.get(annotationClass)!=null)
						try {
							AbstractDescriptor<ChildClass, ?> newDescriptor = descriptorClassMap
									.get(annotationClass).getConstructor(childClass, owner.getClass())
									.newInstance(child, owner);
							newDescriptor.init();
							if (newDescriptor.isValid())
								owner.addOwned(newDescriptor);
						} catch (Exception e) {
							e.printStackTrace();
						}
			}
		}
		return false;
	}
	
	public List<ScannerValidator<ChildClass>> getValidators(){
		return this.validators;
	}
	
	public AbstractScanner<ParentClass, ChildClass> validator(ScannerValidator<ChildClass> validator){
		this.validators.add(validator);
		return this;
	}
	
	protected abstract Annotation getAnnotation(ChildClass child, Class<? extends Annotation> annotationClass);
}
