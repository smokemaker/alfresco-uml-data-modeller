package ru.neodoc.content.codegen.sdoc2.wrap;

import org.eclipse.uml2.uml.Class;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotypeClassified;

public abstract class AbstractClassWrapper<T extends ProfileStereotypeClassified<Class>> extends AbstractClassifiedWrapper<Class, T> {

	protected AbstractClassWrapper(T wrappedElement) {
		super(wrappedElement);
		// TODO Auto-generated constructor stub
	}

}
