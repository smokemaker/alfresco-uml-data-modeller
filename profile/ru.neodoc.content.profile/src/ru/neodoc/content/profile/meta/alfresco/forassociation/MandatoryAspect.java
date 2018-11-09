package ru.neodoc.content.profile.meta.alfresco.forassociation;

import java.util.List;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Aspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterMemberEndByAggregation;

@AImplements(ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect.class)
public class MandatoryAspect extends AssociationMainAbstract
		implements ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForAssociation.MandatoryAspect {

	public MandatoryAspect(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public ClassMain getAspected() {
		List<? extends Property> list = AlfrescoSearchHelperFactory
				.getMemberEndSearcher()
				.startWith(getElementClassified())
				.filter((new UMLSearchFilterMemberEndByAggregation()).value(AggregationKind.NONE_LITERAL))
				.search();
		if (list==null || (list.size()!=1))
			return getSource();
		Type t = list.get(0).getType();
		if (t instanceof Class) {
			return AbstractProfile.asUntyped((Class)t).get(ClassMain.class);
		}
		return null;
	}

	@Override
	public Aspect getAspect() {
		List<? extends Property> list = AlfrescoSearchHelperFactory
				.getMemberEndSearcher()
				.startWith(getElementClassified())
				.filter((new UMLSearchFilterMemberEndByAggregation()).value(AggregationKind.SHARED_LITERAL))
				.search();
		StereotypedElement ps; 
		if (list==null || list.size()!=1)
			ps = getTarget();
		else {
			Type t = list.get(0).getType();
			if (t instanceof Class) {
				ps = AbstractProfile.asUntyped((Class)t);
			} else {
				ps = getTarget();
			}
		}
			
		return ps==null?null:ps.get(Aspect.class);
	}

}
