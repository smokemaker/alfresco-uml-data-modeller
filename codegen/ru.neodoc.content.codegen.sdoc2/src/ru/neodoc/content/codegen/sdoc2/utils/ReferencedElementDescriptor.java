package ru.neodoc.content.codegen.sdoc2.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ru.neodoc.content.utils.CommonUtils;

public class ReferencedElementDescriptor {

	public static Set<ReferencedElementDescriptor> getSet(Collection<String> data){
		Set<ReferencedElementDescriptor> result = new HashSet<>();
		for (String s: data)
			result.add(new ReferencedElementDescriptor(s));
		return result;
	}
	
	protected boolean isIgnored = false;
	protected String elementName = "";
	protected boolean isValid = true;

	public ReferencedElementDescriptor(String elementName, boolean isIgnored) {
		super();
		this.isIgnored = isIgnored;
		this.elementName = elementName;
	}
	
	public ReferencedElementDescriptor(String stringRepresentation) {
		super();
		if (!CommonUtils.isValueable(stringRepresentation))
			isValid = false;
		else {
			isIgnored = stringRepresentation.startsWith("#");
			if (!isIgnored)
				this.elementName = stringRepresentation;
			else
				this.elementName = stringRepresentation.substring(1);
		}
			
	}
	
	public boolean isIgnored() {
		return isIgnored;
	}

	public String getElementName() {
		return elementName;
	}

	public boolean isValid() {
		return isValid;
	}

}
