package ru.neodoc.content.modeller.extensions.jaxb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.w3c.dom.Document;

import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.modeller.extensions.Extensions;
import ru.neodoc.modeller.extensions.Model;
import ru.neodoc.modeller.extensions.ModelObject;
import ru.neodoc.modeller.extensions.ModelObjectsContainer;
import ru.neodoc.modeller.extensions.ObjectFactory;

public class ExtensionsJAXBHelper {

	protected JAXBContext jaxbContext; 
	protected ObjectFactory objectFactory = new ObjectFactory();
	
	protected IFile extensionsFile;
	
	protected Extensions extensionsRoot;
	
	public ExtensionsJAXBHelper() {
		super();
		try {
			jaxbContext = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName(), ObjectFactory.class.getClassLoader());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reload() {
		if (this.extensionsFile==null)
			return;
		load(this.extensionsFile);
	}
	
	public void load(IFile iFile) {
		
		this.extensionsFile = iFile;
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
	        if (!iFile.exists() || (iFile.getContents().available()==0)) {
	        	Document document = db.newDocument();
	        	JaxbUtils.save(document, iFile);
	        	JaxbUtils.write(objectFactory.createExtensions(), jaxbContext.createMarshaller(), this.extensionsFile);
	        }
	        
	        extensionsRoot = (Extensions)jaxbContext.createUnmarshaller().unmarshal(this.extensionsFile.getContents());
	        
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	public void save() {
		Marshaller marshaller = null;
		try {
			marshaller = jaxbContext.createMarshaller();
		} catch (Exception e) {
			
		}
		if (marshaller==null)
			return;
		Map<String, String> mapper = new HashMap<>();
		try {
			mapper.put(objectFactory.getClass().getPackage().getAnnotation(XmlSchema.class).namespace(), "ext");
		} catch (Exception e) {
			
		}
		JaxbUtils.setProperty(marshaller, "eclipselink.namespace-prefix-mapper", mapper);
		JaxbUtils.setProperty(marshaller, "com.sun.xml.bind.namespacePrefixMapper", mapper);
		try {
			JaxbUtils.write(getExtensions(), marshaller, extensionsFile);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public Extensions getExtensions() {
		return this.extensionsRoot;
	}
	
	public ModelObject locate(List<PrefixedName> path, boolean create) {
		Extensions extensions = getExtensions();
		if (extensions==null)
			return null;
		if (path==null)
			return null;
		if (path.isEmpty())
			return null;
		PrefixedName modelName = path.get(0);
		List<PrefixedName> subPath = path.subList(1, path.size());
		Model model = getModel(modelName, create);
		if (model==null)
			return null;
		return locate(model, subPath, create);
	}
	
	public Model getModel(PrefixedName name, boolean create) {
		if (name==null)
			return null;
		Extensions extensions = getExtensions();
		if (extensions==null)
			return null;
		if (extensions.getModels()==null)
			if (!create)
				return null;
			else
				extensions.setModels(objectFactory.createExtensionsModels());
		for (Model m: extensions.getModels().getModel())
			if (name.isEqual(((new PrefixedName(m.getName())).withPrefix(m.getPrefix()))))
				return m;
		if (!create)
			return null;
		Model m = objectFactory.createModel();
		m.setName(name.getName());
		m.setPrefix(name.getPrefix());
		extensions.getModels().getModel().add(m);
		return m;
	}
	
	public ModelObject locate(ModelObjectsContainer container, List<PrefixedName> path, boolean create) {
		ModelObjectsContainer root = container;
		if (path==null)
			return null;
		if (path.isEmpty())
			return null;

		ModelObjectsContainer containerToSearch = root;
		ModelObject result = null;
		for (PrefixedName prefixedName: path) {
			if (prefixedName.isEmpty())
				continue;
			result = recursiveFindInContainer(containerToSearch, prefixedName);
			if (result == null)
				if (!create)
					return null;
				else {
					result = objectFactory.createModelObject();
					result.setName(prefixedName.getName());
					result.setPrefix(prefixedName.getPrefix());
					containerToSearch.getModelObject().add(result);
					containerToSearch = result;
				}
		}
		return result;
	}
	
	public ModelObject recursiveFindInContainer(ModelObjectsContainer container, PrefixedName modelObjectName) {
		if (container.getModelObject().isEmpty())
			return null;
		for (ModelObject mo: container.getModelObject())
			if (PrefixedName.create(mo.getName(), mo.getPrefix()) .isEqual(modelObjectName))
					return mo;
		for (ModelObject mo: container.getModelObject()) {
			ModelObject result = recursiveFindInContainer(mo, modelObjectName);
			if (result!=null)
				return result;
		}
		return null;	
	}
}
