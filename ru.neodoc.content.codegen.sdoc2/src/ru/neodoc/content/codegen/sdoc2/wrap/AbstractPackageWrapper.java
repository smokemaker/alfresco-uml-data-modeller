package ru.neodoc.content.codegen.sdoc2.wrap;

import org.eclipse.uml2.uml.Package;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotypeClassified;

public class AbstractPackageWrapper<T extends ProfileStereotypeClassified<Package>> extends AbstractClassifiedWrapper<Package, T> {

	protected AbstractPackageWrapper(T wrappedElement) {
		super(wrappedElement);
	}
	
}
