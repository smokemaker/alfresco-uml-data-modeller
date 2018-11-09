package ru.neodoc.content.codegen.sdoc2.utils.route;

public interface RouteStep {

	RouteItem<?> item();
	RouteStep prev();
	RouteStep next();
	
	void setPrev(RouteStep step);
	void setNext(RouteStep step);
	void clearForward();
	
	void skip();
	void clearSkip();
	boolean isSkipped();
}
