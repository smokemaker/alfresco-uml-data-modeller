package ru.neodoc.content.modeller.xml2uml.helper.relation.proxy;

public abstract class AbstractProxy {
	
	private Object value = null;
	
	public AbstractProxy(Object value) {
		super();
		this.value = value;
	}
	
	public String asString() {
		return this.value!=null?this.value.toString():"";
	}
	
	public Object asObject() {
		return this.value;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T asType(Class<T> clazz){
		if (this.value==null)
			return (T)null;
		if (clazz.isAssignableFrom(this.value.getClass()))
			return (T)this.value;
		return (T)null;
	}
}