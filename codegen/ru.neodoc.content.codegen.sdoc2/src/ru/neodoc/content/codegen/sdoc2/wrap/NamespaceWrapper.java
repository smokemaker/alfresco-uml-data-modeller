package ru.neodoc.content.codegen.sdoc2.wrap;

import java.util.List;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Aspect;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.Type;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;

public class NamespaceWrapper extends AbstractPackageWrapper<Namespace> {

	public NamespaceWrapper(Namespace wrappedElement) {
		super(wrappedElement);
	}

	public void fill() {
		List<Type> types = getClassifiedWrappedElement().getAllTypes();
		for (Type t: types) {
			TypeWrapper tw = WrapperFactory.get(t);
			if (!hasChild(tw)) {
				tw.setOwner(this);
				tw.fill();
				addChild(tw);
			}
		}
		
		List<Aspect> aspects = getClassifiedWrappedElement().getAllAspects();
		for (Aspect a: aspects) {
			AspectWrapper aw = WrapperFactory.get(a);
			if (!hasChild(aw)) {
				aw.setOwner(this);
				aw.fill();
				addChild(aw);
			}
		}
	}
	
}
