package ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher;

public class DispatchedPageAfterLast extends DispatchedPageImpl {

	public static DispatchedPageAfterLast newInstance(PageDispatcher pd) {
		DispatchedPageAfterLast result = new DispatchedPageAfterLast("afterLast");
		result.setPageDispatcher(pd);
		return result;
	}
	
	protected DispatchedPageAfterLast(String pageName) {
		super(pageName);
	}

	@Override
	public boolean isControlCreated() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void internalRefresh() {

	}

}
