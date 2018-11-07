package ru.neodoc.content.codegen.sdoc.wrap;

import ru.neodoc.content.modeller.utils.uml.elements.MandatoryAspect;

public class MandatoryAspectWrapper extends AssociationWrapper {

	protected MandatoryAspect mandatoryAspect;
	
	public static MandatoryAspectWrapper newInstance(MandatoryAspect wrappedElement) {
		return new MandatoryAspectWrapper(wrappedElement);
	}
	
	private MandatoryAspectWrapper(MandatoryAspect wrappedElement) {
		super(wrappedElement);
		this.mandatoryAspect = wrappedElement;
	}

	@Override
	public String getTargetJavaName() {
		return "";
	}
	
}
