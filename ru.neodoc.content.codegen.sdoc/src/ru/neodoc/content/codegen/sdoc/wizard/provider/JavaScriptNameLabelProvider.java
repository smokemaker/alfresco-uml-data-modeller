package ru.neodoc.content.codegen.sdoc.wizard.provider;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;

import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;
import ru.neodoc.content.codegen.sdoc.wrap.NamespaceWrapper;

public class JavaScriptNameLabelProvider extends BaseWrapperLabelProvider {

	public static CellLabelProvider getCellProvider(){
		JavaScriptNameLabelProvider lp = new JavaScriptNameLabelProvider();
		return new DelegatingStyledCellLabelProvider(lp);
	}
	
	@Override
	protected StyledString getWrapperStyledText(BaseWrapper wrapper) {
		if (wrapper instanceof NamespaceWrapper) {
			StringBuffer sb = new StringBuffer();
			sb.append(wrapper.getFullTargetJavaScriptName());
			return new StyledString(sb.toString());
		}
		return new StyledString(wrapper.getFullTargetJavaScriptName());
	}

}
