package ru.neodoc.content.modeller.palette.helper.advice;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.UnexecutableCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.SetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.UMLPackage;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.ConstraintedInline;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForNamedElement.ConstraintedObject;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class ConstraintedInlineEditHelperAdvice extends AbstractEditHelperAdvice {

	@Override
	protected ICommand getBeforeConfigureCommand(ConfigureRequest request) {
		// TODO Auto-generated method stub
		return super.getBeforeConfigureCommand(request);
	}
	
	@Override
	protected ICommand getAfterConfigureCommand(ConfigureRequest request) {
		EObject source = (EObject)request.getParameter(CreateRelationshipRequest.SOURCE);
		EObject target = (EObject)request.getParameter(CreateRelationshipRequest.TARGET);
		if ((source!=null) && (target!=null)) {
			try {
				NamedElement sourceNamedElement = (NamedElement)source;
				NamedElement targetNamedElement = (NamedElement)target;
				if (sourceNamedElement instanceof DirectedRelationship)
					sourceNamedElement = (NamedElement)((DirectedRelationship)sourceNamedElement).getSources().get(0);
				if (targetNamedElement instanceof Constraint) {
					Constraint constraint = (Constraint)targetNamedElement;
					constraint.setContext(sourceNamedElement instanceof Namespace
							?(Namespace)sourceNamedElement
							:sourceNamedElement.getNamespace());
				}
			} catch (Exception e) {
				
			}
		}
		return super.getAfterConfigureCommand(request);
	}
	
	@Override
	protected ICommand getAfterCreateCommand(CreateElementRequest request) {
		// TODO Auto-generated method stub
		return super.getAfterCreateCommand(request);
	}
	
	@Override
	protected ICommand getBeforeCreateCommand(CreateElementRequest request) {
		// TODO Auto-generated method stub
		return super.getBeforeCreateCommand(request);
	}
	
	@Override
	protected ICommand getAfterCreateRelationshipCommand(CreateRelationshipRequest request) {
		// TODO Auto-generated method stub
		return super.getAfterCreateRelationshipCommand(request);
	}
	
	@Override
	protected ICommand getBeforeCreateRelationshipCommand(CreateRelationshipRequest request) {
		// TODO Auto-generated method stub
		return super.getBeforeCreateRelationshipCommand(request);
	}
	
	@Override
	protected ICommand getAfterEditContextCommand(GetEditContextRequest request) {
		// TODO Auto-generated method stub
		return super.getAfterEditContextCommand(request);
	}
	
	@Override
	protected ICommand getAfterSetCommand(SetRequest request) {
		// TODO Auto-generated method stub
		return super.getAfterSetCommand(request);
	}
	
	@Override
	protected ICommand getBeforeReorientReferenceRelationshipCommand(ReorientReferenceRelationshipRequest request) {
		// TODO Auto-generated method stub
		return super.getBeforeReorientReferenceRelationshipCommand(request);
	}
	
	@Override
	protected ICommand getBeforeReorientRelationshipCommand(ReorientRelationshipRequest request) {
		if (RelationshipHelper.checkReorientRequest(request))
			return super.getBeforeReorientRelationshipCommand(request);
		return UnexecutableCommand.INSTANCE;
	}
	
	@Override
	protected ICommand getAfterReorientRelationshipCommand(ReorientRelationshipRequest request) {
		if (request.getRelationship() instanceof Dependency) {
			Dependency dependency = (Dependency)request.getRelationship();
			RelationshipHelper.RelationPair pair = new RelationshipHelper.RelationPair(request);
			if (pair.isValid() && pair.isChanged()) {
				Element source = pair.getSource();
				StereotypedElement stereotypedSource = AbstractProfile.asUntyped(source);
				if (stereotypedSource.has(ConstraintedObject.class)) {
					@SuppressWarnings("unchecked")
					ConstraintedObject<NamedElement> constraintedSource = stereotypedSource.get(ConstraintedObject.class);
					ConstraintedInline constraintedInline = ConstraintedInline._HELPER.is(dependency)
							?ConstraintedInline._HELPER.getFor(dependency)
							:null;
					if (constraintedInline!=null) {
						SetRequest setRequest = new SetRequest(
								request.getEditingDomain(), 
								constraintedInline.getConstraint().getElement(), 
								UMLPackage.eINSTANCE.getConstraint().getEStructuralFeature(UMLPackage.CONSTRAINT__CONTEXT), 
								constraintedSource.getConstraintContext());
						return new SetValueCommand(setRequest);
					}					
				}
			}
/*			ConstraintedInline constraintedInline = ConstraintedInline._HELPER.is(dependency)
					?ConstraintedInline._HELPER.getFor(dependency)
					:null;
			if (constraintedInline!=null) {
				constraintedInline
					.getConstraint()
					.getElementClassified()
					.setContext(constraintedInline
							.getConstraintedObject()
							.getConstraintContext()
					);
			}*/
		}
		return super.getAfterReorientRelationshipCommand(request);
	}
}
