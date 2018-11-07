package ru.neodoc.content.modeller.utils.sync;

import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Imports.Import;
import org.alfresco.model.dictionary._1.Model.Namespaces.Namespace;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.AlfrescoXMLUtils;
import ru.neodoc.content.modeller.utils.UML2XML;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ItemUpdater;
import ru.neodoc.content.utils.CommonUtils.ListComparator;

public class NamespacesSynchronizer extends ModelPartSynchronizer<Model.Namespaces, Model.Namespaces.Namespace> {

	public NamespacesSynchronizer(Package umlModel, Model xmlModel) {
		super(umlModel, xmlModel);
	}
	
	
	@Override
	protected List<Model.Namespaces.Namespace> getXmlList() {
		return AlfrescoXMLUtils.getNamespaces(xmlModel).getNamespace();
	}

	@Override
	protected List<Model.Namespaces.Namespace> getUmlList() {
		return UML2XML.modelNamespaces(AlfrescoUMLUtils.getNamespaces(umlModel)).getNamespace();
	}

	@Override
	protected ListComparator<Model.Namespaces.Namespace> getComparator() {
		return new CommonUtils.BaseListComparator<Model.Namespaces.Namespace>(){
			public String itemHash(Model.Namespaces.Namespace item){
				return item.getPrefix()==null?"":item.getPrefix();
			}
		};
	}

	@Override
	protected ItemUpdater<Namespace> getItemUpdater() {
		return new ItemUpdater<Model.Namespaces.Namespace>(){
			
			@Override
			public void updateItem(Model.Namespaces.Namespace origin, Model.Namespaces.Namespace updated) {
				origin.setPrefix(updated.getPrefix());
				origin.setUri(updated.getUri());
			}
		};
	}
	
	@Override
	protected void updateXMLModel(List<Model.Namespaces.Namespace> finalList) {
/*		xmlModel.getNamespaces().getNamespace().clear();
		xmlModel.getNamespaces().getNamespace().addAll(finalList);
*/		CommonUtils.<Model.Namespaces.Namespace>applyUpdatesToList(
			AlfrescoXMLUtils.getNamespaces(xmlModel).getNamespace(), 
			finalList, 
			getComparator(), 
			new ItemUpdater<Model.Namespaces.Namespace>(){
	
				@Override
				public void updateItem(Model.Namespaces.Namespace origin, Model.Namespaces.Namespace updated) {
					origin.setPrefix(updated.getPrefix());
					origin.setUri(updated.getUri());
				}});

	}

	@Override
	protected void clearXMLModel() {
		xmlModel.setNamespaces(null);
	}
	
}
