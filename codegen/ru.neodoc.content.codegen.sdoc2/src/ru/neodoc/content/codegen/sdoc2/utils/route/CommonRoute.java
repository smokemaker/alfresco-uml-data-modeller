package ru.neodoc.content.codegen.sdoc2.utils.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonRoute implements Route {

	protected List<CommonRouteItem<?>> items = new ArrayList<>();
	protected CommonRouteItem<?> current = null;
	
	protected CommonRouteItem<?> beforeFirst = null;
	protected CommonRouteItem<?> afterLast = null;

	protected RouteStep lastStep = null;
	
	protected List<RouteItemValueator> valueators = new ArrayList<>();
	protected List<Route.RouteListener> listeners = new ArrayList<>();
	
	protected Map<Object, CommonRouteItem<?>> objectMap = new HashMap<>();
	
	public CommonRoute() {
		beforeFirst = new CommonRouteItem<>(null); 
		items.add(beforeFirst);
		afterLast = new CommonRouteItem<>(null);
		items.add(afterLast);
		current = beforeFirst;
	}
	
	@Override
	public void addValuator(RouteItemValueator valueator) {
		this.valueators.add(valueator);
	}
	
	@Override
	public void clearValueators() {
		this.valueators.clear();
	}
	
	@Override
	public void listen(RouteListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void unlisten(RouteListener listener) {
		listeners.remove(listener);
	}
	
	protected boolean currentIsValueable() {
		return itemIsValueable(current);
	} 
	
	protected boolean itemIsValueable(RouteItem<?> item) {
		return item!=afterLast && item!=beforeFirst;
	} 
	
	@Override
	public <T> void add(T object) {
		CommonRouteItem<T> item = new CommonRouteItem<T>(object);
		add(item);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> RouteItem<T> get(T object) {
		return (RouteItem<T>)objectMap.get(object);
	}
	@Override
	public void add(RouteItem<?> item) {
		if (!(item instanceof CommonRouteItem<?>))
			return;
		CommonRouteItem<?> cri = (CommonRouteItem<?>)item;
		CommonRouteItem<?> last = (items.size()==2?null:items.get(items.size()-2));
		items.add(items.size()-1, cri);
		if (last!=null) {
			last.setNext(cri);
			cri.setPrev(last);
		}
		if (cri.getObject()!=null)
			objectMap.put(cri.getObject(), cri);
	}

	@Override
	public RouteItem<?> current() {
		if (!currentIsValueable())
			return null;
		return current;
	}

	@Override
	public RouteItem<?> getPrev(RouteItem<?> origin) {
		RouteItem<?> item = origin;
		if (item==null)
			item = afterLast;
		if (item==beforeFirst)
			return null;
		RouteStep step = item.step();
		if (step==null)
			return null;
		step = step.prev();
		while (step!=null && step.item().isSkip())
			step = step.prev();
		if (step==null)
			return null;
		return step.item();
	}

	@Override
	public RouteItem<?> getNext(RouteItem<?> origin) {
		RouteItem<?> item = origin;
		if (item==null)
			item = beforeFirst;
		if (item==afterLast)
			return null;
		RouteItem<?> result = null;
		if (item==beforeFirst)
			result = getStartItem();
		else
			result = item.next();
		while (result!=null && result.isSkip() && isValuable(result))
			result = getNextFor(result);
		return result;
	}

	protected boolean isValuable(RouteItem<?> item) {
		boolean result = true;
		for (RouteItemValueator valuator: this.valueators)
			result = result && valuator.valuate(item);
		return result;
	}
	
	protected RouteItem<?> getStartItem(){
		return items.get(1);
	};
	
	protected RouteItem<?> getNextFor(RouteItem<?> item){
		if (item==beforeFirst)
			return getStartItem();
		if (item.hasNext())
			return item.next();
		return afterLast;
	}
	
	@Override
	public RouteItem<?> goPrev(RouteItem<?> origin) {
		RouteItem<?> result = getPrev(origin);
		current = (CommonRouteItem<?>)result;
		if (current!=null)
			lastStep = current.step();
		else {
			lastStep = null;
			current = beforeFirst;
		}
		return current;
	}

	@Override
	public RouteItem<?> goNext(RouteItem<?> origin) {
		RouteItem<?> item = origin;
		RouteItem<?> result = getNext(origin);
		if (result == null)
			return null;
		if (itemIsValueable(item)) 
			if (item.step().next()!=null)
				item.step().clearForward();
		while (item!=result) {
			RouteItem<?> from = item;
			item = (CommonRouteItem<?>)getNextFor(item);
			RouteItem<?> to = item;
			if (lastStep!=null) {
				lastStep.setNext(item.step());
				item.step().setPrev(lastStep);
			}
			lastStep = item.step();
			if (item.isSkip())
				lastStep.skip();
			else
				lastStep.clearSkip();
			for (RouteListener rl: listeners)
				rl.onMoveNext(from, to);
		}
		return result;
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return items.size()-2;
	}

	@Override
	public RouteItem<?> getPrev() {
		return getPrev(current);
	}

	@Override
	public RouteItem<?> getNext() {
		return getNext(current);
	}

	@Override
	public RouteItem<?> goPrev() {
		return goPrev(current);
	}

	@Override
	public RouteItem<?> goNext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RouteItem<?> getPrev(Object origin) {
		return getPrev(objectMap.get(origin));
	}

	@Override
	public RouteItem<?> getNext(Object origin) {
		return getNext(objectMap.get(origin));
	}

	@Override
	public RouteItem<?> goPrev(Object origin) {
		return goPrev(objectMap.get(origin));
	}

	@Override
	public RouteItem<?> goNext(Object origin) {
		return goNext(objectMap.get(origin));
	}
}
