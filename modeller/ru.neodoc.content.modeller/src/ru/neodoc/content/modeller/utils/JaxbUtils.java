package ru.neodoc.content.modeller.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.ObjectFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.persistence.jaxb.JAXBBinder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;

import ru.neodoc.content.modeller.ContentModellerPlugin;
import ru.neodoc.jaxb.utils.CData;

public class JaxbUtils {
	
	public static class JaxbHelper<T> {
		
		protected T object;
		protected Binder<Node> binder;
		protected IFile iFile;
		
		@SuppressWarnings("unchecked")
		public JaxbHelper(JAXBContext context, IFile file) {
			super();
			this.iFile = file;
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setNamespaceAware(true);
				DocumentBuilder db = dbf.newDocumentBuilder();
		        if (!file.exists() || (file.getContents().available()==0)) {
		        	Document document = db.newDocument();
		        	JaxbUtils.save(document, file);
		        }
		        Document document = db.parse(file.getContents());
				this.binder = context.createBinder();
				try {
					//((JAXBBinder)this.binder).getXMLBinder().getMarshaller().setCharacterEscapeHandler();
				} catch (Exception e) {
					
				}
				try {
					
				} catch (Exception e) {
					
				}
				this.object = (T)binder.unmarshal(document);
			} catch (Exception e) {
				ContentModellerPlugin.getDefault().log(e);
			}
		}
		
		public T getObject() {
			return this.object;
		}
		
		public IFile getFile() {
			return iFile;
		}
		
		public void updateObject(Object object) throws JAXBException {
			this.binder.updateXML(object);
		}
		
		public void update(T jaxbObject) throws JAXBException {
			this.binder.updateXML(jaxbObject);
		}
		
		public void update() throws JAXBException {
			update(object);
		}
		
		public void save() {
			try {
				Document d = createNewDocument();
				Node n = this.binder.getXMLNode(this.object);
				Node newNode = n.cloneNode(true);
				d.adoptNode(newNode);
				d.appendChild(newNode);
				XMLUtils.TextToCDATAProcessor.convert(d);
				JaxbUtils.save(d, this.iFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static ObjectFactory ALFRESCO_OBJECT_FACTORY = new ObjectFactory();
	
	protected static JAXBContext alfrescoContext;
	
	public final static String ESCAPE_HANDLER_PROPERTY = /*"com.sun.xml.bind.marshaller.CharacterEscapeHandler"*/
			"com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler";
	
	public static Document createNewDocument() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		return db.newDocument();
	}
	
	public static void save(Document document, IFile file) throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError, UnsupportedEncodingException, CoreException {
		DOMSource source = new DOMSource(document);
    	StringWriter xmlAsWriter = new StringWriter();
    	StreamResult result = new StreamResult(xmlAsWriter);
    	Transformer trans = TransformerFactory.newInstance().newTransformer();
    	trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
    	trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		trans.setOutputProperty(OutputKeys.INDENT, "yes");    	
		trans.transform(source, result);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlAsWriter.toString().getBytes("UTF-8"));
    	if (!file.exists())
    		file.create(inputStream, true, null);
    	else
    		file.setContents(inputStream, true, false, null);		
	}
	
	public static JAXBContext createAlfrescoContentContext() throws JAXBException{
		if (alfrescoContext==null)
			alfrescoContext = JAXBContext.newInstance(Model.class.getPackage().getName(), Model.class.getClassLoader());
		return alfrescoContext;
	}
	
	public static Unmarshaller createAlfrescoContentUnmarshaller() throws JAXBException{
		Unmarshaller um = createAlfrescoContentContext().createUnmarshaller();
		// um.setProperty(ESCAPE_HANDLER_PROPERTY, new CdataCharacterEscapeHandler());
		return um;
	}
	
	@Deprecated
	public static Model read(IFile source) throws JAXBException, CoreException {
		Model result = null;
		Object obj = createAlfrescoContentUnmarshaller().unmarshal(source.getContents());
		if (obj instanceof Model)
			result = (Model)obj;
		return result;
	}
	
	public static JaxbHelper<Model> readModel(IFile source) throws JAXBException, CoreException {
		return new JaxbHelper<>(createAlfrescoContentContext(), source);
	}
	
	public static Marshaller createAlfrescoContentMarshaller() throws JAXBException {
		Marshaller m = createAlfrescoContentContext().createMarshaller(); 
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );	
		return m;
	}
	
	public static void setProperty(Marshaller marshaller, String key, Object value) {
		try {
			marshaller.setProperty(key, value);
		} catch (Exception e) {
			
		}
	}
	
	public static void write(Object object, Marshaller marshaller, IFile target) throws JAXBException, CoreException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		XMLOutputFactory xof = XMLOutputFactory.newInstance();
		XMLStreamWriter streamWriter;
		try {
			streamWriter = xof.createXMLStreamWriter( bos );
			CDataXMLStreamWriter cdataStreamWriter = new CDataXMLStreamWriter( new IndentingXMLStreamWriter(streamWriter ));
			
			setProperty(marshaller, "jaxb.encoding", "UTF-8");
			setProperty(marshaller, "jaxb.formatted.output", true);
			
			marshaller.marshal(object, cdataStreamWriter );
			cdataStreamWriter.flush();
			cdataStreamWriter.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		target.setContents(bis, true, false, null);
		
	}
	
	public static void write(Model model, IFile target) throws JAXBException, CoreException {
		write(model, createAlfrescoContentMarshaller(), target);
	}
	
	public static void textToCDATA(Document document) {
		
	}
	
	public static void replaceTextToCDATA(Node node) {
		if (node.getNodeType()==Node.TEXT_NODE) {
			Node newNode = getCDATANode(node);
			if (!newNode.equals(node))
				node.getParentNode().replaceChild(newNode, node);
			return;
		}
		
	}
	
	public static Node getCDATANode(Node node) {
		if (node.getNodeType()!=Node.TEXT_NODE)
			return node;
		String value = node.getNodeValue();
		if (CData.is(value)) {
			Node result = node.getOwnerDocument().createCDATASection(CData.getContent(value));
			return result;
		}
		return node;
	}
}
