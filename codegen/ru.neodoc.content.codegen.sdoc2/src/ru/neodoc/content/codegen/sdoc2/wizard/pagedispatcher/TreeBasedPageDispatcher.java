package ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher;

import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.codegen.sdoc2.config.Configurable;
import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.utils.tree.Tree;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeBased;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItem;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItem.AvailabilityProvider;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeTraverser;
import ru.neodoc.content.codegen.sdoc2.utils.tree.impl.TreeImpl;

public class TreeBasedPageDispatcher implements PageDispatcher, Configurable, 
		TreeBased<DispatchedPage>, AvailabilityProvider {

	protected Tree<DispatchedPage> tree = new TreeImpl<DispatchedPage>();
	protected TreeTraverser<DispatchedPage> mainTraverser;
	
	protected PageDispatcher parent;
	protected List<PageDispatcher> dispatchers = new ArrayList<>();

	protected Configuration configuration;
	
	public TreeBasedPageDispatcher() {
		super();
	}
	
	public TreeBasedPageDispatcher(Configuration configuration) {
		this();
		setConfiguration(configuration);
		tree.setAvailbalityProvider(this);
	}
	
	@Override
	public boolean isAvailable(TreeItem<?> treeItem) {
		return configuration.isActive();
	}
	
	protected void initMainTraverser() {
		if (mainTraverser==null)
			mainTraverser = tree.startTraverse();
	}
	
	@Override
	public DispatchedPage goNext() {
		initMainTraverser();
		try {
			return mainTraverser.goNext().getObject();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public DispatchedPage goPrev() {
		initMainTraverser();
		try {
			return mainTraverser.goPrev().getObject();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public DispatchedPage getNext() {
		initMainTraverser();
		try {
			return mainTraverser.getNext().getObject();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public DispatchedPage getPrev() {
		initMainTraverser();
		try {
			return mainTraverser.getPrev().getObject();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public DispatchedPage goNext(DispatchedPage current) {
		/*@SuppressWarnings("unchecked")
		TreeTraverser<DispatchedPage> traverser = ((Tree<DispatchedPage>)tree.getRoot()).startNewTraverse();
		traverser.setCurrent(current);
		try {
			return traverser.goNext().getObject();
		} catch (Exception e) {
			return null;
		}*/
		return goNext();
	}

	@Override
	public DispatchedPage goPrev(DispatchedPage current) {
		return goPrev();
	}

	@Override
	public DispatchedPage getNext(DispatchedPage current) {
		TreeTraverser<DispatchedPage> traverser = ((Tree<DispatchedPage>)tree.getRoot()).startNewTraverse();
		traverser.setCurrent(current);
		try {
			return traverser.getNext().getObject();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public DispatchedPage getPrev(DispatchedPage current) {
		return getPrev();
	}

	@Override
	public boolean hasNext() {
		return getNext()!=null;
	}

	@Override
	public boolean hasNext(DispatchedPage current) {
		return getNext(current)!=null;
	}

	@Override
	public PageDispatcher getParent() {
		return this.parent;
	}

	@Override
	public void setParent(PageDispatcher parent) {
		this.parent = parent;
	}

	@Override
	public PageDispatcher top() {
		return this.parent==null?this:this.parent.top();
	}

	@Override
	public void back() {
		goPrev();
	}

	@Override
	public DispatchedPage getStartPage() {
		// we reset the traverse
		tree.startTraverse().setCurrent(null);
		// and start with new item
		return tree.startTraverse().goNext().getObject(); 
	}

	@Override
	public List<DispatchedPage> getPages() {
		return tree.getRoot().getAllItems().getObjects();
	}

	@Override
	public void add(DispatchedPage page) {
		initPage(page);
		tree.add(page);
	}

	protected void initPage(DispatchedPage page) {
		setPageConfiguration(page);
		page.setPageDispatcher(this);
	}
	
	protected void afterInitPage(DispatchedPage page) {
		
	}

	protected void setPageConfiguration(DispatchedPage page) {
		if (Configurable.class.isAssignableFrom(page.getClass())) {
			Configuration config = ((Configurable)page).getConfiguration();
			if (config==null)
				((Configurable)page).setConfiguration(getConfiguration());
		}
		
	}
	
	@Override
	public void add(DispatchedPage... pages) {
		for (int i = 0; i < pages.length; i++) {
			add(pages[i]);
		}
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
	

	@SuppressWarnings("unchecked")
	public void add(PageDispatcher pageDispatcher) {
		if (pageDispatcher==null)
			return;
		this.dispatchers.add(pageDispatcher);
		pageDispatcher.setParent(this);
		init(pageDispatcher);
		if (pageDispatcher instanceof TreeBased)
			tree.add(((TreeBased<DispatchedPage>)pageDispatcher).getTree());
			
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
	public Tree<DispatchedPage> getTree() {
		return tree;
	}
}
