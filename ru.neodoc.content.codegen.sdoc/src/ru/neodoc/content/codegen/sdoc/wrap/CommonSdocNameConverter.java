package ru.neodoc.content.codegen.sdoc.wrap;

public class CommonSdocNameConverter implements NameConverter {

	protected String prefix;
	
	public CommonSdocNameConverter(String prefix){
		super();
		this.prefix = prefix;
	}
	
	@Override
	public String convert(String source) {
		if (source == null)
			return null;
		if (source.length()==0)
			return null;
		String result = source;
		String[] temp = result.split(":");
		result = temp[temp.length-1];
		
		result = result.replaceAll("([^^-])([A-Z])", "$1_$2").replaceAll("-", "_");
		
		return (prefix + result).toUpperCase();
	}

}
