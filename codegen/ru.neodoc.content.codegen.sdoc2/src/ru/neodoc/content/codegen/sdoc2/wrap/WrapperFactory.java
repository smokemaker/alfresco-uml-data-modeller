package ru.neodoc.content.codegen.sdoc2.wrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.profile.stereotype.ProfileStereotype;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class WrapperFactory {

	private static Map<Class<? extends ProfileStereotype>, Class<? extends AbstractWrapper>> dictionary = new HashMap<>();
	
	static {
		dictionary.put(AlfrescoProfile.ForPackage.Namespace.class, NamespaceWrapper.class);
		dictionary.put(AlfrescoProfile.ForClass.Aspect.class, AspectWrapper.class);
		dictionary.put(AlfrescoProfile.ForClass.Type.class, TypeWrapper.class);
		dictionary.put(AlfrescoProfile.ForPrimitiveType.DataType.class, DataTypeWrapper.class);
		dictionary.put(AlfrescoProfile.ForAssociation.Association.class, PeerAssociationWrapper.class);
		dictionary.put(AlfrescoProfile.ForAssociation.ChildAssociation.class, ChildAssociationWrapper.class);
		dictionary.put(AlfrescoProfile.ForAssociation.MandatoryAspect.class, MandatoryAspectWrapper.class);
		dictionary.put(AlfrescoProfile.ForProperty.Property.class, PropertyWrapper.class);
	}
	
	public static <T extends AbstractWrapper, S extends StereotypedElement> T get(S element){
		T result = WrapperRegistry.get(element); 
		if (result == null){
			result = createNew(element);
			if (result != null)
				WrapperRegistry.put(result);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends AbstractWrapper, S extends StereotypedElement> T createNew(S element){

		T result = null;
		
		for (Class<? extends ProfileStereotype> stereotypeClass: dictionary.keySet())
			if (element.has(stereotypeClass)) {
				try {
					result = (T)dictionary.get(stereotypeClass).getConstructor(stereotypeClass).newInstance(element);
					break;
				} catch (Exception e) {
					// NOOP
				}
			}
		
		return result;
	}
	
	public static List<AbstractWrapper> get(List<? extends ProfileStereotype> elements){
		List<AbstractWrapper> result = new ArrayList<>();
		for (ProfileStereotype ps: elements) {
			AbstractWrapper aw = get(ps);
			if (aw!=null)
				result.add(aw);
		}
		return result;
	}
	
/*	protected static void namespaceOwner(ClassWrapper cw) {
		Namespace ns = cw.getClassElement().getNamespace();
		NamespaceWrapper nsw = get(ns);
		// nsw.addChild(cw);
		cw.setOwner(nsw);
	}
*/}
