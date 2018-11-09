package ru.neodoc.content.codegen.sdoc2.utils.route;

import ru.neodoc.content.codegen.sdoc2.utils.Skippable;

public class CommonRouteItem<T> implements RouteItem<T> {

	protected RouteItem<?> next = null;
	protected RouteItem<?> prev = null;
	
	protected T object = null;
	
	protected CommonRouteStep step = null;
	
	public CommonRouteItem() {
		// TODO Auto-generated constructor stub
	}
	
	public CommonRouteItem(T object) {
		 setObject(object);
	}
	
	public void setNext(RouteItem<?> item) {
		this.next = item;
	}
	
	public void setPrev(RouteItem<?> item) {
		this.prev = item;
	}
	
	@Override
	public void setObject(T object) {
		this.object = object;
	}

	@Override
	public T getObject() {
		return this.object;
	}

	@Override
	public RouteItem<?> next() {
		return this.next;
	}

	@Override
	public RouteItem<?> prev() {
		return this.prev;
	}

	@Override
	public boolean isSkip() {
		if (object!=null)
			if (object instanceof Skippable)
				return ((Skippable)object).canSkip();
		return false;
	}

	@Override
	public boolean hasNext() {
		return this.next!=null;
	}

	@Override
	public boolean hasPrev() {
		return this.prev!=null;
	}

	@Override
	public RouteStep step() {
		if (step==null)
			step = new CommonRouteStep(this);
		return step;
	}

}
