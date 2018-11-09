package ru.neodoc.content.modeller.uml2xml.helper.model;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.model.dictionary._1.Model.DataTypes;
import org.alfresco.model.dictionary._1.Model.DataTypes.DataType;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;

import ru.neodoc.content.modeller.uml2xml.helper.AbstractSubHelper;
import ru.neodoc.content.modeller.uml2xml.helper.ObjectContainer;
import ru.neodoc.content.modeller.utils.JaxbUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.CommonUtils.ListComparator;

public class DataTypeHelper extends
		AbstractSubHelper<Package, PrimitiveType, DataTypes, DataType, ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPrimitiveType.DataType> {

	static {
		HELPER_REGISTRY.register(DataTypeHelper.class, DataTypesHelper.class).asContained();
	}
	
	@Override
	public List<PrimitiveType> getSubElements(Package container) {
		AlfrescoProfile.ForPackage.Model umlModel = AlfrescoProfile.ForPackage.Model._HELPER.findNearestFor(container);
		List<PrimitiveType> result = new ArrayList<>();
		if (umlModel!=null) {
			for (Namespace ns: umlModel.getAllNamespaces()) {
				for (AlfrescoProfile.ForPrimitiveType.DataType dt: ns.getAllDataTypes()) {
					if (!result.contains(dt.getElementClassified()))
						result.add(dt.getElementClassified());
				}
			}
		}
		return result;
	}

	@Override
	protected List<ObjectContainer<DataType>> addObjectsToContainer(ObjectContainer<DataTypes> container,
			List<ObjectContainer<DataType>> objectsToAdd) {
		container.getObject().getDataType().addAll(ObjectContainer.FACTORY.extractTyped(objectsToAdd));
		return ObjectContainer.FACTORY.createListTyped(container.getObject().getDataType());
	}

	@Override
	public ListComparator<DataType> getComparator() {
		return new CommonUtils.BaseListComparator<DataType>() {

			@Override
			public boolean equals(DataType item1, DataType item2) {
				// always update
				return false;
			}
			
			@Override
			public String itemHash(DataType item) {
				if (item==null)
					return "";
				return item.getName();
			}
			
		};
	}

	@Override
	protected List<DataType> getOrCreateObjects(DataTypes container) {
		return getObjects(container);
	}

	@Override
	public List<DataType> getObjects(DataTypes container) {
		return container.getDataType();
	}

	@Override
	protected Class<AlfrescoProfile.ForPrimitiveType.DataType> getStereotypeClass() {
		return AlfrescoProfile.ForPrimitiveType.DataType.class;
	}

	@Override
	protected DataType doCreateObject(PrimitiveType element) {
		return JaxbUtils.ALFRESCO_OBJECT_FACTORY.createModelDataTypesDataType();
	}

	@Override
	protected DataType doFillObjectProperties(DataType object, PrimitiveType element,
			AlfrescoProfile.ForPrimitiveType.DataType stereotyped) {
		
		object.setName(stereotyped.getPrfixedName());
		object.setAnalyserClass(stereotyped.getAnalyzerClass());
		object.setJavaClass(stereotyped.getJavaClass());
		object.setTitle(stereotyped.getTitle());
		object.setDescription(stereotyped.getDescription());
		return object;
	}

	@Override
	protected void updateProperties(DataType objectToUpdate, ObjectContainer<DataType> newObjectContainer) {
		objectToUpdate.setName(newObjectContainer.getObject().getName());
		objectToUpdate.setTitle(newObjectContainer.getObject().getTitle());
		objectToUpdate.setDescription(newObjectContainer.getObject().getDescription());
		objectToUpdate.setAnalyserClass(newObjectContainer.getObject().getAnalyserClass());
		objectToUpdate.setJavaClass(newObjectContainer.getObject().getJavaClass());
	}

}
