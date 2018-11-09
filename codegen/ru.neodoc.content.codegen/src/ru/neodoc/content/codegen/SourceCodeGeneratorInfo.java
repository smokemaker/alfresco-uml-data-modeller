package ru.neodoc.content.codegen;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

import ru.neodoc.eclipse.extensionpoints.IRegisteredExtension;

public class SourceCodeGeneratorInfo implements IRegisteredExtension {
	
	protected String generatorID;
	protected String generatorName;
	protected String description;
	protected SourceCodeGenerator generator;
	
	protected boolean isValid;
	
	public SourceCodeGeneratorInfo(){
		
	}
	
	public SourceCodeGeneratorInfo(IExtension extension, IConfigurationElement element){
		this();
		try {
			Object obj = element.createExecutableExtension(CodegenPlugin.EP_GEN_PROP_COMPONENT);
			if (SourceCodeGenerator.class.isAssignableFrom(obj.getClass())){
				setGenerator((SourceCodeGenerator)obj);
			} else {
				throw new IllegalArgumentException();
			}
			
			String id = element.getAttribute(CodegenPlugin.EP_GEN_PROP_ID);
			if (!stringIsValid(id))
				id = this.generator.getGeneratorId();
			if (!stringIsValid(id))
				id = extension.getContributor().getName() + "." + this.generator.getClass().getSimpleName();
			if (!stringIsValid(id))
				throw new IllegalArgumentException();
			setGeneratorID(id);
			
			String name = element.getAttribute(CodegenPlugin.EP_GEN_PROP_NAME);
			if (!stringIsValid(name))
				name = getGeneratorID();
			setGeneratorName(name);
			
			String description = element.getAttribute(CodegenPlugin.EP_GEN_PROP_DESCRIPTION);
			if (!stringIsValid(description))
				description = "No description available";
			setDescription(description);
			
			isValid = true;
			
		} catch (Exception e){
			isValid = false;
		}
	}
	
	private boolean stringIsValid(String s){
		if (s == null)
			return false;
		if (s.trim().length()==0)
			return false;
		return true;
	}
	
	public boolean isValid(){
		return isValid;
	}
	
	public String getGeneratorID() {
		return generatorID;
	}
	public void setGeneratorID(String generatorID) {
		this.generatorID = generatorID;
	}
	public SourceCodeGenerator getGenerator() {
		return generator;
	}
	public void setGenerator(SourceCodeGenerator generator) {
		this.generator = generator;
	}
	public String getGeneratorName() {
		return generatorName;
	}
	public void setGeneratorName(String generatorName) {
		this.generatorName = generatorName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
