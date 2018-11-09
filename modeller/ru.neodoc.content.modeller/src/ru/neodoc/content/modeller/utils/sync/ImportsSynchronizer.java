package ru.neodoc.content.modeller.utils.sync;

import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.Imports.Import;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.AlfrescoXMLUtils;
import ru.neodoc.content.modeller.utils.UML2XML;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ItemUpdater;
import ru.neodoc.content.utils.CommonUtils.ListComparator;

public class ImportsSynchronizer extends ModelPartSynchronizer<Model.Imports, Model.Imports.Import> {

	public ImportsSynchronizer(Package umlModel, Model xmlModel) {
		super(umlModel, xmlModel);
	}
	
	
	@Override
	protected List<Model.Imports.Import> getXmlList() {
		return AlfrescoXMLUtils.getImports(xmlModel).getImport();
	}

	@Override
	protected List<Model.Imports.Import> getUmlList() {
		return UML2XML.modelImports(umlModel.getPackageImports()).getImport();
	}

	@Override
	protected ListComparator<Model.Imports.Import> getComparator() {
		return new CommonUtils.BaseListComparator<Model.Imports.Import>(){
			@Override
			public String itemHash(Model.Imports.Import item){
				return item.getPrefix()==null?"":item.getPrefix();
			}
		};
	}

	@Override
	protected ItemUpdater<Import> getItemUpdater() {
		
		return new ItemUpdater<Model.Imports.Import>(){

			@Override
			public void updateItem(Import origin, Import updated) {
				origin.setPrefix(updated.getPrefix());
				origin.setUri(updated.getUri());
			}};
	}
	
	@Override
	protected void clearXMLModel() {
		xmlModel.setImports(null);
	}
	
}
