package ru.neodoc.content.codegen.sdoc2.extension.javascript;

import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.extension.SdocCodegenExtensionConfiguration;
import ru.neodoc.content.codegen.sdoc2.extension.SdocCodegenExtensionImpl;
import ru.neodoc.content.codegen.sdoc2.extension.java.annotation.DefaultAnnotationFactory;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.generator.JSGenerator;
import ru.neodoc.content.codegen.sdoc2.extension.javascript.pages.Page1SetJSGenerationTarget;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator;
import ru.neodoc.content.codegen.sdoc2.generator.annotation.SdocAnnotationFactory;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.PageDispatcher;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.TreeBasedPageDispatcher;

public class JavaScriptCodegenExtension extends SdocCodegenExtensionImpl {

	public static final String EXTENSION_ID = JavaScriptCodegenExtension.class.getName();
	
	@Override
	protected String getPreferenceQualifier() {
		return EXTENSION_ID;
	}
	
	@Override
	protected void initOptionMessage() {
		this.optionMessage = "Generate JavaScript sources";
	}

	@Override
	protected PageDispatcher doGetPageDispatcher(Configuration configuration) {
		PageDispatcher pd = new TreeBasedPageDispatcher(configuration);
		pd.add(new Page1SetJSGenerationTarget());
		return pd;
	}

	@Override
	public SdocCodegenExtensionConfiguration getConfiguration() {
		SdocCodegenExtensionConfiguration config = super.getConfiguration();
		config.setValue(SdocAnnotationFactory.PROP_NAME, new DefaultAnnotationFactory());
		return config;
	}
	
	@Override
	public SdocGenerator getGenerator(Configuration configuration) {
		return new JSGenerator(configuration);
	}
	
}
