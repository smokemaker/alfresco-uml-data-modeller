package ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher;

import org.eclipse.jface.wizard.IWizardPage;

import ru.neodoc.content.codegen.sdoc2.utils.Skippable;
import ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizardPageImpl;

public abstract class DispatchedPageImpl extends SourceCodeGeneratorWizardPageImpl implements DispatchedPage, Skippable {

	protected PageDispatcher pageDispatcher = null;

	protected boolean skip = false;
	protected boolean skipAlways = false;
	
	protected DispatchedPageImpl(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PageDispatcher getPageDispatcher() {
		return pageDispatcher;
	}

	@Override
	public void setPageDispatcher(PageDispatcher dispatcher) {
		pageDispatcher = dispatcher;
	}

	public boolean canSkip() {
		return isSkip() && canFlipToNextPage();
	}
	
	public boolean isSkip() {
		return skip || skipAlways;
	}

	public boolean isSkipAlways() {
		return skipAlways;
	}

	public void skipOnce() {
		this.skip = true;
	}

	public void skipAlways() {
		this.skipAlways = true;
	}

	public void clearSkip() {
		this.skip = false;
		this.skipAlways = false;
	}

	public void skipped() {
		this.skip = false;
	}

	public void refresh() {
		if (isControlCreated())
			internalRefresh();
	}

	@Override
	public boolean canFlipToNextPage() {
		return pageDispatcher.hasNext(this);
	}
	
	@Override
	public IWizardPage getNextPage() {
		DispatchedPage page = pageDispatcher.getNext(this);
		return page!=null?page:super.getNextPage();
	}
	
	@Override
	public IWizardPage getPreviousPage() {
		DispatchedPage page = pageDispatcher.getPrev(this);
		return page!=null?page:super.getPreviousPage();
	}
	
	public abstract boolean isControlCreated();
	
	protected abstract void internalRefresh();

	@Override
	public void backPressed() {
		DispatchedPage p = pageDispatcher.goPrev(this);
		super.backPressed();
		if (p!=null)
			p.refresh();
	}

	@Override
	public void nextPressed() {
		DispatchedPage p = pageDispatcher.goNext(this);
		super.nextPressed();
		if (p!=null)
			p.refresh();
		getWizard().getContainer().updateButtons();
	}
}
