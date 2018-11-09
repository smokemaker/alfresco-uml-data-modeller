package ru.neodoc.content.modeller.model;
/**
 * 
 */


import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.papyrus.infra.core.resource.AbstractBaseModel;
import org.eclipse.papyrus.infra.core.resource.IModel;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.internationalization.InternationalizationEntry;
import org.eclipse.papyrus.infra.internationalization.InternationalizationLibrary;
import org.eclipse.papyrus.infra.internationalization.common.utils.InternationalizationPreferencesUtils;
import org.eclipse.papyrus.infra.internationalization.utils.InternationalizationResourceOptionsConstants;

import ru.neodoc.content.ecore.alfresco.model.AlfrescoModelHelper;
import ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoFactory;
import ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionaries;
import ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.AlfrescoEditorPlugin;
import ru.neodoc.content.modeller.ContentModellerPlugin;
import ru.neodoc.content.modeller.preferences.PreferenceConstants;

/**
 * @author sstar
 * 
 */
public class AlfrescoModel extends AbstractBaseModel implements IModel {

	
	private static Map<URI, AlfrescoModel> instances = new HashMap<>();
	
	public static AlfrescoModel getModel(URI resourceURI) {
		return AlfrescoModel.instances.get(resourceURI);
	}
	
	/**
	 * File extension used for notation.
	 */
	public static final String ALF_REG_FILE_EXTENSION = "alfresco"; //$NON-NLS-1$

	public static final String CSS_FILE_EXTENSION = "css"; //$NON-NLS-1$
	/**
	 * Model ID.
	 */
	public static final String MODEL_ID = "ru.neodoc.content.ecore.alfresco.model.AlfrescoModel"; //$NON-NLS-1$

	protected AlfrescoPackage alfrescoPackage = AlfrescoPackage.eINSTANCE;

	protected AlfrescoFactory alfrescoFactory = alfrescoPackage.getAlfrescoFactory();
	
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public AlfrescoModel() {

	}

	/**
	 * Get the file extension used for this model.
	 * 
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractBaseModel#getModelFileExtension()
	 * 
	 * @return
	 */
	@Override
	protected String getModelFileExtension() {
		return ALF_REG_FILE_EXTENSION;
	}

	/**
	 * Get the identifier used to register this model.
	 * 
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractBaseModel#getIdentifier()
	 * 
	 * @return
	 */
	@Override
	public String getIdentifier() {
		return MODEL_ID;
	}

	@Override
	public void createModel(URI uri) {
		IPath path = new Path(uri.toPlatformString(true));
		createModel(path);
	}
	
	@Override
	public void createModel(IPath fullPath) {
		/*
		 * copy css file
		 */
		
		IPath path = fullPath.addFileExtension(CSS_FILE_EXTENSION);
/*		IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
		String cssFileLocation = "";
		try{
			String[] sPath = path.makeRelative().toString().split("/");
			IProject p = wsRoot.getProject(sPath[0]);
			IContainer folder = p;
			if (p.exists()){
				for (int i=1; i<sPath.length-1; i++){
					IFolder f = folder.getFolder(new Path(sPath[i]));
					if (!f.exists())
						f.create(true, false, null);
					folder = f;
				}
			
				IFile cssFile = folder.getFile(new Path(sPath[sPath.length-1]));
				if (!cssFile.exists()){
					InputStream is = Platform
							.getBundle(ru.neodoc.content.modeller.ContentModellerPlugin.PLUGIN_ID)
							.getEntry(AlfrescoModelHelper.DEFAULT_CSS_LOCATION)
							.openStream(); 
					cssFile.create(is, true, null);
					
					cssFileLocation = cssFile.getFullPath().toString();
				}
			}
		} catch (Exception e) {
			AlfrescoEditorPlugin.INSTANCE.log(e);
		}
*/		
		/*
		 * create model
		 */
		// Compute model URI
		resourceURI = getPlatformURI(fullPath.addFileExtension(ALF_REG_FILE_EXTENSION));

		// Create Resource of appropriate type
		resource = getModelManager().createResource(resourceURI, AlfrescoPackage.eCONTENT_TYPE);
		
		EObject rootObject = null;
		rootObject = createInitialModel();
//		((Alfresco)rootObject).setDefaultCss(cssFileLocation);
		
		if ((rootObject != null && (rootObject instanceof Dictionaries))) {
			resource.getContents().add(rootObject);
		}
		
/*		try {
//			resource.save(getSaveOptions());
		} catch (Exception e) {
			AlfrescoEditorPlugin.INSTANCE.log(e);
		}
*/		
		AlfrescoModel.instances.put(resourceURI, this);
		
		/*
		 * bugfix for #521273
		 */
		
		if (InternationalizationPreferencesUtils.isInternationalizationNeedToBeLoaded()) {
			if (ContentModellerPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_BUGFIX_521273)) {
				Bugfix521273 bf = new Bugfix521273();
				bf.CreateProperties(getModelManager(), fullPath);
				bf.CreateNotation(getModelManager(), fullPath);
			}
		}
		
//		resource = getResourceSet().createResource(resourceURI, getContentType());
	}

	protected class Bugfix521273 {
		
		public void CreateProperties(ModelSet modelSet, IPath basePath) {
			(new PropertiesResourceCreator(modelSet, basePath)).createResource();
		}
		
		public void CreateNotation(ModelSet modelSet, IPath basePath) {
			(new NotationResourceCreator(modelSet, basePath)).createResource();
		}
		
		protected class ResourceCreator {
			
			protected ModelSet rcModelSet;
			protected IPath rcBasePath;
			protected String rcExtension;
			
			protected URI rcUri;
			
			ResourceCreator(ModelSet modelSet, IPath basePath, String extension) {
				this.rcModelSet = modelSet;
				this.rcBasePath = basePath;
				this.rcExtension = extension;
				this.rcUri = AlfrescoModel.this.getPlatformURI(this.rcBasePath.addFileExtension(this.rcExtension));
			}
			
			public void createResource() {
				Resource r = this.rcModelSet.getResource(rcUri, false);
				if (null == r)
					r = this.rcModelSet.createResource(this.rcUri);
				fillResource(r);
				try {
					r.save(getSaveOptions());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			protected void fillResource(Resource r) {
				
			}
			
			protected Map<Object, Object> getSaveOptions(){
				return Collections.emptyMap();
			}
		}
		
		protected class PropertiesResourceCreator extends ResourceCreator {
			public PropertiesResourceCreator(ModelSet modelSet, IPath basePath) {
				super(modelSet, basePath, "properties");
			}
			
			@Override
			protected Map<Object, Object> getSaveOptions() {
				Map<Object, Object> options = new HashMap<>();
				options.put(InternationalizationResourceOptionsConstants.LOAD_SAVE_OPTION_RESOURCE_CONTENT, 
						new InternationalizationLibrary() {

							@Override
							public EClass eClass() {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public Resource eResource() {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public EObject eContainer() {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public EStructuralFeature eContainingFeature() {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public EReference eContainmentFeature() {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public EList<EObject> eContents() {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public TreeIterator<EObject> eAllContents() {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public boolean eIsProxy() {
								// TODO Auto-generated method stub
								return false;
							}

							@Override
							public EList<EObject> eCrossReferences() {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public Object eGet(EStructuralFeature feature) {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public Object eGet(EStructuralFeature feature, boolean resolve) {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public void eSet(EStructuralFeature feature, Object newValue) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public boolean eIsSet(EStructuralFeature feature) {
								// TODO Auto-generated method stub
								return false;
							}

							@Override
							public void eUnset(EStructuralFeature feature) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public Object eInvoke(EOperation operation, EList<?> arguments)
									throws InvocationTargetException {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public EList<Adapter> eAdapters() {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public boolean eDeliver() {
								// TODO Auto-generated method stub
								return false;
							}

							@Override
							public void eSetDeliver(boolean deliver) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void eNotify(Notification notification) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public EList<InternationalizationEntry> getEntries() {
								return ECollections.emptyEList();
							}
					
				});
				return options;
			}
		}
		
		protected class NotationResourceCreator extends ResourceCreator {
			public NotationResourceCreator(ModelSet modelSet, IPath basePath) {
				super(modelSet, basePath, "notation");
			}
			@Override
			protected Map<Object, Object> getSaveOptions() {
				Map<Object, Object> options = new HashMap<>();
				options.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, "ALL");
				return options;
			}
		}
	}
	
	@Override
	public void loadModel(URI uriWithoutExtension) {
		super.loadModel(uriWithoutExtension);
		AlfrescoModel.instances.put(getResourceURI(), this);
	}
	
	public EObject createInitialModel() {
/*		EClass eClass = (EClass)dictionariesPackage.getEClassifier(initialObjectCreationPage.getInitialObjectName());
		EObject rootObject = dictionariesFactory.create(eClass);
*/		
		/*EPackage alfrescoPackage = EcorePackageImpl.eINSTANCE.getEcoreFactory().createEPackage();
		alfrescoPackage.setName("alfresco");*/
		EObject rootObject = alfrescoFactory.createAlfresco();
		return rootObject;
	}
	
	protected Map<Object, Object> getSaveOptions() {
		Map<Object, Object> saveOptions = new HashMap<Object, Object>();

		// default save options.
		saveOptions.put(XMIResource.OPTION_DECLARE_XML, Boolean.TRUE);
		saveOptions.put(XMIResource.OPTION_PROCESS_DANGLING_HREF, XMIResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);
		saveOptions.put(XMIResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
		saveOptions.put(XMIResource.OPTION_USE_XMI_TYPE, Boolean.TRUE);
		saveOptions.put(XMIResource.OPTION_SAVE_TYPE_INFORMATION, Boolean.TRUE);
		saveOptions.put(XMIResource.OPTION_SKIP_ESCAPE_URI, Boolean.FALSE);
		saveOptions.put(XMIResource.OPTION_ENCODING, "UTF-8");

		//see bug 397987: [Core][Save] The referenced plugin models are saved using relative path
		saveOptions.put(XMIResource.OPTION_URI_HANDLER, new org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl.PlatformSchemeAware());

		saveOptions.put(XMIResource.OPTION_USE_XMI_TYPE, Boolean.TRUE);
		saveOptions.put(XMIResource.OPTION_SAVE_TYPE_INFORMATION, Boolean.TRUE);
		saveOptions.put(XMLResource.OPTION_ENCODING, "UTF-8");

		return saveOptions;
	}
	
	public ModelSet getModelSet(){
		return modelSet;
	}

	@Override
	public boolean canPersist(EObject object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void persist(EObject object) {
		// TODO Auto-generated method stub
		
	}

}
