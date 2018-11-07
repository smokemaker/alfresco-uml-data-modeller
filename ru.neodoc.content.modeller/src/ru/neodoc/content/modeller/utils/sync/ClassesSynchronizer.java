package ru.neodoc.content.modeller.utils.sync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.alfresco.model.dictionary._1.Association;
import org.alfresco.model.dictionary._1.ChildAssociation;
import org.alfresco.model.dictionary._1.Class.Associations;
import org.alfresco.model.dictionary._1.Class.Properties;
import org.alfresco.model.dictionary._1.Constraint;
import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.NamedValue;
import org.alfresco.model.dictionary._1.ObjectFactory;
import org.alfresco.model.dictionary._1.Property;
import org.alfresco.model.dictionary._1.Property.Constraints;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.UML2XML;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ItemUpdater;
import ru.neodoc.content.utils.CommonUtils.ListComparator;

public abstract class ClassesSynchronizer<ContainerType, ElementType extends org.alfresco.model.dictionary._1.Class> 
	extends ModelPartNamespaceBasedSynchronizer<ContainerType, ElementType> {

	protected Class<?> classToSearch = null;
	protected String stereotypeToSearch = null;
	
	public ClassesSynchronizer(Package umlModel, Model xmlModel) {
		super(umlModel, xmlModel);
		this.replaceOnUpdate = true;
	}
	
	@Override
	protected List<ElementType> getUmlListForNamespace(Package namespace) {
		return UML2XML.<ElementType>modelClasses(
				ru.neodoc.content.utils.uml.search.helper.SearchHelperFactory
				.getTypeSearcher()
				.target(this.classToSearch)
				.target(this.stereotypeToSearch)
				.startWith(namespace)
				.search());
	}
	
	@Override
	protected ListComparator<ElementType> getComparator() {
		return new CommonUtils.BaseListComparator<ElementType>(){
			@Override
			public String itemHash(ElementType item){
				return item.getName()==null?"":item.getName();
			}
		};
	}
	
	@Override
	protected ItemUpdater<ElementType> getItemUpdater() {
		return new ItemUpdater<ElementType>() {

			@Override
			public void updateItem(ElementType origin, ElementType updated) {
				
				ObjectFactory of = new ObjectFactory();
				
				origin.setTitle(updated.getTitle());
				origin.setDescription(updated.getDescription());
				origin.setArchive(updated.isArchive());
				origin.setIncludedInSuperTypeQuery(updated.isIncludedInSuperTypeQuery());
				origin.setOverrides(updated.getOverrides());
				origin.setParent(updated.getParent());
				
				// Associations
				
				if (updated.getAssociations()==null)
					origin.setAssociations(null);
				else {
					
					Associations updatedAssociations = updated.getAssociations();
					Associations originAssociations = origin.getAssociations();
					if (originAssociations==null) {
						originAssociations = of.createClassAssociations();
						origin.setAssociations(originAssociations);
					}
					
					// peer
					if (updatedAssociations.getAssociation().isEmpty())
						originAssociations.getAssociation().clear();
					else {
						CommonUtils.ListComparator<Association> peerComparator = 
								new CommonUtils.BaseListComparator<Association>() {

									@Override
									public String itemHash(Association item) {
										if (item==null)
											return null;
										return item.getName();
									}
							
								};
						List<Association> peerAssociations = CommonUtils.<Association>updateList(
								originAssociations.getAssociation(), 
								updatedAssociations.getAssociation(), 
								peerComparator, 
								false);
						
						CommonUtils.<Association>applyUpdatesToList(
								originAssociations.getAssociation(), 
								peerAssociations, 
								peerComparator, 
								new ItemUpdater<Association>() {

									@Override
									public void updateItem(Association originLocal, Association updatedLocal) {
										originLocal.setTitle(updatedLocal.getTitle());
										originLocal.setDescription(updatedLocal.getDescription());
										originLocal.setSource(updatedLocal.getSource());
										originLocal.setTarget(updatedLocal.getTarget());
									}
								});
					}
					
					
					// child
					if (updatedAssociations.getChildAssociation().isEmpty())
						originAssociations.getChildAssociation().clear();
					else {
						CommonUtils.ListComparator<ChildAssociation> childComparator = 
								new CommonUtils.BaseListComparator<ChildAssociation>() {

									@Override
									public String itemHash(ChildAssociation item) {
										if (item==null)
											return null;
										return item.getName() + item.getChildName();
									}
							
								};
						List<ChildAssociation> childAssociations = CommonUtils.<ChildAssociation>updateList(
								originAssociations.getChildAssociation(), 
								updatedAssociations.getChildAssociation(), 
								childComparator, 
								false);
						
						CommonUtils.<ChildAssociation>applyUpdatesToList(
								originAssociations.getChildAssociation(), 
								childAssociations, 
								childComparator, 
								new ItemUpdater<ChildAssociation>() {

									@Override
									public void updateItem(ChildAssociation originLocal, ChildAssociation updatedLocal) {
										originLocal.setTitle(updatedLocal.getTitle());
										originLocal.setDescription(updatedLocal.getDescription());
										originLocal.setSource(updatedLocal.getSource());
										originLocal.setTarget(updatedLocal.getTarget());
										originLocal.setChildName(updatedLocal.getChildName());
										originLocal.setDuplicate(updatedLocal.isDuplicate());
										originLocal.setPropagateTimestamps(updatedLocal.isPropagateTimestamps());
									}
								});
					}
					
				}
				
				// Mandatory aspects
				
				origin.setMandatoryAspects(updated.getMandatoryAspects());
				
				// Properties
				if (updated.getProperties()==null)
					origin.setProperties(null);
				else {
					Properties updatedProperties = updated.getProperties();
					Properties originProperties = origin.getProperties();
					if (originProperties==null) {
						originProperties = of.createClassProperties();
						origin.setProperties(originProperties);
					}
					
					if (updatedProperties.getProperty().isEmpty())
						originProperties.getProperty().clear();
					else {
						
						CommonUtils.ListComparator<Property> propertyComparator = new CommonUtils.BaseListComparator<Property>() {
							@Override
							public String itemHash(Property item) {
								return item==null?"":item.getName();
							}
						};

						CommonUtils.ItemUpdater<Property> propertyUpdater = new CommonUtils.ItemUpdater<Property>() {

							@Override
							public void updateItem(Property originLocal, Property updatedLocal) {
								originLocal.setTitle(updatedLocal.getTitle());
								originLocal.setDescription(updatedLocal.getDescription());
								originLocal.setDefault(updatedLocal.getDefault());
								originLocal.setEncrypted(updatedLocal.isEncrypted());
								originLocal.setIndex(updatedLocal.getIndex());
								originLocal.setMandatory(updatedLocal.getMandatory());
								originLocal.setMultiple(updatedLocal.isMultiple());
								originLocal.setProtected(updatedLocal.isProtected());
								originLocal.setType(updatedLocal.getType());
								
								// constraints
								
								if (updatedLocal.getConstraints()==null)
									originLocal.setConstraints(null);
								else {
									Constraints originConstraints = originLocal.getConstraints();
									Constraints updatedConstraints = updatedLocal.getConstraints();
									if (originConstraints==null) {
										originConstraints = of.createPropertyConstraints();
										originLocal.setConstraints(originConstraints);
									}
									
									CommonUtils.ListComparator<Constraint> constraintComparator = new CommonUtils.BaseListComparator<Constraint>(){

										@Override
										public String itemHash(Constraint item) {
											if (item==null)
												return "";
											if (CommonUtils.isValueable(item.getRef()))
												return item.getRef();
											String hash = item.getName() + "_" + item.getType();
/*											if (!item.getParameter().isEmpty()) {
												NamedValue nv = item.getParameter().get(0);
												hash += ":" + nv.getName();
												if (CommonUtils.isValueable(nv.getValue()))
													hash+=nv.getValue();
												if (nv.getList()!=null) {
													List<String> list = new ArrayList<>(); 
													Collections.copy(nv.getList().getValue(), list);
													list.sort(new Comparator<String>() {
														@Override
														public int compare(String o1, String o2) {
															if (o1==null)
																return -1;
															if (o2==null)
																return 1;
															return o1.compareToIgnoreCase(o2);
														}
													});
													for (String s: list)
														hash += s + "|";
												}
											}
*/												
											return hash;
										}
										
									};
									
									CommonUtils.ItemUpdater<Constraint> constraintUpdater = new CommonUtils.ItemUpdater<Constraint>() {

										@Override
										public void updateItem(Constraint originConstraint, Constraint updatedConstraint) {
											originConstraint.getParameter().clear();
											originConstraint.getParameter().addAll(updatedConstraint.getParameter());
										}
									};
									
									CommonUtils.<Constraint>updateAndApply(originConstraints.getConstraint(), 
											updatedConstraints.getConstraint(), 
											constraintComparator, 
											constraintUpdater);
								}
								
							}
						};
						
						CommonUtils.updateAndApply(originProperties.getProperty(), 
								updatedProperties.getProperty(), 
								propertyComparator, 
								propertyUpdater);
					}
					
				}
			}
			
		};
	}
	
}
