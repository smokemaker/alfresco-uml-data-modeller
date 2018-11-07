package ru.neodoc.content.modeller.utils;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.jaxb.utils.CData;

public class XMLUtils {

	public static class TextToCDATAProcessor {
		
		public static void convert(Document document) {
			(new TextToCDATAProcessor(document)).process();
		}
		
		protected Document document;
		
		protected final Map<Node, Node> nodesToReplace = new HashMap<>();
		
		protected TextToCDATAProcessor(Document document) {
			this.document = document;
		}
		
		protected void process() {
			nodesToReplace.clear();
			processNode(document);
			for (Map.Entry<Node, Node> pair: nodesToReplace.entrySet()) {
				try {
					pair.getKey().getParentNode().replaceChild(pair.getValue(), pair.getKey());
				} catch (Exception e) {
					
				}
			}
		}
		
		protected void processNode(Node node) {
			if (node.getNodeType() == Node.CDATA_SECTION_NODE) {
				if (CommonUtils.isValueable(node.getNodeValue()))
					node.setNodeValue(CData.getContent(node.getNodeValue()));
				return;
			}
			if (node.getNodeType() == Node.TEXT_NODE) {
				String value = node.getNodeValue();
				if (CData.is(value)) {
					Node newNode = node.getOwnerDocument().createCDATASection(CData.getContent(value));
					this.nodesToReplace.put(node, newNode);
				}
			} else {
				Node child = node.getFirstChild();
				while (child!=null) {
					processNode(child);
					child = child.getNextSibling();
				}
				
			}
		}
	}
	
}
