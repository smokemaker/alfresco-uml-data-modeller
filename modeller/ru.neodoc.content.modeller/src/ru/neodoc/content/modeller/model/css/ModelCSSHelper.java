package ru.neodoc.content.modeller.model.css;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.gmfdiag.css.resource.CSSNotationModel;
import org.eclipse.papyrus.infra.gmfdiag.css.stylesheets.ModelStyleSheets;
import org.eclipse.papyrus.infra.gmfdiag.css.stylesheets.StyleSheet;
import org.eclipse.papyrus.infra.gmfdiag.css.stylesheets.StyleSheetReference;
import org.eclipse.papyrus.infra.gmfdiag.css.stylesheets.StylesheetsPackage;

import ru.neodoc.content.ecore.alfresco.model.AlfrescoModelHelper;
import ru.neodoc.content.modeller.model.AlfrescoModelUtils;
import ru.neodoc.content.modeller.model.css.commands.CreateCSSFileCommand;
import ru.neodoc.content.modeller.model.css.commands.SetDefaultCSSCommand;
import ru.neodoc.content.utils.CommonUtils;

public class ModelCSSHelper {

	public static final String CSS_FILE_EXTENSION = "css"; 
	
	
	protected class CSSFileInfo {
		
		URI modelUri;
		URI cssFileUri;
		IPath cssFilepath;
		IPath cssFileRelativePath;
		IFile cssFile;
		String cssFileName;
		String cssTemplatePath;
		
		public CSSFileInfo() {
			modelUri = modelSet.getURIWithoutExtension();
			cssFileUri = modelUri.appendFileExtension(CSS_FILE_EXTENSION);
			cssFilepath = new Path(cssFileUri.toPlatformString(true));
			cssFileRelativePath = cssFilepath.makeRelative();
			prepare();
		}
		
		public void prepare() {
			String[] splitted = cssFileRelativePath.toString().split("/");
			IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
			IProject p = wsRoot.getProject(splitted[0]);
			IContainer folder = p;
			
			try {
				if (p.exists()){
					for (int i=1; i<splitted.length-1; i++){
						IFolder f = folder.getFolder(new Path(splitted[i]));
						if (!f.exists())
							f.create(true, false, null);
						folder = f;
					}
				
					cssFile = folder.getFile(new Path(splitted[splitted.length-1]));
					cssFileName = cssFile.getName();
				}
			} catch (Exception e) {
				cssFile = null;
			}
		}
		
		public boolean isValid() {
			return cssFile!=null;
		}
		
		public boolean isFromTemplate() {
			return cssTemplatePath!=null;
		}
	}
	
	protected ModelSet modelSet;
	
	protected CSSFileInfo cssFileInfo;
	
	public ModelCSSHelper(ModelSet modelSet) {
		this.modelSet = modelSet;
		this.cssFileInfo = new CSSFileInfo();
	}
	
	protected void createCSSFile(InputStream input) {
		getCommandStack().execute(new CreateCSSFileCommand(modelSet, cssFileInfo.cssFile, input));
	}
	
	protected InputStream getDefaultCSSInputStream() {
		InputStream result = null;
		try {
			result = Platform
					.getBundle(ru.neodoc.content.modeller.ContentModellerPlugin.PLUGIN_ID)
					.getEntry(AlfrescoModelHelper.DEFAULT_CSS_LOCATION)
					.openStream(); 
		} catch (Exception e) {
			
		} 		
		
		if (result==null)
			try {
				result = new ByteArrayInputStream("/* default css for model */".getBytes(StandardCharsets.UTF_8.name()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		return result;
	}
	
	protected InputStream getCSSInputStreamFromTemplate(String pluginId, String templatePath) {
		InputStream result = null;
		java.net.URL templateURL = Platform.getBundle(pluginId).getResource(templatePath);
		try {
			result = templateURL.openConnection().getInputStream();
			cssFileInfo.cssTemplatePath = templatePath;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public void initEmptyModel() {
		InputStream is = getDefaultCSSInputStream();
		createCSSFile(is);
		
		updateNotation();
	}
	
	public void initModelFromTemplate(String pluginId, String modelPath) {
		InputStream is = getCSSInputStreamFromTemplate(pluginId, modelPath+"."+CSS_FILE_EXTENSION);
		if (is==null)
			initEmptyModel();
		else {
			createCSSFile(is);
			
			updateNotation();
		}
	}
	
	public void setDefaultCSSToAlfrescoModel() {
		getCommandStack().execute(
				new SetDefaultCSSCommand(modelSet.getTransactionalEditingDomain(), modelSet)
			);		
		
	};
	
	protected CommandStack getCommandStack() {
		return this.modelSet.getTransactionalEditingDomain().getCommandStack();
	}
	
	protected void updateNotation() {
		CSSNotationModel cssNotationModel = AlfrescoModelUtils.getNotationModel(modelSet);
		ModelStyleSheets modelStyleSheets = getModelStyleSheets(cssNotationModel);
		boolean updated = false;
		if (cssFileInfo.isFromTemplate() && (modelStyleSheets!=null)) {
			updated = updateStyleSheetReference(modelStyleSheets);
		}
		if (updated)
			return;
		
		if (modelStyleSheets == null)
			modelStyleSheets = createModelStyleSheets(cssNotationModel); 
		
		createStyleSheetReference(modelStyleSheets);
	}
	
	protected ModelStyleSheets getModelStyleSheets(CSSNotationModel model) {
		ModelStyleSheets modelStyleSheets = null;
		for (EObject eObject: model.getResource().getContents())
			if (eObject instanceof ModelStyleSheets) {
				modelStyleSheets = (ModelStyleSheets)eObject;
				break;
			}
		return modelStyleSheets;
	}

	protected ModelStyleSheets createModelStyleSheets(CSSNotationModel model) {
		final CSSNotationModel notationModel = model;
		final ModelStyleSheets modelStyleSheets = StylesheetsPackage.eINSTANCE.getStylesheetsFactory().createModelStyleSheets();
		Command command = new RecordingCommand(modelSet.getTransactionalEditingDomain()) {
			
			@Override
			protected void doExecute() {
				notationModel.getResource().getContents().add(modelStyleSheets);
			}
		};
		getCommandStack().execute(command);
		return modelStyleSheets;
	}
	
	protected boolean createStyleSheetReference(ModelStyleSheets modelStyleSheets) {
		StyleSheetReference ssr = StylesheetsPackage.eINSTANCE.getStylesheetsFactory().createStyleSheetReference();
		ssr.setPath(cssFileInfo.cssFileName);
		AddCommand command = new AddCommand(
				modelSet.getTransactionalEditingDomain(), 
				modelStyleSheets.getStylesheets(), 
				ssr);
		getCommandStack().execute(command);
		return true;
	}
	
	protected boolean updateStyleSheetReference(ModelStyleSheets modelStyleSheets) {
		StyleSheetReference styleSheetReference = null;
		
		String cssTemplateName = cssFileInfo.cssTemplatePath.substring(cssFileInfo.cssTemplatePath.lastIndexOf("/")+1);
		if (!CommonUtils.isValueable(cssTemplateName))
			return false;
		
		for (StyleSheet ss: modelStyleSheets.getStylesheets())
			if (ss instanceof StyleSheetReference) {
				StyleSheetReference ssr = (StyleSheetReference)ss;
				if (CommonUtils.isValueable(ssr.getPath()))
					if (ssr.getPath().endsWith(cssTemplateName)) {
						styleSheetReference = ssr;
						break;
					}
			}
		
		if (styleSheetReference==null)
			return false;
		
		Command c = new SetCommand(
				modelSet.getTransactionalEditingDomain(), 
				styleSheetReference, 
				StylesheetsPackage.eINSTANCE.getStyleSheetReference().getEStructuralFeature(StylesheetsPackage.STYLE_SHEET_REFERENCE__PATH), 
				cssFileInfo.cssFileName);
		getCommandStack().execute(c);
		return true;
	}
}
