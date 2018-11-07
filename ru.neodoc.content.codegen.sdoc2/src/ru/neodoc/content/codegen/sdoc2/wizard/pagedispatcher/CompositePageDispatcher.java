package ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher;

import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.codegen.sdoc2.config.Configurable;
import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.utils.route.CommonRoute;
import ru.neodoc.content.codegen.sdoc2.utils.route.RouteItem;
import ru.neodoc.content.codegen.sdoc2.utils.route.RouteItemValueator;

public class CompositePageDispatcher implements Configurable, PageDispatcher {

	protected List<PageDispatcher> dispatchers = new ArrayList<>();
	protected PageDispatcher parent = null;
	
	public PageDispatcher getParent() {
		return parent;
	}

	public void setParent(PageDispatcher parent) {
		this.parent = parent;
	}

	protected Configuration configuration;

	protected CommonRoute route = new CommonRoute();
	
	public CompositePageDispatcher() {

		RouteItemValueator valueator = new RouteItemValueator() {
			
			@Override
			public boolean valuate(RouteItem<?> item) {
				PageDispatcher pd = (PageDispatcher)item.getObject();
				if (pd==null)
					return false;
				if (pd instanceof Configurable) {
					Configuration cf = ((Configurable)pd).getConfiguration();
					return (cf!=null && !cf.isActive());
				}
				return true;
			}
		};
		
		route.addValuator(valueator);
	}
	
	public void add(DispatchedPage... pages) {
		PageDispatcher pd = getDefaultInnerDispatcher();
		if (pages==null || pages.length==0)
			return;
		add(pd);
		for (int i=0; i<pages.length; i++)
			pd.add(pages[i]);
	}
	
	protected PageDispatcher getDefaultInnerDispatcher() {
		return new ConfigurablePageDispatcher();
	}
	
	public void add(PageDispatcher pageDispatcher) {
		if (pageDispatcher==null)
			return;
		this.dispatchers.add(pageDispatcher);
		pageDispatcher.setParent(this);
		init(pageDispatcher);
		route.add(pageDispatcher);
	}
	
	protected void init(PageDispatcher pageDispatcher) {
		updateConfiguration(pageDispatcher);
	}
	
	protected void updateConfiguration(PageDispatcher pageDispatcher) {
		if (pageDispatcher instanceof Configurable) {
			Configuration config = ((Configurable)pageDispatcher).getConfiguration();
			if (config!=null)
				config.setParentConfiguration(getConfiguration());
			else
				((Configurable)pageDispatcher).setConfiguration(getConfiguration());
		}
	}
	
	protected void updateConfigurations() {
		for (PageDispatcher pd: this.dispatchers)
			updateConfiguration(pd);
	}
	
	
	public boolean hasNextDispatcher() {
		return route.getNext()!=null;
	}
	
	protected boolean hasPrevDispatcher() {
		return route.getPrev()!=null;
	}
	
	protected PageDispatcher currentDispatcher() {
		return (PageDispatcher)(route.current()==null?null:route.current().getObject());
	}
	
	protected PageDispatcher getNextDispatcher() {
		while (hasNextDispatcher()) {
			PageDispatcher pd = currentDispatcher();
			if (pd==null)
				continue;
			if (pd instanceof Configurable) {
				Configuration cf = ((Configurable)pd).getConfiguration();
				if (cf!=null && !cf.isActive())
					continue;
			}
			return pd;
		}
		return null;
	}
	
	@Override
	public boolean hasNext() {
		return getNext()!=null;
	}
	
	@Override
	public DispatchedPage getNext() {
		DispatchedPage result = currentDispatcher()==null?null:currentDispatcher().getNext();
		while (result==null && hasNextDispatcher()) {
			PageDispatcher pd = (PageDispatcher)route.getNext().getObject();
			if (pd!=null)
				result = pd.getNext();
		}
			 
		return result;
	}

	protected PageDispatcher getPrevDispatcher() {
		return null;
	}
	
	@Override
	public DispatchedPage getPrev() {
		DispatchedPage result = currentDispatcher()==null?null:currentDispatcher().getPrev();
		while (result==null && hasPrevDispatcher()) {
			PageDispatcher pd = (PageDispatcher)route.getPrev().getObject();
			if (pd!=null)
				result = pd.getPrev();
		}
			 
		return result;
	}
	
	@Override
	public DispatchedPage goPrev() {
		DispatchedPage result = currentDispatcher()==null?null:currentDispatcher().goPrev();
		while (result==null && hasPrevDispatcher()) {
			PageDispatcher pd = (PageDispatcher)route.goPrev().getObject();
			if (pd!=null)
				result = pd.goPrev();
		}
			 
		return result;
	}
	
	@Override
	public DispatchedPage goNext() {
		DispatchedPage result = currentDispatcher()==null?null:currentDispatcher().goNext();
		while (result==null && hasNextDispatcher()) {
			PageDispatcher pd = (PageDispatcher)route.goNext().getObject();
			if (pd!=null)
				result = pd.goNext();
		}
			 
		return result;
	}

	@Override
	public void back() {
		goPrev();

	}

	@Override
	public DispatchedPage getStartPage() {
		for (PageDispatcher pd: this.dispatchers)
			if (pd.getStartPage()!=null)
				return pd.getStartPage();
		return null;
	}

	@Override
	public List<DispatchedPage> getPages() {
		List<DispatchedPage> result = new ArrayList<>();
		for (PageDispatcher pd: dispatchers)
			if (pd!=null)
				result.addAll(pd.getPages());
		return result;
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		updateConfigurations();
	}

	@Override
	public Configuration getConfiguration() {
		return this.configuration;
	}

	@Override
	public void add(DispatchedPage page) {
		add(new DispatchedPage[] {page});
	}

	@Override
	public PageDispatcher top() {
		if (parent!=null)
			return parent.top();
		return this;
	}

	@Override
	public DispatchedPage goNext(DispatchedPage current) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DispatchedPage goPrev(DispatchedPage current) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DispatchedPage getNext(DispatchedPage current) {
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

}
