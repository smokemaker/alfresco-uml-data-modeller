package ru.neodoc.content.codegen.sdoc2.extension.javascript;

import java.util.HashMap;
import java.util.Map;

import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapperExtension;
import ru.neodoc.content.codegen.sdoc2.wrap.AspectWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.ChildAssociationWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.DataTypeWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.PeerAssociationWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.PropertyWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.TypeWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.convert.CommonSdocNameConverter;
import ru.neodoc.content.codegen.sdoc2.wrap.convert.NameConverter;
import ru.neodoc.content.codegen.sdoc2.wrap.convert.NamespaceSdocNameConverter;
import ru.neodoc.content.utils.CommonUtils;

public class WrapperJSExtension extends AbstractWrapperExtension {

	protected static Map<Class<? extends AbstractWrapper>, NameConverter> converters = new HashMap<>();
	static {
		converters.put(NamespaceWrapper.class, new NamespaceSdocNameConverter());
		converters.put(AspectWrapper.class, new CommonSdocNameConverter("ASPECT_"));
		converters.put(TypeWrapper.class, new CommonSdocNameConverter("TYPE_"));
		converters.put(DataTypeWrapper.class, new CommonSdocNameConverter(""));
		converters.put(PropertyWrapper.class, new CommonSdocNameConverter("PROP_"));
		converters.put(PeerAssociationWrapper.class, new CommonSdocNameConverter("ASSOC_"));
		converters.put(ChildAssociationWrapper.class, new CommonSdocNameConverter("ASSOC_"));
	}
	
	public static WrapperJSExtension get(AbstractWrapper aw) {
		WrapperJSExtension result = (WrapperJSExtension)aw.getExtension(JavaScriptCodegenExtension.EXTENSION_ID);
		if (result == null) {
			result = new WrapperJSExtension(aw);
			aw.extend(result);
		}
		return result;
	}

	protected String targetJSLocation;
	protected String targetJSName;
	
	
	public WrapperJSExtension(AbstractWrapper extendedWrapper) {
		super(extendedWrapper);
		this.id = JavaScriptCodegenExtension.EXTENSION_ID;
		setTargetJSName(getName());
	}

	public String getFullTargetJSName(){
		String result = getTargetJSName();
		if ((result==null) || (result.length()==0))
			if (getOwnerExtension()!=null)
				return ((WrapperJSExtension)getOwnerExtension()).getFullTargetJSName();
			else
				return "";
		if (getOwnerExtension() != null){
			String parentResult = ((WrapperJSExtension)getOwnerExtension()).getFullTargetJSName();
			if ((parentResult!=null) && (parentResult.length()>0))
				return parentResult + "." + result;
		}
		return result;
	}

	@Override
	public boolean isFull() {
		return CommonUtils.isValueable(targetJSLocation) && CommonUtils.isValueable(targetJSName);
	}
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return isFull();
	}
	
	/*
	 * setters & gettres 
	 */
	
	public String getTargetJSLocation() {
		return targetJSLocation;
	}


	public void setTargetJSLocation(String targetJSLocation) {
		this.targetJSLocation = targetJSLocation;
	}


	public String getTargetJSName() {
		return targetJSName;
	}


	public void setTargetJSName(String targetJSName) {
		this.targetJSName = targetJSName;
	}

	@Override
	public String getName() {
		NameConverter nc = null;
		for (Class<? extends AbstractWrapper> clazz: converters.keySet())
			if (clazz.isAssignableFrom(extendedWrapper.getClass())) {
				nc = converters.get(clazz);
				break;
			}
		return nc==null?super.getName():nc.convert(super.getName());
	}
}
