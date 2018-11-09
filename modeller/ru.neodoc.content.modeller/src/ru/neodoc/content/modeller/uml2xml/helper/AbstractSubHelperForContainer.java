package ru.neodoc.content.modeller.uml2xml.helper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Element;

import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ListComparator;
import ru.neodoc.content.utils.uml.profile.stereotype.DefaultElementStereotype;

public abstract class AbstractSubHelperForContainer<
				ContainerElementType extends Element,
				JAXBContainerType,
				JAXBObjectType> 
		extends AbstractSubHelper<ContainerElementType, ContainerElementType, JAXBContainerType, JAXBObjectType, DefaultElementStereotype> {

	@Override
	protected JAXBObjectType doFillObjectProperties(JAXBObjectType object, ContainerElementType element, DefaultElementStereotype stereotyped) {
		return object;
	}

	@Override
	protected void updateProperties(JAXBObjectType objectToUpdate, ObjectContainer<JAXBObjectType> newObjectContainer) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<ContainerElementType> getSubElements(ContainerElementType container) {
		List<ContainerElementType> result = new ArrayList<>();
		result.add(container);
		return result;
	}

	@Override
	public ListComparator<JAXBObjectType> getComparator() {
		
		return new CommonUtils.BaseListComparator<JAXBObjectType>() {
			
			@Override
			public boolean equals(JAXBObjectType item1, JAXBObjectType item2) {
				return false;
			}
			
			@Override
			public String itemHash(JAXBObjectType item) {
				if (item == null)
					return "";
				// always update
				return item.getClass().getName();
			}
			
		};
	}

	@Override
	protected Class<DefaultElementStereotype> getStereotypeClass() {
		return DefaultElementStereotype.class;
	}


}
