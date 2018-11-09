package ru.neodoc.content.modeller.utils.sync;

import java.io.StringBufferInputStream;

import javax.xml.bind.JAXBException;

import org.alfresco.model.dictionary._1.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;
import ru.neodoc.content.modeller.ContentModellerPlugin;
import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.modeller.utils.JaxbUtils.JaxbHelper;
import ru.neodoc.content.modeller.utils.UML2XML;

public class ModelXMLSynchronizer implements IRunnableWithProgress  {

	protected Package model = null;
	protected String location = null;
	protected IProgressMonitor rootMonitor = null;
	
	public static enum SyncMode {
		SM_XML, SM_UML, SM_SYNC;
	} 
	
	protected SyncMode mode = SyncMode.SM_SYNC;
	
	
	
	public ModelXMLSynchronizer(){
		super();
	}

	public ModelXMLSynchronizer(Package model){
		super();
		this.model = model;
	}

	public ModelXMLSynchronizer(Package model, String location){
		super();
		this.model = model;
		this.location = location;
	}
	
	/*
	 * public
	 */
	
	@Override
	public void run(org.eclipse.core.runtime.IProgressMonitor monitor) 
			throws java.lang.reflect.InvocationTargetException ,InterruptedException {
		
		this.rootMonitor = monitor;
		switch(this.mode){
		case SM_XML:
			toXML();
			break;
		case SM_UML:
			fromXML();
			break;
		case SM_SYNC:
			sync();
			break;
		}
		
	};
	
	public void toXML(){
		this.rootMonitor.beginTask("Exporting model to XML", 100);
		
		/*
		 * check (or create) file
		 */
		this.rootMonitor.subTask("Checking file");
		IFile file = checkAndCreateFile();
		this.rootMonitor.worked(2);
		ContentModellerPlugin.getDefault().log("::: >>> file created");
		/*
		 * read (or create) model
		 */
		this.rootMonitor.subTask("Reading model");
		if (file == null)
			return;
		JaxbHelper<Model> xmlModelHelper;
		try {
			xmlModelHelper = readOrCreateModel(file);
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		Model xmlModel = xmlModelHelper.getObject();
		
		// fill model object
		UML2XML.fillModel(this.model, xmlModel);
		
		ContentModellerPlugin.getDefault().log("::: >>> model created");
		this.rootMonitor.worked(8);

		SubMonitor subMonitor = null;
		
		/*
		 * imports
		 */
		this.rootMonitor.subTask("Processing imports");
		subMonitor = SubMonitor.convert(this.rootMonitor, 10);
//		processImports(xmlModel, model, subMonitor);
		(new ImportsSynchronizer(this.model, xmlModel)).toXML(subMonitor);
		ContentModellerPlugin.getDefault().log("::: >>> imports finished");
/*		try {
			xmlModelHelper.updateObject(xmlModel.getImports());
			for(Model.Imports.Import imp: xmlModel.getImports().getImport())
				xmlModelHelper.updateObject(imp);
		} catch (Exception e) {
			e.printStackTrace();
		}
*/		this.rootMonitor.worked(10);
		
		/*
		 * namespaces
		 */
		this.rootMonitor.subTask("Processing namespaces");
		subMonitor = SubMonitor.convert(this.rootMonitor, 10);
//		processNamespaces(xmlModel, model, subMonitor);
		(new NamespacesSynchronizer(this.model, xmlModel)).toXML(subMonitor);
		ContentModellerPlugin.getDefault().log("::: >>> namespaces finished");
		this.rootMonitor.worked(10);
		
		/*
		 * datatypes
		 */
		this.rootMonitor.subTask("Processing datatypes");
		subMonitor = SubMonitor.convert(this.rootMonitor, 15);
		(new DatatypesSynchronizer(this.model, xmlModel)).toXML(subMonitor);
		ContentModellerPlugin.getDefault().log("::: >>> datatypes finished");
		this.rootMonitor.worked(15);
		
		/*
		 * constraints
		 */
		this.rootMonitor.subTask("Processing constraints");
		subMonitor = SubMonitor.convert(this.rootMonitor, 15);
		(new ConstraintsSynchronizer(this.model, xmlModel)).toXML(subMonitor);
		ContentModellerPlugin.getDefault().log("::: >>> constraints finished");
		this.rootMonitor.worked(15);
		
		/*
		 * types
		 */
		this.rootMonitor.subTask("Processing types");
		subMonitor = SubMonitor.convert(this.rootMonitor, 25);
		(new TypesSynchronizer(this.model, xmlModel)).toXML(subMonitor);
		ContentModellerPlugin.getDefault().log("::: >>> types finished");
		this.rootMonitor.worked(25);
		
		/*
		 * aspects
		 */
		this.rootMonitor.subTask("Processing aspects");
		subMonitor = SubMonitor.convert(this.rootMonitor, 25);
		(new AspectsSynchronizer(this.model, xmlModel)).toXML(subMonitor);
		ContentModellerPlugin.getDefault().log("::: >>> aspects finished");
		this.rootMonitor.worked(25);
		
		/*
		 * save model
		 */
		this.rootMonitor.subTask("Saving model");
		try {
			//JaxbUtils.write(xmlModel, file);
			xmlModelHelper.update();
			xmlModelHelper.save();
		} catch (Exception e) {
			AlfrescoEditorPlugin.INSTANCE.log(e);
		}
		this.rootMonitor.done();
	}
	
	public void fromXML(){
		// TODO implement
	}
	
	public void sync(){
		// TODO implement
	}
	
	/*
	 * routines
	 */
	
	protected JaxbHelper<Model> readOrCreateModel(IFile file) throws JAXBException {
		JaxbHelper<Model> helper = null;
		Model xmlModel = null;
		try {
			helper = JaxbUtils.readModel(file);
			xmlModel = helper.getObject();
		} catch (Exception e) {
			// NOOP
		}
		
		if (xmlModel == null) {
			xmlModel = createEmptyModel(this.model);
			try {
				JaxbUtils.write(xmlModel, file);
				helper = JaxbUtils.readModel(file);
				xmlModel = helper.getObject();				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return helper;
	}
	
	@SuppressWarnings("deprecation")
	protected IFile checkAndCreateFile(){
		IPath path = new Path(this.location);
		IPath fsPath = ResourcesPlugin.getWorkspace().getRoot().getFile(path).getRawLocation();
		IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(fsPath);
		
		if (files.length == 0)
			return null;
		
		IFile result = files[0];
		if (!result.exists())
			try {
				result.create(new StringBufferInputStream(""), true, this.rootMonitor);
			} catch (Exception e) {
				AlfrescoEditorPlugin.INSTANCE.log(e);
				return null;
			}
		
		return result;
	}
	
	protected Model createEmptyModel(Package modelPackage){
		Model result = UML2XML.emptyModel(modelPackage);
		return result;
	}
/*	
	protected void processImports(Model xmlModel, Package umlModel, IProgressMonitor monitor) {
		EList<PackageImport> packageImports = umlModel.getPackageImports();
		monitor.beginTask("Collecting", packageImports.size());
		Model.Imports xmlImports = AlfrescoXMLUtils.getImports(xmlModel);
		Model.Imports umlImports = UML2XML.modelImports(packageImports);
		
		List<Model.Imports.Import> finalImports =  CommonUtils.<Model.Imports.Import>updateList(
				xmlImports.getImport(), 
				umlImports.getImport(), 
				new CommonUtils.BaseListComparator<Model.Imports.Import>(){
					public String itemHash(Model.Imports.Import item){
						return item.getPrefix()==null?"":item.getPrefix();
					}
				});
		monitor.worked(packageImports.size());
		
		monitor.beginTask("Updating", 1);
		xmlModel.getImports().getImport().clear();
		xmlModel.getImports().getImport().addAll(finalImports);
		monitor.done();
	}

	protected void processNamespaces(Model xmlModel, Package umlModel, IProgressMonitor monitor) {
		List<Package> umlNamespacesList = AlfrescoUMLUtils.getNamespaces(umlModel);
		
		monitor.beginTask("Collecting", umlNamespacesList.size());
		
		Model.Namespaces xmlNamespaces = AlfrescoXMLUtils.getNamespaces(xmlModel);
		Model.Namespaces umlNamespaces = UML2XML.modelNamespaces(umlNamespacesList);
		
		List<Model.Namespaces.Namespace> finalNamespaces =  CommonUtils.<Model.Namespaces.Namespace>updateList(
				xmlNamespaces.getNamespace(), 
				umlNamespaces.getNamespace(), 
				new CommonUtils.BaseListComparator<Model.Namespaces.Namespace>(){
					public String itemHash(Model.Namespaces.Namespace item){
						return item.getPrefix()==null?"":item.getPrefix();
					}
				});
		monitor.worked(umlNamespacesList.size());
		
		monitor.beginTask("Updating", 1);
		xmlModel.getNamespaces().getNamespace().clear();
		xmlModel.getNamespaces().getNamespace().addAll(finalNamespaces);
		monitor.done();
	}
*/	
	
	/*
	 * ACCESSORS
	 */
	
	public Package getModel() {
		return this.model;
	}
	public void setModel(Package model) {
		this.model = model;
	}
	public String getLocation() {
		return this.location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public SyncMode getMode() {
		return this.mode;
	}

	public void setMode(SyncMode mode) {
		this.mode = mode;
	}
	
}
