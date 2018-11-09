package ru.neodoc.content.profile.alfresco.ui;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.papyrus.infra.services.labelprovider.service.IFilteredLabelProvider;

// TODO make the class generalized and more specific to current profile/library  
public class AlfrescoLabelProvider extends LabelProvider implements IFilteredLabelProvider {

	@Override
	public boolean accept(Object element) {
		if (element instanceof EObject)
			return accept((EObject)element);
		return false;
	}

	protected boolean accept(EObject element) {
		if (element.eResource()==null)
			return true;
		return false;
	}
	
	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		return super.getText(element);
	}
	
}
