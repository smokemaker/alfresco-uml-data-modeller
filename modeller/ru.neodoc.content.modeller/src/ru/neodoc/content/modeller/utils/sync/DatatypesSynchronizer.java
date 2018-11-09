package ru.neodoc.content.modeller.utils.sync;

import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.DataTypes.DataType;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.AlfrescoXMLUtils;
import ru.neodoc.content.modeller.utils.UML2XML;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ItemUpdater;
import ru.neodoc.content.utils.CommonUtils.ListComparator;

public class DatatypesSynchronizer extends ModelPartNamespaceBasedSynchronizer<Model.DataTypes, Model.DataTypes.DataType> {

	public DatatypesSynchronizer(Package umlModel, Model xmlModel) {
		super(umlModel, xmlModel);
	}
	
	
	@Override
	protected List<Model.DataTypes.DataType> getXmlList() {
		return AlfrescoXMLUtils.getDataTypes(xmlModel).getDataType();
	}

	@Override
	protected List<DataType> getUmlListForNamespace(Package namespace) {
		return UML2XML.modelDataTypes(
				AlfrescoUMLUtils.getDataTypes(namespace)).getDataType();
	}
	
	@Override
	protected ListComparator<Model.DataTypes.DataType> getComparator() {
		return new CommonUtils.BaseListComparator<Model.DataTypes.DataType>(){
			public String itemHash(Model.DataTypes.DataType item){
				return item.getName()==null?"":item.getName();
			}
		};
	}

	@Override
	protected ItemUpdater<DataType> getItemUpdater() {
		return new ItemUpdater<Model.DataTypes.DataType>() {

			@Override
			public void updateItem(DataType origin, DataType updated) {
				// origin.setName(updated.getName());
				origin.setTitle(updated.getTitle());
				origin.setDescription(updated.getDescription());
				origin.setAnalyserClass(updated.getAnalyserClass());
				origin.setJavaClass(updated.getJavaClass());
			}
		};
	}
	

	@Override
	protected void clearXMLModel() {
		xmlModel.setDataTypes(null);
	}
}
