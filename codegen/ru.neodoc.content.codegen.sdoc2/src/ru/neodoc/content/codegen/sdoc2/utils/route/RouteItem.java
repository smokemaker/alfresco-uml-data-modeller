package ru.neodoc.content.codegen.sdoc2.utils.route;

public interface RouteItem<T> {

	void setObject(T object);
	T getObject();
	
	boolean hasNext();
	boolean hasPrev();
	
	RouteItem<?> next();
	RouteItem<?> prev();

	RouteStep step();
	boolean isSkip();
}
