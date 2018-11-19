package ru.neodoc.content.codegen.sdoc2.extension.java;

import ru.neodoc.content.codegen.sdoc2.CodegenManager;
import ru.neodoc.content.codegen.sdoc2.config.Configurable;
import ru.neodoc.content.codegen.sdoc2.config.Configuration;
import ru.neodoc.content.codegen.sdoc2.extension.SdocCodegenExtensionConfiguration;
import ru.neodoc.content.codegen.sdoc2.extension.SdocCodegenExtensionImpl;
import ru.neodoc.content.codegen.sdoc2.extension.java.annotation.DefaultAnnotationFactory;
import ru.neodoc.content.codegen.sdoc2.extension.java.generator.JavaGenerator;
import ru.neodoc.content.codegen.sdoc2.extension.java.pages.Page1JavaSetGenerationTarget;
import ru.neodoc.content.codegen.sdoc2.extension.java.pages.Page2JavaSetImports;
import ru.neodoc.content.codegen.sdoc2.generator.SdocGenerator;
import ru.neodoc.content.codegen.sdoc2.generator.annotation.SdocAnnotationFactory;
import ru.neodoc.content.codegen.sdoc2.utils.tree.Leaf;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItem;
import ru.neodoc.content.codegen.sdoc2.utils.tree.TreeItem.AvailabilityProvider;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.PageDispatcher;
import ru.neodoc.content.codegen.sdoc2.wizard.pagedispatcher.TreeBasedPageDispatcher;

public class JavaCodegenExtension extends SdocCodegenExtensionImpl {

	public static final String EXTENSION_ID = JavaCodegenExtension.class.getName();

	@Override
	protected String getPreferenceQualifier() {
		return EXTENSION_ID;
	}
	
	@Override
	protected void initOptionMessage() {
		this.optionMessage = "Generate Java sources";
	}

	@Override
	protected PageDispatcher doGetPageDispatcher(Configuration configuration) {
		TreeBasedPageDispatcher pd = new TreeBasedPageDispatcher(configuration);
		pd.add(new Page1JavaSetGenerationTarget());
		Page2JavaSetImports page2 = new Page2JavaSetImports(); 
		pd.add(page2);
		pd.getTree().getTreeItem(page2).setAvailbalityProvider(new AvailabilityProvider() {
			
			@Override
			public boolean isAvailable(TreeItem<?> treeItem) {
				try {
					if (!(treeItem instanceof Leaf))
						return true;
					Leaf<?> leaf = (Leaf<?>)treeItem;
					Configurable dp = (Configurable)leaf.getObject();
					return !((CodegenManager)dp.getConfiguration().getValue(CodegenManager.PROP_NAME))
							.getRequiredNamespacesWrapped().isEmpty();
				} catch (Exception e) {
					return true;
				}
				
			}
		});
		return pd;
	}

	@Override
	public SdocCodegenExtensionConfiguration getConfiguration() {
		SdocCodegenExtensionConfiguration config = super.getConfiguration();
		new JavaCodegenManager(config);
		config.setValue(SdocAnnotationFactory.PROP_NAME, new DefaultAnnotationFactory());
		return config;
	}
	
	@Override
	public SdocGenerator getGenerator(Configuration configuration) {
		return new JavaGenerator(configuration);
	}
}
