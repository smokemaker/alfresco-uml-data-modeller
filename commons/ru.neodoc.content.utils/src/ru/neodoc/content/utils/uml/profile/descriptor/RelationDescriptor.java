package ru.neodoc.content.utils.uml.profile.descriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;

import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.annotation.ARelationStereotype;
import ru.neodoc.content.utils.uml.profile.annotation.ARelationStereotype.ARelation;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class RelationDescriptor {
	
	public static RelationDescriptor create(Class<?> clazz) {
		if (clazz.getAnnotation(ARelationStereotype.class) == null)
			return null;
		return new RelationDescriptor(clazz);
	}
	
	protected class RelationInfo {
		
		protected List<Class<? extends ProfileStereotype>> source;
		protected List<Class<? extends ProfileStereotype>> notSource;
		protected List<Class<? extends ProfileStereotype>> target;
		protected List<Class<? extends ProfileStereotype>> notTarget;
		
		protected boolean sourceIsMultiple;
		protected boolean targetIsMultiple;
		
		public RelationInfo(ARelation relation) {
			Class<? extends ProfileStereotype>[] sources = relation.source();
			Class<? extends ProfileStereotype>[] notSources = relation.notSource();
			Class<? extends ProfileStereotype>[] targets = relation.target();
			Class<? extends ProfileStereotype>[] notTargets = relation.notTarget();
			this.source = Arrays.asList(sources);
			this.notSource = Arrays.asList(notSources);
			this.target = Arrays.asList(targets);
			this.notTarget = Arrays.asList(notTargets);
			
			this.sourceIsMultiple = relation.sourceIsMultiple();
			this.targetIsMultiple = relation.targetIsMultiple();
		}
		
		protected boolean elementAllowed(Element element, List<Class<? extends ProfileStereotype>> list) {
			if (list.isEmpty())
				return true;
			StereotypedElement stereotypedElement  = AbstractProfile.asUntyped(element);
			for (Class<? extends ProfileStereotype> stereotypeClass: list)
				if (stereotypedElement.has(stereotypeClass))
					return true;
			return false;
		}

		protected boolean elementProhibited(Element element, List<Class<? extends ProfileStereotype>> list) {
			if (list.isEmpty())
				return false;
			StereotypedElement stereotypedElement  = AbstractProfile.asUntyped(element);
			for (Class<? extends ProfileStereotype> stereotypeClass: list)
				if (stereotypedElement.has(stereotypeClass))
					return true;
			return false;
		}
		
		
		public boolean sourceAllowed(Element sourceElement) {
			return elementAllowed(sourceElement, this.source) && !elementProhibited(sourceElement, this.notSource);
		}

		public boolean targetAllowed(Element targetElement) {
			return elementAllowed(targetElement, this.target) && !elementProhibited(targetElement, this.notTarget);
		}
		
		public boolean hasOutgoingRelation(Element sourceElement, Relationship...relationshipsToIgnore) {
			List<DirectedRelationship> directedRelationships = sourceElement.getSourceDirectedRelationships();
			return hasRelation(directedRelationships, relationshipsToIgnore);
		}
		
		public boolean hasIncomingRelation(Element targetElement, Relationship...relationshipsToIgnore) {
			List<DirectedRelationship> directedRelationships = targetElement.getTargetDirectedRelationships();
			return hasRelation(directedRelationships, relationshipsToIgnore);
		}
		
		protected boolean hasRelation(List<DirectedRelationship> directedRelationships, Relationship...relationshipsToIgnore) {
			if (!ProfileStereotype.class.isAssignableFrom(stereotypeClass))
				return false;
			@SuppressWarnings("unchecked")
			Class<ProfileStereotype> profileStereotypeClass = (Class<ProfileStereotype>)stereotypeClass;
			List<Relationship> list = Arrays.asList(relationshipsToIgnore);
			for (DirectedRelationship directedRelationship: directedRelationships)
				if (list.contains(directedRelationship))
					continue;
				else if (AbstractProfile.asUntyped(directedRelationship).has(profileStereotypeClass))
					return true;
			return false;
		}
		
		public boolean relationAllowed(Element sourceElement, Element targetElement, Relationship...relationshipsToIgnore) {
			boolean componentsAllowed = sourceAllowed(sourceElement) && targetAllowed(targetElement);
			boolean multiplicityAllowed = 
					(this.sourceIsMultiple || !hasIncomingRelation(targetElement, relationshipsToIgnore)) 
					&& (this.targetIsMultiple || !hasOutgoingRelation(sourceElement, relationshipsToIgnore));
			return componentsAllowed && multiplicityAllowed;
		}
	}
	
	protected ARelationStereotype relationStereotype;
	
	protected List<RelationInfo> relationInfos = new ArrayList<>();
	
	protected Class<?> stereotypeClass;
	
	private RelationDescriptor(Class<?> clazz) {
		super();
		this.stereotypeClass = clazz;
		this.relationStereotype = clazz.getAnnotation(ARelationStereotype.class);
		readRelationInfo();
	}
	
	protected void readRelationInfo() {
		ARelation[] relations = this.relationStereotype.relations();
		if (relations.length == 0)
			return;
		for (int i=0; i<relations.length; i++) {
			this.relationInfos.add(new RelationInfo(relations[i]));
		}
	}
	public boolean sourceAllowed(Element sourceElement) {
		if (this.relationInfos.isEmpty())
			return true;
		for (RelationInfo relationInfo: this.relationInfos)
			if (relationInfo.sourceAllowed(sourceElement))
				return true;
		return false;
	}

	public boolean targetAllowed(Element targetElement) {
		if (this.relationInfos.isEmpty())
			return true;
		for (RelationInfo relationInfo: this.relationInfos)
			if (relationInfo.targetAllowed(targetElement))
				return true;
		return false;
	}
	
	public boolean relationAllowed(Element sourceElement, Element targetElement, Relationship...relationshipsToIgnore) {
		if (this.relationInfos.isEmpty())
			return true;
		for (RelationInfo relationInfo: this.relationInfos)
			if (relationInfo.relationAllowed(sourceElement, targetElement, relationshipsToIgnore))
				return true;
		return false;
	}
}
