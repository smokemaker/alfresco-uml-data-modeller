package ru.neodoc.content.codegen.sdoc2.extension;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

import ru.neodoc.content.codegen.sdoc2.SdocCodegenPlugin.EP_CODEGEN_EXTENSION.PROPERTIES;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.eclipse.extensionpoints.IRegisteredExtension;

public class SdocCodegenExtensionInfo implements IRegisteredExtension {

	protected String id;
	
	protected SdocCodegenExtension codegenExtension;
	
	protected boolean useFirst = false;
	protected boolean useLast = false;
	protected Set<String> useBefore = new HashSet<>();
	protected Set<String> useAfter = new HashSet<>();
	
	public SdocCodegenExtensionInfo(IExtension extension, IConfigurationElement element) {
		try {
			
			Object obj = element.createExecutableExtension(PROPERTIES.COMPONENT);
			if (obj==null)
				throw new IllegalArgumentException();
			if (SdocCodegenExtension.class.isAssignableFrom(obj.getClass())) {
				codegenExtension = (SdocCodegenExtension)obj;
			} else {
				throw new IllegalArgumentException();
			}
			
			id = element.getAttribute(PROPERTIES.ID);
			if (!CommonUtils.isValueable(id))
				id = extension.getContributor().getName() + "." + codegenExtension.getClass().getSimpleName();
			
			useFirst = CommonUtils.isTrue(Boolean.valueOf(element.getAttribute(PROPERTIES.USE_FIRST)));
			useLast = CommonUtils.isTrue(Boolean.valueOf(element.getAttribute(PROPERTIES.USE_LAST)));
			
			String tmp = element.getAttribute(PROPERTIES.USE_BEFORE);
			if (tmp!=null) {
				String[] splitted = tmp.split(",");
				for (int i=0; i<splitted.length; i++)
					if (splitted[i]!=null && CommonUtils.isValueable(splitted[i].trim()))
						useBefore.add(splitted[i].trim());
			}
			tmp = element.getAttribute(PROPERTIES.USE_AFTER);
			if (tmp!=null) {
				String[] splitted = tmp.split(",");
				for (int i=0; i<splitted.length; i++)
					if (splitted[i]!=null && CommonUtils.isValueable(splitted[i].trim()))
						useAfter.add(splitted[i].trim());
			}				
			
		} catch (Exception e) {
			codegenExtension = null;
		}
	}

	// GETTERS & SETTERS
	
	public boolean isValid() {
		return codegenExtension!=null;
	}

	public boolean isUseFirst() {
		return useFirst;
	}

	public boolean isUseLast() {
		return useLast;
	}

	public Set<String> getUseBefore() {
		return useBefore;
	}

	public Set<String> getUseAfter() {
		return useAfter;
	}

	public String getId() {
		return id;
	}

	public SdocCodegenExtension getCodegenExtension() {
		return codegenExtension;
	}
	
}
