package ru.neodoc.content.modeller.utils.sync;

import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.AlfrescoXMLUtils;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ItemUpdater;
import ru.neodoc.content.utils.CommonUtils.ListComparator;

public abstract class ModelPartSynchronizer<ContainerType, ContainerElementType> {

	protected Package umlModel = null;
	protected Model xmlModel = null;
	
	protected boolean replaceOnUpdate = false;
	
	public ModelPartSynchronizer(){
		
	}
	
	public ModelPartSynchronizer(Package umlModel, Model xmlModel) {
		this.xmlModel = xmlModel;
		this.umlModel = umlModel;
	}
	
	public void toXML(IProgressMonitor monitor){
		List<ContainerElementType> umlList = getUmlList();
		int size = umlList.size();
		
		monitor.beginTask("Collecting", size);
		List<ContainerElementType> xmlList = getXmlList();
		
		List<ContainerElementType> finalList =  CommonUtils.<ContainerElementType>updateList(
				xmlList, 
				umlList,
				getComparator(),
				this.replaceOnUpdate
			);
		
		monitor.worked(size);

		monitor.beginTask("Updating", 1);
		applyToXMLModel(finalList);
		monitor.done();
		
	}
	
	protected abstract List<ContainerElementType> getXmlList();
	
	protected abstract List<ContainerElementType> getUmlList();
	
	protected abstract ListComparator<ContainerElementType> getComparator();
	
	protected abstract ItemUpdater<ContainerElementType> getItemUpdater();
	
	protected ItemUpdater<ContainerElementType> getDefaultItemUpdater(){
		return new ItemUpdater<ContainerElementType>() {

			@Override
			public void updateItem(ContainerElementType origin, ContainerElementType updated) {
				// NOOP
			}

		};
	}
	
	protected void applyToXMLModel(List<ContainerElementType> finalList){
		if (finalList.size()==0)
			clearXMLModel();
		else
			updateXMLModel(finalList);
	}
	
	protected void updateXMLModel(List<ContainerElementType> finalList) {
		CommonUtils.<ContainerElementType>applyUpdatesToList(
				getXmlList(), 
				finalList, 
				getComparator(),
				getItemUpdater()
		);		
	};

	protected abstract void clearXMLModel();

	public Package getUmlModel() {
		return this.umlModel;
	}

	public void setUmlModel(Package umlModel) {
		this.umlModel = umlModel;
	}

	public Model getXmlModel() {
		return this.xmlModel;
	}

	public void setXmlModel(Model xmlModel) {
		this.xmlModel = xmlModel;
	}
	
}
