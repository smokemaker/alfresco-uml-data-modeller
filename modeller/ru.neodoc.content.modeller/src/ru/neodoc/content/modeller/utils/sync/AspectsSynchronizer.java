package ru.neodoc.content.modeller.utils.sync;

import java.util.List;

import org.alfresco.model.dictionary._1.Aspect;
import org.alfresco.model.dictionary._1.Model;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.AlfrescoXMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class AspectsSynchronizer extends ClassesSynchronizer<Model.Aspects, Aspect> {

	public AspectsSynchronizer(Package umlModel, Model xmlModel) {
		super(umlModel, xmlModel);
		this.stereotypeToSearch = AlfrescoProfile.ForClass.Aspect._NAME;
	}

	@Override
	protected List<Aspect> getXmlList() {
		return AlfrescoXMLUtils.getAspects(xmlModel).getAspect();
	}
	
/*	@Override
	protected void updateXMLModel(List<Aspect> finalList) {
		xmlModel.getAspects().getAspect().clear();
		xmlModel.getAspects().getAspect().addAll(finalList);
	}
*/	
	@Override
	protected void clearXMLModel() {
		xmlModel.setAspects(null);
	}
}
