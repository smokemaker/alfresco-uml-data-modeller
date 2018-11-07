package ru.neodoc.content.modeller.utils.sync;

import java.util.List;

import org.alfresco.model.dictionary._1.Constraint;
import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.NamedValue;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.AlfrescoXMLUtils;
import ru.neodoc.content.modeller.utils.UML2XML;
import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ItemUpdater;
import ru.neodoc.content.utils.CommonUtils.BaseListComparator;
import ru.neodoc.content.utils.CommonUtils.ListComparator;

public class ConstraintsSynchronizer extends ModelPartNamespaceBasedSynchronizer<Model.Constraints, Constraint> {

	public ConstraintsSynchronizer(Package umlModel, Model xmlModel) {
		super(umlModel, xmlModel);
		this.replaceOnUpdate = true;
	}
	
	
	@Override
	protected List<Constraint> getXmlList() {
		return AlfrescoXMLUtils.getConstraint(this.xmlModel).getConstraint();
	}

	@Override
	protected List<Constraint> getUmlListForNamespace(Package namespace) {
		return UML2XML.modelConstraints(
				AlfrescoUMLUtils.getConstraints(namespace)).getConstraint();
	}
	
	@Override
	protected ListComparator<Constraint> getComparator() {
		return new CommonUtils.BaseListComparator<Constraint>(){
			public String itemHash(Constraint item){
				if (CommonUtils.isValueable(item.getRef()))
					return item.getRef();
				return (item.getName()==null?"":item.getName())
						+ (item.getType()==null?"":item.getType());
			}
		};
	}

	@Override
	protected ItemUpdater<Constraint> getItemUpdater() {
		return new ItemUpdater<Constraint>() {
			@Override
			public void updateItem(Constraint origin, Constraint updated) {
				origin.setName(updated.getName());
				origin.setRef(updated.getRef());
				origin.setType(updated.getType());
				
				ListComparator<NamedValue> comparator = new BaseListComparator<NamedValue>() {

					@Override
					public String itemHash(NamedValue item) {
						if (item!=null)
							return item.getName();
						return null;
					}
					
				};
				
				List<NamedValue> parameters = CommonUtils.<NamedValue>updateList(
						origin.getParameter(), 
						updated.getParameter(), 
						comparator, 
						false);
				
				CommonUtils.<NamedValue>applyUpdatesToList(
						origin.getParameter(), 
						parameters, 
						comparator,
						new ItemUpdater<NamedValue>() {

							@Override
							public void updateItem(NamedValue originLocal, NamedValue updatedLocal) {
								originLocal.setList(updatedLocal.getList());
								originLocal.setValue(updatedLocal.getValue());
							}
							
						}
						);
				
			}
		};
	}
	
/*	@Override
	protected void updateXMLModel(List<Constraint> finalList) {
		this.xmlModel.getConstraints().getConstraint().clear();
		this.xmlModel.getConstraints().getConstraint().addAll(finalList);
	}
*/	@Override
	protected void clearXMLModel() {
		this.xmlModel.setConstraints(null);
	}
}
