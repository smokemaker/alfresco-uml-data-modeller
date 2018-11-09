package ru.neodoc.content.modeller.palette.helper.advice;

import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;

public class PropertyOverrideEditHelperAdvice extends AlfrescoRelationEditHelperAdvice {

	@Override
	protected boolean approveCreateAlfrescoRelationRequest(IEditCommandRequest request,
			CreateRelationshipRequest createRelationshipRequest) {
		try {
			Class source = (Class)createRelationshipRequest.getSource();
			Property target = (Property)createRelationshipRequest.getTarget();
			
			if ((source==null) || (target==null))
				return false;
			
			if (source.equals(target.getOwner()))
				return false;
			
			return source.allParents().contains((Classifier)target.getOwner());
			
		} catch (Exception e) {
			
		}
		return false;
	}

}
