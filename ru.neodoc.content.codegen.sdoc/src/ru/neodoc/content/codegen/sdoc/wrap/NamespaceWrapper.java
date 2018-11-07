package ru.neodoc.content.codegen.sdoc.wrap;

import java.util.List;

import ru.neodoc.content.modeller.utils.uml.elements.Aspect;
import ru.neodoc.content.modeller.utils.uml.elements.Namespace;
import ru.neodoc.content.modeller.utils.uml.elements.Type;

public class NamespaceWrapper extends NamedElementWrapper {

	protected Namespace namespace;
	
	public static NamespaceWrapper newInstance(Namespace wrappedElement) {
		return new NamespaceWrapper(wrappedElement);
	}
	
	private NamespaceWrapper(Namespace wrappedElement) {
		super(wrappedElement);
		this.namespace = wrappedElement;
	}

	@Override
	protected String getPrefix() {
		return "";
	}

	@Override
	protected void setInitialTargetJavaName() {
		String prefix = ((Namespace)wrappedElement).getPrefix();
		prefix = prefix.substring(0, 1).toUpperCase() + 
				prefix.substring(1);
		setTargetJavaName(prefix + "Model");
	}
	
	@Override
	protected void setInitialTargetJavaScriptName() {
		String prefix = ((Namespace)wrappedElement).getPrefix();
		prefix = prefix.substring(0, 1).toUpperCase() + 
				prefix.substring(1);
		setTargetJavaScriptName(prefix + "Model");
	}
	
	public void fill(){
		List<Type> types = namespace.getTypes();
		for (Type t: types){
//			TypeWrapper tw = new TypeWrapper(t);
			TypeWrapper tw = WrapperFactory.get(t);
			if (hasChild(tw))
				continue;
			
			tw.setOwner(this);
			tw.fill();
			addChild(tw);
		}
		
		List<Aspect> aspects = namespace.getAspects();
		for (Aspect a: aspects){
//			AspectWrapper aw = new AspectWrapper(a);
			AspectWrapper aw = WrapperFactory.get(a);
			if (hasChild(aw))
				continue;
				
			aw.setOwner(this);
			aw.fill();
			addChild(aw);
		}
	}
	
	public Namespace getNamespace(){
		return this.namespace;
	}
	
	@Override
	public String getKey() {
		if (this.namespace != null) {
			return getKey(this.namespace);
		}
		return super.getKey();
	}
	
	public static String getKey(Namespace namespace){
		if (namespace == null)
			return null;
		
		return namespace.getPrefix() + "$" + namespace.getUri();
	}
}
