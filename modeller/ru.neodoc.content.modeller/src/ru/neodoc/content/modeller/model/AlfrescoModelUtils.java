package ru.neodoc.content.modeller.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.EObjectListValueStyle;
import org.eclipse.gmf.runtime.notation.NamedStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.papyrus.commands.ICreationCommand;
// import org.eclipse.papyrus.commands.wrappers.GMFtoEMFCommandWrapper;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.emf.gmf.command.GMFtoEMFCommandWrapper;
// import org.eclipse.papyrus.infra.core.utils.EditorUtils;
import org.eclipse.papyrus.infra.emf.utils.ServiceUtilsForEObject;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationModel;
import org.eclipse.papyrus.infra.gmfdiag.css.notation.CSSDiagram;
import org.eclipse.papyrus.infra.gmfdiag.css.notation.CSSStyles;
import org.eclipse.papyrus.infra.gmfdiag.css.resource.CSSNotationModel;
import org.eclipse.papyrus.infra.gmfdiag.css.stylesheets.StyleSheetReference;
import org.eclipse.papyrus.infra.gmfdiag.css.stylesheets.StylesheetsFactory;
import org.eclipse.papyrus.infra.gmfdiag.navigation.CreatedNavigableElement;
import org.eclipse.papyrus.infra.gmfdiag.navigation.ExistingNavigableElement;
import org.eclipse.papyrus.infra.gmfdiag.navigation.NavigableElement;
import org.eclipse.papyrus.infra.gmfdiag.navigation.NavigationHelper;
import org.eclipse.papyrus.infra.viewpoints.policy.PolicyChecker;
import org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype;
import org.eclipse.papyrus.uml.diagram.clazz.CreateClassDiagramCommand;
import org.eclipse.papyrus.uml.extensionpoints.utils.Util;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;
import ru.neodoc.content.modeller.ContentModellerPlugin;
import ru.neodoc.content.modeller.diagram.pkg.PackageDiagramCreateCommand;

public class AlfrescoModelUtils {

	public static ModelSet getModelSet(EObject eObject){
		ResourceSet resourceSet = Util.getResourceSet(eObject);
		ModelSet modelSet = null;
		if (resourceSet instanceof ModelSet){
			modelSet = (ModelSet)resourceSet;
			if (modelSet.getModel(AlfrescoModel.MODEL_ID) == null) {
				AlfrescoModel alfrescoModel = new AlfrescoModel();
				modelSet.registerModel(alfrescoModel);
			}
		};
		return modelSet;
	}
	
	public static AlfrescoModel getAlfrescoModel(EObject eObject){
		return getAlfrescoModel(getModelSet(eObject));
	}

	public static AlfrescoModel getAlfrescoModel(ModelSet modelSet){
		if (modelSet==null)
			return null;
		return (AlfrescoModel)modelSet.getModel(AlfrescoModel.MODEL_ID);
	}
	
	public static EObject getAlfrescoRootObject(AlfrescoModel alfrescoModel) {
		return getAlfrescoRootObject(alfrescoModel, false);
	}
	
	public static EObject getAlfrescoRootObject(AlfrescoModel alfrescoModel, boolean create) {
		Resource resource = alfrescoModel.getResource();
		EObject result = null;
		if (resource != null) {
			EList<EObject> contents = resource.getContents();
			if (contents != null)
				if (contents.isEmpty()) {
					try {
						contents.add(alfrescoModel.createInitialModel());
						result = contents.get(0);
					} catch (Exception e) {
						ContentModellerPlugin.getDefault().log(e);
					}
				} else {
					result = contents.get(0);
				}
		}
/*		if (resource == null){
			String fileString = eObject.eResource().getURI().toString();
			fileString = fileString.substring(0, fileString.lastIndexOf(".")+1) + AlfrescoModel.ALF_REG_FILE_EXTENSION;
			fileString = fileString.replaceAll("platform:/resource",	"");
			IPath location = new Path(fileString);
			alfrescoModel.loadModel(location.removeFileExtension());
			resource = alfrescoModel.getResource();
		}
*/		return result;
		
	}
	
	public static EObject getAlfrescoRootObject(ModelSet modelSet) {
		AlfrescoModel model = getAlfrescoModel(modelSet);
		return getAlfrescoRootObject(model);
	}
	
	public static EObject getAlfrescoRootObject(EObject eObject){
		ModelSet modelSet = getModelSet(eObject);
		AlfrescoModel alfrescoModel = getAlfrescoModel(modelSet);
		return getAlfrescoRootObject(alfrescoModel);
	}
	
	public static Alfresco getAlfrescoRoot(EObject eObject) {
		EObject rootObject = getAlfrescoRootObject(eObject);
		if (rootObject == null || !(rootObject instanceof Alfresco))
			return null;
		return (Alfresco)rootObject;
	}
	
	public static CompositeCommand getLinkCreateNavigableDiagramCommand(final NavigableElement navElement, ICreationCommand creationCommandInterface, final String diagramName, ModelSet modelSet) {
		CompositeCommand compositeCommand = new CompositeCommand("Create diagram");

		if(navElement instanceof CreatedNavigableElement) {
			compositeCommand.add(new AbstractTransactionalCommand(modelSet.getTransactionalEditingDomain(), "Create hierarchy", null) {

				@Override
				protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
					NavigationHelper.linkToModel((CreatedNavigableElement)navElement);
					NavigationHelper.setBaseName((CreatedNavigableElement)navElement, "");
					return CommandResult.newOKCommandResult();
				}
			});
		}

		ICommand createDiagCommand = creationCommandInterface.getCreateDiagramCommand(modelSet, navElement.getElement(), diagramName);
		compositeCommand.add(createDiagCommand);
//		compositeCommand.add(new OpenDiagramCommand(diResourceSet.getTransactionalEditingDomain(), createDiagCommand));

		return compositeCommand;
	}
	
	public static void createEmptyClassDiagram(EObject rootObject, Package p){
		createEmptyClassDiagram(rootObject, p, p.getName()+"_classes");
	}
	
	public static void createEmptyClassDiagram(EObject rootObject, Package p, String diagramName){
		try{
			ModelSet modelSet = ServiceUtilsForEObject.getInstance().getModelSet(rootObject);
	
			if(modelSet != null) {
				try {
					CompositeCommand command = getLinkCreateNavigableDiagramCommand(
							new ExistingNavigableElement(p, null), 
							new CreateClassDiagramCommand(), diagramName, modelSet);
					modelSet.getTransactionalEditingDomain().getCommandStack().execute(new GMFtoEMFCommandWrapper(command));
					applyCssToDiagram(p, diagramName);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			AlfrescoEditorPlugin.getPlugin().log(e);
		}
		
	}
	
	public static ViewPrototype findPrototypeByName(EObject owner, String name){
		if (name==null)
			return null;
		if (name.trim().length()==0)
			return null;
		
		for (final ViewPrototype proto : PolicyChecker.getFor(owner).getPrototypesFor(owner)) {
			// TODO change to real 
			if (proto.getRepresentationKind().getImplementationID() != null ) {
				if (name.equals(proto.getRepresentationKind().getName())){
					return proto;
				}
			}
		}		
		return null;
	}
	
	public static ViewPrototype findPrototypeForPackageDiagram(EObject owner){
		return findPrototypeByName(owner, "Package Diagram");
	}
	
	public static void createEmptyPackageDiagram(Package owner, String diagramName) {
		ViewPrototype proto = findPrototypeForPackageDiagram(owner);
		if (proto != null) {
			proto.instantiateOn(owner, diagramName);
		}
	}

	public static void createEmptyPackageDiagram_(Package owner, String diagramName) {
		ModelSet modelSet = null;
		
		try {
			modelSet = ServiceUtilsForEObject.getInstance().getModelSet(owner);
		} catch (Exception e) {
			
		}
		

		if(modelSet != null) {
			try {
				CompositeCommand command = getLinkCreateNavigableDiagramCommand(
						new ExistingNavigableElement(owner, null), 
						new PackageDiagramCreateCommand(), diagramName, modelSet);
				modelSet.getTransactionalEditingDomain().getCommandStack().execute(new GMFtoEMFCommandWrapper(command));
				applyCssToDiagram(owner, diagramName);
			} catch (Exception e) {
			}
		}
		
	}	
	
	public static CSSNotationModel getNotationModel(EObject eObject){
		ModelSet modelSet = getModelSet(eObject);
		return getNotationModel(modelSet);
	}
	
	public static CSSNotationModel getNotationModel(ModelSet modelSet){
		if (modelSet == null)
			return null;
		NotationModel nmodel = (NotationModel)modelSet.getModel(NotationModel.MODEL_ID);
		if (!(nmodel instanceof CSSNotationModel))
			return null;
		CSSNotationModel model = (CSSNotationModel)nmodel;
		return model;
		
	}
	
	public static URI getCSSUri(ModelSet modelSet) {
		CSSNotationModel cssModel = getNotationModel(modelSet);
		if (cssModel == null)
			return null;
		try {
			URI result = cssModel.getResource().getURI();
			result = result.trimFileExtension().appendFileExtension("css");
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void setDefaultCss(Alfresco alfrescoRoot) {
		if (alfrescoRoot == null)
			return;
		ModelSet modelSet = getModelSet(alfrescoRoot);
		URI cssURI = getCSSUri(modelSet);
		if (cssURI != null)
			alfrescoRoot.setDefaultCss(cssURI.toString());
	}
	
	public static List<EObject> getNotationContent(CSSNotationModel model){
		List<EObject> result = Collections.<EObject>emptyList();
		if (model!=null){
			Resource resource = model.getResource();
			if (resource==null){
				String fileString = model.getResourceURI().toString();
				fileString = fileString.substring(0, fileString.lastIndexOf(".")+1) + NotationModel.NOTATION_FILE_EXTENSION;
				fileString = fileString.replaceAll("platform:/resource",	"");
				IPath location = new Path(fileString);
				model.loadModel(location.removeFileExtension());
				resource = model.getResource();
			}
			if (resource != null){
				result = resource.getContents();
			}
		}
		return result;
		
	}
	
	public static List<EObject> getNotationContent(EObject eObject){
		CSSNotationModel model = getNotationModel(eObject);
		return getNotationContent(model);
	}
	
	@SuppressWarnings("unchecked")
	public static void applyCssToDiagram(Package owner, String diagramName){
		List<EObject> notationContent = getNotationContent(owner);
		CSSDiagram diagram = findDiagram(owner, diagramName, notationContent);
		String defaultCss = AlfrescoModelUtils.getAlfrescoRoot(owner).getDefaultCss();
		applyCSSToDiagram(diagram, defaultCss);
	}
	
	public static void applyCSSToDiagram(CSSDiagram diagram, String cssPath){
		EObjectListValueStyle styleList = null;
		for (Object style: diagram.getStyles()){
			if (!(style instanceof NamedStyle))
				continue;
			if (CSSStyles.CSS_DIAGRAM_STYLESHEETS_KEY.equals(((NamedStyle)style).getName())
					&& (style instanceof EObjectListValueStyle)){
				styleList = (EObjectListValueStyle)style;
				break;
			}
		}
		if (styleList==null) {
			styleList = (EObjectListValueStyle)diagram.createStyle(
					NotationPackage.eINSTANCE.getEObjectListValueStyle()
				);
			styleList.setName(CSSStyles.CSS_DIAGRAM_STYLESHEETS_KEY);
			
		}
		StyleSheetReference ref = null;
		for (Object obj: styleList.getEObjectListValue()){
			if (obj instanceof StyleSheetReference)
				if (((StyleSheetReference)obj).getPath().equals(cssPath)){
					ref = (StyleSheetReference)obj;
					break;
				}
		}
		
		if (ref == null) {
			ref = StylesheetsFactory.eINSTANCE.createStyleSheetReference();
			ref.setPath(cssPath);
			styleList.getEObjectListValue().add(ref);
		}
		
	}
	
	public static EObjectListValueStyle getStyleSheetList(CSSDiagram diagram){
		EObjectListValueStyle styleList = null;
		for (Object style: diagram.getStyles()){
			if (!(style instanceof NamedStyle))
				continue;
			if (CSSStyles.CSS_DIAGRAM_STYLESHEETS_KEY.equals(((NamedStyle)style).getName())
					&& (style instanceof EObjectListValueStyle)){
				styleList = (EObjectListValueStyle)style;
				break;
			}
		}
		return styleList;
	} 
	
	public static void removeCssReferencesFromDiagram(CSSDiagram diagram){
		EObjectListValueStyle styleList = getStyleSheetList(diagram);
		if (styleList==null) {
			return;
		}
		List<Object> toRemove = new ArrayList<Object>();
		List<?> styles = styleList.getEObjectListValue();
		for (Object obj: styles){
			if (obj instanceof StyleSheetReference)
				toRemove.add(obj);
		}
		for (Object obj: toRemove)
			styles.remove(obj);
	}

	public static void removeStyleSheetsFromDiagram(CSSDiagram diagram){
		EObjectListValueStyle styleList = getStyleSheetList(diagram);
		if (styleList==null) {
			return;
		}
		styleList.getEObjectListValue().clear();
	}
	
	public static CSSDiagram findDiagram(Package owner, String diagramName, List<EObject> content) {
		CSSDiagram result = null;
		for (EObject eObject: content){
			if (!(eObject instanceof CSSDiagram))
				continue;

			CSSDiagram diagram = (CSSDiagram)eObject;
			if (!diagram.getName().equals(diagramName))
				continue;
			
			if (!diagram.getElement().equals(owner))
				continue;
			
			result = diagram;
		}
		return result;
	}
	
}
