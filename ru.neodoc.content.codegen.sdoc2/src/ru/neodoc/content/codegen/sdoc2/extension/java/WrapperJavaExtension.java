package ru.neodoc.content.codegen.sdoc2.extension.java;

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

public class WrapperJavaExtension extends AbstractWrapperExtension {

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
	
	public static WrapperJavaExtension get(AbstractWrapper aw) {
		WrapperJavaExtension result = (WrapperJavaExtension)aw.getExtension(JavaCodegenExtension.EXTENSION_ID);
		if (result == null) {
			result = new WrapperJavaExtension(aw);
			aw.extend(result);
		}
		return result;
	}
	
	protected String targetJavaLocation;
	protected String targetJavaPackage;
	protected String targetJavaName;
	
	protected Boolean ignored = null;
	
	public WrapperJavaExtension(AbstractWrapper extendedWrapper) {
		super(extendedWrapper);
		this.id = JavaCodegenExtension.EXTENSION_ID;
		setTargetJavaName(getName());
	}

	public boolean isIgnored() {
		if (this.ignored!=null)
			return this.ignored;
		if (this.getOwnerExtension()!=null)
			return ((WrapperJavaExtension)getOwnerExtension()).isIgnored();
		return false;
	}

	public void setIgnored(Boolean ignored) {
		this.ignored = ignored;
	}

	public String getTargetJavaLocation() {
		return this.targetJavaLocation;
	}

	public void setTargetJavaLocation(String targetJavaLocation) {
		this.targetJavaLocation = targetJavaLocation;
	}

	public String getTargetJavaPackage() {
		return targetJavaPackage;
	}

	public void setTargetJavaPackage(String targetJavaPackage) {
		this.targetJavaPackage = targetJavaPackage;
	}

	public String getTargetJavaName() {
		return targetJavaName;
	}

	public void setTargetJavaName(String targetJavaName) {
		this.targetJavaName = targetJavaName;
	}
	
	public String getFullTargetJavaName(){
		String result = getTargetJavaName();
		if ((result==null) || (result.length()==0))
			if (getOwnerExtension()!=null)
				return ((WrapperJavaExtension)getOwnerExtension()).getFullTargetJavaName();
			else
				return "";
		if (getOwnerExtension() != null){
			String parentResult = ((WrapperJavaExtension)getOwnerExtension()).getFullTargetJavaName();
			if ((parentResult!=null) && (parentResult.length()>0))
				return parentResult + "." + result;
		}
		return result;
	}
	
	public String getFinalJavaPackage(){
		String result = getTargetJavaPackage();
		if (result==null)
			result = "";
		String parentResult = "";
		if (getOwnerExtension()!=null)
			parentResult = ((WrapperJavaExtension)getOwnerExtension()).getFinalJavaPackage();
		if (result.length()>0)
			if (parentResult.length()>0)
				return parentResult + "." + result;
			else
				return result;
		else
			return parentResult;
	}

	@Override
	public boolean isFull() {
		return CommonUtils.isValueable(targetJavaLocation)
				&& CommonUtils.isValueable(targetJavaPackage)
				&& CommonUtils.isValueable(targetJavaName);
	}
	
	@Override
	public boolean isValid() {
		return super.isFull();
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
