package ru.neodoc.content.utils;

public class PrefixedName {

	public static String name(String name) {
		return (new PrefixedName(name)).getName();
	}

	public static String name(String name, String newSeparator) {
		return (new PrefixedName(name, newSeparator)).getName();
	}
	
	public static PrefixedName create(String name, String prefix) {
		return (new PrefixedName(name)).withPrefix(prefix);
	}
	
	protected String separator = ":";
	
	protected String origin;
	protected String prefix = "";
	protected String name = "";
	
	public PrefixedName(String s) {
		this(s, null);
	}
	
	public PrefixedName(String s, String newSeparator) {
		if (CommonUtils.isValueable(newSeparator))
			this.separator = newSeparator;
		origin = s;
		if (s!=null) {
			int index = s.lastIndexOf(separator);
			try {
				prefix = s.substring(0, index);
			} catch (Exception e) {
				
			}
			try {
				name = index>=0?s.substring(index+separator.length()):s;
			} catch (Exception e) {
				
			}
		}
	}
	
	public boolean isEmpty() {
		return !CommonUtils.isValueable(name) && !CommonUtils.isValueable(prefix);
	}
	
	public boolean isValid() {
		return (name!=null) 
				&& (name.trim().length()>0 
						|| (null==prefix)
						|| prefix.trim().length()==0
				);
	}
	
	public boolean isEqual(PrefixedName prefixedName) {
		if (prefixedName == null)
			return false;
		if (!isValid() || !prefixedName.isValid())
			return false;
		return getFullName().equalsIgnoreCase(prefixedName.getFullName());
	}
	
	public boolean isPrefixed() {
		return (null!=prefix) && !prefix.trim().isEmpty();
	}

	public String getFullName() {
		String result = "";
		if (isPrefixed())
			result += prefix + ":";
		return result + name; 
	}
	
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// builder methods
	
	public PrefixedName withName(String value) {
		setName(value);
		return this;
	}
	
	public PrefixedName withPrefix(String value) {
		setPrefix(value);
		return this;
	}
	
}