package ru.neodoc.content.profile.meta.alfresco.forprimitivetype;

import org.eclipse.uml2.uml.PrimitiveType;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.DdTextualDescription;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Named;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.Namespaced;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPrimitiveType.DataType.class)
public class DataType extends ImplementationMetaObjectClassified<PrimitiveType>
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPrimitiveType.DataType {

	protected DdTextualDescription td;
	protected Namespaced nd;
	
	public DataType(CompositeMetaObject composite) {
		super(composite);
		td = createAndRegisterSubimplementor(DdTextualDescription.class);
		nd = createAndRegisterSubimplementor(Namespaced.class);
	}

	@Override
	public String getTitle() {
		return td.getTitle();
	}

	@Override
	public void setTitle(String title) {
		td.setTitle(title);
	}

	@Override
	public String getDescription() {
		return td.getDescription();
	}

	@Override
	public void setDescription(String description) {
		td.setDescription(description);
	}

	@Override
	public String getAnalyzerClass() {
		return (String)getAttribute(ANAYLYZER_CLASS);
	}

	@Override
	public String getJavaClass() {
		return (String)getAttribute(JAVA_CLASS);
	}

	@Override
	public void setAnalyzerClass(String value) {
		setAttribute(ANAYLYZER_CLASS, value);
	}

	@Override
	public void setJavaClass(String value) {
		setAttribute(JAVA_CLASS, value);
	}

	@Override
	public String getName() {
		return nd.getName();
	}

	@Override
	public void setName(String value) {
		nd.setName(value);
	}

	@Override
	public Namespace getNamespace() {
		return nd.getNamespace();
	}

	@Override
	public String getPrfixedName() {
		return nd.getPrfixedName();
	}

}
