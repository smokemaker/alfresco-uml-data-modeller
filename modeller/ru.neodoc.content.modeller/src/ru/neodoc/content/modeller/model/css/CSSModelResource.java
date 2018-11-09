package ru.neodoc.content.modeller.model.css;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import ru.neodoc.content.modeller.model.AlfrescoModel;

public class CSSModelResource {

	public static final String CSS_FILE_EXTENSION = "css";
	
	public static CSSModelResource create(URI modelUri, AlfrescoModel alfrescoModel) {
		return new CSSModelResource(modelUri, alfrescoModel);
	}
	
	protected URI modelUri;
	protected AlfrescoModel alfrescoModel;
	
	protected Resource cssResource;
	
	protected boolean isNew = true;
	
	protected CSSModelResource(URI modelUri, AlfrescoModel alfrescoModel) {
		this.modelUri = modelUri;
		this.alfrescoModel = alfrescoModel;
	}
	
	protected void initialize() {
		URI cssUri = modelUri.appendFileExtension(CSS_FILE_EXTENSION);
		for (Resource r: alfrescoModel.getModelSet().getResources()) {
			if (r.getURI().equals(cssUri)) {
				isNew = false;
				break;
			}
		}
		cssResource = alfrescoModel.getModelSet().getResource(cssUri, false);
	}
	
}
