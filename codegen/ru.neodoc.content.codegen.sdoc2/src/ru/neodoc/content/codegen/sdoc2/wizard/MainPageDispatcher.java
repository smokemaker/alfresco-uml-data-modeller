package ru.neodoc.content.codegen.sdoc2.wizard;

import ru.neodoc.content.codegen.sdoc2.CodegenManager;
import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.extension.SdocCodegenExtensionConfiguration;
import ru.neodoc.content.codegen.sdoc2.preferences.PreferenceConstants;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.CompositePageDispatcher;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.DispatchedPage;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.PageDispatcher;
import ru.neodoc.content.codegen.sdoc2.wizard.pages.Page1SelectNamespaces;
import ru.neodoc.content.codegen.sdoc2.wizard.pages.Page2ListNamespacesToGenerate;
import ru.neodoc.content.codegen.sdoc2.wizard.pages.Page5Generation;

@Deprecated
public class MainPageDispatcher extends CompositePageDispatcher {

	public MainPageDispatcher(Configuration configuration) {
		super();
		setConfiguration(configuration);
		init();
	}

	protected void init() {
		DispatchedPage p1, p2;
		p1 = new Page1SelectNamespaces();
		if (configuration.getBoolean(PreferenceConstants.P_SKIP_NAMESPACE_SELECTION))
			p1.skipOnce();
		p2 = new Page2ListNamespacesToGenerate();
		add(p1, p2);
		
		CodegenManager codegenManager = (CodegenManager)configuration.getValue(CodegenManager.PROP_NAME);
		if (codegenManager!=null)
			for (SdocCodegenExtensionConfiguration config: codegenManager.getConfigurations()) {
				PageDispatcher pd = config.getPageDispatcher();
				if (pd!=null)
					add(pd);
			}
				
		
		add(new Page5Generation());
	}
	
}
