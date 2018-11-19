package ru.neodoc.content.utils.uml.profile.meta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.neodoc.content.utils.uml.profile.requirement.ARequirement;
import ru.neodoc.content.utils.uml.profile.requirement.ARequirementAggregation;
import ru.neodoc.content.utils.uml.profile.requirement.ARequirementLink;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class RequirementsResolver {

	protected ImplementationMetaObject implementationMetaObject = null;
	
	protected List<Method> requirements = new ArrayList<>();
	protected List<Method> requirementAggregations = new ArrayList<>();
	protected List<Method> requirementLinks = new ArrayList<>();
	
	public RequirementsResolver(ImplementationMetaObject implementationMetaObject) {
		super();
		this.implementationMetaObject = implementationMetaObject;
		scan();
	}
	
	public void scan() {
		requirements.clear();
		requirementLinks.clear();
		Method[] methods = implementationMetaObject.getImplementedStereotype().getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (method.getAnnotation(ARequirement.class)!=null)
				requirements.add(method);
			if (method.getAnnotation(ARequirementAggregation.class)!=null)
				requirementAggregations.add(method);
			if (method.getAnnotation(ARequirementLink.class)!=null)
				requirementLinks.add(method);
		}
	}
	
	protected List<StereotypedElement> getResult(Method method) {
		List<StereotypedElement> result = new ArrayList<>();
		try {
			Object methodResult = method.invoke(implementationMetaObject);
			if (methodResult instanceof List) {
				for (Object obj: (List<?>)methodResult)
					if (obj instanceof StereotypedElement)
						result.add((StereotypedElement)obj);
			} else if (methodResult instanceof StereotypedElement)
				result.add((StereotypedElement)methodResult);
		} catch (Exception e) {
			// NOOP
		}
		return result;
	}
	
	protected Set<StereotypedElement> doResolveRequirements() {
		Set<StereotypedElement> result = new HashSet<>();
		for (Method method: requirements)
			result.addAll(getResult(method));
		result.remove(implementationMetaObject);
		return result;
	}
	
	public List<StereotypedElement> resolveRequirements() {
		return new ArrayList<>(doResolveRequirements());
	}
	
	protected Set<StereotypedElement> doResolveRequirementAggregations() {
		return doResolveRequirementReferences(requirementAggregations, true);
	}
	
	protected Set<StereotypedElement> doResolveRequirementLinks() {
		return doResolveRequirementReferences(requirementLinks, false);
	}

	protected Set<StereotypedElement> doResolveRequirementReferences(List<Method> methods, boolean aggrgate) {
		Set<StereotypedElement> result = new HashSet<>();
		
		Set<StereotypedElement> requirementOwners = new HashSet<>();
		for (Method m: methods)
			requirementOwners.addAll(getResult(m));

		Set<ImplementationMetaObject> imoSet = new HashSet<>();
		for (StereotypedElement se: requirementOwners)
			for (ProfileStereotype ps: se.getAll(implementationMetaObject.getProfile()))
				if (ps instanceof ImplementationMetaObject)
					if (!((ImplementationMetaObject)ps).isOwned())
						imoSet.add((ImplementationMetaObject)ps);
		
		for (ImplementationMetaObject imo: imoSet)
			if (aggrgate)
				result.addAll((new RequirementsResolver(imo)).resolveAll());
			else
				result.addAll((new RequirementsResolver(imo)).resolveRequirements());
			
		result.remove(implementationMetaObject);
		return result;
	}
	
	public List<StereotypedElement> resolveRequirementLinks() {
		return new ArrayList<>(doResolveRequirementLinks());
	}
	
	public List<StereotypedElement> resolveAll(){
		Set<StereotypedElement> result = new HashSet<>();
		
		result.addAll(doResolveRequirements());
		result.addAll(doResolveRequirementAggregations());
		result.addAll(doResolveRequirementLinks());
		
		return new ArrayList<>(result);
	}
}
