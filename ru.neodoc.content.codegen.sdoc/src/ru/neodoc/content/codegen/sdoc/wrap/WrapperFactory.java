package ru.neodoc.content.codegen.sdoc.wrap;

import ru.neodoc.content.modeller.utils.uml.elements.Aspect;
import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;
import ru.neodoc.content.modeller.utils.uml.elements.ChildAssociation;
import ru.neodoc.content.modeller.utils.uml.elements.DataTypeElement;
import ru.neodoc.content.modeller.utils.uml.elements.MandatoryAspect;
import ru.neodoc.content.modeller.utils.uml.elements.Namespace;
import ru.neodoc.content.modeller.utils.uml.elements.PeerAssociation;
import ru.neodoc.content.modeller.utils.uml.elements.Property;
import ru.neodoc.content.modeller.utils.uml.elements.Type;

public class WrapperFactory {

	public static <T extends BaseWrapper, S extends BaseElement> T get(S element){
		T result = WrapperRegistry.get(element); 
		if (result == null){
			result = createNew(element);
			if (result != null)
				WrapperRegistry.put(result);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends BaseWrapper, S extends BaseElement> T createNew(S element){

		T result = null;
		
		if (check(DataTypeElement.class, element))
			result = (T)DataTypeWrapper.newInstance((DataTypeElement)element);
		
		if (check(result, Property.class, element))
			result = (T)PropertyWrapper.newInstance((Property)element);
		
		if (check(result, MandatoryAspect.class, element))
			result = (T)MandatoryAspectWrapper.newInstance((MandatoryAspect)element);
		
		if (check(result, ChildAssociation.class, element))
			result = (T)ChildAssociationWrapper.newInstance((ChildAssociation)element);
		
		if (check(result, PeerAssociation.class, element))
			result = (T)PeerAssociationWrapper.newInstance((PeerAssociation)element);
		
		if (check(result, Aspect.class, element))
			result = (T)AspectWrapper.newInstance((Aspect)element);
		
		if (check(result, Type.class, element)){
			result = (T)TypeWrapper.newInstance((Type)element);
			namespaceOwner((ClassWrapper)result);
		}
		
		if (check(Namespace.class, element)){
			result = (T)NamespaceWrapper.newInstance((Namespace)element);
		}
		
		return result;
	}
	
	protected static <S extends BaseElement> boolean check(Object current, Class<? extends BaseElement> clazz, S element) {
		return (current == null) && check(clazz, element);
	}
	
	protected static <S extends BaseElement> boolean check(Class<? extends BaseElement> clazz, S element){
		return clazz.isAssignableFrom(element.getClass());
	}
	
	protected static void namespaceOwner(ClassWrapper cw) {
		Namespace ns = cw.getClassElement().getNamespace();
		NamespaceWrapper nsw = get(ns);
		// nsw.addChild(cw);
		cw.setOwner(nsw);
	}
}
