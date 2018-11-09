package ru.neodoc.content.modeller.xml2uml.helper.entity;

import java.util.Collections;
import java.util.List;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.Model.DataTypes.DataType;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;

import ru.neodoc.content.modeller.xml2uml.helper.AbstractModelAwareSubHelper;
import ru.neodoc.content.modeller.xml2uml.structure.ComplexRegistry;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPrimitiveType;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;
import ru.neodoc.content.utils.uml.search.UMLSearchUtils;

public class DataTypeHelper extends AbstractModelAwareSubHelper<DataType, PrimitiveType> {

	static {
		HELPER_REGISTRY.register(DataTypeHelper.class, ModelHelper.class, DataType.class, PrimitiveType.class);
	}
	
	@Override
	protected ModelObject<DataType> createNewModelObject(DataType object) {
		return complexRegistry().getObjectSmartFactory().getObject(object.getName());
	}
	
	@Override
	protected void doCustomFillModelObject(ModelObject<DataType> modelObject,
			DataType object) {
		super.doCustomFillModelObject(modelObject, object);
		PrefixedName pn = new PrefixedName(object.getName());
		modelObject.name = pn.getName();
		modelObject.pack = pn.getPrefix();
	}
	
	@Override
	protected String getSearchName(ModelObject<DataType> modelObject,
			DataType object) {
		return object.getName();
	}
	
	@Override
	public PrimitiveType resolveElement(Package root, String name) {
		return UMLSearchUtils.primitiveTypeByName(root, name);
	}
	
	@Override
	protected List<DataType> getElementsFromContainer(Model container) {
		return container.getDataTypes()==null
				?Collections.emptyList()
				:container.getDataTypes().getDataType()==null
					?Collections.emptyList()
					:container.getDataTypes().getDataType();
	}

	@Override
	protected PrimitiveType createElement(ModelObject<DataType> object) {
		ModelObject<?> moPackage = complexRegistry().getObjectRegistry().get(object.pack);
		if (moPackage==null)
			return null;
		if (moPackage.getElement()==null)
			return null;
		
		PrimitiveType newType = ((Package)moPackage.getElement()).createOwnedPrimitiveType(object.name);
		return newType;
	}
	
	@Override
	protected boolean processStereotypedElement(StereotypedElement stereotypedElement, ModelObject<DataType> object) {
		super.processStereotypedElement(stereotypedElement, object);
		AlfrescoProfile.ForPrimitiveType.DataType theDataType = stereotypedElement.getOrCreate(ForPrimitiveType.DataType.class);
		DataType dt = (DataType) object.source;

		theDataType.setTitle(dt.getTitle());
		theDataType.setDescription(dt.getDescription());
		
		
		Object obj = dt.getAnalyserClass();
		String analyzerClass=null;
		if (obj!=null)
			if (obj instanceof org.w3c.dom.Element)
				analyzerClass = ((org.w3c.dom.Element)obj).getTextContent();
			else
				analyzerClass = obj.toString();
		theDataType.setAnalyzerClass(analyzerClass);
		
		obj = dt.getJavaClass();
		String javaClass=null;
		if (obj!=null)
			if (obj instanceof org.w3c.dom.Element)
				javaClass = ((org.w3c.dom.Element)obj).getTextContent();
			else
				javaClass = obj.toString();
		
		theDataType.setJavaClass(javaClass);
		
		return true;
	}
}
