package ru.neodoc.content.modeller.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.uml2.uml.Model;

import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace;
import ru.neodoc.content.modeller.ContentModellerPlugin;
import ru.neodoc.content.modeller.utils.NamespaceSourceInfo;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;

public class AlfrescoNamespacesCheckedTreeDialog extends
		CheckedTreeSelectionDialog {
	
	protected static class AlfrescoNamespacesLabelProvider implements ILabelProvider {
		
		protected Alfresco alfrescoRoot = null;
		protected List<ILabelProviderListener> listeners = new ArrayList<ILabelProviderListener>(); 
		
		public AlfrescoNamespacesLabelProvider(Alfresco alfrescoRoot){
			this.alfrescoRoot = alfrescoRoot;
		}
		
		@Override
		public void addListener(ILabelProviderListener arg0) {
			listeners.add(arg0);
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isLabelProperty(Object arg0, String arg1) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener arg0) {
			listeners.remove(arg0);
		}

		@Override
		public Image getImage(Object arg0) {
			return null;
		}

		@Override
		public String getText(Object arg0) {
			if (arg0 instanceof Dictionary){
				Dictionary d = (Dictionary)arg0;
				return d.getName() + " [" + d.getLocation() + "]";
			}
			if (arg0 instanceof Namespace){
				Namespace n = (Namespace)arg0;
				return n.getAlias() + " {" + n.getUrl() + "}";
			}
			return null;
		}
		
	} 
	
	protected static class AlfrescoNamespacesTreeContentProvider implements ITreeContentProvider {

		protected Alfresco alfrescoRoot = null;
		protected Model umlRoot = null;
		
		public AlfrescoNamespacesTreeContentProvider(Alfresco alfrescoRoot, Model umlRoot){
			this.alfrescoRoot = alfrescoRoot;
			this.umlRoot = umlRoot;
		}
		
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object[] getChildren(Object arg0) {
			if (arg0 instanceof Dictionary){
				Object[] objects = ((Dictionary)arg0).getNamespaces().toArray();
				return objects;
			}
			return new Object[0];
		}

		@Override
		public Object[] getElements(Object arg0) {
			if (alfrescoRoot!=null && alfrescoRoot.getDictionaries()!=null){
				List<Dictionary> elements = new ArrayList<Dictionary>();
				for (Dictionary d: alfrescoRoot.getDictionaries().getDictionaries())
					if (AlfrescoUMLUtils.findModel(d.getName(), umlRoot)==null
						&& (new NamespaceSourceInfo(d)).loadModel())
						elements.add(d);
				Object[] objects = elements.toArray(); 
				return objects;
			}
			return new Object[0];
		}

		@Override
		public Object getParent(Object arg0) {
			if (arg0 instanceof Namespace){
				return (Dictionary)((Namespace)arg0).eContainer();
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object arg0) {
			if (arg0 instanceof Dictionary)
				return ((Dictionary)arg0).getNamespaces().size()>0;
			return false;
		}
		
	}
	
	public AlfrescoNamespacesCheckedTreeDialog(Alfresco alfrescoRoot, Model umlRoot) {
		super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				new AlfrescoNamespacesLabelProvider(alfrescoRoot), 
				new AlfrescoNamespacesTreeContentProvider(alfrescoRoot, umlRoot)
		);
		setContainerMode(true);
		setTitle("Select namespaces to create");
		setEmptyListMessage("No new dictionaries found");
		setValidator(new ISelectionStatusValidator(){
			@Override
			public IStatus validate(Object[] arg0) {
				getOKButton().setEnabled(arg0.length>0);
				return new Status(arg0.length>0?IStatus.OK:IStatus.ERROR, ContentModellerPlugin.PLUGIN_ID, "");
			}
		});
	}
}
