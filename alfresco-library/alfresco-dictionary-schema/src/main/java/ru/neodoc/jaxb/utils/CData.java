package ru.neodoc.jaxb.utils;

import java.util.regex.Pattern;

public class CData {

	public static final String PREFIX = "<![CDATA[";
	public static final String SUFFIX  = "]]>";
	
	public static final String DEFAULT_PATTERN = "[<>&]";
	
	public static CData create(String value) {
		return create(value, DEFAULT_PATTERN);
	}
	
	public static CData create(String value, String pattern) {
		return new CData(value, pattern);
	}
	
	public static boolean is(String value) {
		return (value!=null) && value.startsWith(PREFIX) && value.endsWith(SUFFIX);
	}
	
	public static String getContent(String value) {
		if (value==null)
			return null;
		if (!is(value))
			return value;
		
        StringBuilder sb = null;
        value = value.trim();
 
        sb = new StringBuilder(value);
        sb.replace(0, PREFIX.length(), "");
 
        value = sb.toString();
        sb = new StringBuilder(value);
        sb.replace(value.lastIndexOf(SUFFIX), value.lastIndexOf(SUFFIX)+SUFFIX.length(),"");
        value = sb.toString();
        return value;
	}
	
	protected String pattern;
	
	protected String origin;
	protected String content;
	protected boolean isCDataNeeded = false;
	
	protected CData(String value, String pattern) {
		this.origin = value;
		this.pattern = pattern;
		if (this.origin==null)
			this.content = "";
		else {
			this.content = CData.getContent(this.origin);
		}
		this.isCDataNeeded = Pattern.compile(this.pattern).matcher(this.content).find();
	}

	public String getOrigin() {
		return origin;
	}

	public String getContent() {
		return content;
	}

	public boolean isCDataNeeded() {
		return isCDataNeeded;
	}
	
	public String getCData() {
		return PREFIX + this.content + SUFFIX;
	}
	
	public String getCDataIfNeeded() {
		return this.isCDataNeeded?getCData():getContent();
	}
}
