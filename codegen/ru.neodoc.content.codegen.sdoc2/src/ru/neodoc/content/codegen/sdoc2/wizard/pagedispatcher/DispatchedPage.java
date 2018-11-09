package ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher;

import ru.neodoc.content.codegen.sdoc2.utils.Skippable;
import ru.neodoc.content.codegen.wizard.SourceCodeGeneratorWizardPage;

public interface DispatchedPage extends SourceCodeGeneratorWizardPage, Skippable {

	PageDispatcher getPageDispatcher();
	void setPageDispatcher(PageDispatcher dispatcher);
	
	void refresh();
	
	void clearSkip();	
	void skipOnce();
	void skipAlways();
	boolean isSkip();
	boolean isSkipAlways();
	void skipped();

}
