package ru.neodoc.content.modeller.common;

public interface Enablable {
	
	public static boolean isEnabled(Object object) {
		if (object==null)
			return false;
		if (object instanceof Enablable)
			return ((Enablable)object).isEnabled();
		return true;
	}
	
	public default boolean isEnabled() {
		return true;
	}
	
}
