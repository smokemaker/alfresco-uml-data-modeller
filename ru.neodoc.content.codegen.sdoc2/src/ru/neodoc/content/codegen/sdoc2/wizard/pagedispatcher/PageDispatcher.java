package ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher;

import java.util.List;

public interface PageDispatcher {

	@Deprecated
	DispatchedPage goNext();
	@Deprecated
	DispatchedPage goPrev();
	@Deprecated
	DispatchedPage getNext();
	@Deprecated
	DispatchedPage getPrev();
	
	DispatchedPage goNext(DispatchedPage current);
	DispatchedPage goPrev(DispatchedPage current);
	DispatchedPage getNext(DispatchedPage current);
	DispatchedPage getPrev(DispatchedPage current);
	
	@Deprecated
	boolean hasNext();
	boolean hasNext(DispatchedPage current);
	
	PageDispatcher getParent();
	void setParent(PageDispatcher parent);
	
	@Deprecated
	PageDispatcher top();
	
	void back();
	
	@Deprecated
	DispatchedPage getStartPage();
	
	List<DispatchedPage> getPages();
	
	void add(DispatchedPage page);
	void add(DispatchedPage... pages);
}
