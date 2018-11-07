package ru.neodoc.content.profile.meta.alfresco.fornamedelement;

import java.util.List;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.DirectedRelationship;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Relationship;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.Constrainted;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForDependency.ConstraintedInline;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchHelperFactory;
import ru.neodoc.content.profile.alfresco.search.helper.AlfrescoSearchUtils;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;
import ru.neodoc.content.utils.uml.profile.meta.ImplementationMetaObjectClassified;
import ru.neodoc.content.utils.uml.search.filter.UMLSearchFilterImpl;

@AImplements(AlfrescoProfile.ForNamedElement.ConstraintedObject.class)
public class ConstraintedObject<T extends NamedElement> extends ImplementationMetaObjectClassified<T>
		implements AlfrescoProfile.ForNamedElement.ConstraintedObject<T>{

	public ConstraintedObject(CompositeMetaObject composite) {
		super(composite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Constrainted> getAllConstraints() {
		List<Constrainted> result = AlfrescoSearchHelperFactory.getAllConstraintedSearcher(this)
			.search()
			.convert(AlfrescoSearchUtils.DependencyToConstraintedConverter.INSTANCE);
		return result;
	}

	@Override
	public List<Constrainted> getConstraintRefs() {
		List<Constrainted> result = AlfrescoSearchHelperFactory.getConstraintedSearcher(this)
				.search()
				.convert(AlfrescoSearchUtils.DependencyToConstraintedConverter.INSTANCE);
		return result;
	}

	@Override
	public List<ConstraintedInline> getInlineConstraints() {
		List<ConstraintedInline> result = AlfrescoSearchHelperFactory.getInlineConstraintedSearcher(this)
				.search()
				.convert(AlfrescoSearchUtils.DependencyToConstraintedInlineConverter.INSTANCE);
		return result;
	}

	@Override
	public Constrainted addConstraintRef(ConstraintMain constraint) {
		final Constraint constraintToAdd = constraint.getElementClassified();
		List<Constrainted> list = AlfrescoSearchHelperFactory.getConstraintedSearcher(this)
			.filter(new UMLSearchFilterImpl<Dependency, NamedElement, Dependency>() {

				@Override
				public boolean matches(Dependency element, NamedElement container) {
					return element.getSuppliers().contains(constraintToAdd);
				}
				
			})
			.search()
			.convert(AlfrescoSearchUtils.DependencyToConstraintedConverter.INSTANCE);
		Constrainted result = null;
		if (list.isEmpty()) {
			Dependency dependency = getElementClassified().createDependency(constraint.getElementClassified());
			result = Constrainted._HELPER.getFor(dependency);
		} else {
			result = list.get(0);
		}
		return result;
	}

	@Override
	public ConstraintedInline addInlineConstraint(ConstraintMain constraint) {
		final Constraint constraintToAdd = constraint.getElementClassified();
		List<ConstraintedInline> list = AlfrescoSearchHelperFactory.getInlineConstraintedSearcher(this)
			.filter(new UMLSearchFilterImpl<Dependency, NamedElement, Dependency>() {

				@Override
				public boolean matches(Dependency element, NamedElement container) {
					return element.getSuppliers().contains(constraintToAdd);
				}
				
			})
			.search()
			.convert(AlfrescoSearchUtils.DependencyToConstraintedInlineConverter.INSTANCE);
		ConstraintedInline result = null;
		if (list.isEmpty()) {
			Dependency dependency = getElementClassified().createDependency(constraint.getElementClassified());
			result = ConstraintedInline._HELPER.getFor(dependency);
		} else {
			result = list.get(0);
		}
		return result;
	}
	
	@Override
	public Namespace getConstraintContext() {
		Element currentElement = getElement();
		while ((currentElement!=null) && !(currentElement instanceof Namespace)) {
			if (currentElement instanceof DirectedRelationship) {
				currentElement = ((DirectedRelationship)currentElement).getSources().get(0);
				continue;
			}
			if (currentElement instanceof Relationship) {
				currentElement = ((Relationship)currentElement).getRelatedElements().get(0);
				continue;
			}
			currentElement = currentElement.getOwner();
		}
		if (currentElement instanceof Namespace)
				return (Namespace)currentElement;
		return null;
	}
}
