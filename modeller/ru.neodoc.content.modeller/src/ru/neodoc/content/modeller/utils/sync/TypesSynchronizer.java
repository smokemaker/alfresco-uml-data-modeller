package ru.neodoc.content.modeller.utils.sync;

import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Type;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.AlfrescoXMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class TypesSynchronizer extends ClassesSynchronizer<Model.Types, Type> {

	public TypesSynchronizer(Package umlModel, Model xmlModel) {
		super(umlModel, xmlModel);
		this.stereotypeToSearch = AlfrescoProfile.ForClass.Type._NAME;
	}

	@Override
	protected List<Type> getXmlList() {
		return AlfrescoXMLUtils.getTypes(this.xmlModel).getType();
	}
	
/*	@Override
	protected void updateXMLModel(List<Type> finalList) {
		this.xmlModel.getTypes().getType().clear();
		this.xmlModel.getTypes().getType().addAll(finalList);
	}
*/	
	@Override
	protected void clearXMLModel() {
		this.xmlModel.setTypes(null);
	}
}
