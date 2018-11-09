package ru.neodoc.content.codegen.sdoc2.extension.javascript.pages.provider;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;

import ru.neodoc.content.codegen.sdoc2.extension.javascript.WrapperJSExtension;
import ru.neodoc.content.codegen.sdoc2.wizard.provider.AbstractWrapperLabelProvider;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;
import ru.neodoc.content.codegen.sdoc2.wrap.NamespaceWrapper;

public class JavaScriptNameLabelProvider extends AbstractWrapperLabelProvider {

	public static CellLabelProvider getCellProvider(){
		JavaScriptNameLabelProvider lp = new JavaScriptNameLabelProvider();
		return new DelegatingStyledCellLabelProvider(lp);
	}
	
	@Override
	protected StyledString getWrapperStyledText(AbstractWrapper wrapper) {
		if (wrapper instanceof NamespaceWrapper) {
			StringBuffer sb = new StringBuffer();
			sb.append(WrapperJSExtension.get(wrapper).getFullTargetJSName());
			return new StyledString(sb.toString());
		}
		return new StyledString(WrapperJSExtension.get(wrapper).getFullTargetJSName());
	}

}
