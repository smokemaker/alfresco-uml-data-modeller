package ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher;

import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.codegen.sdoc2.utils.route.CommonRoute;
import ru.neodoc.content.codegen.sdoc2.utils.route.RouteItem;
import ru.neodoc.content.codegen.sdoc2.utils.route.RouteStep;
import ru.neodoc.content.codegen.sdoc2.utils.route.Route.RouteListener;

public class CommonPageDispatcher implements PageDispatcher {

	protected List<DispatchedPage> pages = new ArrayList<>();
	protected CommonRoute route = new CommonRoute();
	
	protected PageDispatcher parent = null;
	
	public PageDispatcher getParent() {
		return parent;
	}

	public void setParent(PageDispatcher parent) {
		this.parent = parent;
	}

	public CommonPageDispatcher() {

	}

	@Override
	public void add(DispatchedPage page) {
		addPage(page);
	}
	
	public void addPage(DispatchedPage page) {
		this.pages.add(page);
		initPage(page);
		afterInitPage(page);
		route.add(page);
	}
	
	protected void initPage(DispatchedPage page) {
		page.setPageDispatcher(this);
	}
	
	protected void afterInitPage(DispatchedPage page) {
		
	}
	
	protected boolean hasPrev() {
		return route.getPrev()!=null;
	}
	
	public boolean hasNext() {
		return route.getNext()!=null;
	}
	
	protected boolean canShow(DispatchedPage page) {
		return true;
	}
	
	protected boolean canShow(int index) {
		return canShow(pages.get(index));
	}
	
	protected DispatchedPage currentPage() {
		RouteItem<?> ri = route.current(); 
		return ri!=null?(DispatchedPage)ri.getObject():null;
	}
	
	@Override
	public DispatchedPage getNext() {
		RouteItem<?> ri = route.getNext();
		return (DispatchedPage)(ri==null?null:ri.getObject());
	}
	
	@Override
	public DispatchedPage getNext(DispatchedPage current) {
		RouteItem<?> ri = route.getNext(current);
		if (ri!=null)
			return (DispatchedPage)ri.getObject();
		if (ri==null && parent!=null)
			return parent.getNext(DispatchedPageAfterLast.newInstance(this));
		return null;
	}
	
	@Override
	public DispatchedPage goNext() {
		
		RouteItem<DispatchedPage> ri = (RouteItem<DispatchedPage>)route.goNext();
		DispatchedPage result = ri==null?null:ri.getObject();
		if (result!=null)
			result.refresh();
		return result;
	}

	@Override
	public DispatchedPage goNext(DispatchedPage current) {
		final List<RouteStep> rs = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		RouteListener rl = new RouteListener() {
			
			@Override
			public void onMovePrev(RouteItem<?> from, RouteItem<?> to) {
				
			}
			
			@Override
			public void onMoveNext(RouteItem<?> from, RouteItem<?> to) {
				rs.add(to.step());
			}
		};
		route.listen(rl);
		RouteItem<DispatchedPage> ri = (RouteItem<DispatchedPage>)route.goNext(current);
		route.unlisten(rl);
		RouteStep last = rs.size()==0?route.get(current).step():rs.get(rs.size()-1);
		if (ri!=null)
			return ri.getObject();
		// ri==null
		
		return null;
	}
	
	@Override
	public DispatchedPage getPrev() {
		RouteItem<?> ri = route.getPrev();
		return (DispatchedPage)(ri==null?null:ri.getObject());
	}
	
	@Override
	public DispatchedPage goPrev() {
		@SuppressWarnings("unchecked")
		RouteItem<DispatchedPage> ri = (RouteItem<DispatchedPage>)route.goPrev();
		DispatchedPage result = ri==null?null:ri.getObject();
		if (result!=null)
			result.refresh();
		return result;
	}

	@Override
	public void back() {
		goPrev();
	}

	@Override
	public DispatchedPage getStartPage() {
		@SuppressWarnings("unchecked")
		RouteItem<DispatchedPage> ri = (RouteItem<DispatchedPage>)route.getNext();
		return ri==null?null:ri.getObject();
	}

	@Override
	public List<DispatchedPage> getPages() {
		return this.pages;
	}

	@Override
	public PageDispatcher top() {
		if (parent==null)
			return this;
		return parent.top();
	}

	@Override
	public DispatchedPage goPrev(DispatchedPage current) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DispatchedPage getPrev(DispatchedPage current) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasNext(DispatchedPage current) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void add(DispatchedPage... pages) {
		for (int i = 0; i < pages.length; i++) {
			add(pages[i]);
		}
	}

}
