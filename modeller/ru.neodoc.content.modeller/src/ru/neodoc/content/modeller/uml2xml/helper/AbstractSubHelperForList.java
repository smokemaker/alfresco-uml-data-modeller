package ru.neodoc.content.modeller.uml2xml.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotypeClassified;

public abstract class AbstractSubHelperForList<
			ContainerElementType extends Element, 
			ElementType extends Element,
			JAXBContainerType,
			JAXBObjectType, 
			StereotypeClass extends ProfileStereotypeClassified<ElementType>> 
		extends AbstractSubHelper<ContainerElementType, ElementType, JAXBContainerType, JAXBObjectType, StereotypeClass> {

	@Override
	public List<ElementType> getSubElements(ContainerElementType container) {
		return null;
	}
	
	@Override
	protected void doUpdateFromContainer(JAXBContainerType containerToUpdate, List<JAXBObjectType> objectsToUpdate, List<JAXBObjectType> newObjects) {
/*		CommonUtils.<JAXBObjectType>updateAndApply(
				objectsToUpdate, 
				newObjects, 
				getComparator(), 
				getItemUpdater());
*/	}
	
	public abstract CommonUtils.ListComparator<JAXBObjectType> getComparator();
	
	public CommonUtils.ItemUpdater<JAXBObjectType> getItemUpdater(){
/*		return new CommonUtils.ItemUpdater<JAXBObjectType>() {

			@Override
			public void updateItem(JAXBObjectType origin, JAXBObjectType updated) {
				AbstractSubHelperForList.this.updateProperties(origin, updated);
				for (Class<? extends AbstractHelper<?, ?, ?>> helperClass: HELPER_REGISTRY.getContainedSubhelpers((Class<? extends AbstractHelper<?, ?, ?>>) AbstractSubHelperForList.this.getClass())) {
					try {
						AbstractHelper<?, ?, ?> abstractHelper = helperClass.newInstance();
						AbstractSubHelper<ElementType, ? , JAXBObjectType, ?, ?> abstractSubHelper
								= (AbstractSubHelper<ElementType, ? , JAXBObjectType, ?, ?>)abstractHelper;
						if (abstractHelper!=null) {
							CommonUtils.<?>updateAndApply(
									abstractSubHelper.getObjects(origin), 
									abstractSubHelper.getObjects(updated), 
									abstractSubHelper.getComparator(), 
									getItemUpdater());
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		};*/
		return null;
	}
	
/*	public static class ListContainerUpdater<JAXBObjectType> implements CommonUtils.ItemUpdater<JAXBObjectType>{

		protected final List<Class<? extends AbstractHelper<?, ?, ?>>> subHelpers = new ArrayList<>(); 
		
		public ListContainerUpdater(List<Class<? extends AbstractHelper<?, ?, ?>>> subHelpers) {
			super();
			this.subHelpers.addAll(subHelpers);
		}
		
		@Override
		public void updateItem(JAXBObjectType origin, JAXBObjectType updated) {
			
		}
		
	}*/
	
}
