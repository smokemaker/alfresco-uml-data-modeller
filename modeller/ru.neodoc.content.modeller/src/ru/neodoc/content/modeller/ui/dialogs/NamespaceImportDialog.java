package ru.neodoc.content.modeller.ui.dialogs;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.papyrus.uml.profile.ui.dialogs.PackageImportTreeSelectionDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForModel.Alfresco;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class NamespaceImportDialog extends PackageImportTreeSelectionDialog {

	public NamespaceImportDialog(Shell parent, Collection<? extends Package> models) {
		super(parent, models);
		// TODO Auto-generated constructor stub
	}

	public NamespaceImportDialog(Shell parent, Package model) {
		super(parent, model);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Composite createDialogArea(Composite parent) {
		
		this.allowedActions.clear();
		this.allowedActions.add(ImportAction.IMPORT);
		
		Composite result = (Composite) super.createDialogArea(parent);

		elementTree.getTree().setHeaderVisible(true);
		
		Composite buttons = new Composite(result, SWT.NONE);
		buttons.setLayout(new RowLayout());

/*		Button loadAll = new Button(buttons, SWT.PUSH);
		loadAll.setText("Load All");
		loadAll.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectAll(ImportAction.LOAD);
			}
		});
*/
		Button importAll = new Button(buttons, SWT.PUSH);
		importAll.setText("Import All");
		importAll.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectAll(ImportAction.IMPORT);
			}
		});

/*		Button copyAll = new Button(buttons, SWT.PUSH);
		copyAll.setText("Copy All");
		copyAll.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectAll(ImportAction.COPY);
			}
		});
*/
		Button deselectAll = new Button(buttons, SWT.PUSH);
		deselectAll.setText("Deselect All");
		deselectAll.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectAll(ImportAction.NONE);
			}
		});

		return result;
	}
	
	@Override
	protected Collection<? extends Element> getChildren(Package package_) {
		Collection<Package> result = new java.util.ArrayList<Package>();

		StereotypedElement se = AbstractProfile.asUntyped(package_);
		
		AlfrescoProfile.ForModel.Alfresco alfresco = se.get(Alfresco.class); 
		AlfrescoProfile.ForPackage.Model model = se.get(AlfrescoProfile.ForPackage.Model.class); 
		AlfrescoProfile.ForPackage.Namespace namespace = se.get(AlfrescoProfile.ForPackage.Namespace.class);
		
		if (alfresco!=null)
			return AlfrescoSearchHelperFactory.getModelSearcher().startWith(package_).search();
		
		if (model!=null)
			return AlfrescoSearchHelperFactory.getNamespaceSearcher().startWith(package_).search();
		
		return Collections.emptyList();
		
	}	
	
	@Override
	protected String getElementText(Element element) {
		if (element instanceof Package) {
			Package p = (Package)element;
			if (AlfrescoProfile.ForPackage.Namespace._HELPER.is(p))
				return p.getName() + " {" + p.getURI() + "}";
		} 
		return super.getElementText(element);
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}
}
