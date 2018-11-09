package ru.neodoc.content.modeller.palette.helper.advice;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Stereotype;

import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.profile.descriptor.ProfileDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.RelationDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.StereotypeDescriptor;
import ru.neodoc.content.utils.uml.profile.registry.ProfileRegistry;

public class RelationshipHelper {
	
	public static class RelationPair {
		
		protected EObject source;
		protected EObject target;
		
		protected boolean isChanged = false;
		
		public RelationPair(CreateRelationshipRequest createRelationshipRequest) {
			super();
			this.source = createRelationshipRequest.getSource();
			this.target = createRelationshipRequest.getTarget();
			this.isChanged = true;
		}
		
		public RelationPair(ReorientRelationshipRequest reorientRelationshipRequest) {
			super();
			if (reorientRelationshipRequest.getRelationship() instanceof DirectedRelationship) {
				DirectedRelationship directedRelationship = (DirectedRelationship)reorientRelationshipRequest.getRelationship();
				try {
					this.source = directedRelationship.getSources().get(0);
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					this.target = directedRelationship.getTargets().get(0);
				} catch (Exception e) {
					// TODO: handle exception
				}
				update(
						reorientRelationshipRequest.getOldRelationshipEnd(), 
						reorientRelationshipRequest.getNewRelationshipEnd());
				
			}
		}
		
		public void update(EObject oldEnd, EObject newEnd) {
			if (oldEnd==null)
				return;
			if (newEnd == null)
				return;
			if (oldEnd.equals(newEnd))
				return;
			if (oldEnd.equals(this.target)) {
				this.target = newEnd;
				this.isChanged = true;
			}
			if (oldEnd.equals(this.source)) {
				this.source = newEnd;
				this.isChanged = true;
			}
		}
		
		public boolean isValid() {
			return (source!=null)
					&& (target!=null)
					&& (source instanceof Element)
					&& (target instanceof Element);
		}
		
		public boolean isChanged() {
			return isChanged;
		}
		
		public boolean isCircle() {
			return isValid() && (this.source.equals(this.target));
		}
		
		public Element getSource() {
			return (Element)source;
		}
		
		public Element getTarget() {
			return (Element)target;
		}
	}
	
	public static boolean checkStereotype(CreateRelationshipRequest request) {
		String stereotypeToApply = (String)request.getParameter(CommandParameters.STEREOTYPE_TO_APPLY);
		Element sourceElement = (Element)request.getSource();
		Element targetElement = (Element)request.getTarget();
		return checkRelation(sourceElement, targetElement, stereotypeToApply);
	}

	public static boolean checkAllRelations(Element sourceElement, Element targetElement, List<String> stereotypeNames, Relationship...relationshipsToIgnore) {
		for (String stereotypeName: stereotypeNames)
			if (!checkRelation(sourceElement, targetElement, stereotypeName, relationshipsToIgnore))
				return false;
		return true;
	}
	
	public static boolean checkRelation(Element sourceElement, Element targetElement, String stereotypeName, Relationship...relationshipsToIgnore) {
		if (CommonUtils.isValueable(stereotypeName)) {
			PrefixedName stereotypeFullName = new PrefixedName(stereotypeName, "::");
			StereotypeDescriptor descriptor = null;
			if (stereotypeFullName.isPrefixed()) {
				ProfileDescriptor profileDescriptor = ProfileRegistry.INSTANCE.getProfileDescriptor(stereotypeFullName.getPrefix());
				descriptor = profileDescriptor.getStereotypeDescriptor(stereotypeFullName.getName());
			};
			if (descriptor == null) {
				descriptor = StereotypeDescriptor.find(stereotypeName);
			}
			if (descriptor == null)
				return true;
			RelationDescriptor relationDescriptor = descriptor.getRelationDescriptor();
			if (relationDescriptor == null)
				return true;
			if (sourceElement!=null)
				if (targetElement!=null) {
					boolean result = relationDescriptor.relationAllowed(sourceElement, targetElement, relationshipsToIgnore);
					/*ContentModellerPlugin.getDefault().log("\t--> CHECK source= "
							+ ((NamedElement)sourceElement).getName()
							+ ", target="
							+ ((NamedElement)targetElement).getName()
							+ "; result::" + result + "\n");*/
					return result;
				} else 
					return relationDescriptor.sourceAllowed(sourceElement);
		}
		return true;
	}
	
	public static boolean isAlfrescoRelation(CreateRelationshipRequest request) {
		return Boolean.TRUE.equals(request.getParameter(CommandParameters.IS_ALFRESCO_RELATION));
	}
	
	public static boolean checkReorientRequest(ReorientRelationshipRequest request) {
		EObject relation = request.getRelationship();
		if (relation==null)
			return true;
		if (!(relation instanceof Relationship))
			return true;
		Relationship relationship = (Relationship)relation;
		List<Stereotype> stereotypes = relationship.getAppliedStereotypes();
		List<String> stereotypeNames = new ArrayList<>();
		for (Stereotype stereotype: stereotypes) {
			stereotypeNames.add(
					stereotype.getProfile().getName()
					+ "::"
					+ stereotype.getName());
		}
		RelationPair relationPair = new RelationPair(request);
		if (relationPair.isValid() && relationPair.isChanged())
			return checkAllRelations(relationPair.getSource(), relationPair.getTarget(), stereotypeNames, relationship);
		return true;
	}
	
}
