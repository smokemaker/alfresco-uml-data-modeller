package ru.neodoc.content.profile.meta.alfresco.forconstraint;

import java.util.Collections;
import java.util.List;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.profile.annotation.AImplements;
import ru.neodoc.content.utils.uml.profile.meta.CompositeMetaObject;

@AImplements(AlfrescoProfile.ForConstraint.ConstraintList.class)
public class ConstraintList extends ConstraintMainAbstract
		implements AlfrescoProfile.ForConstraint.ConstraintList {

	public ConstraintList(CompositeMetaObject composite) {
		super(composite);
	}

	@Override
	public boolean getCaseSensitive() {
		return getBoolean(AlfrescoProfile.ForConstraint.ConstraintList.PROPERTIES.CASE_SENSITIVE);
	}

	@Override
	public void setCaseSensitive(boolean caseSensitive) {
		setAttribute(AlfrescoProfile.ForConstraint.ConstraintList.PROPERTIES.CASE_SENSITIVE, caseSensitive);
	}

	@Override
	public List<String> getAllowedValues() {
		return getAttribute(
				AlfrescoProfile.ForConstraint.ConstraintList.PROPERTIES.ALLOWED_VALUES, 
				Collections.emptyList());
	}

	@Override
	public void setAllowedValues(List<String> allowedValues) {
		setAttribute(AlfrescoProfile.ForConstraint.ConstraintList.PROPERTIES.ALLOWED_VALUES, allowedValues);
	}

	@Override
	public boolean getSorted() {
		return getBoolean(AlfrescoProfile.ForConstraint.ConstraintList.PROPERTIES.SORTED);
	}

	@Override
	public void setSorted(boolean sorted) {
		setAttribute(AlfrescoProfile.ForConstraint.ConstraintList.PROPERTIES.SORTED, sorted);
	}

}
