package ru.neodoc.content.profile.meta.alfresco.formodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchUtils;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;

@AImplements(AlfrescoProfile.ForModel.Alfresco.class)
public class Alfresco extends ImplementationMetaObjectClassified<org.eclipse.uml2.uml.Model>
		implements AlfrescoProfile.ForModel.Alfresco {

	public Alfresco(CompositeMetaObject composite) {
		super(composite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Namespace> getRequiredNamespaces() {
		Set<Namespace> result = new HashSet<>();
		
		List<Model> models = AlfrescoSearchHelperFactory.getModelSearcher()
										.startWith(getElementClassified())
										.search()
										.convert(AlfrescoSearchUtils.PackageToModelConverter.INSTANCE);
		
		for (Model m: models)
			result.addAll(m.getRequiredNamespaces());
		
		return new ArrayList<>(result);
	}

}
