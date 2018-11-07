package ru.neodoc.jaxb.utils;

public class CDataAdapter  {
	 
    /**
     * Parse a CDATA String.<br />
     * If is a CDATA, removes leading and trailing string<br />
     * Otherwise does nothing
     * @param s the string to parse
     * @return the parsed string
     */
    public static String parse(String s)  {
        return CData.create(s).getContent();
    }
 
    /**
     * Add CDATA leading and trailing to a string if not already a CDATA
     * @param s
     * @return
     */
    public static String print(String s) {
    	if (s==null)
    		return null;
    	return CData.create(s).getCDataIfNeeded();
    }
}