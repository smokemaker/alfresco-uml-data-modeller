package ru.neodoc.content.codegen.sdoc2.utils.route;

import ru.neodoc.content.codegen.sdoc2.utils.Skippable;

public class CommonRouteStep implements RouteStep {

	protected RouteItem<?> item = null;
	
	protected RouteStep prev = null;
	protected RouteStep next = null;
	
	protected boolean isSkipped = false;
	
	public CommonRouteStep(RouteItem<?> item) {
		super();
		this.item = item;
	}
	
	@Override
	public RouteItem<?> item() {
		return this.item;
	}

	@Override
	public RouteStep prev() {
		return this.prev;
	}

	@Override
	public RouteStep next() {
		return this.next;
	}

	@Override
	public void setPrev(RouteStep step) {
		this.prev = step;
	}

	@Override
	public void setNext(RouteStep step) {
		this.next = step;
	}

	@Override
	public void clearForward() {
		if (this.next!=null)
			this.next.clearForward();
		setNext(null);
	}

	@Override
	public void skip() {
		this.isSkipped = true;
		if (item.getObject() instanceof Skippable)
			((Skippable)item.getObject()).skipped();
	}

	@Override
	public void clearSkip() {
		this.isSkipped = false;
	}
	
	@Override
	public boolean isSkipped() {
		return this.isSkipped;
	}

}
