package ru.neodoc.content.codegen.sdoc2.utils.route;

public interface Route {

	public static interface RouteListener {
		void onMoveNext(RouteItem<?> from, RouteItem<?> to);
		void onMovePrev(RouteItem<?> from, RouteItem<?> to);
	}
	
	<T> void add(T object);
	void add(RouteItem<?> item);
	<T> RouteItem<T> get(T object);
	
	RouteItem<?> current();
	
	RouteItem<?> getPrev();
	RouteItem<?> getNext();
	RouteItem<?> goPrev();
	RouteItem<?> goNext();
	
	RouteItem<?> getPrev(RouteItem<?> origin);
	RouteItem<?> getNext(RouteItem<?> origin);
	RouteItem<?> goPrev(RouteItem<?> origin);
	RouteItem<?> goNext(RouteItem<?> origin);
	
	RouteItem<?> getPrev(Object origin);
	RouteItem<?> getNext(Object origin);
	RouteItem<?> goPrev(Object origin);
	RouteItem<?> goNext(Object origin);
	
	int size();
	
	void addValuator(RouteItemValueator valueator);
	void clearValueators();
	
	void listen(RouteListener listener);
	void unlisten(RouteListener listener);
}
