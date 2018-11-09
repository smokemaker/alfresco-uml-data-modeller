package ru.neodoc.content.ecore.alfresco.model.alfresco.presentation.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandActionDelegate;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;

import ru.neodoc.content.ecore.alfresco.model.AlfrescoModelHelper;
import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;

public class LoadDictionariesFromUMLCommand extends CompoundCommand implements CommandActionDelegate {

	 /**
	   * This value is used to indicate that an optional positional index
	   * indicator is unspecified.
	   * @deprecated As of EMF 2.0, use {@link CommandParameter#NO_INDEX}, whose
	   * value is equal to this, instead.
	   */
	  @Deprecated
	  protected static final int NO_INDEX = CommandParameter.NO_INDEX;

	
	protected Alfresco alfrescoModel;
	protected Model umlModel;

	protected EditingDomain domain;
	protected EObject owner;
	
	public static Command create(EditingDomain domain, Object owner, Alfresco alfModel, Model umlModel, Collection<?> selection){
		List<Object> data = new ArrayList<>();
		data.add(alfModel);
		data.add(umlModel);
		return domain.createCommand(
				LoadDictionariesFromUMLCommand.class, 
				new CommandParameter(owner, null, data, new ArrayList<Object>(selection)));
	}
	
	public LoadDictionariesFromUMLCommand(EditingDomain domain,
			EObject owner,
			EStructuralFeature feature,
			List<Object> data,
			int index,
			Collection<?> selection) {
		super();
		
		this.domain = domain;
		this.alfrescoModel = (Alfresco)data.get(0);
		this.umlModel = (Model)data.get(1);
		this.owner = owner;
		
		fillCommandList();
	}
	
	protected void fillCommandList1(){
		Dictionary d = AlfrescoModelHelper.newDictionary();
		d.setName("D1");
		d.setLocation("L1");
		
		List<Dictionary> dictionariesToAdd = new ArrayList<>();
		dictionariesToAdd.add(d);
		
		AddCommand command = new AddCommand(domain, 
				owner, 
				DictionariesPackage.Literals.DICTIONARIES__DICTIONARIES, 
				dictionariesToAdd);
		append(command);
		
	}
	
	protected void fillCommandList(){
		
		@SuppressWarnings("unchecked")
		List<Package> models = (List<Package>)AlfrescoSearchHelperFactory
				.getModelSearcher()
				.startWith(umlModel)
				.search();
		
		List<Dictionary> dictionariesToAdd = new ArrayList<>();
		
		for (Package m: models) {
			Dictionary d = AlfrescoModelHelper.newDictionary();
			d.setName(m.getName());
			d.setIsReadOnly(true);
			d.setLocation("<internal>");
			
			@SuppressWarnings("unchecked")
			List<Package> namespaces = (List<Package>) AlfrescoSearchHelperFactory
					.getNamespaceSearcher()
					.startWith(m)
					.search();
			
			for (Package ns: namespaces)
				AlfrescoModelHelper.newNamespace(d, ns.getName(), ns.getURI());
			
			for (PackageImport pi: m.getPackageImports()) {
				Package ip = pi.getImportedPackage();
				AlfrescoModelHelper.newImport(d, ip.getName(), ip.getURI());
			}
			
			dictionariesToAdd.add(d);
			
		}		
		
		AddCommand command = new AddCommand(domain, 
				owner, 
				DictionariesPackage.Literals.DICTIONARIES__DICTIONARIES, 
				dictionariesToAdd);
		append(command);
	}
	
	@Override
	public Object getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return null;
	}

}
